package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String signupView(){
        return "signup";
    }

    @PostMapping()
    public String postSignupForm(@ModelAttribute User user, Model model, final RedirectAttributes redirectAttributes){
        String signupError = null;

        if (!userService.isUsernameAvailable(user.getUsername())){
            signupError = "Username is taken";
        }

        if (signupError == null) {
            int rowsAdded = userService.addUser(user);
            if (rowsAdded != 1 ) {
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        if (signupError == null) {
            model.addAttribute("signupSuccess", true);
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return "redirect:login";
        } else {
            model.addAttribute("signupError", signupError);
            return "signup";
        }
    }


}
