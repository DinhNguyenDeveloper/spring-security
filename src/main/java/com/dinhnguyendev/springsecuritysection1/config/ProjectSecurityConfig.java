package com.dinhnguyendev.springsecuritysection1.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.dinhnguyendev.springsecuritysection1.exceptionhadling.CustomAccessDeniedHandler;
import com.dinhnguyendev.springsecuritysection1.exceptionhadling.CustomBasicAuthenticationEntryPoint;
import com.dinhnguyendev.springsecuritysection1.filter.AuthoritiesLoggingAfterFiler;
import com.dinhnguyendev.springsecuritysection1.filter.JWTTokenGeneratorFilter;
import com.dinhnguyendev.springsecuritysection1.filter.JwtTokenValidatorFilter;
import com.dinhnguyendev.springsecuritysection1.filter.RequestValidationBeforeFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class  ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.
                securityContext(contextConfig -> contextConfig.requireExplicitSave(false)) // Remove this config when deploying to Production
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        //                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(List.of("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
//                .sessionManagement(smc -> smc.invalidSessionUrl("/InvalidSession"))
//                .redirectToHttps(withDefaults())
                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("apiLogin")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFiler(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter() , BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter() , BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
//                .requestMatchers("/cards").hasAuthority("VIEWCARDS")
//                .requestMatchers("/loan").hasAuthority("VIEWLOANS")
//                .requestMatchers("/balance").hasAnyAuthority("VIEWBALANCE", "VIEWACCOUNT")

                    .requestMatchers("/cards").hasRole("ADMIN")
                .requestMatchers("/loan").hasRole("USER")
                .requestMatchers("/user").hasRole("USER")
                .requestMatchers("/balance").hasAnyRole("USER", "ADMIN")
                .requestMatchers( "/register", "/apiLogin").permitAll()
        );
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()).accessDeniedPage("/denied"));
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
////        InMemoryUserDetailsManager
////        UserDetails user = User.withUsername("user").password("{noop}User@12345").authorities("read").build();
////        UserDetails admin = User.withUsername("admin").password("{bcrypt}$2a$12$uchvScQb5a.X2j97nlL2IOzuTxQXMTigGTJ930EpCFto.zUWxR4mW").authorities("admin").build();
////        return new InMemoryUserDetailsManager(user, admin);
//
//        return new JdbcUserDetailsManager(dataSource);
//    }

    // If you want to custom authentication via Rest API intense of Form login => you have to Override (publish) a custom AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DinhNguyenDevUsernamePwdAuthenticationProvider authenticationProvider = new DinhNguyenDevUsernamePwdAuthenticationProvider(
                userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // return new BCryptPasswordEncoder();
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

//        {noop}Chid12345@
    }

//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker() {
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }
}
