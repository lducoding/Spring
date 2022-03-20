package com.cos.springsecurity.config.oauth;

import com.cos.springsecurity.config.auth.PrincipalDetails;
import com.cos.springsecurity.config.oauth.provider.FacebookUserInfo;
import com.cos.springsecurity.config.oauth.provider.GoogleUserInfo;
import com.cos.springsecurity.config.oauth.provider.OAuth2Userinfo;
import com.cos.springsecurity.model.User;
import com.cos.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어 진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("user getClientRegistration::" + userRequest.getClientRegistration()); //registrationId 어떤 oauth로 로그인 했는지 확인가능
        System.out.println("user getAccessToken::" + userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 버튼 로그인 클릭 -> 구글로그인창 -> 로그인 완료 -> code를 리턴(oauth-client라이브러리) -> access token 요청 여기까지가 userRequest임
        // userRequest정보로 회원프로필 받을 수 있다. -> loadUser 함수를 통해 ->구글로 부터 회원 프로필 받아온다.
        System.out.println("user 정보::" + oAuth2User.getAttributes());


        //회원가입 진행
        OAuth2Userinfo oAuth2Userinfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2Userinfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2Userinfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("우리는 구글 페이스북만 지원함");
        }

        String provider = oAuth2Userinfo.getProvider(); // 구글 로그인 OAuth2Userinfo 인터페이스 만들기 전
//        String providerId = oAuth2User.getAttribute("sub"); // 구글의 primary key
        String providerId = oAuth2Userinfo.getProviderId(); // 페이스북의 primary key
        String username = provider+"_"+providerId; //
        String password = bCryptPasswordEncoder.encode("의미없는비밀번호");
        String emial = oAuth2Userinfo.getEmail(); //
        String role = "ROLE_ADMIN";

        User userEntity = userRepository.findByUsername(username);

        if(userEntity==null) {
            userEntity = User.builder().username(username).password(password).email(emial)
                    .role(role).provider(provider).providerId(providerId).build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
