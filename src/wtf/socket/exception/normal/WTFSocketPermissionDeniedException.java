package wtf.socket.exception.normal;

/**
 * 没有权限异常
 * 发送放必须持有授权许可才能向目标发送数据
 *
 * Created by zfly on 2017/4/22.
 */
public class WTFSocketPermissionDeniedException extends WTFSocketNormalException {

    public WTFSocketPermissionDeniedException(String target) {
        super("Permission denied to <" + target + ">");
    }

    @Override
    public int getErrCode() {
        return 67;
    }
}
