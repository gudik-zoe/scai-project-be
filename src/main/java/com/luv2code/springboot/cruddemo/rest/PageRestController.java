package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.springboot.cruddemo.entity.PageLike;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.PageService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.PageBasicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class PageRestController {
	@Autowired
	private PageService pageService;

	public PageRestController() {

	}

	@GetMapping("/pageFullData/{pageId}")
	public Page getPageFullData(@RequestHeader("Authorization") String authHeader, @PathVariable int pageId) {
		return pageService.getPageFullData(pageId);
	}

	@PostMapping("/create/page")
	public Page createPage(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "profilePhoto", required = true) MultipartFile profilePhoto,
			@RequestPart(value = "coverPhoto", required = true) MultipartFile coverPhoto,
			@RequestPart(value = "pageName", required = true) String pageName,
			@RequestPart(value = "description", required = true) String description) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return pageService.createPage(idExtractor.getIdFromToken(), profilePhoto, coverPhoto, pageName, description);
	}

	@PostMapping("/posts/pageId")
	public Post createPost(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "image", required = false) MultipartFile image,
			@RequestPart(value = "pageId", required = true) String pageId,
			@RequestPart(value = "text", required = true) String text) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return pageService.addPost(idExtractor.getIdFromToken(), pageId, image, text);
	}

	@GetMapping("/pages")
	public List<PageBasicData> getPages() {
		return pageService.getPages();
	}

	@PostMapping("page/likePage/{idPage}")
	public PageLike likePage(@RequestHeader("Authorization") String authHeader, @PathVariable int idPage) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return pageService.likePage(idExtractor.getIdFromToken(), idPage);
	}

	@GetMapping("pages/getPage/{idPage}")
	public PageBasicData getPageById(@PathVariable int idPage) {
		return pageService.getPageBasicDataById(idPage);
	}

	@GetMapping("myPages")
	public List<PageBasicData> getMyPages(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return pageService.getMyPages(idExtractor.getIdFromToken());
	}

//	@GetMapping("page/posts/{pageId}")
//	public List<Post> getPagePosts(@PathVariable int pageId) {
//		return pageService.getPagePosts(pageId);
//	}

	@PutMapping("updatePage")
	public Page updatePage(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto,
			@RequestPart(value = "coverPhoto", required = false) MultipartFile coverPhoto,
			@RequestPart(value = "name", required = false) String name,
			@RequestPart(value = "description", required = false) String description,
			@RequestPart(value = "pageId", required = false) String pageId) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return pageService.updatePage(idExtractor.getIdFromToken(), pageId, profilePhoto, coverPhoto, name,
				description);

	}

	@GetMapping("pagePhotos/{pageId}")
	public List<String> getPagePhotos(@PathVariable int pageId) {
		return pageService.getPagePhotos(pageId);
	}

//	@ExceptionHandler
//	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
//		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
//				System.currentTimeMillis());
//		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//	}
//
//	@ExceptionHandler
//	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
//		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
//				System.currentTimeMillis());
//		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//	}
}
