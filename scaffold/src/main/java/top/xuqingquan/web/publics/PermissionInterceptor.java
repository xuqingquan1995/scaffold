package top.xuqingquan.web.publics;

public interface PermissionInterceptor {
    boolean intercept(String url, String[] permissions, String action);
}
