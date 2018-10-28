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
            //scriptFinished = 1;
        }
        catch (ScriptException se)
        {
            //System.out.println(se.getMessage());
            scriptResult.write(se.getMessage());
            //scriptFinished = 2;
        }

        System.out.println(scriptResult);

    }
}
