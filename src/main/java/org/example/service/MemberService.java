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
        Member existing = memberRepository.findByGithubId(githubId);
        if (existing == null) {
            Member newMember = Member.builder()
                    .githubId(githubId)
                    .nickName(username)
                    .email(email)
                    .build();
            memberRepository.save(newMember);
        }
    }
}
