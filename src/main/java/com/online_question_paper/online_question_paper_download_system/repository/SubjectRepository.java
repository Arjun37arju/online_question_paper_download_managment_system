package com.online_question_paper.online_question_paper_download_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.online_question_paper.online_question_paper_download_system.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, String> {

    boolean existsBySubjectCode(String subjectCode);

    Optional<Subject> findBySubjectCode(String subjectCode);

    List<Subject> findByExamDateIsNotNull();
}