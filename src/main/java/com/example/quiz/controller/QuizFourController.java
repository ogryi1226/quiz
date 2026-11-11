
package com.example.quiz.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.quiz.entity.QuizFour;
import com.example.quiz.form.QuizFourForm;
import com.example.quiz.service.QuizFourService;

/** 4択クイズ用：Controller */
@Controller
@RequestMapping("/quiz/four")
public class QuizFourController {

    @Autowired
    QuizFourService service;

    /** Formの初期設定 */
    @ModelAttribute
    public QuizFourForm setUpForm() {
        QuizFourForm form = new QuizFourForm();
        form.setAnswer(1);
        return form;
    }

    /** 4択クイズ一覧を表示 */
    @GetMapping
    public String showList(QuizFourForm quizFourForm, Model model) {
        quizFourForm.setNewQuiz(true);

        Iterable<QuizFour> list = service.selectAll();

        model.addAttribute("list", list);
        model.addAttribute("title", "4択クイズ登録用フォーム");

        return "four/crud";
    }

    /** 4択クイズを登録 */
    @PostMapping("/insert")
    public String insert(
            @Validated QuizFourForm quizFourForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        QuizFour quiz = makeQuiz(quizFourForm);

        if (!bindingResult.hasErrors()) {
            service.insertQuiz(quiz);

            redirectAttributes.addFlashAttribute(
                    "complete", "登録が完了しました");

            return "redirect:/quiz/four";
        } else {
            return showList(quizFourForm, model);
        }
    }

    /** 編集画面を表示 */
    @GetMapping("/edit/{id}")
    public String showUpdate(
            QuizFourForm quizFourForm,
            @PathVariable("id") Integer id,
            Model model) {

        Optional<QuizFour> quizOpt = service.selectOneById(id);

        if (quizOpt.isEmpty()) {
            return "redirect:/quiz/four";
        }

        quizFourForm = makeQuizFourForm(quizOpt.get());

        makeUpdateModel(quizFourForm, model);

        return "four/crud";
    }

    /** 4択クイズを更新 */
    @PostMapping("/update")
    public String update(
            @Validated QuizFourForm quizFourForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        QuizFour quiz = makeQuiz(quizFourForm);

        if (!bindingResult.hasErrors()) {
            service.updateQuiz(quiz);

            redirectAttributes.addFlashAttribute(
                    "complete", "更新が完了しました");

            return "redirect:/quiz/four";
        } else {
            makeUpdateModel(quizFourForm, model);
            return "four/crud";
        }
    }

    /** 更新画面用のModel設定 */
    private void makeUpdateModel(
            QuizFourForm quizFourForm, Model model) {

        model.addAttribute("id", quizFourForm.getId());

        quizFourForm.setNewQuiz(false);

        model.addAttribute("quizFourForm", quizFourForm);
        model.addAttribute("title", "4択クイズ更新用フォーム");
    }

    /** FormからEntityへ詰め替え */
    private QuizFour makeQuiz(QuizFourForm form) {

        QuizFour quiz = new QuizFour();

        quiz.setId(form.getId());
        quiz.setQuestion(form.getQuestion());
        quiz.setOption1(form.getOption1());
        quiz.setOption2(form.getOption2());
        quiz.setOption3(form.getOption3());
        quiz.setOption4(form.getOption4());
        quiz.setAnswer(form.getAnswer());
        quiz.setAuthor(form.getAuthor());

        return quiz;
    }

    /** EntityからFormへ詰め替え */
    private QuizFourForm makeQuizFourForm(QuizFour quiz) {

        QuizFourForm form = new QuizFourForm();

        form.setId(quiz.getId());
        form.setQuestion(quiz.getQuestion());
        form.setOption1(quiz.getOption1());
        form.setOption2(quiz.getOption2());
        form.setOption3(quiz.getOption3());
        form.setOption4(quiz.getOption4());
        form.setAnswer(quiz.getAnswer());
        form.setAuthor(quiz.getAuthor());

        return form;
    }

    /** 4択クイズを削除 */
    @PostMapping("/delete")
    public String delete(
            @RequestParam("id") String id,
            RedirectAttributes redirectAttributes) {

        service.deleteQuizById(Integer.parseInt(id));

        redirectAttributes.addFlashAttribute(
                "delcomplete", "削除が完了しました");

        return "redirect:/quiz/four";
    }
    
    /** 4択クイズをランダムで1問表示 */
    @GetMapping("/play")
    public String showQuiz(QuizFourForm quizFourForm, Model model) {

        Optional<QuizFour> quizOpt = service.selectOneRandomQuiz();

        if (quizOpt.isPresent()) {
            quizFourForm = makeQuizFourForm(quizOpt.get());
            model.addAttribute("quizFourForm", quizFourForm);
        } else {
            model.addAttribute("msg", "問題がありません・・・");
        }

        return "four/play";
    }
    
    /** 4択クイズの正解・不正解を判定 */
    @PostMapping("/check")
    public String checkQuiz(
            @RequestParam("id") Integer id,
            @RequestParam("answer") Integer answer,
            Model model) {

        Optional<QuizFour> quizOpt = service.selectOneById(id);

        if (quizOpt.isEmpty()) {
            model.addAttribute("msg", "問題が見つかりませんでした。");
            return "four/play";
        }

        QuizFour quiz = quizOpt.get();

        if (service.checkQuiz(id, answer)) {
            model.addAttribute("msg", "正解です！");
        } else {
            model.addAttribute("msg", "残念、不正解です・・・");
        }

        model.addAttribute("quizFourForm", makeQuizFourForm(quiz));
        model.addAttribute("myAnswer", answer);

        return "four/answer";
    }
}
