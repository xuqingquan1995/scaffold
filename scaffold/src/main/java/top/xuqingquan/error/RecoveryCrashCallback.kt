package top.xuqingquan.error

import com.zxy.recovery.callback.RecoveryCallback
import top.xuqingquan.utils.Timber

/**
 * Created by 许清泉 on 2019-04-27 15:33
 */
class RecoveryCrashCallback : RecoveryCallback {
    override fun stackTrace(stackTrace: String) {
        Timber.e(stackTrace)
    }

    override fun cause(cause: String) {
        Timber.e(cause)
    }

    override fun exception(
        throwExceptionType: String,
        throwClassName: String,
        throwMethodName: String,
        throwLineNumber: Int
    ) {
        Timber.e(throwExceptionType)
        Timber.e(throwClassName)
        Timber.e(throwMethodName)
        Timber.e(throwLineNumber.toString())
    }

    override fun throwable(throwable: Throwable) {
        Timber.e(throwable)
    }
}
