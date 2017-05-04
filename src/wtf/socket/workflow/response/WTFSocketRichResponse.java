package wtf.socket.workflow.response;

import wtf.socket.WTFSocketServer;

import java.util.List;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
public interface WTFSocketRichResponse extends WTFSocketResponse{

    WTFSocketServer getContext();

    List<WTFSocketResponseData> getResponseDataList();

    boolean isSecure();

    void setSecure(boolean secure);
}
