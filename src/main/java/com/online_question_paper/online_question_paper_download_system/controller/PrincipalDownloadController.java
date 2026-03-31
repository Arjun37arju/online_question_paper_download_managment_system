package com.online_question_paper.online_question_paper_download_system.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.online_question_paper.online_question_paper_download_system.entity.Principal;
import com.online_question_paper.online_question_paper_download_system.entity.QuestionPaper;
import com.online_question_paper.online_question_paper_download_system.entity.Subject;
import com.online_question_paper.online_question_paper_download_system.entity.DownloadLog;

import com.online_question_paper.online_question_paper_download_system.repository.QuestionPaperRepository;
import com.online_question_paper.online_question_paper_download_system.repository.SubjectRepository;
import com.online_question_paper.online_question_paper_download_system.repository.CollegeDepartmentRepository;
import com.online_question_paper.online_question_paper_download_system.repository.DownloadLogRepository;

import com.online_question_paper.online_question_paper_download_system.service.EmailService;

@Controller
@RequestMapping("/principal")
public class PrincipalDownloadController {

    @Autowired
    private QuestionPaperRepository paperRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private CollegeDepartmentRepository collegeDeptRepo;

    @Autowired
    private DownloadLogRepository downloadLogRepo;

    @Autowired
    private EmailService emailService;

    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_FAILED = "FAILED";
    private static final String STATUS_BLOCKED = "BLOCKED";
    private static final String STATUS_EXPIRED = "EXPIRED";
    private static final String STATUS_POSTPONED = "POSTPONED";


    /* ================= DOWNLOAD PAGE ================= */

    @GetMapping("/download")
    public String downloadPage(HttpSession session, Model model) {

        Principal principal = (Principal) session.getAttribute("principal");

        if (principal == null) {
            return "redirect:/principal/login";
        }

        String collegeCode = principal.getCollegeCode();

        model.addAttribute("principalName", principal.getPrincipalName());
        model.addAttribute("collegeName", collegeCode);

        List<Subject> subjects = subjectRepo.findByExamDateIsNotNull();

        LocalDate today = LocalDate.now();

        subjects = subjects.stream()
                .filter(subject ->
                        subject.getExamDate() != null &&
                                !subject.getExamDate().isBefore(today) &&
                                (
                                        "ALL".equalsIgnoreCase(subject.getDepartmentId()) ||
                                                collegeDeptRepo.existsByCollegeCodeAndDepartmentId(
                                                        collegeCode,
                                                        subject.getDepartmentId()
                                                )
                                )
                )
                .sorted(
                        Comparator.comparing(Subject::getExamDate)
                                .thenComparing(Subject::getExamTime)
                )
                .collect(Collectors.toList());

        if (subjects.isEmpty()) {
            model.addAttribute("noPaper", true);
        } else {
            model.addAttribute("subjects", subjects);
        }

        return "principal-download";
    }


    /* ================= REALTIME STATUS ================= */

    @GetMapping("/status/{subjectCode}")
    public void checkStatus(@PathVariable String subjectCode,
                            HttpSession session,
                            HttpServletResponse response) throws IOException {

        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        if (session.getAttribute("principal") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Optional<Subject> subjectOpt = subjectRepo.findById(subjectCode);

        if (subjectOpt.isEmpty()) {
            response.getWriter().write("NO_PAPER");
            return;
        }

        Subject subject = subjectOpt.get();

        if (STATUS_POSTPONED.equalsIgnoreCase(subject.getExamStatus())) {
            response.getWriter().write("POSTPONED");
        } else {
            response.getWriter().write("ACTIVE");
        }
    }


    /* ================= DOWNLOAD PAPER ================= */

    @GetMapping("/download-paper/{subjectCode}")
    public void downloadPaper(@PathVariable String subjectCode,
                              HttpSession session,
                              HttpServletResponse response,
                              HttpServletRequest request) throws IOException {

        Principal principal = (Principal) session.getAttribute("principal");

        if (principal == null) {
            response.sendRedirect("/principal/login");
            return;
        }

        String principalCode = principal.getPrincipalCode();
        String ipAddress = getClientIp(request);

        Optional<QuestionPaper> paperOpt = paperRepo.findFirstBySubjectCode(subjectCode);

        if (paperOpt.isEmpty()) {

            saveDownloadLog(principalCode, subjectCode, STATUS_FAILED, ipAddress);
            response.getWriter().write("Question Paper Not Uploaded");
            return;
        }

        Optional<Subject> subjectOpt = subjectRepo.findById(subjectCode);

        if (subjectOpt.isEmpty()) {

            saveDownloadLog(principalCode, subjectCode, STATUS_FAILED, ipAddress);
            response.getWriter().write("Subject not found");
            return;
        }

        Subject subject = subjectOpt.get();

        if (STATUS_POSTPONED.equalsIgnoreCase(subject.getExamStatus())) {

            saveDownloadLog(principalCode, subjectCode, STATUS_POSTPONED, ipAddress);

            emailService.sendExamPostponedNotification(
                    principal.getEmail(),
                    principal.getPrincipalName(),
                    subjectCode
            );

            response.getWriter().write("Exam Postponed");
            return;
        }

        if (subject.getExamDate() != null && subject.getExamTime() != null) {

            LocalDateTime examDateTime =
                    LocalDateTime.of(subject.getExamDate(), subject.getExamTime());

            LocalDateTime allowedTime = examDateTime.minusHours(1);
            LocalDateTime expiryTime = allowedTime.plusMinutes(90);

            LocalDateTime now = LocalDateTime.now();

            if (now.isBefore(allowedTime)) {

                saveDownloadLog(principalCode, subjectCode, STATUS_BLOCKED, ipAddress);
                response.getWriter().write("Download not allowed yet");
                return;
            }

            if (now.isAfter(expiryTime)) {

                saveDownloadLog(principalCode, subjectCode, STATUS_EXPIRED, ipAddress);
                response.getWriter().write("Question Paper Expired");
                return;
            }
        }

        QuestionPaper paper = paperOpt.get();

        Path filePath = Paths.get("uploads", "question_papers", paper.getFileName());

        if (!Files.exists(filePath)) {

            saveDownloadLog(principalCode, subjectCode, STATUS_FAILED, ipAddress);
            response.getWriter().write("File not found on server");
            return;
        }

        saveDownloadLog(principalCode, subjectCode, STATUS_SUCCESS, ipAddress);

        emailService.sendDownloadNotification(
                principal.getEmail(),
                principal.getPrincipalName(),
                subjectCode,
                subject.getExamDate().toString(),
                subject.getExamTime().toString()
        );

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + paper.getFileName() + "\"");

        Files.copy(filePath, response.getOutputStream());
        response.getOutputStream().flush();
    }


    /* ================= GET CLIENT IP ================= */

    private String getClientIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }


    /* ================= SAVE DOWNLOAD LOG ================= */

    private void saveDownloadLog(String principalCode,
                                 String subjectCode,
                                 String status,
                                 String ipAddress) {

        DownloadLog log = new DownloadLog();

        log.setPrincipalCode(principalCode);
        log.setSubjectCode(subjectCode);
        log.setDownloadTime(LocalDateTime.now());
        log.setIpAddress(ipAddress);
        log.setStatus(status);

        downloadLogRepo.save(log);
    }
}