package com.example.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {

    @GetMapping("/info")
    @ResponseBody
    public String getDetailsUsingRequestParam(@RequestParam(name = "alternativeName", defaultValue = "defaultValue") String user) {
        return "<h2>You have requested for details of the <i>"+ user+"</i> using default request param</h2>";
    }

    @GetMapping("/")
    public String formPage() {
        return "editName";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/logout-success")
    public String logoutPage() {
        return "logout";
    }


}
