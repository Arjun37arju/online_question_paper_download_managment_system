package com.online_question_paper.online_question_paper_download_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.online_question_paper.online_question_paper_download_system.entity.QuestionPaper;

public interface QuestionPaperRepository
        extends JpaRepository<QuestionPaper, Integer> {

    List<QuestionPaper> findBySubjectCode(String subjectCode);

    /* ✅ CRITICAL FIX – Fetch paper by subject */
    Optional<QuestionPaper> findFirstBySubjectCode(String subjectCode);

    Optional<QuestionPaper> findByQpSelected(String qpSelected);

    Optional<QuestionPaper> findFirstByQpSelectedOrderByIdDesc(String qpSelected);

    Optional<QuestionPaper> findTopByOrderByIdAsc();

    @Modifying
    @Transactional
    @Query("UPDATE QuestionPaper q SET q.qpSelected = 'NO'")
    void resetAllSelections();
}