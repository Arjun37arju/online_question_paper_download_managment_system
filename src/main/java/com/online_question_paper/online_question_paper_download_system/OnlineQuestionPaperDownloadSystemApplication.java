package com.online_question_paper.online_question_paper_download_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class OnlineQuestionPaperDownloadSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineQuestionPaperDownloadSystemApplication.class, args);
    }
}