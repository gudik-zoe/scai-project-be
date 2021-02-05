package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.springboot.cruddemo.entity.PageLike;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.utility.PageBasicData;
import com.luv2code.utility.UpdatePage;

public interface PageService {

	public List<PageBasicData> getPages();

	public PageLike likePage(int accountId, int pageId);

	public List<Integer> getMyLikedPages(int accountId);

	public PageBasicData getPageById(int pageId);

	public Page createPage(Page page);

	public List<PageBasicData> getMyPages(int accountId);

	public List<Post> getPagePosts(int pageId);

	public Post addPost(int accountId, int pageId, Post thePost);

	public Page getPageFullData(int pageId, int accountId);

	public boolean checkIfPageLikedByAccount(int accountId, int pageId);

	public Page updatePage(UpdatePage theNewPage, Page theOldPage) throws Exception;

	public List<String> getPagePhotos(int pageId);

}
