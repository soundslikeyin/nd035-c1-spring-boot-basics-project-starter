package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Tab;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/home/files")
public class FilesController {
    private FileService fileService;
    private UserService userService;
    private Tab tab;

    public FilesController(FileService fileService, UserService userService, Tab tab) {
        this.fileService = fileService;
        this.userService = userService;
        this.tab = tab;
    }

    @PostMapping(path = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String uploadFile(Authentication authentication, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        String fileNameAlreadyExistsMessage = "You already uploaded a file with the same filename";
        Integer userId = getUserId(authentication);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("fileIsEmpty", true);
        } else if (fileService.checkFileNameExists(file.getOriginalFilename(), userId)) {
            redirectAttributes.addFlashAttribute("fileUploadFail", fileNameAlreadyExistsMessage);
        } else {
            try {
                fileService.addFile(file, userId);
                redirectAttributes.addFlashAttribute("fileUploadSuccess", true);
            } catch (Exception e)  {
                redirectAttributes.addFlashAttribute("fileUploadFail", e.getMessage());
            }
        }

        tab.setCurrentTab("files");
        return "redirect:/home";
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(Authentication authentication, @PathVariable String fileName, RedirectAttributes redirectAttributes) {
        Integer userId = getUserId(authentication);

        try {
            File file = fileService.getFileByFileName(fileName, userId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(new ByteArrayResource(file.getFileData()));

        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("fileDownloadFail", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(id);
            redirectAttributes.addFlashAttribute("deleteFileSuccess", true);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("deleteFileFail", e.getMessage());
        }

        tab.setCurrentTab("files");
        return "redirect:/home";
    }

    private Integer getUserId(Authentication authentication){
        String username = authentication.getName();
        return userService.getUserId(username);
    }
}
