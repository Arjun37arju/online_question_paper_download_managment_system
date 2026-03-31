package com.online_question_paper.online_question_paper_download_system.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online_question_paper.online_question_paper_download_system.entity.Principal;

@Controller
@RequestMapping("/principal")
public class PrincipalInstructionsController {

    @GetMapping("/instructions")
    public String instructionsPage(HttpSession session) {

        // ✅ Check principal session (VERY IMPORTANT)
        Principal principal = (Principal) session.getAttribute("principal");

        if(principal == null){
            return "redirect:/principal/login";
        }

        // ✅ Mark instructions viewed (optional but recommended)
        session.setAttribute("instructionsViewed", true);

        return "principal-instructions";
        // Must match: principal-instructions.html
    }
}