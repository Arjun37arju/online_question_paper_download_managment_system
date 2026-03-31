package com.online_question_paper.online_question_paper_download_system.controller;

import com.online_question_paper.online_question_paper_download_system.entity.Principal;
import com.online_question_paper.online_question_paper_download_system.repository.PrincipalRepository;
import com.online_question_paper.online_question_paper_download_system.service.EmailService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequestMapping("/principal")
public class PrincipalAuthController {

    @Autowired
    private PrincipalRepository principalRepo;

    @Autowired
    private EmailService emailService;

    // ================= LOGIN PAGE =================

    @GetMapping("/login")
    public String loginPage() {
        return "principal-login";
    }

    // ================= LOGIN + SEND EMAIL OTP =================

    @PostMapping("/login")
    public String doLogin(@RequestParam String principalCode,
                          HttpSession session,
                          Model model) {

        Principal p = principalRepo.findByPrincipalCode(principalCode);

        if (p == null) {
            model.addAttribute("error", "Invalid Principal Code");
            return "principal-login";
        }

        if (!"APPROVED".equals(p.getStatus())) {
            model.addAttribute("error", "Principal not approved yet");
            return "principal-login";
        }

        session.setAttribute("principal", p);

        sendEmailOtp(p, session);

        return "redirect:/principal/verify-otp";
    }

    // ================= OTP PAGE =================

    @GetMapping("/verify-otp")
    public String verifyOtpPage(HttpSession session, Model model) {

        Principal p = (Principal) session.getAttribute("principal");

        if (p == null) {
            return "redirect:/principal/login";
        }

        model.addAttribute("maskedEmail", maskEmail(p.getEmail()));

        return "principal-otp";
    }

    // ================= VERIFY OTP =================

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otp,
                            HttpSession session,
                            Model model) {

        Principal p = (Principal) session.getAttribute("principal");

        if (p == null) {
            return "redirect:/principal/login";
        }

        String sessionOtp = (String) session.getAttribute("emailOtp");
        Long expiry = (Long) session.getAttribute("emailOtpExpiry");

        if (sessionOtp == null || expiry == null) {
            model.addAttribute("error", "OTP expired. Please login again.");
            model.addAttribute("maskedEmail", maskEmail(p.getEmail()));
            return "principal-otp";
        }

        if (System.currentTimeMillis() > expiry) {

            session.removeAttribute("emailOtp");
            session.removeAttribute("emailOtpExpiry");

            model.addAttribute("error", "OTP expired. Please login again.");
            model.addAttribute("maskedEmail", maskEmail(p.getEmail()));
            return "principal-otp";
        }

        if (!sessionOtp.equals(otp)) {
            model.addAttribute("error", "Invalid OTP");
            model.addAttribute("maskedEmail", maskEmail(p.getEmail()));
            return "principal-otp";
        }

        // ✅ SUCCESS
        session.removeAttribute("emailOtp");
        session.removeAttribute("emailOtpExpiry");

        return "redirect:/principal/dashboard";
    }

    // ================= RESEND OTP =================

    @GetMapping("/resend-otp")
    public String resendOtp(HttpSession session, Model model) {

        Principal p = (Principal) session.getAttribute("principal");

        if (p == null) {
            return "redirect:/principal/login";
        }

        sendEmailOtp(p, session);

        model.addAttribute("message", "New OTP sent successfully");
        model.addAttribute("maskedEmail", maskEmail(p.getEmail()));

        return "principal-otp";
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/principal/login";
    }

    // ================= EMAIL OTP LOGIC =================

    private void sendEmailOtp(Principal p, HttpSession session) {

        String otp = generateOtp();

        session.setAttribute("emailOtp", otp);
        session.setAttribute(
                "emailOtpExpiry",
                System.currentTimeMillis() + (2 * 60 * 1000)
        );

        emailService.sendOtpEmail(
                p.getEmail(),
                otp,
                p.getPrincipalName()
        );
    }

    private String generateOtp() {

        Random r = new Random();
        int number = 100000 + r.nextInt(900000);

        return String.valueOf(number);
    }

    private String maskEmail(String email){

        if(email == null) return "";

        int atIndex = email.indexOf("@");

        if(atIndex <= 2) return email;

        return email.substring(0,2) + "****" + email.substring(atIndex);
    }
}