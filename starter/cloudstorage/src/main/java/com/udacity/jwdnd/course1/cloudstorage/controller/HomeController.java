package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController{

    private NoteService noteService;
    private UserService userService;
    private FileService fileService;
    private CredentialService credentialService;
    private Tab tab;

    public HomeController(UserService userService, NoteService noteService, FileService fileService, CredentialService credentialService, Tab tab) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.tab = tab;
    }

    @GetMapping()
    public String homeView(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, @ModelAttribute("credential") Credential credential, Model model) {
        Integer userId = getUserId(authentication);

        model.addAttribute("noteList", this.noteService.getAllNotes(userId));
        model.addAttribute("files", fileService.getAllFiles(userId));
        model.addAttribute("credentialList", this.credentialService.getAllCredential(userId));

        return "home";
    }

    @ModelAttribute("showTab")
    public String showTab() {
        return tab.getCurrentTab();
    }

    private Integer getUserId(Authentication authentication){
        String username = authentication.getName();
        return userService.getUserId(username);
    }
}

