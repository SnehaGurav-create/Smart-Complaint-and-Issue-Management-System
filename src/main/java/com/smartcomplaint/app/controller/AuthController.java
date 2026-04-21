package com.smartcomplaint.app.controller;

import com.smartcomplaint.app.dto.RegisterDTO;
import com.smartcomplaint.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

// [REQUIREMENT MET] @Controller layer
@Controller
public class AuthController {

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        return "index";
    }

    @GetMapping("/dispatch")
    public String dispatch(Principal principal) {
        if (principal != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/user/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String type, Model model) {
        if (type != null) {
            model.addAttribute("loginType", type);
        }
        return "auth/login";
    }

    // GET /register -> show register form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "auth/register";
    }

    // POST /register -> save user
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO, 
                                      BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerUser(registerDTO);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "error.registerDTO", e.getMessage());
            return "auth/register";
        }
    }
}
