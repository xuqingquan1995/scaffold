package top.xuqingquan.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 许清泉 on 2019-08-08 21:30
 */
@RunWith(AndroidJUnit4.class)
public class DeviceTest {
    private final static String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_FLYME_VERSION_NAME = "ro.build.display.id";
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";

    @Test
    public void deviceTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Properties properties = new Properties();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // android 8.0，读取 /system/uild.prop 会报 permission denied
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
                properties.load(fileInputStream);
            } catch (Throwable e) {
                Timber.e(e, "read file error");
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Class<?> clzSystemProperties;
        try {
            clzSystemProperties = Class.forName("android.os.SystemProperties");
            Method getMethod = clzSystemProperties.getDeclaredMethod("get", String.class);
            // miui
            String sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME);
            System.out.println("DeviceTest::sMiuiVersionName===>"+sMiuiVersionName);
            //flyme
            String sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME);
            System.out.println("DeviceTest::sFlymeVersionName===>"+sFlymeVersionName);
            getFlymeVersion(sFlymeVersionName);
            // emus
            String sEmuiVersionName = getLowerCaseName(properties, getMethod, KEY_EMUI_VERSION_NAME);
            System.out.println("DeviceTest::sEmuiVersionName===>"+sEmuiVersionName);
            getFlymeVersion(sEmuiVersionName);
        } catch (Throwable e) {
            Timber.e(e, "read SystemProperties error");
        }
    }


    public static void getFlymeVersion(String flymeVersion) {
        if (flymeVersion != null && !flymeVersion.equals("")) {
            Pattern pattern = Pattern.compile("(\\d+\\.){2}\\d");
            Matcher matcher = pattern.matcher(flymeVersion);
            if (matcher.find()) {
                String versionString = matcher.group();
                System.out.println("DeviceTest::sFlymeVersionName===>"+versionString);
            }
        }
    }


    @Nullable
    private static String getLowerCaseName(Properties p, Method get, String key) {
        String name = p.getProperty(key);
        if (name == null) {
            try {
                name = (String) get.invoke(null, key);
            } catch (Throwable ignored) {
            }
        }
        if (name != null) name = name.toLowerCase();
        return name;
    }

}
