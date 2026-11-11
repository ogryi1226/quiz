
package com.example.quiz.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.quiz.entity.Quiz;
import com.example.quiz.form.QuizChallengeForm;
import com.example.quiz.service.QuizService;

/** 10問チャレンジ用：Controller */
@Controller
@RequestMapping("/quiz/challenge")
public class QuizChallengeController {

    @Autowired
    QuizService service;

    /** 10問をランダムに出題 */
    @GetMapping
    public String showChallenge(Model model) {

        List<Quiz> quizList = service.selectTenRandomQuizzes();

        model.addAttribute("quizList", quizList);
        model.addAttribute("quizChallengeForm", new QuizChallengeForm());

        if (quizList.size() < 10) {
            model.addAttribute("msg", "10問出題するには、問題を10件以上登録してください。");
        }

        return "challenge";
    }

    /** 10問まとめて採点 */
    @PostMapping("/check")
    public String checkChallenge(
            @ModelAttribute QuizChallengeForm quizChallengeForm,
            Model model) {

        List<Integer> ids = quizChallengeForm.getIds();
        List<Boolean> answers = quizChallengeForm.getAnswers();

        // 10問分の回答がそろっているか確認
        if (ids == null || answers == null
                || ids.size() != 10 || answers.size() != 10
                || answers.contains(null)) {
            return "redirect:/quiz/challenge";
        }

        int score = 0;

        List<Map<String, Object>> results = new ArrayList<>();

        // 10問それぞれの正解・不正解を判定
        for (int i = 0; i < 10; i++) {

            Integer id = ids.get(i);
            Boolean myAnswer = answers.get(i);

            Optional<Quiz> quizOpt = service.selectOneById(id);

            if (quizOpt.isEmpty()) {
                return "redirect:/quiz/challenge";
            }

            Quiz quiz = quizOpt.get();

            boolean correct = quiz.getAnswer().equals(myAnswer);

            if (correct) {
                score++;
            }

            results.add(Map.of(
                    "question", quiz.getQuestion(),
                    "myAnswer", myAnswer,
                    "correctAnswer", quiz.getAnswer(),
                    "correct", correct
            ));
        }

        model.addAttribute("score", score);
        model.addAttribute("results", results);
        model.addAttribute("msg", score == 10
                ? "全問正解です！おめでとう！"
                : "お疲れさまでした！");

        return "challenge-answer";
    }
}
