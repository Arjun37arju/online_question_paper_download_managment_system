package com.online_question_paper.online_question_paper_download_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.online_question_paper.online_question_paper_download_system.entity.College;
import com.online_question_paper.online_question_paper_download_system.repository.CollegeRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/college")
public class CollegeController {

    @Autowired
    private CollegeRepository collegeRepo;


    // Show Page
    @GetMapping
    public String collegePage(Model model) {

        model.addAttribute("college", new College());

        List<College> list = collegeRepo.findAll();
        model.addAttribute("colleges", list);

        model.addAttribute("editMode", false);

        return "college";
    }


    // Save College
    @PostMapping("/save")
    public String saveCollege(@Valid @ModelAttribute("college") College college,
                              BindingResult result,
                              Model model) {

        if (result.hasErrors()) {

            model.addAttribute("colleges", collegeRepo.findAll());
            model.addAttribute("editMode", false);

            return "college";
        }

        // Duplicate check
        if (collegeRepo.existsByCollegeCode(college.getCollegeCode())) {

            model.addAttribute("error", "College Code already exists!");
            model.addAttribute("colleges", collegeRepo.findAll());
            model.addAttribute("editMode", false);

            return "college";
        }

        collegeRepo.save(college);

        return "redirect:/admin/college";
    }


    // Edit College
    @GetMapping("/edit/{code}")
    public String editCollege(@PathVariable String code, Model model) {

        College college = collegeRepo.findById(code).orElse(null);

        model.addAttribute("college", college);
        model.addAttribute("colleges", collegeRepo.findAll());

        model.addAttribute("editMode", true);

        return "college";
    }


    // Update College
    @PostMapping("/update")
    public String updateCollege(@Valid @ModelAttribute("college") College college,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {

            model.addAttribute("colleges", collegeRepo.findAll());
            model.addAttribute("editMode", true);

            return "college";
        }

        collegeRepo.save(college);

        return "redirect:/admin/college";
    }


    // Delete College
    @GetMapping("/delete/{code}")
    public String deleteCollege(@PathVariable String code, Model model) {

        try {

            collegeRepo.deleteById(code);

        } catch (DataIntegrityViolationException e) {

            model.addAttribute("error",
                    "Cannot delete this college. It is already used in Department / Subject / Principal.");

            model.addAttribute("college", new College());
            model.addAttribute("colleges", collegeRepo.findAll());
            model.addAttribute("editMode", false);

            return "college";
        }

        return "redirect:/admin/college";
    }
}