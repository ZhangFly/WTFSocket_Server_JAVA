package main;

import parser.UserDefinedRegisterProtocolParser;
import wtf.socket.WTFSocketServer;
import wtf.socket.WTFSocketConfig;
import wtf.socket.controller.WTFSocketControllers;

public class TestServer {

    public static void main(String[] args) {

        final WTFSocketServer server = WTFSocketServer.newNettyServer();

        server.getProtocolFamily().registerParser(new UserDefinedRegisterProtocolParser());

//        server.getSecureDelegateGroup().addDelegate(msg -> StringUtils.equals(msg.getFrom(), "123") && StringUtils.equals(msg.getTo(), "321"), WTFSocketSecureDelegateType.SEND_PERMISSION);

        server.getControllerGroup().add(WTFSocketControllers.heartbeatController());

        server.run(new WTFSocketConfig()
                .setTcpPort(1234)
                .setUseDebug(true)
                .addEOT("\\r\\n"));
    }
}
