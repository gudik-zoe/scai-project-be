package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.springboot.cruddemo.entity.PageLike;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.utility.PageBasicData;
import com.luv2code.utility.UpdatePage;

public interface PageDAO {
	
	public List<Page> getPages();

	public PageLike likePage(int accountId , int pageId);

	public List<Integer> getMyLikedPages(int accountId);

	public PageBasicData getPageById(int pageId);

	public Page createPage(Page page);

	public List<Page> getMyPages(int accountId);

	public List<Post> getPagePosts(int pageId);

	public Post addPost(int accountId, int pageId , Post thePost);

	public Post editPost(Post post);

	public Comment addCommentAsPage(int accountId, int postId, String commentText);

	public CommentLike addLikeAsPage(int accountId,int pageId , int commentId);

	public Page getPageFullData(int pageId , int accountId);

	public Page updatePage(UpdatePage theNewPage , Page theOldPage) throws Exception;

	public List<String> getPagePhotos(int pageId);


}
