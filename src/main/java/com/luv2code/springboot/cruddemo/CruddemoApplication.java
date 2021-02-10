package com.luv2code.springboot.cruddemo;

import com.luv2code.filter.AuthFilter;
import com.luv2code.utility.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableSwagger2
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	@Bean
	public AuthFilter authFilters() {
		return new AuthFilter();
	}

	@Bean
	public Docket swaggerConfiguration(){
		return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.ant("/api/**"))
				.apis(RequestHandlerSelectors.basePackage("com.luv2code.springboot.cruddemo.rest")).build().apiInfo(apiDetails());
	}

//	private ApiKey apiKey() {
//		return new ApiKey("AUTHORIZATION", "api_key", "header");
//	}

	private ApiInfo apiDetails(){
		return new ApiInfo("Adress book API" , "Sample API for Cruddemo tutorial" , "1,0" , "free to user" , new springfox.documentation.service.Contact("antoine khoury" , "http://cruddemo.io" , "asd@asd.com"),"API License" , "http://cruddemo.io" , Collections.emptyList());
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
