package io.pbhuyan.security.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    //TODO: Inject user repo and get actual users from backend.
    private static final Set<UserDetails> USERS = Set.of(
            User.builder().username("admin").password("$2a$10$CZ/ku7JkDNyWor0QF7hTf.6t95ptUHY9Gf1d5dxtrfT67/XqMWaqC").roles("ADMIN", "USER").build(),
            User.builder().username("user").password("$2a$10$TsfCa6CQCt10lf1GEi9AdehMppPW6vEWQalxv0MzV0J7fRWwgQvou").roles("USER").build()
    );

//    public void createUser(@NonNull User user) {
//        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
//            throw new RuntimeException("User already exists");
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }

    public Set<UserDetails> getAllUsers() {
        return USERS;
    }

//    public void deleteUser(@NonNull UserEntity user) {
//
//        userRepository.delete(user);
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Load user by username: {}", username);
        UserDetails user = USERS.stream().filter(it -> it.getUsername().equalsIgnoreCase(username))
                .findAny().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("User found: {}", user);
        return user;
    }
}