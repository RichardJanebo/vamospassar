package com.vamospassar.respostabot.repository.elastic;

import com.vamospassar.respostabot.model.elastic.QuestionDocument;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataElasticsearchTest
@Testcontainers
class QuestionElasticRepositoryTest {


    @Container
    static ElasticsearchContainer elasticsearchContainer =
            new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.12.0")
                    .withEnv("discovery.type", "single-node")
                    .withEnv("xpack.security.enabled", "false")
                    .withStartupAttempts(3)
                    .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", elasticsearchContainer::getHttpHostAddress);
    }

    @Autowired
    private QuestionElasticRepository questionElasticRepository;

    @DisplayName("Should search by question when exist")
    @Order(1)
    @Test
    void search_by_question_when_exist() {
        QuestionDocument questionDocument = new QuestionDocument();
        questionDocument.setId("1");
        questionDocument.setQuestion("Qual a capital da frança");
        questionDocument.setResponse("Paris");

        questionElasticRepository.save(questionDocument);

        List<QuestionDocument> results = questionElasticRepository.searchByQuestion("capital França");


        Assertions.assertThat(results).isNotEmpty();
        Assertions.assertThat(results.get(0).getResponse()).contains("Paris");
    }


    @Order(2)
    @DisplayName("Should search by question when not exist")
    @Test
    void search_by_question_when_not_exist(){
        Assertions.assertThat(questionElasticRepository.searchByQuestion("Essa questão não existe")).isEmpty();
    }
}