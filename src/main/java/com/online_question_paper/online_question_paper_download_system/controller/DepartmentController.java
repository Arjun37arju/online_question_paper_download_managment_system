package com.online_question_paper.online_question_paper_download_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.online_question_paper.online_question_paper_download_system.entity.CollegeDepartment;
import com.online_question_paper.online_question_paper_download_system.entity.CollegeDepartmentId;
import com.online_question_paper.online_question_paper_download_system.repository.*;

@Controller
@RequestMapping("/admin/department")
public class DepartmentController {

    @Autowired
    private DepartmentMasterRepository deptRepo;

    @Autowired
    private CollegeDepartmentRepository collegeDeptRepo;

    @Autowired
    private CollegeRepository collegeRepo;


    // ================= SHOW PAGE =================
    @GetMapping
    public String page(Model model) {

        model.addAttribute("mapping", new CollegeDepartment());

        // ❌ Hide ALL department from mapping dropdown
        model.addAttribute("departments",
                deptRepo.findAll()
                        .stream()
                        .filter(d -> !"ALL".equalsIgnoreCase(d.getDepartmentId()))
                        .toList());

        model.addAttribute("colleges", collegeRepo.findAll());

        // ✅ Sorted Table Data
        model.addAttribute("mappings",
                collegeDeptRepo.findAllByOrderByCollegeCodeAscDepartmentIdAsc());

        model.addAttribute("editMode", false);

        return "department";
    }


    // ================= SAVE =================
    @PostMapping("/save")
    public String save(@ModelAttribute CollegeDepartment cd,
                       Model model) {

        boolean exists =
                collegeDeptRepo.existsByCollegeCodeAndDepartmentId(
                        cd.getCollegeCode(),
                        cd.getDepartmentId()
                );

        if (exists) {

            model.addAttribute("error",
                    "Department Already Added For This College!");

            model.addAttribute("mapping", cd);

            // ❌ Hide ALL again
            model.addAttribute("departments",
                    deptRepo.findAll()
                            .stream()
                            .filter(d -> !"ALL".equalsIgnoreCase(d.getDepartmentId()))
                            .toList());

            model.addAttribute("colleges", collegeRepo.findAll());

            model.addAttribute("mappings",
                    collegeDeptRepo.findAllByOrderByCollegeCodeAscDepartmentIdAsc());

            model.addAttribute("editMode", false);

            return "department";
        }

        collegeDeptRepo.save(cd);

        return "redirect:/admin/department";
    }


    // ================= EDIT =================
    @GetMapping("/edit/{college}/{dept}")
    public String edit(@PathVariable String college,
                       @PathVariable String dept,
                       Model model) {

        CollegeDepartmentId id =
                new CollegeDepartmentId(college, dept);

        CollegeDepartment cd =
                collegeDeptRepo.findById(id).orElse(null);

        if (cd == null) {
            return "redirect:/admin/department";
        }

        model.addAttribute("mapping", cd);

        // ❌ Hide ALL again
        model.addAttribute("departments",
                deptRepo.findAll()
                        .stream()
                        .filter(d -> !"ALL".equalsIgnoreCase(d.getDepartmentId()))
                        .toList());

        model.addAttribute("colleges", collegeRepo.findAll());

        model.addAttribute("mappings",
                collegeDeptRepo.findAllByOrderByCollegeCodeAscDepartmentIdAsc());

        model.addAttribute("editMode", true);

        return "department";
    }


    // ================= DELETE =================
    @GetMapping("/delete/{college}/{dept}")
    public String delete(@PathVariable String college,
                         @PathVariable String dept) {

        CollegeDepartmentId id =
                new CollegeDepartmentId(college, dept);

        collegeDeptRepo.deleteById(id);

        return "redirect:/admin/department";
    }
}