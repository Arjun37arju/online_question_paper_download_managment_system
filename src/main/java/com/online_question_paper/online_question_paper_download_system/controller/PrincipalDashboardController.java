package com.online_question_paper.online_question_paper_download_system.controller;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online_question_paper.online_question_paper_download_system.entity.Principal;
import com.online_question_paper.online_question_paper_download_system.entity.QuestionPaper;
import com.online_question_paper.online_question_paper_download_system.repository.QuestionPaperRepository;

@Controller
@RequestMapping("/principal")
public class PrincipalDashboardController {

    @Autowired
    private QuestionPaperRepository paperRepo;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model){

        // ✅ Session check
        Principal principal =
                (Principal) session.getAttribute("principal");

        if(principal == null){
            return "redirect:/principal/login";
        }

        // ✅ Get selected question paper safely
        Optional<QuestionPaper> paperOpt =
                paperRepo.findByQpSelected("YES");

        if(paperOpt.isPresent()){

            QuestionPaper paper = paperOpt.get();

            // ⚠ TEMP STATIC VALUES (Later connect Subject table)
            LocalDate examDate = LocalDate.parse("2026-03-15");
            LocalTime examTime = LocalTime.parse("10:00");

            LocalDateTime examDateTime =
                    LocalDateTime.of(examDate, examTime);

            LocalDateTime allowedTime =
                    examDateTime.minusHours(1);

            LocalDateTime now = LocalDateTime.now();

            boolean canDownload =
                    now.isAfter(allowedTime);

            model.addAttribute("paper", paper);
            model.addAttribute("examDate", examDate);
            model.addAttribute("examTime", examTime);
            model.addAttribute("canDownload", canDownload);
        }
        else{
            model.addAttribute("noPaper", true);
        }

        return "principal-dashboard";
    }
}