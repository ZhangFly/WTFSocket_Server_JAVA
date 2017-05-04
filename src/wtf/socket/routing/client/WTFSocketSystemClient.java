package wtf.socket.routing.client;

import org.springframework.stereotype.Component;
import wtf.socket.io.WTFSocketIOTerm;

/**
 *
 * Created by ZFly on 2017/5/4.
 */
@Component
public class WTFSocketSystemClient extends WTFSocketFormalClient {

    WTFSocketSystemClient() {
        setDeviceType("System");
        setAccept("2.0");
        addAuthTarget("*");
        setCover(false);
    }

    public void setAddress(String address) {
        setTerm(new WTFSocketIOTerm() {
            @Override
            public String getIoTag() {
                return "0";
            }

            @Override
            public void setIoTag(String ioTag) {
            }

            @Override
            public String getConnectType() {
                return "System";
            }

            @Override
            public void setConnectType(String connectType) {
            }

            @Override
            public void write(String data) {
                logger.info("System receive:\n" + data);
            }

            @Override
            public void close() {
            }
        });
        super.setAddress(address);
    }
}
