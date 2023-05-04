package com.example.onlineshop.Service;

import com.example.onlineshop.Entity.Role;
import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return user;
    }

    public ResponseEntity<?> signUpUser(User user) {
        if (userRepository.findUserByUsername(user.getUsername()) != null)
            return new ResponseEntity<>(Map.of("message", "Пользователь с таким юзернеймом уже зарегестрирован!"), HttpStatus.CONFLICT);
        if (userRepository.findUserByEmail(user.getEmail()) != null)
            return new ResponseEntity<>(Map.of("message", "Пользователь с такой почтой уже зарегестрирован!"), HttpStatus.CONFLICT);
        user.setRole(Role.USER);
        user.setAccountNonLocked(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBalance(0.0);
        userRepository.save(user);
        return new ResponseEntity<>(Map.of("message", "Успешная регистрация!"), HttpStatus.CREATED);
    }

    public ResponseEntity<?> singInUser(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            return new ResponseEntity<>(Map.of("message", "Пользователь с такой почтой не найден"), HttpStatus.CONFLICT);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>(Map.of("message", "Вы успешно вошли!"), HttpStatus.OK);
        } else
            return new ResponseEntity<>(Map.of("message", "Неправильный логин или пароль!"), HttpStatus.CONFLICT);
    }
}