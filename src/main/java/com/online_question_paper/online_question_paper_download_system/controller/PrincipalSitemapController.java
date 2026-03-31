package com.online_question_paper.online_question_paper_download_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrincipalSitemapController {

    @GetMapping("/principal/sitemap")
    public String showSitemap() {

        return "principal-sitemap";

    }

}