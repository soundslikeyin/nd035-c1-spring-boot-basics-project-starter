package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFileByFileId(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    File getFileByFileName(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFilesByUserId(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Update("UPDATE FILES SET filename = #{fileName}, contenttype = #{contentType}, filesize = #{fileSize}, userid = #{userId}, filedata = #{fileData} WHERE fileid = #{fileId}")
    int updateFile(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    int deleteFile(Integer fileId);

    @Delete("DELETE FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    int deleteFilesByFileName(String fileName, Integer userId);

    @Delete("DELETE FROM FILES WHERE userid = #{userId}")
    int deleteFilesByUserId(Integer userId);
}
