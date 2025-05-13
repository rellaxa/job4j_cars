package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.user.UserService;

@Controller
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "/users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("newUser") User user, Model model, HttpServletRequest request) {
        var savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            var error = String.format("User with login: %s already exists.", user.getLogin());
            model.addAttribute("error", error);
            return "errors/404";
        }
        initSession(savedUser.get(), request);
        return "redirect:/posts";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "The login or password incorrect.");
            return "errors/404";
        }
        initSession(userOptional.get(), request);
        return "redirect:/posts";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    private void initSession(User user, HttpServletRequest request) {
        var session = request.getSession();
        session.setAttribute("user", user);
    }
}
