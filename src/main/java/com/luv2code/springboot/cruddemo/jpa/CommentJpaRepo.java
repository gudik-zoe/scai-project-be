package com.luv2code.springboot.cruddemo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Comment;

@Repository
public interface CommentJpaRepo extends JpaRepository<Comment, Integer> {

}
