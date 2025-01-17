package com.emumba.blogapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPostResponseDto {
    private Long id;
    private String email;
    private String username;
}
