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
        String oauthId = principal.getName();

        Member member = memberService.getByOauthId(oauthId);

        model.addAttribute("member", member);

        return "main";
    }

}
