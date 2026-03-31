package com.online_question_paper.online_question_paper_download_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/principal")
public class PrincipalPolicyController {

    @GetMapping("/copyright")
    public String copyright(){
        return "copyright-policy";
    }

    @GetMapping("/security")
    public String security(){
        return "security-policy";
    }

    @GetMapping("/terms")
    public String terms(){
        return "terms-conditions";
    }

    @GetMapping("/privacy")
    public String privacy(){
        return "privacy-policy";
    }

    @GetMapping("/help")
    public String help(){
        return "principal-help";
    }

    @GetMapping("/guidelines")
    public String guidelines(){
        return "principal-guidelines";
    }

}