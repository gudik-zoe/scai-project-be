package com.luv2code.springboot.cruddemo.service;

import com.luv2code.exception.error.handling.NotFoundException;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.entity.PostLike;
import com.luv2code.springboot.cruddemo.entity.Relationship;
import com.luv2code.springboot.cruddemo.jpa.repositories.PostLikeJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PostLikesServiceImpl implements PostLikesService {
	@Autowired
	private PostLikeJpaRepo postLikeJpaRepo;

	@Autowired
	private RelationshipService relationshipService;

	@Autowired
	private PostService postService;

	@Autowired
	private NotificationService notificationService;



	public PostLikesServiceImpl() {

	}

	@Override
	public PostLike addLike(int accountId, int postId) {
		Post theRelatedPost = postService.findPostByPostId(postId);
		Relationship theRelationship = relationshipService.checkRelation(accountId , theRelatedPost.getPostCreatorId() );
		if (theRelationship == null || theRelationship.getStatus() != 1  ) {
			throw new NotFoundException("cannot like a post of a user that is not ur friend");
		}
		PostLike postLike = postLikeJpaRepo.likePost(accountId, postId);
		if (postLike == null) {
			PostLike thePostLike = new PostLike(postId, accountId);
			postLikeJpaRepo.save(thePostLike);
			if (theRelatedPost.getPostCreatorId() != null && theRelatedPost.getPostCreatorId() != accountId) {
				Notification likeNotification = new Notification(accountId, theRelatedPost.getPostCreatorId(),
						"liked your post", new Date(System.currentTimeMillis()), theRelatedPost.getIdPost(), false);
				notificationService.addNotification(likeNotification);
			}
			return thePostLike;
		} else {
			postLikeJpaRepo.delete(postLike);
			return null;
		}
	}

}
