package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.io.File;

public class FilesTab {
    private WebDriverWait webDriverWait;

    @FindBy(id="nav-files-tab")
    private WebElement filesTab;

    @FindBy(id="fileUpload")
    private WebElement chooseFileButton;

    @FindBy(id="uploadButton")
    private WebElement uploadButton;

    @FindAll(@FindBy(id = "file-list"))
    private List<WebElement> fileList;

    @FindBy(id="view-file-button")
    private WebElement firstViewFileButton;

    @FindBy(id="delete-file-button")
    private WebElement firstDeleteFileButton;

    public FilesTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = new WebDriverWait(driver, 2);
    }

    public void addFile(String fileName) {
        filesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        chooseFileButton.sendKeys((new File(fileName).getAbsolutePath()));
        uploadButton.click();
    }

    public void deleteFirstFile() {
        filesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-file-button")));
        firstDeleteFileButton.click();
    }

}
