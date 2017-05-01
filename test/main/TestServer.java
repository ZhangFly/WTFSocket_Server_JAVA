package main;

import org.apache.commons.lang.StringUtils;
import parser.UserDefinedRegisterProtocolParser;
import wtf.socket.WTFSocketServer;
import wtf.socket.WTFSocketConfig;
import wtf.socket.secure.delegate.WTFSocketSecureDelegateType;

public class TestServer {

    public static void main(String[] args) {

        final WTFSocketServer server = new WTFSocketServer();

        server.getProtocolFamily().registerParser(new UserDefinedRegisterProtocolParser());

        server.getSecureDelegatesGroup().addDelegate(msg -> StringUtils.equals(msg.getFrom(), "123") && StringUtils.equals(msg.getTo(), "321"), WTFSocketSecureDelegateType.SEND_PERMISSION);

        server.run(new WTFSocketConfig()
                .setTcpPort(1234)
                .setUseDebug(true)
                .addEOT("\\r\\n"));
    }
}
