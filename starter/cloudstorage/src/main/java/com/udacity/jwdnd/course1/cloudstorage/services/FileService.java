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
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getAllFiles(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public File getFileByFileName(String fileName, Integer userId) {
        return fileMapper.getFileByFileName(fileName, userId);
    }

    public int addFile(MultipartFile file, Integer userId) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        File newFile = new File(null, fileName, file.getContentType(), file.getSize(), userId, file.getBytes() );

        return fileMapper.insertFile(newFile);
    }

    public int deleteFile(Integer fileId) {
        return fileMapper.deleteFile(fileId);
    }

    public int updateFile(File file) {
        return fileMapper.updateFile(file);
    }

    public boolean checkFileNameExists(String fileName, Integer userId) {
        return fileMapper.getFileByFileName(fileName, userId) != null;
    }
}
