package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
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
    private String currentTab = "files";

    public HomeController(NoteService noteService, UserService userService, FileService fileService, CredentialService credentialService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
    }

    private Integer getUserId(Authentication authentication){
        String username = authentication.getName();
        return userService.getUserId(username);
    }

    @GetMapping()
    public String homeView(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, @ModelAttribute("credential") Credential credential, Model model) {
        Integer userId = getUserId(authentication);

        model.addAttribute("noteList", this.noteService.getAllNotes(userId));
        model.addAttribute("files", fileService.getAllFiles(userId));
        model.addAttribute("credentialList", this.credentialService.getAllCredential(userId));

        return "home";
    }

    @PostMapping("/notes")
    public String addNote(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, RedirectAttributes redirectAttributes) {
        Integer userId = getUserId(authentication);

        if (checkEmptyOrWhiteSpace(noteForm.getNoteTitle()) || checkEmptyOrWhiteSpace(noteForm.getNoteDescription())) {
            if (noteForm.getNoteId() == null) {
                redirectAttributes.addFlashAttribute("addNoteFail", "Note is empty or starts with an empty space, please try again");
            } else {
                redirectAttributes.addFlashAttribute("updateNoteFail", "Note is empty or starts with an empty space, please try again");
            }
        } else if (noteForm.getNoteId() == null) {
            try {
                Note note = new Note(null, noteForm.getNoteTitle(), noteForm.getNoteDescription(), userId);
                int result = noteService.addNote(note);
                if (result >= 1) {
                    redirectAttributes.addFlashAttribute("addNoteSuccess", true);
                } else {
                    redirectAttributes.addFlashAttribute("addNoteFail", "Adding note was unsuccessful, please try again");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("addNoteFail", "An exception has occurred. Adding note was unsuccessful, please try again");
            }
        } else {
            try {
                Note note = new Note(noteForm.getNoteId(), noteForm.getNoteTitle(), noteForm.getNoteDescription(), userId);
                int result = noteService.updateNote(note);
                if (result >= 1) {
                    redirectAttributes.addFlashAttribute("updateNoteSuccess", true);
                } else {
                    redirectAttributes.addFlashAttribute("updateNoteFail", "Note update was unsuccessful, please try again");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("updateNoteFail", "An exception has occurred. Note update was unsuccessful, please try again");
            }
        }

        noteForm.setNoteTitle("");
        noteForm.setNoteDescription("");
        currentTab = "notes";
        return "redirect:/home";
    }

    @GetMapping("/notes/delete/{id}")
    public String deleteNote(Authentication authentication, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Integer userId = getUserId(authentication);

        try {
            int result = noteService.deleteNote(id);
            if (result >= 1) {
                redirectAttributes.addFlashAttribute("deleteNoteSuccess", true);
            } else {
                redirectAttributes.addFlashAttribute("deleteNoteFail", "Note delete was unsuccessful, please try again");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("deleteNoteFail", "An exception has occurred. Note delete was unsuccessful, please try again");
        }

        currentTab = "notes";
        return "redirect:/home";
    }

    @PostMapping(path = "/files/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String uploadFile(Authentication authentication, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        Integer userId = getUserId(authentication);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("fileIsEmpty", true);
        } else if (fileService.checkFileNameExists(file.getOriginalFilename(), userId)) {
            redirectAttributes.addFlashAttribute("fileUploadFail", "You already uploaded a file with the same filename");
        } else {
            try {
                int result = fileService.addFile(file, userId);
                if (result >= 1) {
                    redirectAttributes.addFlashAttribute("fileUploadSuccess", true);
                } else {
                    redirectAttributes.addFlashAttribute("fileUploadFail", "File upload was unsuccessful, please try again");
                }
            } catch (Exception e)  {
                redirectAttributes.addFlashAttribute("fileUploadFail", "An exception has occurred. File upload was unsuccessful, please try again");
            }
        }

        currentTab = "files";
        return "redirect:/home";
    }

    @GetMapping("/files/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(Authentication authentication, @PathVariable String fileName, RedirectAttributes redirectAttributes) {
        Integer userId = getUserId(authentication);

        File file = fileService.getFileByFileName(fileName, userId);

        if (file == null) {
            redirectAttributes.addFlashAttribute("fileDownloadFail", "File download was unsuccessful, please try again");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

    @GetMapping("/files/delete/{id}")
    public String deleteFile(Authentication authentication, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Integer userId = getUserId(authentication);

        try {
            int result = fileService.deleteFile(id);
            if (result >= 1) {
                redirectAttributes.addFlashAttribute("deleteFileSuccess", true);
            } else {
                redirectAttributes.addFlashAttribute("deleteFileFail", "File delete was unsuccessful, please try again");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("deleteFileFail", "An exception has occurred. File delete was unsuccessful, please try again");
        }

        currentTab = "files";
        return "redirect:/home";
    }

    @PostMapping("/credential")
    public String addCredential(@ModelAttribute("credential") Credential credential, Authentication authentication, RedirectAttributes redirectAttributes) {
        Integer userId = getUserId(authentication);

        if (checkEmptyOrWhiteSpace(credential.getUrl()) || checkEmptyOrWhiteSpace(credential.getUsername()) ) {
            if (credential.getCredentialId() == null) {
                redirectAttributes.addFlashAttribute("addCredentialFail", "Credential URL or Username is empty or starts with an empty space, please try again");
            } else {
                redirectAttributes.addFlashAttribute("updateCredentialFail", "Credential URL or Username is empty or starts with an empty space, please try again");
            }
        } else if (credential.getCredentialId() == null) {
            try {
                Credential newCredential = new Credential(null, credential.getUrl(), credential.getUsername(), null, credential.getPassword(), userId);
                int result = credentialService.addCredential(newCredential);
                if (result >= 1) {
                    redirectAttributes.addFlashAttribute("addCredentialSuccess", true);
                } else {
                    redirectAttributes.addFlashAttribute("addCredentialFail", "Adding credential was unsuccessful, please try again");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("addCredentialFail", "An exception has occurred. Adding credential was unsuccessful, please try again");
            }
        } else {
            try {
                Credential updatedCredential = new Credential(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), null, credential.getPassword(), userId);
                int result = credentialService.updateCredential(updatedCredential);
                if (result >= 1) {
                    redirectAttributes.addFlashAttribute("updateCredentialSuccess", true);
                } else {
                    redirectAttributes.addFlashAttribute("updateCredentialFail", "Credential update was unsuccessful, please try again");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("updateCredentialFail", "An exception has occurred. Credential update was unsuccessful, please try again");
            }
        }

        currentTab = "credentials";
        return "redirect:/home";
    }

    @GetMapping("/credential/delete/{id}")
    public String deleteCredential(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            int result = credentialService.deleteCredential(id);
            if (result >= 1) {
                redirectAttributes.addFlashAttribute("deleteCredentialSuccess", true);
            } else {
                redirectAttributes.addFlashAttribute("deleteCredentialFail", "Credential delete was unsuccessful, please try again");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("deleteCredentialFail", "An exception has occurred. Credential delete was unsuccessful, please try again");
        }

        currentTab = "credentials";
        return "redirect:/home";
    }

    @ModelAttribute("showTab")
    public String showTab() {
        return currentTab;
    }

    private boolean checkEmptyOrWhiteSpace(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        } else return Character.isWhitespace(str.charAt(0));
    }
}

