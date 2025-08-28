package com.dinhnguyendev.springsecuritysection1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {


    @GetMapping("balance")
    public String getBalance() {
        return "get Balance successfully!";
    }
}
