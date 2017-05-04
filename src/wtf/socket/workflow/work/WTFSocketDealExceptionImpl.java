package wtf.socket.workflow.work;

import org.springframework.stereotype.Component;
import wtf.socket.exception.fatal.WTFSocketFatalException;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketRichResponse;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
@Component
public class WTFSocketDealExceptionImpl extends WTFSocketLogSupport implements WTFSocketWork {

    WTFSocketDealExceptionImpl() {}

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {
        logger.debug("WTFSocketLogException work");
        if (null != request.getException()) {

            final WTFSocketClient sourceClient = request.getSourceClient();
            final String exceptionMsg = String.format(
                    "Exception[%s] on [%s]:\n%s\n",
                    sourceClient.getTerm().getConnectType(),
                    sourceClient.getAddress(),
                    request.getException().getMessage());

            sourceClient.getTerm().write(exceptionMsg);
            if (request.getException() instanceof WTFSocketFatalException) {
                sourceClient.close();
            }
            output(exceptionMsg);
        }
        logger.debug("WTFSocketLogException finish");
    }
}
