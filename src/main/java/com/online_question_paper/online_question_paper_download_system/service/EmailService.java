package com.online_question_paper.online_question_paper_download_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    /* ================= OTP EMAIL ================= */

    public void sendOtpEmail(String toEmail, String otp, String name){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("OTP Verification");

        msg.setText(
                "Hello " + name + ",\n\n" +
                        "Your login OTP is: " + otp + "\n\n" +
                        "This OTP will expire in 2 minutes.\n\n" +
                        "Online Question Paper Download Management System"
        );

        mailSender.send(msg);
    }


    /* ================= PRINCIPAL ACCOUNT APPROVED ================= */

    @Async
    public void sendPrincipalPassword(String toEmail, String principalCode, String password){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Principal Login Approved");

        msg.setText(
                "Your principal account has been approved.\n\n" +
                        "Principal Code: " + principalCode + "\n" +
                        "Password: " + password + "\n\n" +
                        "Please change your password after login.\n\n" +
                        "Online Question Paper Download Management System"
        );

        mailSender.send(msg);
    }


    /* ================= DOWNLOAD CONFIRMATION EMAIL ================= */

    @Async
    public void sendDownloadNotification(String toEmail,
                                         String principalName,
                                         String subjectCode,
                                         String examDate,
                                         String examTime){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Question Paper Download Confirmation");

        msg.setText(
                "Hello " + principalName + ",\n\n" +
                        "The question paper has been successfully downloaded.\n\n" +
                        "Subject Code : " + subjectCode + "\n" +
                        "Exam Date : " + examDate + "\n" +
                        "Exam Time : " + examTime + "\n\n" +
                        "Please keep the question paper confidential until the exam begins.\n\n" +
                        "Online Question Paper Download Management System"
        );

        mailSender.send(msg);
    }


    /* ================= EXAM POSTPONED EMAIL ================= */

    @Async
    public void sendExamPostponedNotification(String toEmail,
                                              String principalName,
                                              String subjectCode){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Exam Postponed Notification");

        msg.setText(
                "Hello " + principalName + ",\n\n" +
                        "The exam for the following subject has been postponed.\n\n" +
                        "Subject Code : " + subjectCode + "\n\n" +
                        "The new schedule will be updated soon.\n\n" +
                        "Please check the system regularly for updates.\n\n" +
                        "Online Question Paper Download Management System"
        );

        mailSender.send(msg);
    }


    /* ================= EXAM RESCHEDULE EMAIL ================= */

    @Async
    public void sendExamRescheduleNotification(String toEmail,
                                               String principalName,
                                               String subjectCode,
                                               String newDate,
                                               String newTime){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Exam Rescheduled Notification");

        msg.setText(
                "Hello " + principalName + ",\n\n" +
                        "The postponed exam has been rescheduled.\n\n" +
                        "Subject Code : " + subjectCode + "\n" +
                        "New Exam Date : " + newDate + "\n" +
                        "New Exam Time : " + newTime + "\n\n" +
                        "Please download the question paper at the scheduled time.\n\n" +
                        "Online Question Paper Download Management System"
        );

        mailSender.send(msg);
    }


    /* ================= EXAM REMINDER EMAIL ================= */

    @Async
    public void sendExamReminderEmail(String toEmail,
                                      String principalName,
                                      String subjectCode,
                                      String examDate,
                                      String examTime){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Exam Reminder - Question Paper Download");

        msg.setText(
                "Hello " + principalName + ",\n\n" +
                        "Reminder: An exam is scheduled today.\n\n" +
                        "Subject Code : " + subjectCode + "\n" +
                        "Exam Date : " + examDate + "\n" +
                        "Exam Time : " + examTime + "\n\n" +
                        "You can download the question paper 1 hour before exam time.\n\n" +
                        "Online Question Paper Download Management System"
        );

        mailSender.send(msg);
    }

}