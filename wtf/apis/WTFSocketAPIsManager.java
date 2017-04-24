package wtf.apis;

import com.alibaba.fastjson.JSONObject;
import wtf.socket.schedule.WTFSocketHandler;
import wtf.socket.protocol.WTFSocketMsg;

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

    @Override
    public void invoke(WTFSocketMsg request, List<WTFSocketMsg> responses) {

        final JSONObject requestBody = request.getBody();

        if (requestBody == null) {
            responses.add(request);
            return;
        }

        final String version = requestBody.containsKey("version") ? requestBody.getString("version") : "1.0";

        if (!APIsVersions.containsKey(version)) {
            final WTFSocketMsg errResponse = request.makeResponse();
            errResponse.setBody(new JSONObject() {{
                put("flag", 0);
                put("errCode", 34);
                put("cause", "invalid APIs version => <" + version + ">");
            }});
            responses.add(errResponse);
            return;
        }

        APIsVersions.get(version).doAction(request, responses);
    }

}
