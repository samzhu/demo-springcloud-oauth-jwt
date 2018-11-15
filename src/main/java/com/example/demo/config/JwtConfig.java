package com.example.demo.config;

import com.example.demo.service.NtTokenServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    @Autowired
    private DataSource dataSource;
    @Value("${security.oauth2.authorization.jwt.key-value}")
    private String jwtKeyValue;


    // AuthorizationServerProperties 在2.1會加進來再看怎麼配置
//    private AuthorizationServerProperties authorizationServerProperties;

    @Bean
    public TokenStore tokenStore() {
        JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
        return jdbcTokenStore;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(jwtKeyValue);
        return jwtAccessTokenConverter;
    }


}
