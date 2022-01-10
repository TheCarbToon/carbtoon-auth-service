package kr.springboot.dcinside.cartoon.auth.service;

import kr.springboot.dcinside.cartoon.auth.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);
    User registerUser(User user);

}
