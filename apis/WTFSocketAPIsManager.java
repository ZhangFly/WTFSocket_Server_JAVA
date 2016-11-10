package wtf.apis;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import wtf.socket.main.WTFSocketHandler;
import wtf.socket.protocols.templates.WTFSocketProtocol;
import wtf.socket.protocols.templates.WTFSocketProtocol_2_0;

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
            responses.add(request);
            return;
        }

        String version = requestBody.containsKey("version") ? requestBody.getString("version") : "1.0";

        if (!APIsVersions.containsKey(version)) {
            WTFSocketProtocol_2_0 errResponse = WTFSocketProtocol_2_0.makeResponse(request);
            JSONObject body = new JSONObject();
            body.put("flag", 0);
            body.put("errCode", 34);
            body.put("cause", "invalid APIs version => <" + version + ">");
            errResponse.setBody(body);
            responses.add(errResponse);
            return;
        }

        APIsVersions.get(version).doAction(cxt, request, responses);
    }

}
