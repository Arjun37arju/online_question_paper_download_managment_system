package com.online_question_paper.online_question_paper_download_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_question_paper.online_question_paper_download_system.entity.DepartmentMaster;

public interface DepartmentMasterRepository
        extends JpaRepository<DepartmentMaster, String> {
}