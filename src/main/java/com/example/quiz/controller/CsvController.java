
package com.example.quiz.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.example.quiz.entity.Quiz;
import com.example.quiz.entity.QuizFour;
import com.example.quiz.service.QuizFourService;
import com.example.quiz.service.QuizService;

/** CSV一括登録用：Controller */
@Controller
@RequestMapping("/quiz/csv")
public class CsvController {

    @Autowired
    QuizService quizService;

    @Autowired
    QuizFourService quizFourService;

    /** CSV登録画面を表示 */
    @GetMapping
    public String showCsv() {
        return "csv";
    }

    /** CSVファイルを読み込んで一括登録 */
    @PostMapping("/import")
    @Transactional
    public String importCsv(
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            model.addAttribute("msg", "CSVファイルを選択してください。");
            return "csv";
        }

        if (!type.equals("two") && !type.equals("four")) {
            model.addAttribute("msg", "クイズの種類が正しくありません。");
            return "csv";
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            List<Quiz> quizList = new ArrayList<>();
            List<QuizFour> quizFourList = new ArrayList<>();

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // 1行目は見出しなので読み飛ばす
                if (lineNumber == 1) {
                    line = line.replace("\uFEFF", "");
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                List<String> columns = parseCsvLine(line);

                if (type.equals("two")) {

                    if (columns.size() != 3) {
                        throw new IllegalArgumentException(
                                lineNumber + "行目：2択CSVは3項目必要です。");
                    }

                    String question = columns.get(0).trim();
                    String answerText = columns.get(1).trim().toLowerCase();
                    String author = columns.get(2).trim();

                    if (question.isEmpty() || author.isEmpty()
                            || author.length() > 20
                            || (!answerText.equals("true")
                            && !answerText.equals("false"))) {
                        throw new IllegalArgumentException(
                                lineNumber + "行目：問題・回答・作成者を確認してください。");
                    }

                    Quiz quiz = new Quiz();
                    quiz.setQuestion(question);
                    quiz.setAnswer(Boolean.parseBoolean(answerText));
                    quiz.setAuthor(author);

                    quizList.add(quiz);

                } else {

                    if (columns.size() != 7) {
                        throw new IllegalArgumentException(
                                lineNumber + "行目：4択CSVは7項目必要です。");
                    }

                    String question = columns.get(0).trim();
                    String option1 = columns.get(1).trim();
                    String option2 = columns.get(2).trim();
                    String option3 = columns.get(3).trim();
                    String option4 = columns.get(4).trim();
                    String answerText = columns.get(5).trim();
                    String author = columns.get(6).trim();

                    int answer;

                    try {
                        answer = Integer.parseInt(answerText);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                lineNumber + "行目：正解番号は1～4で入力してください。");
                    }

                    if (question.isEmpty()
                            || option1.isEmpty()
                            || option2.isEmpty()
                            || option3.isEmpty()
                            || option4.isEmpty()
                            || author.isEmpty()
                            || author.length() > 20
                            || answer < 1 || answer > 4) {
                        throw new IllegalArgumentException(
                                lineNumber + "行目：入力内容を確認してください。");
                    }

                    QuizFour quiz = new QuizFour();
                    quiz.setQuestion(question);
                    quiz.setOption1(option1);
                    quiz.setOption2(option2);
                    quiz.setOption3(option3);
                    quiz.setOption4(option4);
                    quiz.setAnswer(answer);
                    quiz.setAuthor(author);

                    quizFourList.add(quiz);
                }
            }

            if (lineNumber <= 1 || (quizList.isEmpty() && quizFourList.isEmpty())) {
                model.addAttribute("msg", "登録するデータがありません。");
                return "csv";
            }

            // 内容をすべて確認してからデータベースへ登録
            for (Quiz quiz : quizList) {
                quizService.insertQuiz(quiz);
            }

            for (QuizFour quiz : quizFourList) {
                quizFourService.insertQuiz(quiz);
            }

            int count = quizList.size() + quizFourList.size();

            redirectAttributes.addFlashAttribute(
                    "msg", count + "件の登録が完了しました。");

            return "redirect:/quiz/csv";

        } catch (Exception e) {

            // 登録途中のエラーも含めて、すべて取り消す
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            model.addAttribute("msg", "CSVの読み込みに失敗しました：" + e.getMessage());
            return "csv";
        }
    }

    /** CSVの1行を項目ごとに分割 */
    private List<String> parseCsvLine(String line) {

        List<String> columns = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {
                    value.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                columns.add(value.toString());
                value.setLength(0);
            } else {
                value.append(c);
            }
        }

        if (inQuotes) {
            throw new IllegalArgumentException("CSVのダブルクォーテーションが正しくありません。");
        }

        columns.add(value.toString());

        return columns;
    }
}
