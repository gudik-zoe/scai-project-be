package com.luv2code.springboot.cruddemo.rest;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.PostService;
import com.luv2code.springboot.cruddemo.service.RelationshipService;
import com.luv2code.springboot.cruddemo.service.StorageService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.ImageUrl;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostsRestController {
	@Autowired
	private PostService postService;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private StorageService storageService;
	@Autowired
	private RelationshipService relationshipService;

	public PostsRestController() {

	}

	@GetMapping("/homePage/posts")
	public List<Post> getHomePagePosts(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return postService.getHomePagePosts(idExtractor.getIdFromToken());
	}

	@GetMapping("/posts/accountId/{accountId}")
	public List<Post> getPostsByAccountId(@PathVariable int accountId,
			@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return postService.findPostByAccountId(accountId, idExtractor.getIdFromToken());

	}

	@GetMapping("/posts/postId/{idPost}")
	public Post getPostsByPostId(@PathVariable int idPost) {
		return postService.findPostByPostId(idPost);

	}

	@PostMapping("/posts/accountId")
	public Post addPost(@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "text", required = true) String text,
			@RequestPart(value = "postOption", required = true) String postOption,
			@RequestHeader("Authorization") String authHeader) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Post thePost = new Post();
		thePost.setDate(new Date(System.currentTimeMillis()));
		thePost.setText(text);
		if (postOption.equals("public")) {
			thePost.setStatus(0);
		} else if (postOption.equals("just-friends")) {
			thePost.setStatus(1);
		} else {
			thePost.setStatus(2);
		}
		if (image != null) {
			ImageUrl theImageUrl = storageService.pushImage(image);
			thePost.setImage(theImageUrl.getImageUrl());
		} else {
			thePost.setImage(null);
		}
		return postService.savePost(idExtractor.getIdFromToken(), thePost);
	}

	@PostMapping("/posts/postOnWall/{postedOn}")
	public Post postOnWall(@PathVariable int postedOn,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "text", required = true) String text,
			@RequestHeader("Authorization") String authHeader) throws Exception, CustomeException {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Integer theRelationshipStatus = relationshipService.getStatus(idExtractor.getIdFromToken(), postedOn);
		if (theRelationshipStatus != 1) {
			throw new CustomeException("cannot add a post on a user's wall that is not ur friend");
		} else {

			Post thePost = new Post();
			thePost.setPostedOn(postedOn);
			thePost.setText(text);
			thePost.setDate(new Date(System.currentTimeMillis()));
			if (image != null || theRelationshipStatus != 1) {
				ImageUrl theImage = storageService.pushImage(image);
				thePost.setImage(theImage.getImageUrl());
			} else {
				thePost.setImage(null);
			}
			return postService.postOnWall(idExtractor.getIdFromToken(), thePost);
		}
	}

	@PostMapping("/post/resharePost/{idPost}")
	public Post resharePost(@RequestHeader("Authorization") String authHeader, @PathVariable int idPost,
			@RequestPart(value = "extraText", required = true) String extraText,
			@RequestPart(value = "postOption", required = true) String postOption) {
		Session currentSession = entityManager.unwrap(Session.class);
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Post thePostWeWantToShare = currentSession.get(Post.class, idPost);
		Integer postIsPublic = null;
		if (postOption.equals("public")) {
			postIsPublic = 0;
		} else if (postOption.equals("just-friends")) {
			postIsPublic = 1;
		} else {
			postIsPublic = 2;
		}
		if (thePostWeWantToShare == null) {
			throw new CustomeException("post dosen't exist");
		}
		if (thePostWeWantToShare.getPageCreatorId() != null && !extraText.isBlank()) {
			System.out.println("sharing a page's post");
			return postService.resharePost(idExtractor.getIdFromToken(), idPost, extraText, postIsPublic);
		} else if (thePostWeWantToShare.getStatus() == 2
				&& thePostWeWantToShare.getPostCreatorId() != idExtractor.getIdFromToken()) {
			throw new CustomeException("cannot share a post that is private and not yours");
		} else {
			Integer theRelationshipStatus = relationshipService.getStatus(idExtractor.getIdFromToken(),
					thePostWeWantToShare.getPostCreatorId());
			if (theRelationshipStatus == null || theRelationshipStatus != 1) {
				throw new CustomeException("cannot like a user's post that is not ur friend");
			} else if (extraText.isBlank()) {
				throw new CustomeException("you should enter yout own text");
			} else {
				return postService.resharePost(idExtractor.getIdFromToken(), idPost, extraText, postIsPublic);
			}
		}
	}

	@PutMapping("/posts/update/idAccount/{postId}/{postWithImage}")
	public Post updatePost(@PathVariable int postId,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "text", required = true) String newText, @PathVariable Boolean postWithImage,
			@RequestHeader("Authorization") String authHeader) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Session currentSession = entityManager.unwrap(Session.class);
		Post theRequestedPost;
		try {
			theRequestedPost = currentSession.get(Post.class, postId);
			if (theRequestedPost.getPostCreatorId() != null
					&& theRequestedPost.getPostCreatorId() == idExtractor.getIdFromToken()) {
				return postService.updatePost(theRequestedPost, image, postWithImage, newText);
			} else if (theRequestedPost.getPostCreatorId() == null && theRequestedPost.getPageCreatorId() != null) {
				Page page = currentSession.get(Page.class, theRequestedPost.getPageCreatorId());
				if (page.getPageCreatorId() == idExtractor.getIdFromToken()) {
					return postService.updatePost(theRequestedPost, image, postWithImage, newText);
				} else {
					throw new CustomeException("cannot update a page's post that is not yours");
				}
			}
		} catch (Exception e) {
			throw new CustomeException("post doesn't exist");

		}
		throw new CustomeException("uhnknown error occured");
	}

	@DeleteMapping("posts/{postId}")
	public void deletePost(@PathVariable int postId, @RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Session currentSession = entityManager.unwrap(Session.class);
		Post theRequestedPost = currentSession.get(Post.class, postId);
		if (theRequestedPost == null) {
			throw new CustomeException("this post doesn't exist");
		} else {
			postService.deletePostById(idExtractor.getIdFromToken(), postId);
		}
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
