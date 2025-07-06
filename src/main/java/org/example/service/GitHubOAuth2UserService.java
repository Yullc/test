package org.example.service;

import org.example.vo.Member;
import org.example.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GitHubOAuth2UserService extends DefaultOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // ✅ super.loadUser() 호출 가능해짐
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // github 또는 google

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String oauthId = null;
        String nickName = null;
        String email = null;

        if ("github".equals(registrationId)) {
            oauthId = attributes.get("id").toString();
            nickName = (String) attributes.get("login");
            email = fetchPrimaryEmail(userRequest); // GitHub는 별도 호출
        } else if ("google".equals(registrationId)) {
            oauthId = attributes.get("sub").toString();
            nickName = (String) attributes.get("name");
            email = (String) attributes.get("email");
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }

        // 🔧 사용자 정보를 DB에 저장 또는 조회
        memberService.processOAuthPostLogin(oauthId, nickName, email);

        return oAuth2User;
    }

    // 📡 GitHub 사용자 이메일 추가 요청
    private String fetchPrimaryEmail(OAuth2UserRequest userRequest) {
        System.out.println("🌐 fetchPrimaryEmail() 호출됨");

        String accessToken = userRequest.getAccessToken().getTokenValue();
        String emailApiUrl = "https://api.github.com/user/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                emailApiUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        System.out.println("📡 이메일 API 응답 상태: " + response.getStatusCode());

        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> emails = response.getBody();
            System.out.println("📨 이메일 리스트: " + emails);

            for (Map<String, Object> emailEntry : emails) {
                Boolean primary = (Boolean) emailEntry.get("primary");
                Boolean verified = (Boolean) emailEntry.get("verified");
                String email = (String) emailEntry.get("email");

                System.out.println("🔹 email: " + email + ", primary: " + primary + ", verified: " + verified);

                if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
                    System.out.println("✅ primary & verified 이메일 선택됨: " + email);
                    return email;
                }
            }
        }

        System.out.println("⚠️ 이메일을 가져오지 못했습니다.");
        return null;
    }
}
