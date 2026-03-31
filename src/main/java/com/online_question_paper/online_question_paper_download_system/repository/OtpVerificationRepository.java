package com.online_question_paper.repository;

import com.online_question_paper.model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByPhoneOrderByIdDesc(String phone);
}