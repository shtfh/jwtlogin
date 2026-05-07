package com.skhu.jwtlogin.post.api.dto;

import com.skhu.jwtlogin.post.domain.Post;

public record PostResponse(
        Long postId,
        String title,
        String content
) {

    public static PostResponse from(Post post) {

        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent()
        );
    }
}