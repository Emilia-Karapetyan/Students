package com.example.demo.student.security;

import com.example.demo.student.model.User;
import com.example.demo.student.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailService implements UserDetailsService {

     final UserRepository studentRepository;

    @Autowired
    public JwtUserDetailService(UserRepository userRepository) {
        this.studentRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserEntityByUsername(username.toLowerCase()).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with name=%s was not found", username)));
        return new CurrentUser(user);
    }

    private Optional<User> getUserEntityByUsername(String email) {
        User user = studentRepository.findUserByEmailIgnoreCase(email);
        return Optional.ofNullable(user);
    }
}
