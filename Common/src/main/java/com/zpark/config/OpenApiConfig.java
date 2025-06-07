package com.zpark.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("小众租房平台项目 API")
						.version("1.0")
						.description("小众租房平台项目 API文档")
						.termsOfService("https://example.com/terms")
						.license(new License()
								.name("xmc&&fufuking")
								.url("https://springdoc.org")));
	}
}