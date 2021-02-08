package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.PostLike;

public interface PostLikesService {

	public PostLike addLike(int accountId, int postId);

}
