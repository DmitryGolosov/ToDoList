package ru.Dmitriy.NauJava_ToDo_List.service;

import ru.Dmitriy.NauJava_ToDo_List.entity.User;
import ru.Dmitriy.NauJava_ToDo_List.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.Dmitriy.NauJava_ToDo_List.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findByIdOrThrow(id);
    }

    public List<User> getAllUsersSorted() {

        return userRepository.findAllByOrderByCreatedAtDesc();
    }


    public Page<User> getUsersPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());
        return userRepository.findAll(pageable);
    }

    public void deactivateUser(Long userId) {
        userRepository.deactivateById(userId);
    }

    public List<User> searchUsers(String name) {
        return userRepository.findByNameContaining(name);
    }

    public List<User> findUsersCreatedLastWeek() {
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
        return userRepository.findByCreatedAtAfter(weekAgo);
    }

    public List<User> getActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    public long countActiveUsers() {
        return userRepository.countActive();
    }

    public void cleanupInactiveUsers() {
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        userRepository.deleteInactiveOlderThan(monthAgo);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByLogin(username);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(username);
        }
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Пользователь с логином/email '" + username + "' не найден");
        }

        User user = userOpt.get();

        if (!user.getActive()) {
            throw new RuntimeException("Пользователь деактивирован");
        }


        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return user;
    }
}
