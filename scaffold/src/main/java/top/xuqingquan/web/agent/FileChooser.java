package top.xuqingquan.web.agent;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import top.xuqingquan.utils.FileUtils;
import top.xuqingquan.utils.PermissionUtils;
import top.xuqingquan.utils.Timber;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by 许清泉 on 2019-06-04 23:12
 */
public abstract class FileChooser {

    /**
     * Activity
     */
    protected Activity mActivity;
    /**
     * Activity Request Code
     */
    protected static final int REQUEST_CODE = 0x254;
    /**
     * 当前系统是否高于 Android 5.0 ；
     */
    protected boolean mIsAboveLollipop;
    /**
     * 是否为Js Channel
     */
    protected boolean mJsChannel;
    /**
     * TAG
     */
    protected static final String TAG = FileChooser.class.getSimpleName();
    /**
     * 是否为 Camera State
     */
    protected boolean mCameraState = false;
    /**
     * 权限拦截
     */
    protected PermissionInterceptor mPermissionInterceptor;
    /**
     * FROM_INTENTION_CODE 用于表示当前Action
     */
    protected int FROM_INTENTION_CODE = 21;
    /**
     * 选择文件类型
     */
    protected String mAcceptType;
    /**
     * 修复某些特定手机拍照后，立刻获取照片为空的情况
     */
    private static final int MAX_WAIT_PHOTO_MS = 8 * 1000;
    /**
     * 如果是通过 JavaScript 打开文件选择器 ，那么 mJsChannelCallback 不能为空
     */
    protected JsChannelCallback mJsChannelCallback;

    protected FileChooser(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mIsAboveLollipop = builder.mIsAboveLollipop;
        this.mJsChannel = builder.mJsChannel;
        this.mPermissionInterceptor = builder.mPermissionInterceptor;
        this.mAcceptType = builder.mAcceptType;
        if (this.mJsChannel) {
            this.mJsChannelCallback = JsChannelCallback.create(builder.mJsChannelCallback);
        }
    }

    protected List<String> checkNeedPermission() {
        List<String> deniedPermissions = new ArrayList<>();
        if (!PermissionUtils.hasPermission(mActivity, AgentWebPermissions.getCAMERA())) {
            deniedPermissions.add(AgentWebPermissions.getCAMERA()[0]);
        }
        if (!PermissionUtils.hasPermission(mActivity, AgentWebPermissions.getSTORAGE())) {
            deniedPermissions.addAll(Arrays.asList(AgentWebPermissions.getSTORAGE()));
        }
        return deniedPermissions;
    }

    protected void openCameraAction() {
        Action mAction = new Action();
        mAction.setAction(Action.ACTION_CAMERA);
        ActionActivity.setChooserListener(this.getChooserListener());
        ActionActivity.start(mActivity, mAction);
    }

    protected Uri[] processData(Intent data) {
        Uri[] datas = null;
        if (data == null) {
            return null;
        }
        String target = data.getDataString();
        if (!TextUtils.isEmpty(target)) {
            return new Uri[]{Uri.parse(target)};
        }
        ClipData mClipData = data.getClipData();
        if (mClipData != null && mClipData.getItemCount() > 0) {
            datas = new Uri[mClipData.getItemCount()];
            for (int i = 0; i < mClipData.getItemCount(); i++) {
                ClipData.Item mItem = mClipData.getItemAt(i);
                datas[i] = mItem.getUri();

            }
        }
        return datas;
    }

    private void fileChooser() {
        if (PermissionUtils.getDeniedPermissions(mActivity, AgentWebPermissions.getSTORAGE()).isEmpty()) {
            touchOffFileChooserAction();
        } else {
            Action mAction = Action.createPermissionsAction(AgentWebPermissions.getSTORAGE());
            mAction.setFromIntention(FROM_INTENTION_CODE >> 2);
            ActionActivity.setPermissionListener(mPermissionListener);
            ActionActivity.start(mActivity, mAction);
        }
    }

