package loggingAop.logging.controller;

import loggingAop.logging.aop.LogExecutionTime;
import loggingAop.logging.component.Interceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class HomeController {

    @GetMapping("home")
    @LogExecutionTime
    public String getHome() {
        return "get home";
    }

    @PostMapping("home")
    public String posthHome() {
        return "post home";
    }

    @PutMapping("home")
    public String putHome() {
        return "put home";
    }

    @PatchMapping("home")
    public String patchHome() {
        return "patch home";
    }

    @DeleteMapping("home")
    public String deleteHome() {
        return "delete home";
    }
}
