package top.xuqingquan.http.log;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import top.xuqingquan.http.GlobalHttpHandler;
import top.xuqingquan.utils.Timber;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 许清泉 on 2019/4/14 20:51
 */
@Singleton
public class RequestInterceptor implements Interceptor {

    @Inject
    FormatPrinter mPrinter;
    @Inject
    GlobalHttpHandler mHandler;
    @Inject
    Level printLevel;

    @Inject
    public RequestInterceptor() {
    }


    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        boolean logRequest = printLevel == Level.ALL || printLevel == Level.REQUEST;
        if (logRequest) {
            //打印请求信息
            if (request.body() != null && HttpParseUtils.isParseable(request.body().contentType())) {
                mPrinter.printJsonRequest(request, HttpParseUtils.parseParams(request));
            } else {
                mPrinter.printFileRequest(request);
            }
        }
        boolean logResponse = printLevel == Level.ALL || printLevel == Level.RESPONSE;
        long t1 = logResponse ? System.nanoTime() : 0;
        Response originalResponse;
        try {
            originalResponse = chain.proceed(request);
        } catch (Exception e) {
            Timber.w("Http Error: " + e);
            throw e;
        }
        long t2 = logResponse ? System.nanoTime() : 0;
        ResponseBody responseBody = originalResponse.body();
        //打印响应结果
        String bodyString = null;
        if (responseBody != null && HttpParseUtils.isParseable(responseBody.contentType())) {
            bodyString = HttpParseUtils.printResult(originalResponse);
        }
        if (logResponse) {
            final List<String> segmentList = request.url().encodedPathSegments();
            final String header = originalResponse.headers().toString();
            final int code = originalResponse.code();
            final boolean isSuccessful = originalResponse.isSuccessful();
            final String message = originalResponse.message();
            final String url = originalResponse.request().url().toString();
            if (responseBody != null && HttpParseUtils.isParseable(responseBody.contentType())) {
                mPrinter.printJsonResponse(TimeUnit.NANOSECONDS.toMillis(t2 - t1), isSuccessful,
                        code, header, responseBody.contentType(), bodyString, segmentList, message, url);
            } else {
                mPrinter.printFileResponse(TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                        isSuccessful, code, header, segmentList, message, url);
            }
        }

        if (mHandler != null) {//这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);
        }
        return originalResponse;
    }

}