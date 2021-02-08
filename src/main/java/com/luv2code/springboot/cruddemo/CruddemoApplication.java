package com.luv2code.springboot.cruddemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.luv2code.filter.AuthFilter;
import com.luv2code.utility.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
//@EnableJpaRepositories ("com.luv2code.springboot.jpa.repositories") 
//@ComponentScan("com.luv2code.springboot.cruddemo.service")
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

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
