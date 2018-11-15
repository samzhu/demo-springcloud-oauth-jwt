package com.example.demo.config;

import com.example.demo.service.NtClientDetailsService;
import com.example.demo.service.NtTokenServices;
import com.example.demo.service.NtUserDetailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * https://www.cnblogs.com/Irving/p/9430460.html
 */

@EnableAuthorizationServer
@Configuration
@RequiredArgsConstructor
public class Oauth2Config extends AuthorizationServerConfigurerAdapter {

    @NonNull
    private final TokenStore tokenStore;
    @NonNull
    private final AccessTokenConverter accessTokenConverter;
    @NonNull
    private final AuthenticationManager authenticationManagerBean;
    @NonNull
    private final NtUserDetailService ntUserDetailService;
    @NonNull
    private final NtClientDetailsService ntClientDetailsService;
    @NonNull
    private NtTokenServices tokenServices;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(ntClientDetailsService);
//        clients.inMemory()
//                .withClient("AndroidApp").authorizedGrantTypes("password", "refresh_token").scopes("client").autoApprove(true).and()
//                .withClient("IosApp").authorizedGrantTypes(("password", "refresh_token").scopes("client").autoApprove(true).and()
//                .withClient("OAuth-service").authorizedGrantTypes("client_credentials").scopes("client").autoApprove(true);
    }

    /**
     * <p>
     * 取得 Token 簽章金鑰
     * curl -X GET \
     * http://localhost:5566/oauth/token_key \
     * -H 'Authorization: Basic QW5kcm9pZEFwcDoxMjM0NTY=' \
     * {
     * "alg": "HMACSHA256",
     * "value": "5566"
     * }
     * <p/>
     * <p>
     * 驗證 Token
     * curl -X POST \
     * 'http://localhost:5566/oauth/check_token?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiZ29vZHMiXSwiZXhwIjoxNTQyMDc3NTU4LCJ1c2VyX25hbWUiOiJhZG1pbiIsImp0aSI6IjAwZTY1MjM2LTcxZWItNDhmMC1iZDRhLWIyZDliYWU4MDBmYiIsImNsaWVudF9pZCI6IkFuZHJvaWRBcHAiLCJzY29wZSI6WyJnb29kIiwicHJvbW90aW9uIiwiZ29vZC5yZWFkb25seSJdfQ.2JdqpAVl-7oZUyqerAvwLUD1WOyjNwSQSDEMIDwpaBY' \
     * -H 'Authorization: Basic QW5kcm9pZEFwcDoxMjM0NTY='
     * {
     * "aud": [
     * "goods"
     * ],
     * "user_name": "admin",
     * "scope": [
     * "good",
     * "promotion",
     * "good.readonly"
     * ],
     * "active": true,
     * "exp": 1542077558,
     * "jti": "00e65236-71eb-48f0-bd4a-b2d9bae800fb",
     * "client_id": "AndroidApp"
     * }
     * <p/>
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //super.configure(security);
        security
                .tokenKeyAccess("permitAll()")         //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
                .checkTokenAccess("isAuthenticated()") //url:/oauth/check_token allow check token
                .allowFormAuthenticationForClients();
    }

    /**
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .tokenServices(tokenServices)
                .accessTokenConverter(accessTokenConverter)
                .authenticationManager(authenticationManagerBean)
                .userDetailsService(ntUserDetailService);
    }
}
