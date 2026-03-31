package com.online_question_paper.online_question_paper_download_system.service;

import com.online_question_paper.online_question_paper_download_system.entity.Admin;
import com.online_question_paper.online_question_paper_download_system.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin login(String email, String password) {
        return adminRepository.findByEmailAndPassword(email, password);
    }
}