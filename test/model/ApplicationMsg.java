package model;

import com.alibaba.fastjson.JSONArray;

public class ApplicationMsg {

    private Integer flag;
    private Integer cmd;
    private Integer errCode;
    private JSONArray params;
    private String version = "1.0";
    private String cause = null;

    public static ApplicationMsg failure(int errCode, String cause) {
        return new ApplicationMsg().setFlag(0).setErrCode(errCode).setCause(cause);
    }

    public Integer getFlag() {
        return flag;
    }

    public ApplicationMsg setFlag(Integer flag) {
        this.flag = flag;
        return this;
    }

    public Integer getCmd() {
        return cmd;
    }

    public ApplicationMsg setCmd(Integer cmd) {
        this.cmd = cmd;
        return this;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public ApplicationMsg setErrCode(Integer errCode) {
        this.errCode = errCode;
        return this;
    }

    public JSONArray getParams() {
        return params;
    }

    public ApplicationMsg setParams(JSONArray params) {
        this.params = params;
        return this;
    }

    public ApplicationMsg addParam(Object param) {
        if (params == null) {
            params = new JSONArray();
        }
        params.add(param);
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ApplicationMsg setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getCause() {
        return cause;
    }

    public ApplicationMsg setCause(String cause) {
        this.cause = cause;
        return this;
    }
}
