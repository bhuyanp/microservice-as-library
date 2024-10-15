package io.pbhuyan.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static io.pbhuyan.security.common.controller.AuthenticationController.AUTH_API_BASE_URI;

@Slf4j
@SpringBootApplication
@ComponentScan("io.pbhuyan.security.common")
@ComponentScan("io.pbhuyan.product")
public class ProductGraphApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductGraphApiApplication.class, args);
	}


	@Value("${server.port}")
	private int port;
	@Bean
	ApplicationRunner applicationRunner(){
		return args -> {
			log.info("GraphIQL URL: http://localhost:%s/graphiql".formatted(port));
		};
	}
}
