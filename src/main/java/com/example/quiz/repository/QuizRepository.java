
package com.example.quiz.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.quiz.entity.Quiz;

/** quizテーブル用：Repository */
public interface QuizRepository extends CrudRepository<Quiz, Integer> {

    /** ランダムで1件取得 */
    @Query("SELECT id FROM quiz ORDER BY RANDOM() LIMIT 1")
    Integer getRandomId();

    /** ランダムで10件取得 */
    @Query("SELECT * FROM quiz ORDER BY RANDOM() LIMIT 10")
    List<Quiz> getRandomQuizzes();
}
