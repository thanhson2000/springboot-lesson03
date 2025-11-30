package com.springbootdemo.common;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.springbootdemo.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;

@Component
public class TokenHandler {

    private static final Logger log = LoggerFactory.getLogger(TokenHandler.class);

    @Value("${jwt.signKey}")
    private String signKey;

    public String generate(Account account){

        Date now = new Date();
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        Object buildScope;
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("localhost")
                .issueTime(now)
                .expirationTime(new Date(now.getTime()+ 60*60*1000))
                .claim("scope",buildScope(account))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jweObject = new JWSObject(jwsHeader,payload);

        try {
            jweObject.sign(new MACSigner(signKey.getBytes()));
            return jweObject.serialize();
        }catch (JOSEException e){
            log.error("生成token失敗",e);
            throw new RuntimeException("生成token失敗");
        }
    }

    private String buildScope(Account account){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (! account.getRoles().isEmpty())
            account.getRoles().forEach(role -> stringJoiner.add("ROLE_"+role.name()));
        if (! account.getPermissions().isEmpty()){
            account.getPermissions().forEach(permission -> stringJoiner.add(permission.name()));
        }
        return stringJoiner.toString();
    }

    public boolean checkToken(String token){
        try {
            JWSVerifier jwsVerifier = new MACVerifier(signKey);
            SignedJWT parse = SignedJWT.parse(token);
            return parse.verify(jwsVerifier) && parse.getJWTClaimsSet().getExpirationTime().after(new Date());
        }catch (JOSEException e){
            log.error("signKey有問題",e);
            throw new RuntimeException("signKey有問題");
        }catch( ParseException e){
            log.error("token不匹配",e);
            throw new RuntimeException("token不匹配");
        }
    }
}
