package com.luv2code.springboot.cruddemo.rest;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.luv2code.springboot.cruddemo.entity.PageLike;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.PageService;
import com.luv2code.springboot.cruddemo.service.StorageService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.ImageUrl;
import com.luv2code.utility.PageBasicData;
import com.luv2code.utility.UpdatePage;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PageRestController {

	private PageService pageService;

	private EntityManager entityManager;

	private StorageService storageService;

	@Autowired
	public PageRestController(PageService thePageService, EntityManager theEntityManager,
			StorageService theStorageService) {
		pageService = thePageService;
		storageService = theStorageService;
		entityManager = theEntityManager;
	}

	@GetMapping("/pageFullData/{pageId}")
	public Page getPageFullData(@RequestHeader("Authorization") String authHeader, @PathVariable int pageId) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return pageService.getPageFullData(pageId, idExtractor.getIdFromToken());
	}

	@PostMapping("/create/page")
	public Page createPage(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "profilePhoto", required = true) MultipartFile profilePhoto,
			@RequestPart(value = "coverPhoto", required = true) MultipartFile coverPhoto,
			@RequestPart(value = "pageName", required = true) String pageName,
			@RequestPart(value = "description", required = true) String description) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		ImageUrl profile = storageService.pushImage(profilePhoto);
		String proPhoto = profile.getImageUrl();
		ImageUrl cover = storageService.pushImage(coverPhoto);
		String coPhoto = cover.getImageUrl();
		Page page = new Page(pageName, description, proPhoto, coPhoto, idExtractor.getIdFromToken());
		return pageService.createPage(page);
	}

	@PostMapping("/posts/pageId")
	public Post createPost(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "pageId", required = true) String pageId,
			@RequestPart(value = "text", required = true) String text) throws Exception {

		IdExtractor idExtractor = new IdExtractor(authHeader);
		int idPage = Integer.parseInt(pageId);
		Post pageNewPost = new Post();
		if (image != null) {
			ImageUrl postPhotoUrl = storageService.pushImage(image);
			pageNewPost.setImage(postPhotoUrl.getImageUrl());
		}
		pageNewPost.setText(text);
		return pageService.addPost(idExtractor.getIdFromToken(), idPage, pageNewPost);
	}

	@GetMapping("/pages")
	public List<PageBasicData> getPages() {
		return pageService.getPages();
	}

	@PostMapping("page/likePage/{idPage}")
	public PageLike likePage(@RequestHeader("Authorization") String authHeader, @PathVariable int idPage) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Session currentSession = entityManager.unwrap(Session.class);
		Page theLikedPage = currentSession.get(Page.class, idPage);
		if (theLikedPage == null && theLikedPage.getPageCreatorId() != idExtractor.getIdFromToken()) {
			throw new CustomeException("this page doesn't exist or it's yours");
		} else {
			return pageService.likePage(idExtractor.getIdFromToken(), idPage);
		}
	}

	@GetMapping("pages/getPage/{idPage}")
	public PageBasicData getPageById(@PathVariable int idPage) {
		return pageService.getPageById(idPage);
	}

	@GetMapping("myPages")
	public List<PageBasicData> getMyPages(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return pageService.getMyPages(idExtractor.getIdFromToken());
	}

	@GetMapping("page/posts/{pageId}")
	public List<Post> getPagePosts(@PathVariable int pageId) {
		return pageService.getPagePosts(pageId);
	}

	@PutMapping("updatePage")
	public Page updatePage(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto,
			@RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto,
			@RequestPart(value = "name", required = false) String name,
			@RequestPart(value = "description", required = false) String description,
			@RequestPart(value = "pageId", required = false) String pageId) throws Exception {
		Session currentSession = entityManager.unwrap(Session.class);
		IdExtractor idExtractor = new IdExtractor(authHeader);
		int idPage = Integer.parseInt(pageId);
		Page theRequestedPage = currentSession.get(Page.class, idPage);
		if (theRequestedPage.getPageCreatorId() == idExtractor.getIdFromToken()) {
			UpdatePage theNewPage = new UpdatePage(idPage, name, description, profilePhoto, coverPhoto);
			return pageService.updatePage(theNewPage, theRequestedPage);
		} else {
			throw new CustomeException("cannot update a page that is not yours");
		}
	}

	@GetMapping("pagePhotos/{pageId}")
	public List<String> getPagePhotos(@PathVariable int pageId) {
		return pageService.getPagePhotos(pageId);
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
