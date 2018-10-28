package services;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ScriptActions {

    private static AtomicInteger                counter = new AtomicInteger(0);
    private static Map<Integer, RequestInfo>    history = Collections.synchronizedMap(new HashMap<Integer, RequestInfo>());
    private static ExecutorService              service = Executors.newScheduledThreadPool(10);
    private static ScriptEngineManager          factory = new ScriptEngineManager();

    public static void postScript(String scriptText) {

        int newId = counter.incrementAndGet();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        StringWriter scriptResult = new StringWriter();
        ScriptContext context = engine.getContext();
        context.setWriter(scriptResult);

        RequestInfo requestInfo = RequestInfo.getNewRequestInfo(scriptResult);
        history.put(newId, requestInfo);

        ScriptRunner scriptRunner = ScriptRunner.getNewScriptRunner(scriptResult, engine, scriptText, newId);

        requestInfo.setHandler(service.submit(scriptRunner));

        //final ScriptRunner scriptRunner = ScriptRunner.getScriptRunner (id, 0, engine, jsText);
    }

    public static Map<Integer, RequestInfo> getHistory() {
        return history;
    }
}
