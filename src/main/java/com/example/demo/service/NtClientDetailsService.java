package com.example.demo.service;

import com.example.demo.model.OauthClientDetails;
import com.example.demo.repository.OauthClientDetailsRepo;
import com.example.demo.repository.OauthClientGrantTypesRepo;
import com.example.demo.repository.ResourceRepo;
import com.example.demo.repository.ResourceScopRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class NtClientDetailsService implements org.springframework.security.oauth2.provider.ClientDetailsService {
    @Autowired
    private OauthClientDetailsRepo oauthClientDetailsRepo;
    @Autowired
    private OauthClientGrantTypesRepo oauthClientGrantTypesRepo;
    @Autowired
    private ResourceRepo resourceRepo;
    @Autowired
    private ResourceScopRepo resourceScopRepo;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.debug(">> NtClientDetailsService.loadClientByClientId clientId={}", clientId);
        OauthClientDetails oauthClientDetails = oauthClientDetailsRepo.getOne(clientId);
        BaseClientDetails baseClientDetails = new BaseClientDetails();
        baseClientDetails.setClientId(oauthClientDetails.getClientId());
        baseClientDetails.setClientSecret(oauthClientDetails.getClientSecret());

        List<String> resourceIds = resourceRepo.findByOauthClientId(clientId)
                .stream().map(resource -> resource.getResourceId()).collect(Collectors.toList());
        baseClientDetails.setResourceIds(resourceIds);

        List<String> scopes = resourceScopRepo.findByResourceIdIn(resourceIds)
                .stream().map(resourceScop -> resourceScop.getScopCode()).collect(Collectors.toList());
        baseClientDetails.setScope(scopes);

        List<String> authorizedGrantTypes = oauthClientGrantTypesRepo.findByClientId(clientId)
                .stream().map(oauthClientGrantTypes -> oauthClientGrantTypes.getGrantType()).collect(Collectors.toList());
        log.debug("authorizedGrantTypes={}", authorizedGrantTypes);
        baseClientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);
        baseClientDetails.setAccessTokenValiditySeconds(oauthClientDetails.getAccessTokenValidity());
        baseClientDetails.setRefreshTokenValiditySeconds(oauthClientDetails.getRefreshTokenValidity());

        if (oauthClientDetails.getAutoApprove().booleanValue() == Boolean.TRUE.booleanValue()) {
            baseClientDetails.setAutoApproveScopes(scopes);
        }
        log.debug("<< NtClientDetailsService.loadClientByClientId BaseClientDetails={}", baseClientDetails);
        return baseClientDetails;
    }
}
