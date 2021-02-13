package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.exception.error.handling.NotFoundException;
import com.luv2code.springboot.cruddemo.service.StorageService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.ImageUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class FileUploadController {

	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

//	@GetMapping("/files/{filename}")
//	@ResponseBody
//	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//
//		Resource file = storageService.loadAsResource(filename);
//
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//				.body(file);
//	}

	@PostMapping("/upload")
	public void handleFileUpload(@RequestHeader("Authorization") String authHeader,
			@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		if (!file.getContentType().contains("image")) {
			throw new NotFoundException("file not supported");
		} else {
			storageService.store(file);
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded " + file.getOriginalFilename() + "!");
		}
	}

	@PostMapping("/addImage")
	public ImageUrl pushImage(@RequestHeader("Authorization") String authHeader,
			@RequestBody MultipartFile image) throws Exception {
		System.out.println(image);
		IdExtractor idExtractor = new IdExtractor(authHeader);
		if (image.getSize() > 2000000) {
			throw new NotFoundException("image size exceeds the permitted limit");
		} else {
			return storageService.pushImage(image);
		}
	}
	
	
//	@ExceptionHandler
//	public ResponseEntity<StorageErrorResponse> handleException(CustomeException exc) {
//		StorageErrorResponse error = new StorageErrorResponse(HttpStatus.UNAUTHORIZED.value(), exc.getMessage(),
//				System.currentTimeMillis());
//		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
//	}
//
//	@ExceptionHandler
//	public ResponseEntity<StorageErrorResponse> handleException(Exception exc) {
//		StorageErrorResponse error = new StorageErrorResponse(HttpStatus.UNAUTHORIZED.value(), exc.getMessage(),
//				System.currentTimeMillis());
//		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
//	}

}
