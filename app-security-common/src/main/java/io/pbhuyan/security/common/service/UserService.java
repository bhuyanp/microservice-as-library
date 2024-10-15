package io.pbhuyan.security.common.service;

import io.pbhuyan.security.common.constant.ROLE;
import io.pbhuyan.security.common.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    //TODO: Inject user repo and get actual users from backend.
    private static final Set<UserEntity> USERS = Set.of(
            UserEntity.builder().username("admin").password("$2a$10$CZ/ku7JkDNyWor0QF7hTf.6t95ptUHY9Gf1d5dxtrfT67/XqMWaqC").roles(Set.of(ROLE.ADMIN, ROLE.USER)).build(),
            UserEntity.builder().username("user").password("$2a$10$TsfCa6CQCt10lf1GEi9AdehMppPW6vEWQalxv0MzV0J7fRWwgQvou").roles(Set.of(ROLE.USER)).build()
    );

    public Set<UserEntity> getAllUsers() {
        return USERS;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Load user by username: {}", username);
        UserEntity userEntity = USERS.stream().filter(it -> it.getUsername().equalsIgnoreCase(username))
                .findAny().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("User found: {}", userEntity);
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles().toArray(String[]::new))
                .build();
    }
}