package com.example.demo.service;

import com.example.demo.model.UserAccount;
import com.example.demo.repository.RoleInfoRepo;
import com.example.demo.repository.UserAccountRepo;
import com.example.demo.repository.UserProfileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class NtUserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserAccountRepo userAccountRepo;
    @Autowired
    private RoleInfoRepo roleInfoRepo;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        log.debug(">> NtUserDetailService.loadUserByUsername account={}", account);
        UserAccount userAccount = userAccountRepo.findByAccount(account);
        if (userAccount == null) {
            throw new BadCredentialsException("Invalid username or password");
            //throw new UsernameNotFoundException("User " + username + " not found.");
        }

        // 取得角色代碼
        Collection<GrantedAuthority> grantedAuthorities = roleInfoRepo.findByUserAccountUid(userAccount.getUid())
                .stream().map(role -> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toList());
        log.debug("user grantedAuthorities={}", grantedAuthorities);
        User userDetails = new User(userAccount.getAccount(),
                userAccount.getPassword(),
                userAccount.getEnabled(), //是否可用
                !userAccount.getExpired(), //是否過期
                !userAccount.getCredentialsExpired(), //證書不過期為true
                !userAccount.getLocked(), //帳號未鎖定為true
                grantedAuthorities);
        log.debug("<< NtUserDetailService.loadUserByUsername User={}", userDetails);
        return userDetails;
    }
}
