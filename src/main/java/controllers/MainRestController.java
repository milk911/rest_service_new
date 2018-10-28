package controllers;

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
        ScriptActions.postScript(scriptText, response);
     }

    @RequestMapping(value="/scripts/{id}", method= RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void getScriptResult(@PathVariable("id") RequestInfo requestInfo, HttpServletResponse response) {
         ScriptActions.getResult(requestInfo, response);
    }

    @RequestMapping(value="/scripts/{id}", method= RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void DeleteScript(@PathVariable("id") RequestInfo requestInfo, HttpServletResponse response) {
        ScriptActions.stopScript(requestInfo, response);
    }


}

