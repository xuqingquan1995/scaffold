package top.xuqingquan.web;

public class HookManager {

    public static AgentWeb hookAgentWeb(AgentWeb agentWeb, AgentWeb.AgentBuilder agentBuilder) {
        return agentWeb;
    }

    public static boolean permissionHook(String url, String[] permissions) {
        return true;
    }
}
