package com.skhu.jwtlogin.post.api;

import com.skhu.jwtlogin.common.error.SuccessCode;
import com.skhu.jwtlogin.common.template.ApiResTemplate;
import com.skhu.jwtlogin.post.application.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}