package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.service.PostService;
import com.luv2code.springboot.cruddemo.service.StorageService;
import com.luv2code.springboot.cruddemo.utility.IdExtractor;
import com.luv2code.springboot.cruddemo.dto.ImageUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class PostsRestController {
	@Autowired
	private PostService postService;

	@Autowired
	private StorageService storageService;

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
			@RequestHeader("Authorization") String authHeader) throws Exception, NotFoundException {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return postService.postOnWall(idExtractor.getIdFromToken(), postedOn, image, text);
	}

	@PostMapping("posts/pushImage")
	public ImageUrl pushImage(@RequestBody MultipartFile image) throws Exception {
		if (!image.isEmpty()) {
			return storageService.pushImage(image);
		}
		return null;
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

}
