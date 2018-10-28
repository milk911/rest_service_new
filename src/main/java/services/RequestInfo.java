package services;

import java.io.StringWriter;
import java.util.concurrent.Future;


public class RequestInfo {

    private StringWriter    scriptResult;
    private Integer         errorStatus = 0;
    private Future          handler;

    private RequestInfo() {
    }

    public static RequestInfo getNewRequestInfo(StringWriter scriptResult) {
        RequestInfo requestInfo     = new RequestInfo();
        requestInfo.scriptResult    = scriptResult;
        return requestInfo;
    }

    public void setHandler(Future handler) {
        this.handler = handler;
    }
}
