
package com.example.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** トップ画面用：Controller */
@Controller
public class HomeController {

    /** トップ画面を表示 */
    @GetMapping("/")
    public String showHome() {
        return "home";
    }
}
