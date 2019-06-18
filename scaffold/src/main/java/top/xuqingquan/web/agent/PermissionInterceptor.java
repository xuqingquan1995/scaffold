package top.xuqingquan.web.agent;

public interface PermissionInterceptor {
    boolean intercept(String url, String[] permissions, String action);
}
