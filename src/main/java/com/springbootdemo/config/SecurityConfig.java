package com.springbootdemo.config;

import com.springbootdemo.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.
        BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Value("${bcrypt.password.length}")
    private int passwordLength;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(passwordLength);
    }

    private final String[] permit_patterns = {"/account/register","/account/login"};

    private final String[] hasRole_patterns = {"/users/getAll"};

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 配置攔截, 哪些請求忽略攔截
        http
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers(HttpMethod.POST, permit_patterns).permitAll()
                        .requestMatchers(hasRole_patterns).hasAnyRole(Role.ADMIN.name(),Role.ENGINEER.name())
                        .anyRequest().authenticated()
                );

        // 關閉Csrf導致403 Forbidden
        http.csrf(AbstractHttpConfigurer::disable);

        // 處理請求頭傳遞的Authentication , 此方法要求Auth Type為Oauth2.0 或者 Bearer Token
        http.oauth2ResourceServer( oauth2ResourceServerConfigurer ->
                oauth2ResourceServerConfigurer.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthConverter()))
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
        );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
