package com.luv2code.springboot.cruddemo.service;


import java.io.IOException;
import java.io.InputStream;



import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.exception.error.handling.StorageException;
import com.luv2code.utility.ImageUrl;


@Service
public class StorageServiceImpl implements StorageService  {


	@Override
	public void store(MultipartFile file) {

		String filename = StringUtils.cleanPath(file.getOriginalFilename());

		if (file.isEmpty()) {
			throw new StorageException("Failed to store empty file " + filename);
		}
		if (!file.getContentType().contains("image")) {
			throw new StorageException("file type is not supported");
		}
		if (file.getSize() > 2000000) {
			throw new StorageException("image size exceeds the permitted limit");
		}
		try (InputStream inputStream = file.getInputStream()) {
//			Files.copy(inputStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



//	@Override
//	public Stream<Path> loadAll() {
//		try {
//			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
//					.map(this.rootLocation::relativize);
//		} catch (IOException e) {
//			throw new StorageException("Failed to read stored files", e);
//		}
//
//	}

//	@Override
//	public Path load(String filename) {
//		return rootLocation.resolve(filename);
//	}

//	@Override
//	public Resource loadAsResource(String filename) {
//		try {
//			Path file = load(filename);
//			Resource resource = new UrlResource(file.toUri());
//			if (resource.exists() || resource.isReadable()) {
//				return resource;
//			} else {
//				throw new StorageFileNotFoundException("file doesn't exist " + filename);
//
//			}
//		} catch (MalformedURLException e) {
//			throw new StorageFileNotFoundException("file doesn't exist " + filename, e);
//		}
//	}
//
//	@Override
//	public void init() {
//		try {
//			Files.createDirectories(rootLocation);
//		} catch (IOException e) {
//			throw new StorageException("Could not initialize storage", e);
//		}
//	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleStorageException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException1(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ImageUrl pushImage( MultipartFile theImage) throws IOException  {
		if (!theImage.getContentType().contains("image")) {
			throw new CustomeException("file type is not supported");
		}
		if (theImage.getSize() > 2000000) {
			throw new CustomeException("image size exceeds the permitted limit");
		}
		if (theImage.isEmpty() || !theImage.getContentType().contains("image")) {
			throw new CustomeException("Failed to store empty file or the file is not an image");
		}
		String url = "https://api.imgbb.com/1/upload?key=cd6c2303364ac3ce4edb3ccc3615d28c";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		byte[] image = theImage.getBytes();
		HttpPost httppost = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody("image", image, ContentType.DEFAULT_BINARY, theImage.getOriginalFilename());
		HttpEntity entity = builder.build();
		httppost.setEntity(entity);
		CloseableHttpResponse response = httpclient.execute(httppost);
		JSONObject obj;
		obj = new JSONObject(EntityUtils.toString(response.getEntity()));
		JSONObject obj2 = obj.getJSONObject("data");
		String imageUrl =obj2.getString("display_url");
		ImageUrl imgUrlClass = new ImageUrl();
		imgUrlClass.setImageUrl(imageUrl);
//		System.out.println(imgUrlClass.getImageUrl());
		return imgUrlClass;
		
	}





}
