package com.online_question_paper.online_question_paper_download_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.online_question_paper.online_question_paper_download_system.entity.Subject;
import com.online_question_paper.online_question_paper_download_system.entity.Principal;
import com.online_question_paper.online_question_paper_download_system.entity.CollegeDepartment;

import com.online_question_paper.online_question_paper_download_system.repository.SubjectRepository;
import com.online_question_paper.online_question_paper_download_system.repository.DepartmentMasterRepository;
import com.online_question_paper.online_question_paper_download_system.repository.PrincipalRepository;
import com.online_question_paper.online_question_paper_download_system.repository.CollegeDepartmentRepository;

import com.online_question_paper.online_question_paper_download_system.service.EmailService;

import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/admin/subject")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private DepartmentMasterRepository deptRepo;

    @Autowired
    private PrincipalRepository principalRepo;

    @Autowired
    private CollegeDepartmentRepository collegeDeptRepo;

    @Autowired
    private EmailService emailService;


    // ================= SHOW PAGE =================
    @GetMapping
    public String page(Model model){

        model.addAttribute("subject", new Subject());
        model.addAttribute("subjects", subjectRepo.findAll());
        model.addAttribute("departments", deptRepo.findAll());
        model.addAttribute("editMode", false);

        return "subject";
    }


    // ================= SAVE =================
    @PostMapping("/save")
    public String save(@ModelAttribute Subject subject,
                       Model model){

        if(subjectRepo.existsById(subject.getSubjectCode())){

            model.addAttribute("error","Subject Code Already Exists!");

            model.addAttribute("subject", subject);
            model.addAttribute("subjects", subjectRepo.findAll());
            model.addAttribute("departments", deptRepo.findAll());
            model.addAttribute("editMode", false);

            return "subject";
        }

        subjectRepo.save(subject);

        return "redirect:/admin/subject";
    }


    // ================= EDIT =================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id,
                       Model model){

        Optional<Subject> subject = subjectRepo.findById(id);

        if(subject.isPresent()){

            model.addAttribute("subject", subject.get());
            model.addAttribute("subjects", subjectRepo.findAll());
            model.addAttribute("departments", deptRepo.findAll());
            model.addAttribute("editMode", true);

            return "subject";
        }

        return "redirect:/admin/subject";
    }


    // ================= UPDATE (RESCHEDULE EMAIL) =================
    @PostMapping("/update")
    public String update(@ModelAttribute Subject subject){

        subjectRepo.save(subject);

        try{

            // If COMMON subject send to ALL principals
            if(subject.getDepartmentId().equalsIgnoreCase("ALL")
                    || subject.getDepartmentId().equalsIgnoreCase("COMMON")){

                List<Principal> principals = principalRepo.findAll();

                for(Principal p : principals){

                    if(p.getEmail() != null){

                        emailService.sendExamRescheduleNotification(
                                p.getEmail(),
                                p.getPrincipalName(),
                                subject.getSubjectCode(),
                                subject.getExamDate() != null ? subject.getExamDate().toString() : "TBA",
                                subject.getExamTime() != null ? subject.getExamTime().toString() : "TBA"
                        );
                    }
                }

            }else{

                // Find colleges having this department
                List<CollegeDepartment> mappings =
                        collegeDeptRepo.findByDepartmentId(subject.getDepartmentId());

                for(CollegeDepartment map : mappings){

                    Principal p = principalRepo.findByCollegeCode(map.getCollegeCode());

                    if(p != null && p.getEmail() != null){

                        emailService.sendExamRescheduleNotification(
                                p.getEmail(),
                                p.getPrincipalName(),
                                subject.getSubjectCode(),
                                subject.getExamDate() != null ? subject.getExamDate().toString() : "TBA",
                                subject.getExamTime() != null ? subject.getExamTime().toString() : "TBA"
                        );
                    }
                }
            }

        }catch(Exception e){
            System.out.println("Email error: " + e.getMessage());
        }

        return "redirect:/admin/subject";
    }


    // ================= DELETE =================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id){

        subjectRepo.deleteById(id);

        return "redirect:/admin/subject";
    }


    // ================= POSTPONE =================
    @GetMapping("/postpone/{id}")
    public String postpone(@PathVariable String id){

        Optional<Subject> subject = subjectRepo.findById(id);

        if(subject.isPresent()){

            Subject s = subject.get();

            s.setExamStatus("POSTPONED");

            subjectRepo.save(s);

            try{

                // COMMON subject → send to ALL principals
                if(s.getDepartmentId().equalsIgnoreCase("ALL")
                        || s.getDepartmentId().equalsIgnoreCase("COMMON")){

                    List<Principal> principals = principalRepo.findAll();

                    for(Principal p : principals){

                        if(p.getEmail() != null){

                            emailService.sendExamPostponedNotification(
                                    p.getEmail(),
                                    p.getPrincipalName(),
                                    s.getSubjectCode()
                            );
                        }
                    }

                }else{

                    List<CollegeDepartment> mappings =
                            collegeDeptRepo.findByDepartmentId(s.getDepartmentId());

                    for(CollegeDepartment map : mappings){

                        Principal p = principalRepo.findByCollegeCode(map.getCollegeCode());

                        if(p != null && p.getEmail() != null){

                            emailService.sendExamPostponedNotification(
                                    p.getEmail(),
                                    p.getPrincipalName(),
                                    s.getSubjectCode()
                            );
                        }
                    }
                }

            }catch(Exception e){
                System.out.println("Email error: " + e.getMessage());
            }
        }

        return "redirect:/admin/subject";
    }

}