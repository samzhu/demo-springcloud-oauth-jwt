package com.example.demo.service;

import com.example.demo.model.OauthClientDetails;
import com.example.demo.model.ResourceScop;
import com.example.demo.repository.OauthClientDetailsRepo;
import com.example.demo.repository.OauthClientGrantTypesRepo;
import com.example.demo.repository.ResourceScopRepo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 原始
 * https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/token/DefaultTokenServices.java
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class NtTokenServices extends org.springframework.security.oauth2.provider.token.DefaultTokenServices {
    private int refreshTokenValiditySeconds = 2592000; // default 30 days.
    private int accessTokenValiditySeconds = 43200; // default 12 hours.
    private boolean supportRefreshToken = false;
    private boolean reuseRefreshToken = true;
    private TokenStore tokenStore;
    private ClientDetailsService clientDetailsService;
    private TokenEnhancer accessTokenEnhancer;
    private AuthenticationManager authenticationManager;
    private OauthClientDetailsRepo oauthClientDetailsRepo;
    private OauthClientGrantTypesRepo oauthClientGrantTypesRepo;
    private ResourceScopRepo resourceScopRepo;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.tokenStore, "tokenStore must be set");
    }

    @Transactional
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        log.debug(">> public NtTokenServices.createAccessToken authentication={}", authentication);
//        log.debug(">> NtTokenServices.createAccessToken getCredentials={}", authentication.getCredentials()); // 空

//        log.debug("Principal={}", authentication.getPrincipal());
        // org.springframework.security.core.userdetails.User@586034f: Username: admin; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Granted Authorities: admin
        // 但是 Principal 沒有可取出的方法

//        log.debug("UserAuthentication={}", authentication.getUserAuthentication());
//        log.debug("UserAuthentication getCredentials={}", authentication.getUserAuthentication().getCredentials()); // null
//        log.debug("UserAuthentication getAuthorities={}", authentication.getUserAuthentication().getAuthorities()); // [admin] 用戶角色
//        log.debug("UserAuthentication getDetails={}", authentication.getUserAuthentication().getDetails()); // {grant_type=password, username=admin}
//        log.debug("UserAuthentication getPrincipal={}", authentication.getUserAuthentication().getPrincipal()); // org.springframework.security.core.userdetails.User@586034f: Username: admin; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Granted Authorities: admin

//        log.debug("OAuth2Request={}", authentication.getOAuth2Request());
//        log.debug("OAuth2Request getAuthorities={}", authentication.getOAuth2Request().getAuthorities()); // []
//        log.debug("OAuth2Request getGrantType={}", authentication.getOAuth2Request().getGrantType()); // password
//        log.debug("OAuth2Request getExtensions={}", authentication.getOAuth2Request().getExtensions()); // {}
//        log.debug("OAuth2Request getRedirectUri={}", authentication.getOAuth2Request().getRedirectUri()); // null
//        log.debug("OAuth2Request getRefreshTokenRequest={}", authentication.getOAuth2Request().getRefreshTokenRequest()); // null
//        log.debug("OAuth2Request getResourceIds={}", authentication.getOAuth2Request().getResourceIds()); // [account]
//        log.debug("OAuth2Request getResponseTypes={}", authentication.getOAuth2Request().getResponseTypes()); // []
//        log.debug("OAuth2Request getClientId={}", authentication.getOAuth2Request().getClientId()); // app
//        log.debug("OAuth2Request getRequestParameters={}", authentication.getOAuth2Request().getRequestParameters()); // {grant_type=password, username=admin}
//        log.debug("OAuth2Request getScope={}", authentication.getOAuth2Request().getScope()); // [account, account.readonly, role, role.readonly]

