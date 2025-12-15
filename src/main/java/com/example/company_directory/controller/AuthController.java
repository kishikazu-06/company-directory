package com.example.company_directory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "locked", required = false) String locked,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "ユーザーIDまたはパスワードが正しくありません。");
        }
        if (locked != null) {
            model.addAttribute("error", "ログイン失敗が続いたため、このアカウントは15分後まで利用できません。");
        }
        if (logout != null) {
            model.addAttribute("message", "ログアウトしました。");
        }

        return "login";
    }
}
