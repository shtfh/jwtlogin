package com.skhu.jwtlogin.post.api;

import com.skhu.jwtlogin.common.error.SuccessCode;
import com.skhu.jwtlogin.common.template.ApiResTemplate;
import com.skhu.jwtlogin.post.api.dto.PostCreateRequest;
import com.skhu.jwtlogin.post.application.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts")
    public ApiResTemplate<?> getPosts() {
        return ApiResTemplate.successResponse(
                SuccessCode.GET_SUCCESS,
                postService.getPosts()
        );
    }

    @PostMapping("/api/posts")
    public ApiResTemplate<?> createPost(
            Authentication authentication,
            @Valid @RequestBody PostCreateRequest request
    ) {
        Long memberId = (Long) authentication.getPrincipal();

        return ApiResTemplate.successResponse(
                SuccessCode.SAVE_SUCCESS,
                postService.createPost(memberId, request)
        );
    }
}