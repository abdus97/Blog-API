package com.emumba.blogapi.client.dto;

import com.emumba.blogapi.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserToken {
    User user;
    String token;
}
