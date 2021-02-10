package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.PostService;
import com.luv2code.utility.IdExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class PostsRestController {
	@Autowired
	private PostService postService;

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
		return postService.savePost(idExtractor.getIdFromToken(), text, image, postOption);
	}

	@PostMapping("/posts/postOnWall/{postedOn}")
	public Post postOnWall(@PathVariable int postedOn,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "text", required = true) String text,
			@RequestHeader("Authorization") String authHeader) throws Exception, CustomeException {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return postService.postOnWall(idExtractor.getIdFromToken(), postedOn, image, text);
	}

	@PostMapping("/post/resharePost/{idPost}")
	public Post resharePost(@RequestHeader("Authorization") String authHeader, @PathVariable int idPost,
			@RequestPart(value = "extraText", required = true) String extraText,
			@RequestPart(value = "postOption", required = true) String postOption) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return postService.resharePost(idExtractor.getIdFromToken(), idPost, extraText, postOption);

	}

	@PutMapping("/posts/update/idAccount/{postId}/{postWithImage}")
	public Post updatePost(@PathVariable int postId,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "text", required = true) String newText, @PathVariable Boolean postWithImage,
			@RequestHeader("Authorization") String authHeader) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return postService.updatePost(idExtractor.getIdFromToken(), postId, image, postWithImage, newText);

	}

	@DeleteMapping("posts/{postId}")
	public void deletePost(@PathVariable int postId, @RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		postService.deletePostById(idExtractor.getIdFromToken(), postId);
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
