package com.lazy.longtengzt.esdao;

import com.lazy.longtengzt.model.dto.question.QuestionEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 题目 ES 操作
 *
 * @author Lazy
 */
public interface QuestionEsDao extends ElasticsearchRepository<QuestionEsDTO, Long> {
    List<QuestionEsDTO> findByUserId(Long userId);
}