package com.example.demo.config;

import com.example.demo.repository.OauthClientDetailsRepo;
import com.example.demo.repository.OauthClientGrantTypesRepo;
import com.example.demo.repository.ResourceScopRepo;
import com.example.demo.service.NtTokenServices;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@RequiredArgsConstructor
@Configuration
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @NonNull
    private AuthenticationProvider authenticationProvider;
    @NonNull
    private TokenStore tokenStore;
    @NonNull
    private TokenEnhancer tokenEnhancer;
    @NonNull
    private ClientDetailsService clientDetailsService;
    @NonNull
    private OauthClientDetailsRepo oauthClientDetailsRepo;
    @NonNull
    private OauthClientGrantTypesRepo oauthClientGrantTypesRepo;
    @NonNull
    private ResourceScopRepo resourceScopRepo;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll();
        //我们指定任何用户都可以访问多个URL的模式。
        //任何用户都可以访问以"/resources/","/signup", 或者 "/about"开头的URL。
//                .antMatchers("/oauth/token_key", "/signup", "/about").permitAll()
//                .and()
//                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                .and()
//                .authorizeRequests().antMatchers("/**").authenticated().and().httpBasic();
        // @formatter:on
    }

    @Bean
    public NtTokenServices getDefaultTokenServices() throws Exception {
        NtTokenServices tokenServices = new NtTokenServices();
        tokenServices.setAuthenticationManager(authenticationManagerBean());
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setAccessTokenEnhancer(tokenEnhancer);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setOauthClientDetailsRepo(oauthClientDetailsRepo);
        tokenServices.setOauthClientGrantTypesRepo(oauthClientGrantTypesRepo);
        tokenServices.setResourceScopRepo(resourceScopRepo);
        return tokenServices;
    }

}
