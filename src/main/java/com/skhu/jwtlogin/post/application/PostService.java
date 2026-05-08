package com.skhu.jwtlogin.post.application;

import com.skhu.jwtlogin.post.api.dto.PostCreateRequest;
import com.skhu.jwtlogin.post.api.dto.PostResponse;
import com.skhu.jwtlogin.post.domain.Post;
import com.skhu.jwtlogin.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostResponse> getPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse createPost(Long memberId, PostCreateRequest request) {
        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .memberId(memberId)
                .build();

        Post savedPost = postRepository.save(post);

        return PostResponse.from(savedPost);
    }
}