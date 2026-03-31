package com.online_question_paper.online_question_paper_download_system.repository;

import com.online_question_paper.online_question_paper_download_system.entity.Admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Add this 👇
    Admin findByEmailAndPassword(String email, String password);

    // Already added before
    Admin findByEmail(String email);
}