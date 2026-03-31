package com.online_question_paper.online_question_paper_download_system.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.online_question_paper.online_question_paper_download_system.entity.QuestionPaper;
import com.online_question_paper.online_question_paper_download_system.repository.QuestionPaperRepository;
import com.online_question_paper.online_question_paper_download_system.repository.SubjectRepository;

@Controller
@RequestMapping("/admin/question-paper")
public class QuestionPaperController {

    @Autowired
    private QuestionPaperRepository paperRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    // ✅ Upload folder
    private final String UPLOAD_DIR = "uploads/question_papers/";



    // ================= PAGE =================
    @GetMapping
    public String page(Model model){

        model.addAttribute("subjects", subjectRepo.findAll());
        model.addAttribute("papers", paperRepo.findAll());

        return "question-paper";
    }



    // ================= UPLOAD =================
    @PostMapping("/upload")
    public String upload(@RequestParam("subjectCode") String subjectCode,
                         @RequestParam("file") MultipartFile file,
                         Model model) {

        try {

            if (file.isEmpty()) {
                model.addAttribute("error", "Please select a file!");
                return "question-paper";
            }

            // Create folder if not exists
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // ✅ Unique file name
            String fileName =
                    System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(UPLOAD_DIR + fileName);

            Files.write(path, file.getBytes());

            // Save DB
            QuestionPaper paper = new QuestionPaper();

            paper.setSubjectCode(subjectCode);
            paper.setFileName(fileName);
            paper.setFilePath(path.toString());
            paper.setQpSelected("NO");

            paperRepo.save(paper);

            return "redirect:/admin/question-paper";

        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute("error", "Upload Failed!");
            return "question-paper";
        }
    }



    // ================= VIEW (ADMIN) =================
    @GetMapping("/view/{id}")
    public void view(@PathVariable int id,
                     HttpServletResponse response) throws IOException {

        QuestionPaper paper = paperRepo.findById(id).orElse(null);

        if (paper == null) return;

        File file = new File(paper.getFilePath());

        if (!file.exists()) return;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=\"" + paper.getFileName() + "\"");

        FileInputStream fis = new FileInputStream(file);
        StreamUtils.copy(fis, response.getOutputStream());
        fis.close();
    }



    // ================= SELECT (FOR PRINCIPAL - KEEP) =================
    @GetMapping("/select/{id}")
    public String select(@PathVariable int id){

        QuestionPaper paper = paperRepo.findById(id).orElse(null);

        if (paper == null) return "redirect:/admin/question-paper";

        // Reset old selection
        List<QuestionPaper> list =
                paperRepo.findBySubjectCode(paper.getSubjectCode());

        for(QuestionPaper p : list){

            p.setQpSelected("NO");
            paperRepo.save(p);
        }

        // Set new selection
        paper.setQpSelected("YES");
        paper.setSelectedPaperName(paper.getFileName());

        paperRepo.save(paper);

        return "redirect:/admin/question-paper";
    }



    // ================= DELETE =================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id){

        QuestionPaper paper = paperRepo.findById(id).orElse(null);

        if (paper == null) return "redirect:/admin/question-paper";

        // Delete file
        File file = new File(paper.getFilePath());

        if (file.exists()) {
            file.delete();
        }

        paperRepo.deleteById(id);

        return "redirect:/admin/question-paper";
    }

}