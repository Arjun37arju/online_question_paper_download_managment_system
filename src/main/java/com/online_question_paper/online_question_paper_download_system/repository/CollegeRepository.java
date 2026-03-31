package com.online_question_paper.online_question_paper_download_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_question_paper.online_question_paper_download_system.entity.College;

public interface CollegeRepository extends JpaRepository<College, String> {

    boolean existsByCollegeCode(String collegeCode);
}