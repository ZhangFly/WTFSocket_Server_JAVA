package wtf.socket.util;

/**
 * 框架中优先级定义
 * 建议使用 HIGH ~ LOW
 * HIGHEST, LOWEST 可能被一些服务器保留方法使用
 * <p>
 * Created by ZFly on 2017/4/29.
 */
public final class WTFSocketPriority {

    private WTFSocketPriority() {
    }

    public final static int HIGHEST = -3;
    public final static int HIGH = -2;
    public final static int MEDIUM_HIGH = -1;
    public final static int MEDIUM = 0;
    public final static int MEDIUM_LOW = 1;
    public final static int LOW = 2;
    public final static int LOWEST = 3;

}