    protected void touchOffFileChooserAction() {
        Action mAction = new Action();
        mAction.setAction(Action.ACTION_FILE);
        ActionActivity.setChooserListener(getChooserListener());
        mActivity.startActivity(new Intent(mActivity, ActionActivity.class).putExtra(ActionActivity.KEY_ACTION, mAction)
                .putExtra(ActionActivity.KEY_FILE_CHOOSER_INTENT, getFileChooserIntent()));
    }

    private ActionActivity.ChooserListener getChooserListener() {
        return (requestCode, resultCode, data) -> {
            Timber.i("request:" + requestCode + "  resultCode:" + resultCode);
            onIntentResult(requestCode, resultCode, data);
        };
    }

    protected ActionActivity.PermissionListener mPermissionListener = (permissions, grantResults, extras) -> {
        boolean tag = PermissionUtils.hasPermission(mActivity, Arrays.asList(permissions));
        permissionResult(tag, extras.getInt(ActionActivity.KEY_FROM_INTENTION));
    };

    protected Handler.Callback getCallBack() {
        return msg -> {
            switch (msg.what) {
                case 0:
                    mCameraState = true;
                    onCameraAction();

                    break;
                case 1:
                    mCameraState = false;
                    fileChooser();
                    break;
                default:
                    cancel();
                    break;
            }
            return true;
        };
    }

    public void openFileChooser() {
        if (!AgentWebUtils.isUIThread()) {
            AgentWebUtils.runInUiThread(this::openFileChooser);
            return;
        }
        openFileChooserInternal();
    }

    public abstract void onIntentResult(int requestCode, int resultCode, Intent data);

    protected abstract void permissionResult(boolean grant, int from_intention);

    protected abstract Intent getFileChooserIntent();

    protected abstract void onCameraAction();

    protected abstract void cancel();

    protected abstract void openFileChooserInternal();

    // 必须执行在子线程, 会阻塞直到文件转换完成;
    private static Queue<FileParcel> convertFile(String[] paths) throws Exception {
        if (paths == null || paths.length == 0) {
            return null;
        }
        int tmp = Runtime.getRuntime().availableProcessors() + 1;
        int result = paths.length > tmp ? tmp : paths.length;
        Executor mExecutor = Executors.newFixedThreadPool(result);
        final Queue<FileParcel> mQueue = new LinkedBlockingQueue<>();
        CountDownLatch mCountDownLatch = new CountDownLatch(paths.length);
        int i = 1;
        for (String path : paths) {
            Timber.i("path:" + path);
            if (TextUtils.isEmpty(path)) {
                mCountDownLatch.countDown();
                continue;
            }
            mExecutor.execute(new EncodeFileRunnable(path, mQueue, mCountDownLatch, i++));
        }
        mCountDownLatch.await();
        if (!((ThreadPoolExecutor) mExecutor).isShutdown()) {
            ((ThreadPoolExecutor) mExecutor).shutdownNow();
        }
        Timber.i("convertFile isShutDown:" + (((ThreadPoolExecutor) mExecutor).isShutdown()));
        return mQueue;
    }

