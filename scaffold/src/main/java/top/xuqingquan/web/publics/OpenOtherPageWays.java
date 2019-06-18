package top.xuqingquan.web.publics;

/**
 * Created by 许清泉 on 2019-06-19 00:30
 */
public enum OpenOtherPageWays {

    /**
     * 直接打开跳转页
     */
    DERECT(AgentWebConfig.DERECT_OPEN_OTHER_PAGE),
    /**
     * 咨询用户是否打开
     */
    ASK(AgentWebConfig.ASK_USER_OPEN_OTHER_PAGE),
    /**
     * 禁止打开其他页面
     */
    DISALLOW(AgentWebConfig.DISALLOW_OPEN_OTHER_APP);
    public int code;

    OpenOtherPageWays(int code) {
        this.code = code;
    }
}
