package com.usermanage.userdatamanage.jwt.model;

import com.usermanage.userdatamanage.entity.UserDetail;
import com.usermanage.userdatamanage.repository.UserDetailsRepo;
import com.usermanage.userdatamanage.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component //component annotation is used to declare a class as a bean so that it can be injected using autowiring.
public class UserInfoDetailsService implements UserDetailsService {

    @Autowired
    private UserDetailsRepo userDetailsRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDetail> userDetail = userDetailsRepo.findByUserName(username);
        return userDetail.map(UserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
