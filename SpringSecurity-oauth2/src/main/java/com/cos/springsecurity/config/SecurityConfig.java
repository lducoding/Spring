package com.cos.springsecurity.config;

import com.cos.springsecurity.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// 구글 로그인 절차 1.코드받기(인증) 2.엑세스 토큰(권한)
// 3.사용 프로필 정보 가져옴 4.그 정보를 토대로 회원가입 진행하기도 함

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preauthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hsaRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN") //이것도 가능
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
//                .usernameParameter() // 이걸 사용하면 사용자명을 form username 이외의 다른걸로 변경가능
                .loginProcessingUrl("/login") //login 주소가 호출되면 스프리시큐리티 낚아채서 대신 로그인 진행한다
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint() // 구글 로그인이 완료가 되 (엑세스 토큰 + 사용자 프로필 정보)
                .userService(principalOauth2UserService);
    }
}
