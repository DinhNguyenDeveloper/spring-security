package com.dinhnguyendev.springsecuritysection1.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dinhnguyendev.springsecuritysection1.model.CustomerEntity;
import com.dinhnguyendev.springsecuritysection1.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailServiceCustomer implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomerEntity customerEntity = this.customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User details not found for the user" + username));

        List<GrantedAuthority> authorities = customerEntity.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(
                        Collectors.toList());
//        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customerEntity.getRole()));
        return new User(customerEntity.getEmail(), customerEntity.getPwd(), authorities);
    }
}
