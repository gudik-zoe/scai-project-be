package com.luv2code.springboot.cruddemo.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.CommentLike;

@Repository
public interface CommentLikeJpaRepo extends JpaRepository<CommentLike, Integer> {

	@Query("from CommentLike as c where c.relatedCommentId =:commentId and c.commentLikeCreatorId=:accountId")
	public CommentLike addUserLike(@Param(value = "accountId") int accountId,
			@Param(value = "commentId") int commentId);

	@Query("from CommentLike as c where c.relatedCommentId =:commentId and c.pageCreatorId=:pageId")
	public CommentLike addPageLike(@Param(value = "pageId") int pageId, @Param(value = "commentId") int commentId);

}
