package wtf.socket.protocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import wtf.socket.exception.WTFSocketException;
import wtf.socket.exception.normal.WTFSocketProtocolBrokenException;
import wtf.socket.exception.normal.WTFSocketProtocolUnsupportedException;
import wtf.socket.protocol.msg.WTFSocketDefaultProtocolParser;
import wtf.socket.workflow.WTFSocketWork;
import wtf.socket.workflow.request.WTFSocketRichRequest;
import wtf.socket.workflow.response.WTFSocketResponseData;
import wtf.socket.workflow.response.WTFSocketRichResponse;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 协议簇，通过向协议簇注册解析器
 * 将不同协议的数据包统一转化到 WTFSocketMessage
 * <p>
 * Created by ZFly on 2017/4/21.
 */
@Component
public class WTFSocketProtocolFamily implements WTFSocketWork {

    private static final Log logger = LogFactory.getLog(WTFSocketProtocolFamily.class);

    private Queue<WTFSocketProtocolParser> parsers = new PriorityQueue<WTFSocketProtocolParser>(Comparator.comparingInt(WTFSocketProtocolParser::getPriority)) {{
        add(new WTFSocketDefaultProtocolParser());
    }};

    @Override
    public void execute(WTFSocketRichRequest request, WTFSocketRichResponse response) {
        logger.debug("WTFSocketProtocolFamily work");
        if (null == request.getMessage()) {
            try {
                final WTFSocketMessage message = parse(request.getDataPacket());
                message.setIoTag(request.getIoTag());
                message.setConnectType(request.getConnectType());

                request.setMessage(message);
            } catch (WTFSocketException e) {
                request.setException(e);
            }
        }

        if (!response.getResponseDataList().isEmpty()) {
            for (WTFSocketResponseData responseData : response.getResponseDataList()) {
                try {
                    responseData.setDataPacket(parse(responseData.getMessage()));
                } catch (WTFSocketException e) {
                    request.setException(e);
                }
            }
        }
        logger.debug("WTFSocketProtocolFamily finish");
    }

    public WTFSocketMessage parse(String dataPacket) throws WTFSocketProtocolBrokenException, WTFSocketProtocolUnsupportedException {
        for (WTFSocketProtocolParser parser : parsers) {
            if (parser.isResponse(dataPacket)) {
                logger.debug("Use parser [" + parser.getClass().getSimpleName() + "]");
                final WTFSocketMessage message = parser.parse(dataPacket);
                logger.debug("Convert string [" + dataPacket + "] to message");
                return message;
            }
        }
        throw new WTFSocketProtocolUnsupportedException("There was no protocol parser to convert string to message");
    }

    public String parse(WTFSocketMessage msg) throws WTFSocketProtocolUnsupportedException {
        for (WTFSocketProtocolParser parser : parsers) {
            if (parser.isResponse(msg)) {
                logger.debug("Use parser [" + parser.getClass().getSimpleName() + "]");
                final String dataPacket = parser.parse(msg);
                logger.debug("Convert message to string [" + dataPacket + "]");
                return dataPacket;
            }
        }
        throw new WTFSocketProtocolUnsupportedException("There was no protocol parser to convert message to string");
    }

    public void registerParser(WTFSocketProtocolParser parser) {
        parsers.add(parser);
    }

    public void unRegisterParser(WTFSocketProtocolParser parser) {
        parsers.remove(parser);
    }

    public void clear() {
        parsers.clear();
    }

    public int size() {
        return parsers.size();
    }
}
