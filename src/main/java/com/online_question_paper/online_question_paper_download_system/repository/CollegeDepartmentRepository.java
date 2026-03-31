package com.online_question_paper.online_question_paper_download_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online_question_paper.online_question_paper_download_system.entity.CollegeDepartment;
import com.online_question_paper.online_question_paper_download_system.entity.CollegeDepartmentId;

public interface CollegeDepartmentRepository
        extends JpaRepository<CollegeDepartment, CollegeDepartmentId> {

    // ✅ Check mapping exists
    boolean existsByCollegeCodeAndDepartmentId(String collegeCode,
                                               String departmentId);

    // ✅ Sort by College Code Ascending
    List<CollegeDepartment> findAllByOrderByCollegeCodeAsc();

    // ✅ Sort by College Code + Department Id Ascending
    List<CollegeDepartment> findAllByOrderByCollegeCodeAscDepartmentIdAsc();

    // ✅ NEW: find colleges having specific department
    List<CollegeDepartment> findByDepartmentId(String departmentId);
}