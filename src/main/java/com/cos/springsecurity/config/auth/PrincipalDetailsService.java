package com.cos.springsecurity.config.auth;

import com.cos.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.cos.springsecurity.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl()에서 설정한 로그인이 오면
// 자동으로 UserDetailsService 타입으로 IOC 되어있는 loadUserByUsername 가 실행능
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity!=null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
