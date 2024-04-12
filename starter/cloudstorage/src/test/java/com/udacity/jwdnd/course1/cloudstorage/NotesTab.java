package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class NotesTab{
    private WebDriverWait webDriverWait;

    @FindBy(id="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id="add-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "edit-note-button")
    private WebElement firstEditNoteButton;

    @FindBy(id = "delete-note-button")
    private WebElement firstDeleteNoteButton;

    @FindBy(id = "note-title")
    private WebElement enterNoteTitle;

    @FindBy(id = "note-description")
    private WebElement enterNoteDescription;

    @FindBy(id = "save-note-button")
    private WebElement saveNoteButton;

    @FindAll(@FindBy(id = "note-list"))
    private List<WebElement> noteList;

    public NotesTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = new WebDriverWait(driver, 2);
    }

    public void editNote(String title, String description) {
        enterNoteTitle.clear();
        enterNoteTitle.sendKeys(title);
        enterNoteDescription.clear();
        enterNoteDescription.sendKeys(description);
        saveNoteButton.click();
    }

    public void addNote(String title, String description) {
        notesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-button")));
        addNoteButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        editNote(title, description);
    }


    public void clickUpdateNoteButton() {
        notesTab.click();
        firstEditNoteButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
    }

    public void deleteFirstNote() {
        notesTab.click();
        firstDeleteNoteButton.click();
    }

    public Note getNotesDisplayed(int noteDisplayNumber) {
        List<Note> notesDisplayed = new ArrayList<>();

        noteList.forEach(noteList -> {
            String noteTitle = noteList.findElement(By.id("list-note-title")).getText();
            String noteDescription = noteList.findElement(By.id("list-note-description")).getText();
            Note note = new Note(null, noteTitle, noteDescription, null);
            notesDisplayed.add(note);
        });

        return notesDisplayed.get(noteDisplayNumber - 1);
    }

    public int getNumberOfNotesDisplayed() {
        return noteList.size();
    }


}
