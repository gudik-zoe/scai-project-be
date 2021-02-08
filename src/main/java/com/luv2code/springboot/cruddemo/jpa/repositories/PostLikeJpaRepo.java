package com.luv2code.springboot.cruddemo.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.PostLike;

@Repository
public interface PostLikeJpaRepo extends JpaRepository<PostLike, Integer> {

	@Query("from PostLike as p where p.relatedPostId =:postId and p.postLikeCreatorId=:accountId")
	public PostLike likePost(@Param(value = "accountId") int accountId, @Param(value = "postId") int postId);

}
