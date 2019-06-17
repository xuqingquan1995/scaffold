package top.xuqingquan.web;

public interface PermissionInterceptor {
    boolean intercept(String url, String[] permissions, String action);
}
