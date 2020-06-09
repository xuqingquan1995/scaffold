package top.xuqingquan.sample;

import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;
import com.swift.sandhook.annotation.Param;
import com.swift.sandhook.annotation.SkipParamCheck;
import com.swift.sandhook.annotation.ThisObject;
import com.swift.sandhook.wrapper.HookWrapper;

import java.lang.reflect.Method;

import top.xuqingquan.utils.Timber;

/**
 * Create by 许清泉 on 2020/6/9 21:42
 */
@HookClass(Integer.class)
public class IntegerHook {

    @HookMethodBackup("parseInt")
    @MethodParams(String.class)
    static Method parseIntBackup;

    @HookMethod("parseInt")
    @MethodParams(String.class)
    public static int parseInt(String str) throws Throwable {
        Timber.e("parseInt===>" + str);
        if (str.contains("=")){
            str=str.split("=")[1];
        }
        return (int)SandHook.callOriginByBackup(parseIntBackup, null, str);
    }
}
