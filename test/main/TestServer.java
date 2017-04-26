package main;

import org.apache.commons.lang.StringUtils;
import parser.UserDefinedRegisterProtocolParser;
import wtf.socket.WTFSocket;
import wtf.socket.event.WTFSocketEventsType;
import wtf.socket.exception.fatal.WTFSocketInvalidSourceException;
import wtf.socket.schedule.WTFSocketConfig;


public class TestServer {

    public static void main(String[] args) {

        WTFSocket.PROTOCOL_FAMILY.registerParser(new UserDefinedRegisterProtocolParser());

        WTFSocket.EVENTS_GROUP.addEventListener(((item, info) -> {
            if (StringUtils.contains(item.getTerm().getIoTag(), "127.0.0.1"))
                throw new WTFSocketInvalidSourceException(item.getTerm().getIoTag());
        }), WTFSocketEventsType.Connect);

        WTFSocket.run(new WTFSocketConfig()
                .setTcpPort(1234)
                .setUseDebug(true));
    }
}
