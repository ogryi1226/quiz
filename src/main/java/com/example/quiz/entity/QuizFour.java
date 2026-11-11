
package com.example.quiz.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** quiz_fourテーブル用：Entity */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("quiz_four")
public class QuizFour {

    /** 識別ID */
    @Id
    private Integer id;

    /** クイズの内容 */
    private String question;

    /** 選択肢1 */
    private String option1;

    /** 選択肢2 */
    private String option2;

    /** 選択肢3 */
    private String option3;

    /** 選択肢4 */
    private String option4;

    /** クイズの正解番号（1〜4） */
    private Integer answer;

    /** 作成者 */
    private String author;
}
