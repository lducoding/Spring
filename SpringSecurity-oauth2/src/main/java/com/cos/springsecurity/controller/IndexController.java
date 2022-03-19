package com.cos.springsecurity.controller;

import com.cos.springsecurity.config.auth.PrincipalDetails;
import com.cos.springsecurity.model.User;
import com.cos.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetail) {
        //@AuthenticationPrincipal 를 통해 세션정보에 접근가능 PrincipalDetails로도 받을 수 있음 왜냐 상속받았자나!
        System.out.println("testlogin================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication::" + principalDetails.getUser());
        System.out.println("userDetails::" + userDetail.getUser());
        return "세션정보확인";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("testOauthlogin================");
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); // 얘는 principalDetails로 캐스팅이 안되어서ㅓ
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal(); // 이것처럼 OAuth2User로 캐스팅 해야함
        System.out.println("authentication::" + oAuth2User.getAttributes());
        System.out.println("oauth2user::"+ oauth.getAttributes());
        return "Oauth2 세션정보확인";
    }

    @GetMapping({"","/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        if(user.getUsername().equals("user")) {
            user.setRole("ROLE_USER");
        } else if(user.getUsername().equals("manager")) {
            user.setRole("ROLE_MANAGER");
        } else if(user.getUsername().equals("admin")) {
            user.setRole("ROLE_ADMIN");
        }

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')") //함수시작전에
//    @PostAuthorize("hasRole('ROLE_MANAGER')") //끝난뒤
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "개인정보";
    }
}
