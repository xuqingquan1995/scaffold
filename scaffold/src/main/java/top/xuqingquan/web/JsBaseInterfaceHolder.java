package top.xuqingquan.web;

import android.webkit.JavascriptInterface;
import top.xuqingquan.web.agent.AgentWebConfig;
import top.xuqingquan.web.agent.JsInterfaceHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class JsBaseInterfaceHolder implements JsInterfaceHolder {

	@Override
	public boolean checkObject(Object v) {
		if (AgentWebConfig.WEBVIEW_TYPE == AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE) {
			return true;
		}
		boolean tag = false;
		Class clazz = v.getClass();
		Method[] mMethods = clazz.getMethods();
		for (Method mMethod : mMethods) {
			Annotation[] mAnnotations = mMethod.getAnnotations();
			for (Annotation mAnnotation : mAnnotations) {
				if (mAnnotation instanceof JavascriptInterface) {
					tag = true;
					break;
				}
			}
			if (tag) {
				break;
			}
		}
		return tag;
	}

}
