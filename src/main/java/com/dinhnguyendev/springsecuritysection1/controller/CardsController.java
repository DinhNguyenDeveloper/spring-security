package com.dinhnguyendev.springsecuritysection1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {



//    user, admin

    @GetMapping("cards")
    public String getCards(){
        return "get Cards successfully!!!";
    }
}
