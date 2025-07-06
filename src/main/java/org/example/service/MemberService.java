package org.example.service;

import org.example.repository.MemberRepository;
import org.example.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public void processOAuthPostLogin(String githubId, String username, String email) {
        System.out.println("📥 processOAuthPostLogin() 진입");
        System.out.println("➡️ githubId: " + githubId + ", username: " + username + ", email: " + email);

        Member existing = memberRepository.findByGithubId(githubId);
        System.out.println("🔎 기존 회원 조회 결과: " + existing);

        if (existing == null) {
            Member newMember = Member.builder()
                    .githubId(githubId)
                    .nickName(username)
                    .email(email)
                    .build();

            System.out.println("🆕 신규 회원 저장 시도: " + newMember);
            memberRepository.save(newMember);
            System.out.println("✅ 저장 완료");
        } else {
            System.out.println("ℹ️ 이미 존재하는 회원 - 저장 생략");
        }
    }


    public Member findByGithubId(String githubId) {
        return memberRepository.findByGithubId(githubId);
    }
}
