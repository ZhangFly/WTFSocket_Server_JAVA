package controller;

import model.ApplicationMsg;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import wtf.socket.controller.WTFSocketSimpleController;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.protocol.WTFSocketMessage;
import wtf.socket.routing.client.WTFSocketClient;
import wtf.socket.routing.client.WTFSocketTmpClient;
import wtf.socket.workflow.response.WTFSocketResponse;

import java.util.List;

/**
 * 注册功能
 */
@Controller
public class RegisterController implements WTFSocketSimpleController {

    @Override
    public boolean isResponse(WTFSocketMessage msg) {
        ApplicationMsg body = msg.getBody(ApplicationMsg.class);
        return body.getCmd() != null &&
                body.getCmd() == 64;
    }

    @Override
    public boolean work(WTFSocketClient item, WTFSocketMessage msg, WTFSocketResponse response) throws WTFSocketException{

        final ApplicationMsg body = msg.getBody(ApplicationMsg.class);

        if (!(item instanceof WTFSocketTmpClient)) {
            throw new WTFSocketInvalidSourceException("[" + item.getTerm().getIoTag() + "] has registered" );
        }

        item.setAddress(msg.getFrom());
        item.setAccept(msg.getVersion());

        if (body.hasParams())
            item.setDeviceType(body.firstParam().getString("deviceType"));

        if (StringUtils.startsWith(msg.getFrom(), "Debug_")) {
            ((WTFSocketTmpClient) item).shiftToDebug();
        }else {
            ((WTFSocketTmpClient) item).shiftToFormal();
        }

        final WTFSocketMessage message = msg.makeResponse();
        message.setFrom("server");
        message.setBody(ApplicationMsg.success());
        response.addMessage(message);

        return true;
    }
}
