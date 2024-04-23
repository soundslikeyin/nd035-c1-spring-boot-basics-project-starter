package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {
    String addNoteFailMessage = "Adding note was unsuccessful, please try again";
    String updateNoteFailMessage = "Note update was unsuccessful, please try again";
    String deleteNoteFailMessage = "Note delete was unsuccessful, please try again";

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public void addNote(NoteForm noteForm, Integer userId) throws Exception {
        Note note = new Note(null, noteForm.getNoteTitle(), noteForm.getNoteDescription(), userId);

        int result = noteMapper.insertNote(note);
        if (result < 1) {
            throw new Exception(addNoteFailMessage);
        }
    }

    public void deleteNote(Integer noteId) throws Exception {

        int result = noteMapper.deleteNote(noteId);
        if (result < 1) {
            throw new Exception(deleteNoteFailMessage);
        }
    }

    public void updateNote(NoteForm noteForm, Integer userId) throws Exception{
        Note note = new Note(noteForm.getNoteId(), noteForm.getNoteTitle(), noteForm.getNoteDescription(), userId);

        int result = noteMapper.updateNote(note);
        if (result < 1) {
            throw new Exception(updateNoteFailMessage);
        }

    }
}
