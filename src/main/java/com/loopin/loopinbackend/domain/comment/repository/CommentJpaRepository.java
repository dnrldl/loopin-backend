package com.loopin.loopinbackend.domain.comment.repository;

import com.loopin.loopinbackend.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
