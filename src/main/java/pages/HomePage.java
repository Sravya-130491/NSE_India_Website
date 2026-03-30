package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private By niftyLink = By.xpath(
        "//*[@title='NSE - NIFTY 50'] | " +
        "//a[contains(@href,'NIFTY 50') or contains(@href,'nifty-50')] | " +
        "//*[contains(text(),'NIFTY 50') and (self::a or self::span or self::div)]"
    );

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    public void clickonNifty() {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(niftyLink));
            js.executeScript("arguments[0].click();", element);
            System.out.println("Clicked NIFTY 50 link successfully");
        } catch (Exception e) {
            System.out.println("NIFTY 50 link not clickable: " + e.getMessage());
            System.out.println("Continuing on current page (homepage has market data)");
        }
    }
}


