package com.online_question_paper.online_question_paper_download_system.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.online_question_paper.online_question_paper_download_system.entity.Principal;
import com.online_question_paper.online_question_paper_download_system.repository.PrincipalRepository;

@Controller
@RequestMapping("/principal")
public class PrincipalProfileController {

    @Autowired
    private PrincipalRepository principalRepo;

    /* ✅ OPEN PROFILE PAGE */
    @GetMapping("/profile")
    public String profilePage(HttpSession session){

        Principal principal =
                (Principal) session.getAttribute("principal");

        if(principal == null){
            return "redirect:/principal/login";
        }

        return "principal-profile";
    }

    /* ✅ UPDATE PROFILE */
    @PostMapping("/update-profile")
    public String updateProfile(HttpSession session,
                                @RequestParam String principalName,
                                @RequestParam String mobile){

        Principal principal =
                (Principal) session.getAttribute("principal");

        if(principal == null){
            return "redirect:/principal/login";
        }

        principal.setPrincipalName(principalName);
        principal.setMobile(mobile);

        principalRepo.save(principal);

        /* ✅ Update session */
        session.setAttribute("principal", principal);

        return "redirect:/principal/profile?success";
    }
}