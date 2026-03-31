package com.online_question_paper.online_question_paper_download_system.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.online_question_paper.online_question_paper_download_system.entity.Principal;
import com.online_question_paper.online_question_paper_download_system.repository.PrincipalRepository;

@Service
public class PrincipalUserDetailsService implements UserDetailsService {

    @Autowired
    private PrincipalRepository principalRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // username = mobile number
        Principal principal =
                principalRepository.findByMobile(username);

        if (principal == null) {
            throw new UsernameNotFoundException("Principal not found");
        }

        return new User(
                principal.getMobile(),        // username
                principal.getPassword(),      // password

                "APPROVED".equals(principal.getStatus()), // enabled
                true,   // accountNonExpired
                true,   // credentialsNonExpired
                true,   // accountNonLocked

                new ArrayList<>()
        );
    }
}