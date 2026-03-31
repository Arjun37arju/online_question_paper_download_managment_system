package com.online_question_paper.online_question_paper_download_system.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online_question_paper.online_question_paper_download_system.entity.DownloadLog;
import com.online_question_paper.online_question_paper_download_system.repository.DownloadLogRepository;

@Controller
@RequestMapping("/admin")
public class AdminDownloadLogController {

    @Autowired
    private DownloadLogRepository logRepo;

    /* ================= DOWNLOAD LOG PAGE ================= */

    @GetMapping("/download-logs")
    public String showDownloadLogs(Model model) {

        // Fetch logs ordered by latest first
        List<DownloadLog> logs = logRepo.findAll(
                Sort.by(Sort.Direction.DESC, "downloadTime")
        );

        // Send logs to Thymeleaf page
        model.addAttribute("logs", logs);

        return "admin-download-logs";
    }


    /* ================= DOWNLOAD LOGS FILE ================= */

    @GetMapping("/download-logs-file")
    public void downloadLogsFile(HttpServletResponse response) throws IOException {

        List<DownloadLog> logs = logRepo.findAll(
                Sort.by(Sort.Direction.DESC, "downloadTime")
        );

        // CSV file settings
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=download_logs.csv");

        // CSV Header
        String header = "Principal Code,Subject Code,Download Time,IP Address,Status\n";
        response.getWriter().write(header);

        // CSV Data
        for (DownloadLog log : logs) {

            String row =
                    log.getPrincipalCode() + "," +
                            log.getSubjectCode() + "," +
                            log.getDownloadTime() + "," +
                            log.getIpAddress() + "," +
                            log.getStatus() + "\n";

            response.getWriter().write(row);
        }

        response.getWriter().flush();
    }
}