package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialDisplay;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().clearDriverCache().setup();
		WebDriverManager.chromedriver().clearResolutionCache().setup();
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}

	private void doLogOut()
	{
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
		WebElement logoutButton = driver.findElement(By.id("logout-button"));
		logoutButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Login"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 *
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 *
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Nested
	class TestSignupPage {
		private void doSignUp(String firstName, String lastName, String userName, String password){
			// Create a dummy account for logging in later.

			// Visit the sign-up page.
			WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
			driver.get("http://localhost:" + port + "/signup");
			webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

			// Fill out credentials
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
			WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
			inputFirstName.click();
			inputFirstName.sendKeys(firstName);

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
			WebElement inputLastName = driver.findElement(By.id("inputLastName"));
			inputLastName.click();
			inputLastName.sendKeys(lastName);

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
			WebElement inputUsername = driver.findElement(By.id("inputUsername"));
			inputUsername.click();
			inputUsername.sendKeys(userName);

			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
			WebElement inputPassword = driver.findElement(By.id("inputPassword"));
			inputPassword.click();
			inputPassword.sendKeys(password);

			// Attempt to sign up.
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
			WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
			buttonSignUp.click();
		}

		@BeforeEach
		public void beforeEach() {
			doSignUp("Signup", "Test User", "signuptestuser", "randompassword123");
		}

		@Test
		public void testSignUp(){
			doLogIn("signuptestuser", "randompassword123");
			Assertions.assertEquals("Home", driver.getTitle());
		}

		@Test
		public void testDuplicateUsernameSignUp(){
			doSignUp("Signup", "Test User", "signuptestuser", "randompassword123");
			Assertions.assertNotEquals("Home", driver.getTitle());
			Assertions.assertNotNull(driver.findElement(By.id("error-msg")));
			assertEquals("Username is taken", driver.findElement(By.id("signup-error")).getText());
		}
	}

	@Nested
	class TestAuthentication{

		@Test
		public void testHomePageRequiresAuthentication(){
			driver.get("http://localhost:" + port + "/home");
			Assertions.assertEquals("Login", driver.getTitle());
		}

		@Test
		public void testHomePageInaccessibleAfterLogout(){
			doMockSignUp("Homepage", "Test User", "homepagetestuser", "randompassword123");
			doLogIn("homepagetestuser", "randompassword123");
			doLogOut();
			driver.get("http://localhost:" + port + "/home");
			Assertions.assertEquals("Login", driver.getTitle());
		}
	}

	@Nested
	class TestFileTab {
		private FilesTab filesTab;
		private WebDriverWait webDriverWait;

		@BeforeEach
		public void beforeEach() {
			filesTab = new FilesTab(driver);
		}

		@Test
		public void testFileUpload() {
			doMockSignUp("File Upload","Test User","fileuploadtestuser","123");
			doLogIn("fileuploadtestuser", "123");
			filesTab.addFile("uploadfile.txt");

			Assertions.assertNotNull( driver.findElement(By.id("file-upload-success")));
		}

		@Test
		public void testFileDelete() {
			doMockSignUp("File Delete","Test User","filedeletetestuser","123");
			doLogIn("filedeletetestuser", "123");
			filesTab.addFile("uploadfile.txt");
			filesTab.deleteFirstFile();

			Assertions.assertNotNull( driver.findElement(By.id("delete-file-success")));
		}

		@Test
		public void testEmptyFileUpload() {
			doMockSignUp("Empty File","Test User","emptyfiletestuser","123");
			doLogIn("emptyfiletestuser", "123");
			filesTab.addFile("uploademptyfile.txt");

			Assertions.assertNotNull( driver.findElement(By.id("file-empty-error")));
		}

		@Test
		public void testDuplicateFileUploadError() {
			doMockSignUp("Duplicate File Upload","Test User","duplicatefileupload","123");
			doLogIn("duplicatefileupload", "123");
			filesTab.addFile("uploadfile.txt");
			webDriverWait = new WebDriverWait(driver, 2);
			webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("file-upload-success")));
			filesTab.addFile("uploadfile.txt");

			Assertions.assertNotNull( driver.findElement(By.id("file-upload-error")));
		}
	}

	@Nested
	class TestNotesTab{
		private NotesTab notesTab;

		@BeforeEach
		public void beforeEach() {
			notesTab = new NotesTab(driver);
		}

		private void addNotes()
		{
			notesTab.addNote("First title", "#1 description");
			notesTab.addNote("Second title", "#2 description");
			notesTab.addNote("Third title", "#3 description");
		}

		@Test
		public void testAddNote(){
			doMockSignUp("Add Note", "Test User", "addnotetestuser", "randompassword123");
			doLogIn("addnotetestuser", "randompassword123");
			addNotes();

			Assertions.assertNotNull(driver.findElement(By.id("add-note-success")));
			assertEquals(3, notesTab.getNumberOfNotesDisplayed());

			doLogOut();
			doLogIn("addnotetestuser", "randompassword123");

			Note firstNote = notesTab.getNotesDisplayed(1);
			assertEquals("First title", firstNote.getNoteTitle());
			assertEquals("#1 description", firstNote.getNoteDescription());

			Note secondNote = notesTab.getNotesDisplayed(2);
			assertEquals("Second title", secondNote.getNoteTitle());
			assertEquals("#2 description", secondNote.getNoteDescription());

			Note thirdNote = notesTab.getNotesDisplayed(3);
			assertEquals("Third title", thirdNote.getNoteTitle());
			assertEquals("#3 description", thirdNote.getNoteDescription());
		}

		@Test
		public void testUpdateNote(){
			doMockSignUp("Update Note", "Test User", "updatenotetestuser", "randompassword123");
			doLogIn("updatenotetestuser", "randompassword123");
			addNotes();
			notesTab.clickUpdateNoteButton();
			Assertions.assertNotNull(driver.findElement(By.id("noteModal")));
			assertEquals("First title", driver.findElement(By.id("note-title")).getAttribute("value"));
			assertEquals("#1 description", driver.findElement(By.id("note-description")).getAttribute("value"));

			notesTab.editNote("updated title 123","updated description 123");
			Assertions.assertNotNull(driver.findElement(By.id("update-note-success")));

			Note updatedFirstNote = notesTab.getNotesDisplayed(1);
			assertEquals("updated title 123", updatedFirstNote.getNoteTitle());
			assertEquals("updated description 123", updatedFirstNote.getNoteDescription());
		}

		@Test
		public void testDeleteNote(){
			doMockSignUp("Delete Note", "Test User", "deletenotetestuser", "randompassword123");
			doLogIn("deletenotetestuser", "randompassword123");
			addNotes();
			assertEquals(3, notesTab.getNumberOfNotesDisplayed());

			notesTab.deleteFirstNote();
			Assertions.assertNotNull(driver.findElement(By.id("delete-note-success")));

			assertEquals(2, notesTab.getNumberOfNotesDisplayed());
			Note firstNote = notesTab.getNotesDisplayed(1);
			assertEquals("Second title", firstNote.getNoteTitle());
			assertEquals("#2 description", firstNote.getNoteDescription());
		}

		@Test
		public void testAddNoteError() {
			doMockSignUp("Add Note Error", "Test User", "addnoteerror", "randompassword123");
			doLogIn("addnoteerror", "randompassword123");
			notesTab.addNote(" ", "no title");

			Assertions.assertNotNull(driver.findElement(By.id("add-note-error")));
			assertEquals(0, notesTab.getNumberOfNotesDisplayed());
		}

		@Test
		public void testUpdateNoteError() {
			doMockSignUp("Update Note Error", "Test User", "updatenoteerror", "randompassword123");
			doLogIn("updatenoteerror", "randompassword123");
			addNotes();

			notesTab.clickUpdateNoteButton();
			notesTab.editNote(" "," ");
			Assertions.assertNotNull(driver.findElement(By.id("update-note-error")));
		}
	}

	@Nested
	class TestCredentialsTab{
		private CredentialsTab credentialsTab;

		@BeforeEach
		public void beforeEach() {
			credentialsTab = new CredentialsTab(driver);
		}

		private void addCredentials() {
			credentialsTab.addCredential("First url", "url1user", "#1 password");
			credentialsTab.addCredential("Second url", "url2user", "#2 password");
			credentialsTab.addCredential("Third url", "url3user", "#3 password");
		}

		@Test
		public void testAddCredential(){
			doMockSignUp("Add Credential", "Test User", "addcredentialtestuser", "randompassword123");
			doLogIn("addcredentialtestuser", "randompassword123");
			addCredentials();

			Assertions.assertNotNull(driver.findElement(By.id("add-credential-success")));
			assertEquals(3, credentialsTab.getNumberOfCredentialsDisplayed());

			doLogOut();
			doLogIn("addcredentialtestuser", "randompassword123");

			CredentialDisplay firstCredential = credentialsTab.getCredentialDisplayed(1);
			assertEquals("First url", firstCredential.getUrl());
			assertEquals("url1user", firstCredential.getUsername());
			Assertions.assertNotEquals("#1 password", firstCredential.getPassword());

			credentialsTab.clickEditCredentialButton();
			Assertions.assertNotNull(driver.findElement(By.id("credentialModal")));
			assertEquals("#1 password", driver.findElement(By.id("credential-password")).getAttribute("value"));
		}

		@Test
		public void testUpdateCredential(){
			doMockSignUp("Update Credential", "Test User", "updatecredentialtestuser", "randompassword123");
			doLogIn("updatecredentialtestuser", "randompassword123");
			addCredentials();

			credentialsTab.clickEditCredentialButton();
			Assertions.assertNotNull(driver.findElement(By.id("credentialModal")));
			assertEquals("First url", driver.findElement(By.id("credential-url")).getAttribute("value"));
			assertEquals("url1user", driver.findElement(By.id("credential-username")).getAttribute("value"));
			assertEquals("#1 password", driver.findElement(By.id("credential-password")).getAttribute("value"));

			credentialsTab.editCredential("updated url","updatedurl1user", "updated #1 password");
			Assertions.assertNotNull(driver.findElement(By.id("update-credential-success")));

			CredentialDisplay updatedFirstCredential = credentialsTab.getCredentialDisplayed(1);
			assertEquals("updated url", updatedFirstCredential.getUrl());
			assertEquals("updatedurl1user", updatedFirstCredential.getUsername());

			credentialsTab.clickEditCredentialButton();
			assertEquals("updated #1 password", driver.findElement(By.id("credential-password")).getAttribute("value"));
		}

		@Test
		public void testDeleteCredential(){
			doMockSignUp("Delete Credential", "Test User", "deletecredentialtestuser", "randompassword123");
			doLogIn("deletecredentialtestuser", "randompassword123");
			addCredentials();
			assertEquals(3, credentialsTab.getNumberOfCredentialsDisplayed());

			credentialsTab.deleteFirstCredential();
			Assertions.assertNotNull(driver.findElement(By.id("delete-credential-success")));
			assertEquals(2, credentialsTab.getNumberOfCredentialsDisplayed());

			CredentialDisplay firstCredential = credentialsTab.getCredentialDisplayed(1);
			assertEquals("Second url", firstCredential.getUrl());
			assertEquals("url2user", firstCredential.getUsername());

			credentialsTab.clickEditCredentialButton();
			assertEquals("#2 password", driver.findElement(By.id("credential-password")).getAttribute("value"));
		}

		@Test
		public void testAddCredentialError(){
			doMockSignUp("Add Credential Error", "Test User", "addcredentialerror", "randompassword123");
			doLogIn("addcredentialerror", "randompassword123");
			credentialsTab.addCredential(" ", "url1user", "#1 password");

			Assertions.assertNotNull(driver.findElement(By.id("add-credential-error")));
			assertEquals(0, credentialsTab.getNumberOfCredentialsDisplayed());
		}

		@Test
		public void testUpdateCredentialError(){
			doMockSignUp("Update Credential Error", "Test User", "updatecredentialerror", "randompassword123");
			doLogIn("updatecredentialerror", "randompassword123");
			addCredentials();

			credentialsTab.clickEditCredentialButton();
			credentialsTab.editCredential("updated url"," ", "updated #1 password");
			Assertions.assertNotNull(driver.findElement(By.id("update-credential-error")));
		}

	}

}
