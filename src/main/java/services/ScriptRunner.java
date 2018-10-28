package services;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.StringWriter;

public class ScriptRunner implements Runnable{

    private StringWriter    scriptResult;
    private Integer         errorStatus = 0;
    private ScriptEngine    engine;
    private String          scriptText;
    private int             id;

    private ScriptRunner() {
    }

    public static ScriptRunner getNewScriptRunner(StringWriter scriptResult, ScriptEngine engine, String scriptText, int id) {
        ScriptRunner scriptRunner   = new ScriptRunner();
        scriptRunner.scriptResult   = scriptResult;
        scriptRunner.engine         = engine;
        scriptRunner.scriptText     = scriptText;
        scriptRunner.id             = id;
        return scriptRunner;
    }

    @Override
    public void run() {
        try {
            engine.eval(scriptText);
        }
        catch (ScriptException se)
        {
            scriptResult.write(se.getMessage());
        }

        System.out.println(scriptResult);
    }
}
