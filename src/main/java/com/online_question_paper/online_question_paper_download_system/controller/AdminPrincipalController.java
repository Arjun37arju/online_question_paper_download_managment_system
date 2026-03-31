package com.online_question_paper.online_question_paper_download_system.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.online_question_paper.online_question_paper_download_system.entity.Principal;
import com.online_question_paper.online_question_paper_download_system.repository.CollegeRepository;
import com.online_question_paper.online_question_paper_download_system.repository.PrincipalRepository;

@Controller
@RequestMapping("/admin/principal")
public class AdminPrincipalController {

    @Autowired
    private PrincipalRepository principalRepo;

    @Autowired
    private CollegeRepository collegeRepo;

    // ===================== SHOW PAGE =====================
    @GetMapping
    public String page(Model model){

        model.addAttribute("principals", principalRepo.findAll());
        model.addAttribute("principal", new Principal());
        model.addAttribute("editMode", false);
        model.addAttribute("colleges", collegeRepo.findAll());

        return "admin-principal";
    }

    // ===================== SAVE =====================
    @PostMapping("/save")
    public String save(@ModelAttribute Principal principal, Model model){

        // ✅ Principal Code Validation
        if(principal.getPrincipalCode() == null ||
                principal.getPrincipalCode().trim().isEmpty()){

            model.addAttribute("error", "Principal Code is required!");
            reload(model, principal);
            return "admin-principal";
        }

        // ✅ Email Validation (NEW)
        if(principal.getEmail() == null ||
                principal.getEmail().trim().isEmpty()){

            model.addAttribute("error", "Email is required!");
            reload(model, principal);
            return "admin-principal";
        }

        // ✅ Duplicate Check
        if(principalRepo.existsById(principal.getPrincipalCode())){

            model.addAttribute("error", "Principal already exists!");
            reload(model, principal);
            return "admin-principal";
        }

        // Default values
        principal.setPassword(null);
        principal.setStatus("PENDING");

        principalRepo.save(principal);

        return "redirect:/admin/principal";
    }

    // ===================== EDIT =====================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model){

        Principal p = principalRepo.findById(id).orElse(null);

        if(p == null){
            return "redirect:/admin/principal"; // ✅ Safety
        }

        model.addAttribute("principals", principalRepo.findAll());
        model.addAttribute("principal", p);
        model.addAttribute("editMode", true);
        model.addAttribute("colleges", collegeRepo.findAll());

        return "admin-principal";
    }

    // ===================== UPDATE =====================
    @PostMapping("/update")
    public String update(@ModelAttribute Principal principal){

        Principal old = principalRepo
                .findById(principal.getPrincipalCode())
                .orElse(null);

        // Keep old password
        if(old != null){
            principal.setPassword(old.getPassword());
        }

        principalRepo.save(principal);

        return "redirect:/admin/principal";
    }

    // ===================== DELETE =====================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id){

        principalRepo.deleteById(id);

        return "redirect:/admin/principal";
    }

    // ===================== APPROVE =====================
    @GetMapping("/approve/{id}")
    public String approve(@PathVariable String id){

        Principal p = principalRepo.findById(id).orElse(null);

        if(p != null){

            if(p.getPassword() == null){
                String password = generatePassword();
                p.setPassword(password);

                // ✅ Console Output (unchanged)
                System.out.println(
                        "Principal Login Password (" +
                                p.getPrincipalCode() + "): " + password
                );
            }

            p.setStatus("APPROVED");
            principalRepo.save(p);
        }

        return "redirect:/admin/principal";
    }

    // ===================== UTIL =====================
    private void reload(Model model, Principal principal){
        model.addAttribute("principals", principalRepo.findAll());
        model.addAttribute("principal", principal);
        model.addAttribute("editMode", false);
        model.addAttribute("colleges", collegeRepo.findAll());
    }

    // ===================== PASSWORD GENERATOR =====================
    private String generatePassword(){

        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder pass = new StringBuilder();
        Random r = new Random();

        for(int i = 0; i < 6; i++){
            pass.append(chars.charAt(r.nextInt(chars.length())));
        }

        return pass.toString();
    }
}