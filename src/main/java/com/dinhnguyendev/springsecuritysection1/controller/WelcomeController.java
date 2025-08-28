package com.dinhnguyendev.springsecuritysection1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class WelcomeController {

    @RequestMapping(value = "welcome", method = RequestMethod.GET)
    @ResponseBody
    public String welcome() {
       return "welcome controller to spring security course";
    }
}
