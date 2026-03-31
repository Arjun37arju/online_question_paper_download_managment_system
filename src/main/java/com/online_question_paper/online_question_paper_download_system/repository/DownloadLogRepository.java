package com.online_question_paper.online_question_paper_download_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.online_question_paper.online_question_paper_download_system.entity.DownloadLog;

public interface DownloadLogRepository extends JpaRepository<DownloadLog, Long> {
}