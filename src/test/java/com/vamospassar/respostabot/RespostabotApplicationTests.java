	package com.vamospassar.respostabot;

	import org.junit.jupiter.api.Test;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
	import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
	import org.springframework.test.context.ActiveProfiles;


	@ActiveProfiles("test")
	@SpringBootTest
	class RespostabotApplicationTests {

		@Test
		void contextLoads() {
		}

	}
