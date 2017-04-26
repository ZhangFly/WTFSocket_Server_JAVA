package controller;

import com.alibaba.fastjson.JSONObject;
import model.ApplicationMsg;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import wtf.socket.WTFSocket;
import wtf.socket.controller.WTFSocketController;
import wtf.socket.protocol.WTFSocketMsg;
import wtf.socket.routing.item.WTFSocketRoutingTmpItem;

import java.util.List;

/**
 * 注册功能
 */
@Controller
public class RegisterController implements WTFSocketController {

    @Override
    public boolean isResponse(WTFSocketMsg msg) {
        ApplicationMsg body = msg.getBody(ApplicationMsg.class);
        return body.getCmd() != null &&
                body.getCmd() == 64;
    }

    public void work(WTFSocketMsg msg, List<WTFSocketMsg> responses) {

        final ApplicationMsg body = msg.getBody(ApplicationMsg.class);
        final WTFSocketRoutingTmpItem item = WTFSocket.ROUTING.TMP_MAP.getItem(msg.getIoTag());

        if (item == null) {
            final WTFSocketMsg response = msg.makeResponse();
            response.setBody(new ApplicationMsg().setFlag(1));
            responses.add(response);
            return;
        }

        item.setAddress(msg.getFrom());
        item.setAccept(msg.getVersion());

        if (body.getParams() != null) {
            final JSONObject param = body.getParams().getJSONObject(0);
            final String itemType = param.getString("deviceType");
            if (itemType != null)
                item.setType(itemType);
        }else {
            item.setType("Unknown");
        }

        if (StringUtils.startsWith(msg.getFrom(), "Debug_")) {
            item.shiftToDebug();
        }else {
            item.shiftToFormal();
        }

        final WTFSocketMsg response = msg.makeResponse();
        response.setBody(new ApplicationMsg().setFlag(1));
        responses.add(response);
    }
}
