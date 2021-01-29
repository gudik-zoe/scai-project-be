package com.luv2code.springboot.cruddemo.service;


import org.springframework.web.multipart.MultipartFile;

import com.luv2code.utility.ImageUrl;

public interface StorageService  {

//	public void init();

	public void store(MultipartFile file);

//	public Stream<Path> loadAll();
//
//	public Path load(String filename);

//	public Resource loadAsResource(String filename);
	
	public ImageUrl pushImage( MultipartFile image) throws Exception;
	


}
