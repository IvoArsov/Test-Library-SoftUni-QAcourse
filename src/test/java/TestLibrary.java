import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class TestLibrary {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private String currentBook;
    private String currentClient;
    private String startDateOfLend;
    private String returnDateOfLend;

    private WebDriver driver;

    @Before
    public void setUp(){
        this.driver = new FirefoxDriver();
        this.driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        this.driver.manage().window().maximize();
        this.driver.get("http://localhost:9966/library/admin/users/login");
    }

    private void loginAsAdmin(){
        WebElement usernameField = this.driver.findElement(By.id("username"));
        WebElement passwordField = this.driver.findElement(By.id("password"));
        WebElement submitButton = this.driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/fieldset/div[3]/div/button[2]"));

        usernameField.sendKeys(ADMIN_USERNAME);
        passwordField.sendKeys(ADMIN_PASSWORD);
        submitButton.click();
    }

    private void createOwnLibrary(){
        WebElement softuniAcc = this.driver.findElement(By.id("inputSoftuniAccount"));
        WebElement passwordField = this.driver.findElement(By.id("inputPassword"));
        WebElement submitButton = this.driver.findElement(By.xpath("/html/body/div/div[2]/div/form/fieldset/div[3]/div/button"));

        softuniAcc.sendKeys(ADMIN_USERNAME);
        passwordField.sendKeys(ADMIN_PASSWORD);
        submitButton.click();
    }


    private void createUniqueBook(){
        this.driver.get("http://localhost:9966/library/admin/books/add");

        WebElement titleField = this.driver.findElement(By.id("inputTitle"));
        WebElement isbnField = this.driver.findElement(By.id("inputIsbn"));
        WebElement dateField = this.driver.findElement(By.id("inputPublishedDate"));
        WebElement submitButton = this.driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/fieldset/div[5]/div/button[2]"));

        String uniqueBookName = UUID.randomUUID().toString();
        this.currentBook = uniqueBookName;

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddSSS");
        Date date = new Date();
        String uniqueIsbn = dateFormat.format(date);

        String publishedDate = "18-April-1997";

        titleField.sendKeys(uniqueBookName);
        isbnField.sendKeys(uniqueIsbn);
        dateField.sendKeys(publishedDate);
        submitButton.click();

    }

    private void createUniqueClient(){
        this.driver.get("http://localhost:9966/library/admin/clients/add");

        WebElement firstNameField = this.driver.findElement(By.id("inputFirstName"));
        WebElement lastNameField = this.driver.findElement(By.id("inputLastName"));
        WebElement pidField = this.driver.findElement(By.id("inputPid"));
        WebElement birthDateField = this.driver.findElement(By.id("inputBirthDate"));
        WebElement submitButton = this.driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/fieldset/div[5]/div/button[2]"));

        String uniqueClientFirstName = UUID.randomUUID().toString();
        String uniqueClientLastName = UUID.randomUUID().toString();
        this.currentClient = uniqueClientFirstName + " " + uniqueClientLastName;

        DateFormat dateFormat = new SimpleDateFormat("ddSSS");
        Date date = new Date();
        String uniquePid = dateFormat.format(date);

        String birthDateClient = "01-May-1987";

        firstNameField.sendKeys(uniqueClientFirstName);
        lastNameField.sendKeys(uniqueClientLastName);
        pidField.sendKeys(uniquePid);
        birthDateField.sendKeys(birthDateClient);
        submitButton.click();
    }

    private String startDateOfLend(){

        DateFormat dateFormatStartDate = new SimpleDateFormat("dd-MMM-yyyy");
        Date dateStartDate = new Date();
        String startDate = dateFormatStartDate.format(dateStartDate);
        this.startDateOfLend = startDate;

        return startDate;
    }

    private String returnDateOfLend(){

        DateFormat dateFormatReturnDate = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, 5);

        String returnDate = dateFormatReturnDate.format(calendar.getTime());
        this.returnDateOfLend = returnDate;

        return returnDate;
    }

    private void booksDropDown(String bookName){
        Select booksDropDown = new Select(this.driver.findElement(By.id("selectBook")));
        booksDropDown.selectByVisibleText(bookName);
    }

    private void clientsDropDown(String clientName){
        Select clientsDropDown = new Select(this.driver.findElement(By.id("selectClient")));
        clientsDropDown.selectByVisibleText(clientName);
    }

    private void startDate(String startDate){
        WebElement startDateField = this.driver.findElement(By.id("inputStartDate"));
        startDateField.sendKeys(startDate);
    }

    private void returnDate(String returnDate){
        WebElement returnDateField = this.driver.findElement(By.id("inputReturnDate"));
        returnDateField.sendKeys(returnDate);
    }

    private void submit(){
        WebElement submitButton = this.driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/form/fieldset/div[5]/div/button[2]"));
        submitButton.click();
    }

    @Test
    public void testCreateLend_allValid_expectedLendBeCreated(){
        loginAsAdmin();
        createOwnLibrary();
        createUniqueBook();
        createUniqueClient();

        this.driver.get("http://localhost:9966/library/admin/lends/add");

        booksDropDown(currentBook);
        clientsDropDown(currentClient);
        startDate(startDateOfLend());
        returnDate(returnDateOfLend());
        submit();

        WebElement lendInfo = this.driver.findElement(By.xpath("/html/body/div[2]/table/tbody"));

        Boolean isContainsClientName = lendInfo.getText().contains(currentClient);
        Boolean isContainsBookName = lendInfo.getText().contains(currentBook);
        //Boolean isContainsStartDate = lendInfo.getText().contains(startDateOfLend);
        //Boolean isContainsReturnDate = lendInfo.getText().contains(returnDateOfLend);

        Assert.assertTrue(isContainsClientName);
        Assert.assertTrue(isContainsBookName);
        //Assert.assertTrue(isContainsStartDate);
        //Assert.assertTrue(isContainsReturnDate);
    }

    @After
    public void tearDown(){
        this.driver.quit();
    }

}
