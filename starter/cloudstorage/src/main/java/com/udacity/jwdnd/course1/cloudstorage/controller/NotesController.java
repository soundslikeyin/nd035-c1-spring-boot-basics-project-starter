package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Tab;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/notes")
public class NotesController {
    private NoteService noteService;
    private UserService userService;
    private Tab tab;

    public NotesController(NoteService noteService, UserService userService, Tab tab) {
        this.noteService = noteService;
        this.userService = userService;
        this.tab = tab;
    }

    @PostMapping()
    public String addNote(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, RedirectAttributes redirectAttributes) {
        Integer userId = getUserId(authentication);
        String emptyNoteErrorMessage = "Note is empty or starts with an empty space, please try again";

        if (isNoteEmpty(noteForm) && noteForm.getNoteId() == null) {
            redirectAttributes.addFlashAttribute("addNoteFail", emptyNoteErrorMessage);
        } else if (isNoteEmpty(noteForm) && noteForm.getNoteId() != null) {
            redirectAttributes.addFlashAttribute("updateNoteFail", emptyNoteErrorMessage);
        } else if (noteForm.getNoteId() == null) {
            try {
                noteService.addNote(noteForm, userId);
                redirectAttributes.addFlashAttribute("addNoteSuccess", true);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("addNoteFail", e.getMessage());
            }
        } else {
            try {
                noteService.updateNote(noteForm, userId);
                redirectAttributes.addFlashAttribute("updateNoteSuccess", true);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("updateNoteFail", e.getMessage());
            }
        }

        noteForm.setNoteTitle("");
        noteForm.setNoteDescription("");
        tab.setCurrentTab("notes");

        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(Authentication authentication, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Integer userId = getUserId(authentication);

        try {
            noteService.deleteNote(id);
            redirectAttributes.addFlashAttribute("deleteNoteSuccess", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("deleteNoteFail", e.getMessage());
        }

        tab.setCurrentTab("notes");
        return "redirect:/home";
    }

    private Integer getUserId(Authentication authentication){
        String username = authentication.getName();
        return userService.getUserId(username);
    }

    private boolean checkEmptyOrWhiteSpace(String str) {
        return str == null || str.isEmpty() || Character.isWhitespace(str.charAt(0));
    }

    private boolean isNoteEmpty(NoteForm noteForm) {
        return checkEmptyOrWhiteSpace(noteForm.getNoteTitle()) || checkEmptyOrWhiteSpace(noteForm.getNoteDescription());
    }

}
