package services;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.util.concurrent.Future;

public class RequestInfo implements Runnable{

    private Integer         completeStatus = 0;
    private Future          handler;
    private ScriptEngine    engine;
    private String          scriptText;
    private StringWriter    scriptResult;

    private RequestInfo() {
    }

    public static RequestInfo getNewRequestInfo(ScriptEngine engine, String scriptText) {
        RequestInfo requestInfo     = new RequestInfo();
        requestInfo.engine          = engine;
        requestInfo.scriptText      = scriptText;

        requestInfo.initEngineOutput();

        return requestInfo;
    }

    @Override
    public void run() {
        try {
            engine.eval(scriptText);
            completeStatus = 1;
        }
        catch (ScriptException se)
        {
            scriptResult.write(se.getMessage());
            completeStatus = 2;
        }
    }

    private void initEngineOutput() {
        scriptResult = new StringWriter();
        ScriptContext context = engine.getContext();
        context.setWriter(scriptResult);
     }

    public void setHandler(Future handler) {
        this.handler = handler;
    }

    public Future getHandler() {
        return handler;
    }

    public StringWriter getScriptResult() {
        return scriptResult;
    }

    public Integer getCompleteStatus() {
        return completeStatus;
    }
}
