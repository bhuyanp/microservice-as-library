package io.pbhuyan.security.common.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Set;


@Data
@Builder
public class UserEntity {
    private String username;
    @ToString.Exclude
    private String password;
    private Set<String> roles;


}
