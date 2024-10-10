package com.lew663.blog;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BlogApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().directory("./.env").load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue()));
		dotenv.entries().forEach(entry ->
        log.info("{} : {}", entry.getKey(), entry.getValue()));
		log.info("RDS : {}",System.getenv("DATABASE_URL"));
		SpringApplication.run(BlogApplication.class, args);
	}

}