    private static String convertFileParcelObjectsToJson(Collection<FileParcel> collection) {
        if (collection == null || collection.size() == 0) {
            return null;
        }
        Iterator<FileParcel> mFileParcels = collection.iterator();
        JSONArray mJSONArray = new JSONArray();
        try {
            while (mFileParcels.hasNext()) {
                JSONObject jo = new JSONObject();
                FileParcel mFileParcel = mFileParcels.next();
                jo.put("contentPath", mFileParcel.getContentPath());
                jo.put("fileBase64", mFileParcel.getFileBase64());
                jo.put("mId", mFileParcel.getId());
                mJSONArray.put(jo);
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
        return mJSONArray + "";
    }

    protected static class Builder {
        protected Activity mActivity;
        protected boolean mIsAboveLollipop = false;
        protected boolean mJsChannel = false;
        protected PermissionInterceptor mPermissionInterceptor;
        protected String mAcceptType = "*/*";
        protected Handler.Callback mJsChannelCallback;
    }

    protected static final class WaitPhotoRunnable implements Runnable {
        private String path;
        private Handler.Callback mCallback;

        public WaitPhotoRunnable(String path, Handler.Callback callback) {
            this.path = path;
            this.mCallback = callback;
        }

        @Override
        public void run() {
            if (TextUtils.isEmpty(path) || !new File(path).exists()) {
                if (mCallback != null) {
                    mCallback.handleMessage(Message.obtain(null, -1));
                }
                return;
            }
            int ms = 0;
            while (ms <= MAX_WAIT_PHOTO_MS) {
                ms += 300;
                SystemClock.sleep(300);
                File mFile = new File(path);
                if (mFile.length() > 0) {
                    if (mCallback != null) {
                        mCallback.handleMessage(Message.obtain(null, 1));
                        mCallback = null;
                    }
                    break;
                }
            }
            if (ms > MAX_WAIT_PHOTO_MS) {
                Timber.i("WaitPhotoRunnable finish!");
                if (mCallback != null) {
                    mCallback.handleMessage(Message.obtain(null, -1));
                }
            }
            mCallback = null;
            path = null;

        }
    }

    protected static class EncodeFileRunnable implements Runnable {
        private String filePath;
        private Queue<FileParcel> mQueue;
        private CountDownLatch mCountDownLatch;
        private int id;

        EncodeFileRunnable(String filePath, Queue<FileParcel> queue, CountDownLatch countDownLatch, int id) {
            this.filePath = filePath;
            this.mQueue = queue;
            this.mCountDownLatch = countDownLatch;
            this.id = id;
        }

        @Override
        public void run() {
            InputStream is = null;
            ByteArrayOutputStream os = null;
            try {
                File mFile = new File(filePath);
                if (mFile.exists()) {
                    is = new FileInputStream(mFile);
                    os = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = is.read(b, 0, 1024)) != -1) {
                        os.write(b, 0, len);
                    }
                    mQueue.offer(new FileParcel(id, mFile.getAbsolutePath(), Base64.encodeToString(os.toByteArray(), Base64.DEFAULT)));
                    Timber.i("enqueue");
                } else {
                    Timber.i("File no exists");
                }

            } catch (Throwable e) {
                Timber.i("throwwable");
                e.printStackTrace();
            } finally {
                FileUtils.closeIO(is);
                FileUtils.closeIO(os);
                mCountDownLatch.countDown();
            }


        }
    }

    protected static class JsChannelCallback {
        WeakReference<Handler.Callback> callback;

        JsChannelCallback(Handler.Callback callback) {
            this.callback = new WeakReference<>(callback);
        }

        public static JsChannelCallback create(Handler.Callback callback) {
            return new JsChannelCallback(callback);
        }

        public void call(String value) {
            if (this.callback != null && this.callback.get() != null) {
                this.callback.get().handleMessage(Message.obtain(null, "JsChannelCallback".hashCode(), value));
            }
        }
    }

    protected static class CovertFileThread extends Thread {
        private WeakReference<JsChannelCallback> mJsChannelCallback;
        private String[] paths;

        public CovertFileThread(JsChannelCallback JsChannelCallback, String[] paths) {
            super("agentweb-thread");
            this.mJsChannelCallback = new WeakReference<>(JsChannelCallback);
            this.paths = paths;
        }

        @Override
        public void run() {
            try {
                Queue<FileParcel> mQueue = convertFile(paths);
                String result = convertFileParcelObjectsToJson(mQueue);
                if (mJsChannelCallback != null && mJsChannelCallback.get() != null) {
                    mJsChannelCallback.get().call(result);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
