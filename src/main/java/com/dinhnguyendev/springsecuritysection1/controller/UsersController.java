package com.dinhnguyendev.springsecuritysection1.controller;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dinhnguyendev.springsecuritysection1.constants.ApplicationConstants;
import com.dinhnguyendev.springsecuritysection1.model.CustomerEntity;
import com.dinhnguyendev.springsecuritysection1.model.LoginRequestDto;
import com.dinhnguyendev.springsecuritysection1.model.LoginResponseDto;
import com.dinhnguyendev.springsecuritysection1.repository.CustomerRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@RestController
public class UsersController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Environment environment;

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

    @GetMapping("user")
    public String getUser() {
        return "get User successfully!!!";
    }

    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        String jwt = "";

        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(request.username(),
                request.password());

        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        if (Objects.nonNull(authenticationResponse) && authenticationResponse.isAuthenticated()) {
            String secret = environment.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                    ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            jwt = Jwts.builder().issuer("Dinhnguyendev Bank")
                    .subject("JWT Token")
                    .claim("username", authenticationResponse.getName())
                    .claim("authorities",
                            authenticationResponse.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
                                    Collectors.joining(",")))
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + 30000000))
                    .signWith(secretKey).compact();
        }
        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER, jwt).body(
                new LoginResponseDto(HttpStatus.OK.getReasonPhrase(), jwt)
        );
    }

}
