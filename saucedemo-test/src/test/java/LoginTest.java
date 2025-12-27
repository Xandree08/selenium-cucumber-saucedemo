import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.LoggerFactory;

import com.saucedemo.test.utils.selenium.DriverFactory;
import org.slf4j.Logger;


public class LoginTest {
    
    private static final Logger log = LoggerFactory.getLogger(LoginTest.class);

    private WebDriver driver;

    @BeforeEach
    void setup(){
        driver = DriverFactory.getDriver();
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    void successfulLogin(){
        log.info("Initializing successfull login test ... ");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");;
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        log.info("Test Successful, this login is correct.");

    }

    @Test
    void invalidLoginPasswordNotMatch(){
        log.info("Initializing test if password not match ...");
        driver.findElement(By.id("user-name")).sendKeys("secret-user");
        driver.findElement(By.id("password")).sendKeys("invalidpassword");
        driver.findElement(By.id("login-button")).click();

        String errorMessage = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        assertTrue(errorMessage.contains("Username and password do not match"));
        log.info("Test Successfull, this Password not Match");
    }

    @Test
    void lockedOutLogin (){
        log.info("Initializing test for locked out logins ...");
        driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        String errorMessage = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        assertTrue(errorMessage.contains("Sorry, this user has been locked out."));
        log.info("Test Successfull, user has been locked out");
    }

    @Test
    void problemUserProductImagesEquals(){
        driver.findElement(By.id("user-name")).sendKeys("problem_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());

        List<WebElement> images = driver.findElements(By.cssSelector(".inventory_item_img"));
        Set<String> srcImages= new HashSet<>();

        for (WebElement img : images){
            srcImages.add(img.getAttribute("src"));
        }

        assertTrue(srcImages.size() > 1, "All images are equals, bug detected");
    }

    @Test
    void errorUserSortingProducts (){
        driver.findElement(By.id("user-name")).sendKeys("problem_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        driver.findElement(By.className(".select_container")).click();

        Select select = new Select(driver.findElement(By.className(".product_sort_container")));
        select.selectByValue("az");
        Alert alert = driver.switchTo().alert();

        assertEquals(alert.getText(), "Sorting is broken! This error has been reported to Backtrace.");
        alert.accept();
    }



    @AfterEach
    void quit(){
        DriverFactory.quitDriver();
    }

}
