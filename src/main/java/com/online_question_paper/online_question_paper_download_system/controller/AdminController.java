package com.online_question_paper.online_question_paper_download_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online_question_paper.online_question_paper_download_system.repository.CollegeRepository;
import com.online_question_paper.online_question_paper_download_system.repository.CollegeDepartmentRepository;
import com.online_question_paper.online_question_paper_download_system.repository.SubjectRepository;
import com.online_question_paper.online_question_paper_download_system.repository.QuestionPaperRepository;
import com.online_question_paper.online_question_paper_download_system.repository.PrincipalRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CollegeRepository collegeRepository;

    // ✅ Use mapping table repository instead of master
    @Autowired
    private CollegeDepartmentRepository collegeDepartmentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private QuestionPaperRepository questionPaperRepository;

    @Autowired
    private PrincipalRepository principalRepository;

    // ✅ Login Page
    @GetMapping("/login")
    public String loginPage() {
        return "admin-login";
    }

    // ✅ Dashboard Page
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("collegeCount", collegeRepository.count());

        // ✅ Now counting only mapped departments
        model.addAttribute("departmentCount", collegeDepartmentRepository.count());

        model.addAttribute("subjectCount", subjectRepository.count());
        model.addAttribute("paperCount", questionPaperRepository.count());
        model.addAttribute("principalCount", principalRepository.count());

        return "admin-dashboard";
    }
}