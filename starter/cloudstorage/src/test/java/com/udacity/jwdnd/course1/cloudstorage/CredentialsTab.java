package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialDisplay;
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

public class CredentialsTab {
    private WebDriverWait webDriverWait;

    @FindBy(id="nav-credentials-tab")
    private WebElement credentialTab;

    @FindBy(id="add-credential-button")
    private WebElement addCredentialButton;

    @FindBy(id = "edit-credential-button")
    private WebElement firstEditCredentialButton;

    @FindBy(id = "delete-credential-button")
    private WebElement firstDeleteCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement enterUrl;

    @FindBy(id = "credential-username")
    private WebElement enterUsername;

    @FindBy(id = "credential-password")
    private WebElement enterPassword;

    @FindBy(id = "save-credential-button")
    private WebElement saveCredentialButton;

    @FindAll(@FindBy(id = "credential-list"))
    private List<WebElement> credentialList;

    public CredentialsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = new WebDriverWait(driver, 2);
    }

    public void editCredential(String url, String username, String password) {
        enterUrl.clear();
        enterUrl.sendKeys(url);
        enterUsername.clear();
        enterUsername.sendKeys(username);
        enterPassword.clear();
        enterPassword.sendKeys(password);
        saveCredentialButton.click();
    }

    public void addCredential(String url, String username, String password) {
        credentialTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential-button")));
        addCredentialButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        editCredential(url, username, password);
    }


    public void clickEditCredentialButton() {
        credentialTab.click();
        firstEditCredentialButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
    }

    public void deleteFirstCredential() {
        credentialTab.click();
        firstDeleteCredentialButton.click();
    }

    public CredentialDisplay getCredentialDisplayed(int credentialDisplayNumber) {
        List<CredentialDisplay> credentialDisplayedList = new ArrayList<>();

        credentialList.forEach(credentialList -> {
            String url = credentialList.findElement(By.id("list-credential-url")).getText();
            String username = credentialList.findElement(By.id("list-credential-username")).getText();
            String password = credentialList.findElement(By.id("list-credential-password")).getText();
            CredentialDisplay credentialDisplayed = new CredentialDisplay(null, url, username, password, null, null);
            credentialDisplayedList.add(credentialDisplayed);
        });

        return credentialDisplayedList.get(credentialDisplayNumber - 1);
    }

    public int getNumberOfCredentialsDisplayed() {
        return credentialList.size();
    }


}
