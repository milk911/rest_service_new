package services;

import org.springframework.http.HttpStatus;

import javax.script.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        if (!compileScript(engine, scriptText, response)) {
            return;
        }

        RequestInfo requestInfo = RequestInfo.getNewRequestInfo(engine, scriptText);
        history.put(newId, requestInfo);

        requestInfo.setHandler(service.submit(requestInfo));
        response.addHeader("Location", "/scripts/" + newId);
    }

    public static void getResult(RequestInfo requestInfo, HttpServletResponse response) {
        if (!checkRequestInfoNullable(requestInfo, response)) return;
        if (requestInfo.getCompleteStatus()==2) {
            sendError(response, HttpStatus.BAD_REQUEST, requestInfo.getScriptResult().toString()); // 400
            return;
        }
        else if (requestInfo.getCompleteStatus()==1) {
            response.setStatus(HttpStatus.ACCEPTED.value()); //202
        }
        printResultText(response, requestInfo.getScriptResult().toString());
    }

    public static void stopScript(RequestInfo requestInfo, HttpServletResponse response) {
        if (!checkRequestInfoNullable(requestInfo, response)) return;
        requestInfo.getHandler().cancel(true);
    }

    public static boolean compileScript(ScriptEngine engine, String scriptText, HttpServletResponse response) {
        Compilable compilingEngine = (Compilable) engine;
        try {
            CompiledScript compiledScript = compilingEngine.compile(scriptText);
            return true;
        }
        catch (ScriptException e) {
            sendError(response, HttpStatus.BAD_REQUEST, e.getMessage()); // 400
            return false;
        }
    }

    public static void printResultText(HttpServletResponse response, String text) {
        try {
            response.getWriter().print(text);
        }
        catch (IOException ex) {
            sendError(response, HttpStatus.INTERNAL_SERVER_ERROR, ""); // 500
        }
    }

    public static void sendError(HttpServletResponse response, HttpStatus errorStatus, String errorText) {
        try {
            response.sendError(errorStatus.value(), errorText);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean checkRequestInfoNullable(RequestInfo requestInfo, HttpServletResponse response) {
        if (requestInfo == null) {
            sendError(response, HttpStatus.NOT_FOUND, "Script doesn't exist");
            return false;
        }
        return true;
    }

    public static Map<Integer, RequestInfo> getHistory() {
        return history;
    }
}
