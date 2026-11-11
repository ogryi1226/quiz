
package com.example.quiz.service;

import java.util.Optional;

import com.example.quiz.entity.QuizFour;

/** 4択クイズサービス処理：Service */
public interface QuizFourService {

    /** 4択クイズ情報を全件取得します */
    Iterable<QuizFour> selectAll();

    /** IDを指定して1件取得します */
    Optional<QuizFour> selectOneById(Integer id);

    /** ランダムで1件取得します */
    Optional<QuizFour> selectOneRandomQuiz();

    /** 正解・不正解を判定します */
    Boolean checkQuiz(Integer id, Integer myAnswer);

    /** 4択クイズを登録します */
    void insertQuiz(QuizFour quiz);

    /** 4択クイズを更新します */
    void updateQuiz(QuizFour quiz);

    /** 4択クイズを削除します */
    void deleteQuizById(Integer id);
}