//        OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
        // 這邊希望每次進來都重新做授權
        OAuth2AccessToken existingAccessToken = null;
        OAuth2RefreshToken refreshToken = null;
        if (existingAccessToken != null) {
            if (existingAccessToken.isExpired()) {
                if (existingAccessToken.getRefreshToken() != null) {
                    refreshToken = existingAccessToken.getRefreshToken();
                    // The token store could remove the refresh token when the
                    // access token is removed, but we want to
                    // be sure...
                    tokenStore.removeRefreshToken(refreshToken);
                }
                tokenStore.removeAccessToken(existingAccessToken);
            } else {
                // Re-store the access token in case the authentication has changed
                tokenStore.storeAccessToken(existingAccessToken, authentication);
                return existingAccessToken;
            }
        }

        // Only create a new refresh token if there wasn't an existing one
        // associated with an expired access token.
        // Clients might be holding existing refresh tokens, so we re-use it in
        // the case that the old access token
        // expired.
        if (refreshToken == null) {
            refreshToken = createRefreshToken(authentication);
        }
        // But the refresh token itself might need to be re-issued if it has
        // expired.
        else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
            if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
                refreshToken = createRefreshToken(authentication);
            }
        }
        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        // In case it was modified
        refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null) {
            tokenStore.storeRefreshToken(refreshToken, authentication);
        }
        log.debug("<< public NtTokenServices.createAccessToken accessToken={}", accessToken);
        return accessToken;
    }

    @Transactional(noRollbackFor = {InvalidTokenException.class, InvalidGrantException.class})
    public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
            throws AuthenticationException {
        log.debug(">> NtTokenServices.refreshAccessToken refreshTokenValue={}, tokenRequest={}", refreshTokenValue, tokenRequest);
        if (!supportRefreshToken) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }

        OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
        log.debug("OAuth2RefreshToken={}", refreshToken);
        if (refreshToken == null) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }

        OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
        log.debug("OAuth2Authentication={}", authentication);
        // Start No AuthenticationProvider found for org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
        // 會出現上面錯誤, 但直接拿掉好像沒什麼問題
