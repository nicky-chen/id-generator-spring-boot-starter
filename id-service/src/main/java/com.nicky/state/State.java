package com.nicky.state;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/7/25 at 15:36
 * 自动流转状态模式
 */
public interface State {

    /**
     * 获取id
     */
    void getIdEvent(Context context);

    /**
     * 等待id重新获取
     *
     */
    void waitEvent(Context context);

    /**
     * 出异常
     */
    void handlerErrorEvent(Context context);

    /**
     * 失败
     * @param context
     */
    void failEvent(Context context);


    String getCurrentState();

}
