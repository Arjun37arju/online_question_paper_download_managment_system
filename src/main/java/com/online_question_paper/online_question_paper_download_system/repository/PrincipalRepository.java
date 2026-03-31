package com.online_question_paper.online_question_paper_download_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.online_question_paper.online_question_paper_download_system.entity.Principal;

public interface PrincipalRepository extends JpaRepository<Principal, String> {

    Principal findByPrincipalCodeAndPassword(String principalCode, String password);

    Principal findByPrincipalCode(String principalCode);

    Principal findByMobile(String mobile);

    boolean existsByCollegeCode(String collegeCode);

    boolean existsByPrincipalCode(String principalCode);

    boolean existsByMobile(String mobile);

    // ✅ NEW METHOD
    Principal findByCollegeCode(String collegeCode);
}