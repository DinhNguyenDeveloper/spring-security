package com.dinhnguyendev.springsecuritysection1.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    @GetMapping("loan")
//    @PreAuthorize("hasRole('USER')")
    public String getLoan() {
        return "get Loan successfully!!!";
    }
}
