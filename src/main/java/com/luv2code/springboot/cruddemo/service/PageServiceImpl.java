package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.springboot.cruddemo.entity.PageLike;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.jpa.PageJpaRepo;
import com.luv2code.springboot.cruddemo.jpa.PageLikeJpaRepo;
import com.luv2code.springboot.cruddemo.jpa.PostJpaRepo;
import com.luv2code.springboot.cruddemo.dto.PageBasicData;
import com.luv2code.springboot.cruddemo.dto.UpdatePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PageServiceImpl implements PageService {
	@Autowired
	private PageJpaRepo pageJpaRepo;

	@Autowired
	private StorageService storageService;

	@Autowired
	private PostJpaRepo postRepoJpa;

	@Autowired
	private PageLikeJpaRepo pageLikeJpaRepo;

	public PageServiceImpl() {

	}

	@Override
	public List<PageBasicData> getPages() {
		List<Page> pagesFullData = pageJpaRepo.findAll();
		List<PageBasicData> pages = new ArrayList<PageBasicData>();
		for (Page page : pagesFullData) {
			pages.add(new PageBasicData(page.getIdPage(), page.getName(), page.getProfilePhoto(), page.getCoverPhoto(),
					page.getPageCreatorId(), page.getPageLike()));
		}
		return pages;
	}

	@Override
	public PageLike likePage(int accountId, int pageId) {
		PageLike pageLike = pageLikeJpaRepo.getIfPageLikedByUser(accountId, pageId);
		if (pageLike == null) {
			PageLike newPageLike = new PageLike(accountId, pageId);
			pageLikeJpaRepo.save(newPageLike);
			return newPageLike;
		} else {
			pageLikeJpaRepo.delete(pageLike);
			return null;
		}

	}

	@Override
	public List<Integer> getMyLikedPages(int accountId) {
		List<PageLike> myLikedPages = pageLikeJpaRepo.getMyLikedPages(accountId);
		List<Integer> myLikedPagesIds = new ArrayList<Integer>();
		for (PageLike pageLike : myLikedPages) {
			myLikedPagesIds.add(pageLike.getRelatedPageId());
		}
		return myLikedPagesIds;
	}

	@Override
	public PageBasicData getPageBasicDataById(int pageId) {
		Page thePage = getPageFullData(pageId);
		PageBasicData pageBasicData = new PageBasicData(thePage.getIdPage(), thePage.getName(),
				thePage.getProfilePhoto(), thePage.getCoverPhoto(), thePage.getPageCreatorId(), thePage.getPageLike());

		return pageBasicData;

	}

	@Override
	public Page createPage(int accountId, MultipartFile profilePhoto, MultipartFile coverPhoto, String pageName,
			String description) throws Exception {
		String pPhoto = storageService.pushImage(profilePhoto).getImageUrl();
		String cPhoto = storageService.pushImage(coverPhoto).getImageUrl();
		Page thePage = new Page(pageName, description, pPhoto, cPhoto, accountId);
		pageJpaRepo.save(thePage);
		return thePage;
	}

	@Override
	public List<PageBasicData> getMyPages(int accountId) {
		List<Page> myPagesFullData = pageJpaRepo.getMyPages(accountId);
		List<PageBasicData> myPages = new ArrayList<PageBasicData>();
		for (Page page : myPagesFullData) {
			myPages.add(new PageBasicData(page.getIdPage(), page.getName(), page.getProfilePhoto(),
					page.getCoverPhoto(), page.getPageCreatorId(), page.getPageLike()));
		}
		return myPages;
	}

	@Override
	public Post addPost(int accountId, String pageId, MultipartFile image, String text) throws Exception {
		int idPage = Integer.parseInt(pageId);
		Post pageNewPost = new Post();
		if (image != null) {
			String postImage = storageService.pushImage(image).getImageUrl();
			pageNewPost.setImage(postImage);
		}
		pageNewPost.setPageCreatorId(idPage);
		pageNewPost.setStatus(0);
		pageNewPost.setText(text);
		pageNewPost.setDate(new Date(System.currentTimeMillis()));
		postRepoJpa.save(pageNewPost);
		return pageNewPost;

	}

	@Override
	public Page getPageFullData(int pageId) {
		Optional<Page> result = pageJpaRepo.findById(pageId);
		Page thePage = null;
		if (result.isPresent()) {
			thePage = result.get();
		} else {
			throw new NotFoundException("no such id for a page");
		}
		return thePage;
	}

	@Override
	public boolean checkIfPageLikedByAccount(int accountId, int pageId) {
		Page thePage = getPageFullData(pageId);
		for (PageLike like : thePage.getPageLike()) {
			if (like.getPageLikeCreatorId() == accountId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Page updatePage(int accountId, UpdatePage newPage) throws Exception {
		Page thePage = getPageFullData(newPage.getIdPage());
		if (thePage.getPageCreatorId() != accountId) {
			throw new NotFoundException("cannot update a page that is not yours");
		} else {
			if (newPage.getProfilePhoto() != null) {
				String newProPhoto = storageService.pushImage(newPage.getProfilePhoto()).getImageUrl();
				thePage.setProfilePhoto(newProPhoto);
			}
			if (newPage.getCoverPhoto() != null) {
				String newCoPhoto = storageService.pushImage(newPage.getCoverPhoto()).getImageUrl();
				thePage.setCoverPhoto(newCoPhoto);
			}
			thePage.setName(newPage.getName());
			thePage.setDescription(newPage.getDescription());
			pageJpaRepo.save(thePage);
			return thePage;

		}
	}

	@Override
	public List<String> getPagePhotos(int pageId) {
		Page thePage = getPageFullData(pageId);
		List<String> photos = new ArrayList<String>();
		for (Post post : thePage.getPosts()) {
			if (post.getImage() != null) {
				photos.add(post.getImage());
			}
		}
		photos.add(thePage.getProfilePhoto());
		photos.add(thePage.getCoverPhoto());
		return photos;
	}

}
