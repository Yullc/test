package org.example.controller;

import org.example.service.MemberService;
import org.example.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/main")
    public String mainPage(Principal principal, Model model) {
        String githubId = principal.getName(); // 로그인한 GitHub 아이디

        Member member = memberService.findByGithubId(githubId); // 하나만 가져오기

        model.addAttribute("member", member); // JSP에서 member.nickName, member.email 사용 가능

        return "main";
    }

}
