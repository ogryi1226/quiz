
package com.example.quiz.form;

import java.util.List;

import lombok.Data;

/** 10問チャレンジ用：Form */
@Data
public class QuizChallengeForm {

    /** 問題ID一覧 */
    private List<Integer> ids;

    /** ユーザーの回答一覧 */
    private List<Boolean> answers;
}
