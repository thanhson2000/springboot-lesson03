package com.springbootdemo.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.springbootdemo.common.TokenHandler;
import com.springbootdemo.repository.InValidTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Date;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signKey}")
    private String signedKey;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private InValidTokenRepository inValidTokenRepository;

    @Override
    public Jwt decode(String token) throws JwtException {
        SignedJWT signedJWT = tokenHandler.getSignedJWT(token);
        try {
            String jwtid = signedJWT.getJWTClaimsSet().getJWTID();

            if (inValidTokenRepository.existsById(jwtid)) {
                throw new JwtException("此帳號已經退出登錄，請重新登錄！");
            }
            SecretKeySpec secretKeySpec = new SecretKeySpec(signedKey.getBytes(), "HS256");
            return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS256).build().decode(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
