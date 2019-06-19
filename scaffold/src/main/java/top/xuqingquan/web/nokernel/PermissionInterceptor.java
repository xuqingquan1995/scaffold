package top.xuqingquan.web.nokernel;

public interface PermissionInterceptor {
    boolean intercept(String url, String[] permissions, String action);
}
