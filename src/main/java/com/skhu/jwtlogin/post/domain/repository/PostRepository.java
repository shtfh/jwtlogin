package com.skhu.jwtlogin.post.domain.repository;

import com.skhu.jwtlogin.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}