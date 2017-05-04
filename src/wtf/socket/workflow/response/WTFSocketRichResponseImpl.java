package wtf.socket.workflow.response;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wtf.socket.WTFSocketServer;
import wtf.socket.protocol.WTFSocketMessage;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by ZFly on 2017/5/3.
 */
@Component
@Scope("prototype")
public class WTFSocketRichResponseImpl implements WTFSocketRichResponse{

    private final List<WTFSocketResponseData> dataList = new ArrayList<>();

    @Resource
    private WTFSocketServer context;
    private boolean secure;

    WTFSocketRichResponseImpl() {}

    @Override
    public void addMessage(WTFSocketMessage message) {
        dataList.add(new WTFSocketResponseData(message));
    }

    @Override
    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    @Override
    public WTFSocketServer getContext() {
        return context;
    }

    @Override
    public List<WTFSocketResponseData> getResponseDataList() {
        return dataList;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public void setSecure(boolean secure) {
        this.secure = secure;
    }
}
