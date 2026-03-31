package com.online_question_paper.online_question_paper_download_system.service;

import com.online_question_paper.online_question_paper_download_system.entity.Admin;
import com.online_question_paper.online_question_paper_download_system.repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Admin admin = adminRepository.findByEmail(email);

        if (admin == null) {

            throw new UsernameNotFoundException("Admin not found");
        }

        return new User(
                admin.getEmail(),
                admin.getPassword(),
                new ArrayList<>()
        );
    }
}