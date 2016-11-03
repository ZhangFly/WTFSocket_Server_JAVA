package wtf.apis;

import application.model.AppMsg;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import wtf.socket.protocols.templates.WTFSocketProtocol;
import wtf.socket.protocols.templates.WTFSocketProtocol_2_0;
import wtf.socket.main.WTFSocketHandler;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口执行者
 */
public class WTFSocketAPIsManager implements WTFSocketHandler {

    private ConcurrentHashMap<String, WTFSocketAPIsGroup> APIsVersions = new ConcurrentHashMap<>();

    /**
     * 创建新的接口版本
     *
     * @param name 版本名
     * @return 接口组对象
     */
    public WTFSocketAPIsGroup createVersion(String name) {

        WTFSocketAPIsGroup version = new WTFSocketAPIsGroup();
        APIsVersions.put(name, version);

        return version;

    }

    /**
     * 执行接口
     *
     * @param cxt     通信环境
     * @param request 数据
     */
    @Override
    public void invoke(Channel cxt, WTFSocketProtocol request, List<WTFSocketProtocol> responses) {

        JSONObject requestBody = request.getBody();

        if (requestBody == null) {
            WTFSocketProtocol_2_0 errResponse = WTFSocketProtocol_2_0.makeResponse(request);
            errResponse.setBody(AppMsg.failure(34, "lack necessary param => <body>"));
            responses.add(errResponse);
            return;
        }

        String version = requestBody.containsKey("version") ? requestBody.getString("version") : "1.0";

        if (!APIsVersions.containsKey(version)) {
            WTFSocketProtocol_2_0 errResponse = WTFSocketProtocol_2_0.makeResponse(request);
            errResponse.setBody(AppMsg.failure(36, "invalid APIs version => <" + version + ">"));
            responses.add(errResponse);
            return;
        }

        APIsVersions.get(version).doAction(cxt, request, responses);
    }

}
