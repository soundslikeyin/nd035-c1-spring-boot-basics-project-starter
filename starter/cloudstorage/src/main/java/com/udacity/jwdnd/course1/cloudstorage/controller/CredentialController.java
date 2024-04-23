package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Tab;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/credential")
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;
    private Tab tab;

    public CredentialController(CredentialService credentialService, UserService userService, Tab tab) {
        this.credentialService = credentialService;
        this.userService = userService;
        this.tab = tab;
    }

    @PostMapping("")
    public String addCredential(@ModelAttribute("credential") Credential credential, Authentication authentication, RedirectAttributes redirectAttributes) {
        Integer userId = getUserId(authentication);
        String emptyCredentialErrorMessage = "Credential URL or Username is empty or starts with an empty space, please try again";

        if (isCredentialUrlOrUserNameEmpty(credential) && credential.getCredentialId() == null) {
            redirectAttributes.addFlashAttribute("addCredentialFail", emptyCredentialErrorMessage);
        } else if (isCredentialUrlOrUserNameEmpty(credential) && credential.getCredentialId() != null) {
            redirectAttributes.addFlashAttribute("updateCredentialFail", emptyCredentialErrorMessage);
        } else if (credential.getCredentialId() == null) {
            try {
                credentialService.addCredential(credential, userId);
                redirectAttributes.addFlashAttribute("addCredentialSuccess", true);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("addCredentialFail", e.getMessage());
            }
        } else {
            try {
                credentialService.updateCredential(credential, userId);
                redirectAttributes.addFlashAttribute("updateCredentialSuccess", true);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("updateCredentialFail", e.getMessage());
            }
        }

        tab.setCurrentTab("credentials");
        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteCredential(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            credentialService.deleteCredential(id);
            redirectAttributes.addFlashAttribute("deleteCredentialSuccess", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("deleteCredentialFail", e.getMessage());
        }

        tab.setCurrentTab("credentials");
        return "redirect:/home";
    }

    private Integer getUserId(Authentication authentication){
        String username = authentication.getName();
        return userService.getUserId(username);
    }

    private boolean checkEmptyOrWhiteSpace(String str) {
        return str == null || str.isEmpty() || Character.isWhitespace(str.charAt(0));
    }

    private boolean isCredentialUrlOrUserNameEmpty(Credential credential) {
        return checkEmptyOrWhiteSpace(credential.getUrl()) || checkEmptyOrWhiteSpace(credential.getUsername());
    }

}
