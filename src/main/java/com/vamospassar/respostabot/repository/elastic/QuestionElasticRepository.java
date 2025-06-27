package com.vamospassar.respostabot.repository.elastic;

import com.vamospassar.respostabot.model.elastic.QuestionDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface QuestionElasticRepository extends ElasticsearchRepository<QuestionDocument, String> {
    @Query("{\"match\": {\"question\": \"?0\"}}")
    List<QuestionDocument> searchByQuestion(String question);

}