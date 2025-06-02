package com.example.pasir_lapka_konrad.service;

import com.example.pasir_lapka_konrad.dto.LoginDto;
import com.example.pasir_lapka_konrad.dto.UserDto;
import com.example.pasir_lapka_konrad.model.User;
import com.example.pasir_lapka_konrad.repository.UserRepository;
import com.example.pasir_lapka_konrad.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User register(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    public String login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Nieprawidłowe dane logowania");
        }

        return jwtUtil.generateToken(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika " + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), List.of());
    }
}
