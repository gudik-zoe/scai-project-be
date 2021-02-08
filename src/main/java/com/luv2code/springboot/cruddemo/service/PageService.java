package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.springboot.cruddemo.entity.PageLike;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.utility.PageBasicData;

public interface PageService {

	public List<PageBasicData> getPages();

	public PageLike likePage(int accountId, int pageId);

	public List<Integer> getMyLikedPages(int accountId);

	public PageBasicData getPageBasicDataById(int pageId);

	public Page createPage(int accountId, MultipartFile profilePhoto, MultipartFile coverPhoto, String pageName,
			String description) throws Exception;

	public List<PageBasicData> getMyPages(int accountId);

	public Post addPost(int accountId, String pageId, MultipartFile image, String text) throws Exception;

	public Page getPageFullData(int pageId);

	public boolean checkIfPageLikedByAccount(int accountId, int pageId);

	public Page updatePage(int accountId, String pageId, MultipartFile profilePhoto, MultipartFile coverPhoto,
			String name, String description) throws Exception;

	public List<String> getPagePhotos(int pageId);

}
