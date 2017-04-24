package wtf.socket.inter.delegate;

/**
 * 授权器接口
 *
 * Created by zfly on 2017/4/22.
 */
@FunctionalInterface
public interface WTFSocketAuthDelegate {

    boolean invoke(String from, String to);

}
