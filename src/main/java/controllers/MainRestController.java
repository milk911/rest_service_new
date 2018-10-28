package controllers;

import exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.RequestInfo;
import services.ScriptActions;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class MainRestController {

    @RequestMapping(value="/scripts", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void StartScript(@RequestBody String scriptText, HttpServletResponse response) {
        ScriptActions.postScript(scriptText);
    }

    @RequestMapping(value="/scripts/{id}", method= RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void DeleteScript(@PathVariable("id") RequestInfo requestInfo, HttpServletResponse response) {

        if (requestInfo == null) {
            throw new NotFoundException();
        }
        /*
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        if (id==null) {
            return;
        }

        if (!history.containsKey(Integer.parseInt(id))) {
            return;
        }

        RequestInfo requestInfo = history.get(Integer.parseInt(id));

        if (requestInfo.getScriptRunner()!=null)
            if (requestInfo.getScriptRunner().getScriptFinished()==0) {

                requestInfo.getScriptRunnerThread().interrupt();
                requestInfo.getScriptCheckerThread().interrupt();
                requestInfo.setResult(requestInfo.getResult() + "\n" + "INTERRUPTED BY CLIENT");
                try {
                    response.getWriter().print(requestInfo.getResult());
                    response.setStatus(HttpServletResponse.SC_OK); //202
                    requestInfo.setScriptRunner(null);
                }
                catch (IOException ex) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); //500

                }
                return;
            }

        try {
            response.getWriter().print("Script is not active");
            return;

        }
        catch (IOException ex) {}
        */
    }



}

