package com.luv2code.springboot.cruddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import com.luv2code.filter.AuthFilter;
import com.luv2code.utility.StorageProperties;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
		
		
	}

//	@Bean
//	public CommandLineRunner init(StorageService storageService) {
//		return (args) -> {
////			storageService.deleteAll();
//			storageService.init();
//		};
//	}

	@Bean
	public AuthFilter authFilters() {
		return new AuthFilter();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
						.exposedHeaders("Authorization");
			}
		};
	}

}
