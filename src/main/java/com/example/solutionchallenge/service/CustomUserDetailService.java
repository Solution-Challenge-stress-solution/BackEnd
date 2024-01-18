package com.example.solutionchallenge.service;

import com.example.solutionchallenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String keyCode) throws UsernameNotFoundException {
        return userRepository.findByKeyCode(keyCode)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseCode.USER_NOT_FOUND.getMessage()));
    }
}
