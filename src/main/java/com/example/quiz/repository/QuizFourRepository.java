
package com.example.quiz.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.quiz.entity.QuizFour;

/** 4択クイズ用：Repository */
public interface QuizFourRepository
        extends CrudRepository<QuizFour, Integer> {

    /** ランダムでIDを1件取得します */
    @Query("SELECT id FROM quiz_four ORDER BY RANDOM() LIMIT 1")
    Integer getRandomId();
}
