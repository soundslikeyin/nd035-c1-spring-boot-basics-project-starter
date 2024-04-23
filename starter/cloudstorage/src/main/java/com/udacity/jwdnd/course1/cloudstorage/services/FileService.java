package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    String fileDownloadErrorMessage = "File download was unsuccessful, please try again";
    String fileUploadErrorMessage = "File upload was unsuccessful, please try again";
    String fileUpdateErrorMessage = "File update was unsuccessful, please try again";
    String fileDeleteErrorMessage = "File delete was unsuccessful, please try again";

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getAllFiles(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public File getFileByFileName(String fileName, Integer userId) throws Exception {

        File file = fileMapper.getFileByFileName(fileName, userId);

        if (file == null) {
            throw new Exception (fileDownloadErrorMessage);
        }

        return fileMapper.getFileByFileName(fileName, userId);
    }

    public void addFile(MultipartFile file, Integer userId) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        File newFile = new File(null, fileName, file.getContentType(), file.getSize(), userId, file.getBytes() );

        int result = fileMapper.insertFile(newFile);
        if (result < 1) {
            throw new Exception(fileUploadErrorMessage);
        }
    }

    public void deleteFile(Integer fileId) throws Exception {

        int result = fileMapper.deleteFile(fileId);
        if (result < 1) {
            throw new Exception(fileDeleteErrorMessage);
        }
    }

    public void updateFile(File file) throws Exception {
        int result = fileMapper.updateFile(file);
        if (result < 1) {
            throw new Exception(fileUpdateErrorMessage);
        }
    }

    public boolean checkFileNameExists(String fileName, Integer userId) {
        return fileMapper.getFileByFileName(fileName, userId) != null;
    }
}
