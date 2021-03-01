package com.luv2code.springboot.cruddemo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springboot.cruddemo.entity.PostLike;
import com.luv2code.springboot.cruddemo.service.PostLikesService;
import com.luv2code.springboot.cruddemo.utility.IdExtractor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class PostLikesRestController {
	@Autowired
	private PostLikesService postLikesService;

	public PostLikesRestController() {

	}

	@PostMapping("/postLikes/accountId/{postId}")
	private PostLike addLike(@RequestHeader("Authorization") String authHeader, @PathVariable int postId)
			throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return postLikesService.addLike(idExtractor.getIdFromToken(), postId);
	}

}
