package com.dinhnguyendev.springsecuritysection1.controller;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dinhnguyendev.springsecuritysection1.model.CustomerEntity;
import com.dinhnguyendev.springsecuritysection1.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UsersController {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody CustomerEntity customerEntity) {
        try {
            String hasPwd = this.passwordEncoder.encode(customerEntity.getPwd());
            customerEntity.setPwd(hasPwd);
            CustomerEntity saveCustomer = this.customerRepository.save(customerEntity);
            if (Objects.nonNull(saveCustomer.getId())) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body("create user successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("An Exception bad request occurred");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An Exception occurred" + e.getMessage());
        }
    }

}
