package com.cos.springsecurity.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("user getClientRegistration" + userRequest.getClientRegistration()); //registrationId 어떤 oauth로 로그인 했는지 확인가능
        System.out.println("user getAccessToken" + userRequest.getAccessToken().getTokenValue());
        // 구글 버튼 로그인 클릭 -> 구글로그인창 -> 로그인 완료 -> code를 리턴(oauth-client라이브러리) -> access token 요청 여기까지가 userRequest임
        // userRequest정보로 회원프로필 받을 수 있다. -> loadUser 함수를 통해 ->구글로 부터 회원 프로필 받아온다.
        System.out.println("user 정보" + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        return super.loadUser(userRequest);
    }
}
