package wtf.socket.workflow;

import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
public interface WTFSocketWork {

    void execute(WTFSocketRichRequest request, WTFSocketRichResponse response);

}
