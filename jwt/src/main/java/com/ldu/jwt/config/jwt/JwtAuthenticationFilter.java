package com.ldu.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldu.jwt.config.auth.PrincipalDetails;
import com.ldu.jwt.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;


// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
// login 요청해서 username, password 전송하면 (Post)
// UsernamePasswordAuthenticationFilter 동작함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    //   /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("jwtFilter 로그인 시도중");

        // 1. username password 받아서
        // 2. 정상인지 로그인 시도 함. autheticationManager로 로그인 시도 하면 principalDetailsService가 호출
        // loadUserByUsername() 함수 실행됨
        // 3. PrincipalDetails를 세션에 담고 (권한 관리를 위해서)
        // 4. jwt 만들어서 응답하면 됨
        try {
//            BufferedReader br = request.getReader();
//            String input = null;
//            while ((input =br.readLine()) !=null) {
//                System.out.println(input);
//            }
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()); // 첫번쨰파라미터username 두번째파라미터 password

            // PrincipalDetailsService의 loadUserByUsername() 실행된 후 정상이면 authentication이 리턴 됨
            // DB에 있는 username과 password가 일치한다
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 로그인이 됨
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료됨::"+principalDetails.getUser().getUsername());
            // Authentication 객체가 session 영역에 저장을 해야하고 그 방법이 return 하는 것이다.
            // 리턴 이유는 security가 권한 관리를 대신 해주기 때문에 편하다.
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 실행
    // JWT 토큰 만들어서 request 요청한 사용자에게 jwt토큰을 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("인증이 완료됨");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("lduToken") // 토큰 이름
                .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10))) // 토큰 유효시간
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("donguking")); // 암호화 비밀키

        response.addHeader("Authorization","Bearer "+jwtToken);
    }
}
