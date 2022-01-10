package kr.springboot.dcinside.cartoon.auth.service.impl;

import kr.springboot.dcinside.cartoon.auth.domain.Role;
import kr.springboot.dcinside.cartoon.auth.domain.User;
import kr.springboot.dcinside.cartoon.auth.exception.EmailAlreadyExistsException;
import kr.springboot.dcinside.cartoon.auth.exception.UsernameAlreadyExistsException;
import kr.springboot.dcinside.cartoon.auth.messaging.UserEventSender;
import kr.springboot.dcinside.cartoon.auth.repo.UserRepository;
import kr.springboot.dcinside.cartoon.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserEventSender userEventSender;

    @Override
    public Optional<User> findByUsername(String username) {
        log.info("retrieving user {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public User registerUser(User user) {
        log.info("registering user {}", user.getUsername());

        if(userRepository.existsByUsername(user.getUsername())) {
            log.warn("username {} already exists.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }

        user = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .active(true)
                .userProfile(user.getUserProfile())
                .roles(new HashSet<>() {{
                    add(Role.USER);
                }})
                .build();

        User savedUser = userRepository.save(user);
        userEventSender.sendUserCreated(savedUser);

        return savedUser;
    }

}
