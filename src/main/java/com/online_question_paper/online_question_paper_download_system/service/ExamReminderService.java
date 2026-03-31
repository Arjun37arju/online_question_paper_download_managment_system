package com.online_question_paper.online_question_paper_download_system.service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.online_question_paper.online_question_paper_download_system.entity.Subject;
import com.online_question_paper.online_question_paper_download_system.entity.Principal;
import com.online_question_paper.online_question_paper_download_system.repository.SubjectRepository;
import com.online_question_paper.online_question_paper_download_system.repository.PrincipalRepository;

@Service
public class ExamReminderService {

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private PrincipalRepository principalRepo;

    @Autowired
    private EmailService emailService;


    /* ================= AUTOMATIC REMINDER ================= */

    @Scheduled(fixedRate = 600000)
    // runs every 10 minutes

    public void sendExamReminder() {

        List<Subject> subjects = subjectRepo.findByExamDateIsNotNull();

        LocalDateTime now = LocalDateTime.now();

        for (Subject subject : subjects) {

            if (subject.getExamDate() == null || subject.getExamTime() == null)
                continue;

            LocalDate examDate = subject.getExamDate();
            LocalTime examTime = subject.getExamTime();

            LocalDateTime examDateTime =
                    LocalDateTime.of(examDate, examTime);

            LocalDateTime reminderTime =
                    examDateTime.minusHours(1);

            if (now.isAfter(reminderTime) && now.isBefore(reminderTime.plusMinutes(10))) {

                List<Principal> principals = principalRepo.findAll();

                for (Principal p : principals) {

                    emailService.sendExamReminderEmail(
                            p.getEmail(),
                            p.getPrincipalName(),
                            subject.getSubjectCode(),
                            subject.getExamDate().toString(),
                            subject.getExamTime().toString()
                    );

                }
            }
        }
    }
}