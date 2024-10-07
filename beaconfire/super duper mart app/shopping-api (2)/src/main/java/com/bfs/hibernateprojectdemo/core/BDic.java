package com.bfs.hibernateprojectdemo.core;


public class BDic {
    // 定义成功状态码
    public static final int SUCCESS = 200;
    // 定义失败状态码
    public static final int FAIL = 403;

    // 定义状态码对应的描述信息
    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "You do not have permission to view this page, please contact the administrator.";

    // 私有构造函数，防止实例化
    private BDic() {
    }

    // 根据状态码获取对应的消息
    public static String getStatusMsg(int statusCode) {
        switch (statusCode) {
            case SUCCESS:
                return SUCCESS_MSG;
            case FAIL:
                return FAIL_MSG;
            default:
                return "未知错误";
        }
    }
}
