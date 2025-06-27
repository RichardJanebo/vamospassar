package com.vamospassar.respostabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class RespostabotApplication {

	public static void main(String[] args) {
		SpringApplication.run(RespostabotApplication.class, args);
	}

}

