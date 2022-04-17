package com.ldu.jwt.config;

import com.ldu.jwt.config.jwt.JwtAuthenticationFilter;
import com.ldu.jwt.config.jwt.JwtAuthorizationFilter;
import com.ldu.jwt.filter.MyFilter3;
import com.ldu.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);
//        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않겠다는 의미
                .and()
                .addFilter(corsFilter) // @CrossOrigin(인증이 없을 경우 사용),  인증이 있을 경우 시큐리티 필터에 등록
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) //AuthenticationManager 파라미터로 던져야 함 왜냐하면
                // JwtAuthenticationFilter가 UsernamePasswordAuthenticationFilter 상속 받는데 UsernamePasswordAuthenticationFilter는 로그인 시 작동되는 필터
                // 이 필터가 로그인 할 때 AuthenticationManager를 통해서 로그인을 함
                // 바로 파라미터에 authenticationManager() 넣을 수 있는 이뉴는 WebSecurityConfigurerAdapter가 들고있음 ,  로그인 할 때 실행되는 필터
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository)) //AuthenticationManager 파라미터로 던져야 함
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").permitAll()
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/home")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }
}
