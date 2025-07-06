package org.example.service;

import org.example.vo.Member;
import org.example.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GitHubOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("🛰️ loadUser() 진입");

        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        String githubId = attributes.get("id").toString();
        String username = (String) attributes.get("login");

        System.out.println("🔍 GitHub ID: " + githubId);
        System.out.println("🔍 GitHub 로그인명: " + username);

        // 👇 이메일을 추가 요청으로 안전하게 가져옴
        String email = fetchPrimaryEmail(userRequest);
        System.out.println("📧 가져온 이메일: " + email);

        memberService.processOAuthPostLogin(githubId, username, email);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id"
        );
    }

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

                if (primary != null && primary && verified != null && verified) {
                    System.out.println("✅ primary & verified 이메일 선택됨: " + email);
                    return email;
                }
            }
        }

        System.out.println("⚠️ 이메일을 가져오지 못했습니다.");
        return null;
    }
}

