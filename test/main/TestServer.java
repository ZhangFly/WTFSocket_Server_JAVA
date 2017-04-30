package main;

import parser.UserDefinedRegisterProtocolParser;
import wtf.socket.WTFSocketServer;
import wtf.socket.WTFSocketConfig;
import wtf.socket.controller.WTFSocketControllers;

public class TestServer {

    public static void main(String[] args) {
        final WTFSocketServer server = new WTFSocketServer();

        server.addController(WTFSocketControllers.unconditionalRegisterController());
        server.addController(WTFSocketControllers.echoController());

        server.getProtocolFamily().registerParser(new UserDefinedRegisterProtocolParser());

        server.run(new WTFSocketConfig()
                .setTcpPort(1234)
                .setUseDebug(true)
                .addEOT("\\r\\n"));
    }
}
