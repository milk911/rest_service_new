package services;

import org.springframework.http.HttpStatus;
import javax.script.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    public static void postScript(String scriptText, HttpServletResponse response) {
        int newId = counter.incrementAndGet();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        StringWriter scriptResult = getEngineScriptResult(engine);

        if (!compileScript(engine, scriptText, response)) {
            return;
        }

        RequestInfo requestInfo = RequestInfo.getNewRequestInfo(scriptResult);
        history.put(newId, requestInfo);

        ScriptRunner scriptRunner = ScriptRunner.getNewScriptRunner(scriptResult, engine, scriptText, newId);
        requestInfo.setHandler(service.submit(scriptRunner));
        response.addHeader("Location", "/scripts/" + newId);
    }

    public static void getResult(RequestInfo requestInfo, HttpServletResponse response) {
        printResultText(response, requestInfo.getScriptResult().toString());
    }

    public static void stopScript(RequestInfo requestInfo, HttpServletResponse response) {
        requestInfo.getHandler().cancel(true);
    }

    public static StringWriter getEngineScriptResult(ScriptEngine engine) {
        StringWriter scriptResult = new StringWriter();
        ScriptContext context = engine.getContext();
        context.setWriter(scriptResult);
        return scriptResult;
    }

    public static boolean compileScript(ScriptEngine engine, String scriptText, HttpServletResponse response) {
        Compilable compilingEngine = (Compilable) engine;
        try {
            CompiledScript compiledScript = compilingEngine.compile(scriptText);
            return true;
        }
        catch (ScriptException e) {
            sendError(response, HttpStatus.BAD_REQUEST, e.getMessage());
            return false;
        }
    }

    public static void printResultText(HttpServletResponse response, String text) {
        try {
            response.getWriter().print(text);
        }
        catch (IOException ex) {
            sendError(response, HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    public static void sendError(HttpServletResponse response, HttpStatus errorStatus, String errorText) {
        try {
            response.sendError(errorStatus.value(), errorText);
        }
        catch (IOException ex) {
            // TODO:
        }
    }

    public static Map<Integer, RequestInfo> getHistory() {
        return history;
    }
}
