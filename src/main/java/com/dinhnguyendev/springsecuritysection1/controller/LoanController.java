package com.dinhnguyendev.springsecuritysection1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    @GetMapping("loan")
    public String getLoan() {
        return "get Loan successfully!!!";
    }
}
