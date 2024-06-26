package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNote(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotesByUserId(Integer userId);

    @Select("SELECT * FROM NOTES WHERE notetitle = #{noteTitle} AND userid = #{userId}")
    List<Note> getNotesByNoteTitle(String noteTitle, Integer userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription}, userid = #{userId} WHERE noteid = #{noteId}")
    int updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    int deleteNote(Integer noteId);

    @Delete("DELETE FROM NOTES WHERE userid = #{userId}")
    int deleteNotesByUserId(Integer userId);

}