//        if (this.authenticationManager != null && !authentication.isClientOnly()) {
//            // The client has already been authenticated, but the user authentication might be old now, so give it a
//            // chance to re-authenticate.
//            Authentication user = new PreAuthenticatedAuthenticationToken(authentication.getUserAuthentication(), "", authentication.getAuthorities());
//            user = authenticationManager.authenticate(user);
//            Object details = authentication.getDetails();
//            authentication = new OAuth2Authentication(authentication.getOAuth2Request(), user);
//            authentication.setDetails(details);
//        }
        // End No AuthenticationProvider found for org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
        String clientId = authentication.getOAuth2Request().getClientId();
        if (clientId == null || !clientId.equals(tokenRequest.getClientId())) {
            throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
        }

        // clear out any access tokens already associated with the refresh
        // token.
        tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);

        if (isExpired(refreshToken)) {
            tokenStore.removeRefreshToken(refreshToken);
            throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
        }

        authentication = createRefreshedAuthentication(authentication, tokenRequest);

        if (!reuseRefreshToken) {
            tokenStore.removeRefreshToken(refreshToken);
            refreshToken = createRefreshToken(authentication);
        }

        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        if (!reuseRefreshToken) {
            tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
        }
        log.debug("<< NtTokenServices.refreshAccessToken accessToken={}", accessToken);
        return accessToken;
    }

    private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication, TokenRequest request) {
        log.debug(">> NtTokenServices.createRefreshedAuthentication authentication={}, request={}", authentication, request);
        Set<String> scope = request.getScope();
        OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(request);
        if (scope != null && !scope.isEmpty()) {
            Set<String> originalScope = clientAuth.getScope();
            if (originalScope == null || !originalScope.containsAll(scope)) {
                throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope + ".", originalScope);
            }

            clientAuth = clientAuth.narrowScope(scope);
        }

        OAuth2Authentication narrowed = new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
        log.debug("<< NtTokenServices.createRefreshedAuthentication narrowed={}", narrowed);
        return narrowed;
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
        log.debug(">> NtTokenServices.createRefreshToken authentication={}", authentication);
        if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
            return null;
        }
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        String value = UUID.randomUUID().toString();
        if (validitySeconds > 0) {
            return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
                    + (validitySeconds * 1000L)));
        }
        OAuth2RefreshToken oAuth2RefreshToken = new DefaultOAuth2RefreshToken(value);
        log.debug("<< NtTokenServices.createRefreshToken oAuth2RefreshToken={}", oAuth2RefreshToken);
        return oAuth2RefreshToken;
    }

    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        log.debug(">> private NtTokenServices.createAccessToken authentication={}, refreshToken={}", authentication, refreshToken);
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
        int validitySeconds = this.getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (long) validitySeconds * 1000L));
        }
        token.setRefreshToken(refreshToken);
        //
        OauthClientDetails oauthClientDetails = oauthClientDetailsRepo.getOne(authentication.getOAuth2Request().getClientId());
        log.debug("OauthClientDetails={}", oauthClientDetails);
        if (oauthClientDetails.getAutoApprove().booleanValue() == Boolean.TRUE.booleanValue()) { // 如果是自動授權則依照角色
            // 先取得用戶所屬角色代碼
            List<String> roleCodeList = authentication.getUserAuthentication().getAuthorities()
                    .stream().map(auth -> ((GrantedAuthority) auth).getAuthority()).collect(Collectors.toList());
            // 取得角色對應的 Scop
            Set<String> scopSet = resourceScopRepo.findByUserRoleCodeList(roleCodeList)
                    .stream().map(ResourceScop::getScopCode).collect(Collectors.toSet());
            log.debug("scopSet={}", scopSet);
            // 配置範圍
            token.setScope(scopSet);
        } else {
            token.setScope(authentication.getOAuth2Request().getScope());
        }
        OAuth2AccessToken oAuth2AccessToken = (OAuth2AccessToken) (this.accessTokenEnhancer != null ? this.accessTokenEnhancer.enhance(token, authentication) : token);
        log.debug("<< private NtTokenServices.createAccessToken oAuth2AccessToken={}", oAuth2AccessToken);
        return oAuth2AccessToken;
    }


    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        log.debug(">> NtTokenServices.getAccessTokenValiditySeconds clientAuth={}", clientAuth);
        if (this.clientDetailsService != null) {
            ClientDetails client = this.clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                log.debug("<< NtTokenServices.getAccessTokenValiditySeconds validity={}", validity);
                return validity;
            }
        }
        log.debug("<< NtTokenServices.getAccessTokenValiditySeconds this.accessTokenValiditySeconds={}", this.accessTokenValiditySeconds);
        return this.accessTokenValiditySeconds;
    }

    protected int getRefreshTokenValiditySeconds(OAuth2Request clientAuth) {
        log.debug(">> NtTokenServices.getRefreshTokenValiditySeconds clientAuth={}", clientAuth);
        if (this.clientDetailsService != null) {
            ClientDetails client = this.clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getRefreshTokenValiditySeconds();
            if (validity != null) {
                log.debug("<< NtTokenServices.getRefreshTokenValiditySeconds validity={}", validity);
                return validity;
            }
        }
        log.debug("<< NtTokenServices.getRefreshTokenValiditySeconds this.refreshTokenValiditySeconds={}", this.refreshTokenValiditySeconds);
        return this.refreshTokenValiditySeconds;
    }

    /**
     * Is a refresh token supported for this client (or the global setting if
     * {@link #setClientDetailsService(ClientDetailsService) clientDetailsService} is not set.
     *
     * @param clientAuth the current authorization request
     * @return boolean to indicate if refresh token is supported
     */
    protected boolean isSupportRefreshToken(OAuth2Request clientAuth) {
        log.debug(">> NtTokenServices.isSupportRefreshToken clientAuth={}", clientAuth);
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            log.debug(">> NtTokenServices.isSupportRefreshToken return={}", client.getAuthorizedGrantTypes().contains("refresh_token"));
            return client.getAuthorizedGrantTypes().contains("refresh_token");
        }
        log.debug(">> NtTokenServices.isSupportRefreshToken this.supportRefreshToken={}", this.supportRefreshToken);
        return this.supportRefreshToken;
    }
}
