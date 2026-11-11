
package com.example.quiz.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 4択クイズ用：Form */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizFourForm {

    /** 識別ID */
    private Integer id;

    /** クイズの内容 */
    @NotBlank
    private String question;

    /** 選択肢1 */
    @NotBlank
    private String option1;

    /** 選択肢2 */
    @NotBlank
    private String option2;

    /** 選択肢3 */
    @NotBlank
    private String option3;

    /** 選択肢4 */
    @NotBlank
    private String option4;

    /** クイズの正解番号 */
    @NotNull
    @Min(1)
    @Max(4)
    private Integer answer;

    /** 作成者 */
    @NotBlank
    private String author;

    /** 登録・変更判定用 */
    private Boolean newQuiz;
}
