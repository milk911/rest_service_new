package controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import services.RequestInfo;
import services.ScriptActions;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class MainRestController {

    @PostMapping(value="/scripts")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void startScript(@RequestBody String scriptText, HttpServletResponse response) {
        ScriptActions.postScript(scriptText, response);
     }

    @GetMapping(value="/scripts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void getScriptResult(@PathVariable("id") RequestInfo requestInfo, HttpServletResponse response) {
         ScriptActions.getResult(requestInfo, response);
    }

    @DeleteMapping(value="/scripts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScript(@PathVariable("id") RequestInfo requestInfo, HttpServletResponse response) {
        ScriptActions.stopScript(requestInfo, response);
    }


}

