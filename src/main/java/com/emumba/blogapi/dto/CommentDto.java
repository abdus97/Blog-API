package com.emumba.blogapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String content;
    private String authorName;
    private Long postId;
    private Long parentId;
    private LocalDateTime createdAt;
}
