package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class HomePage  {

    private WebDriver driver;
    private WebDriverWait wait;
    private By niftyimage = By.xpath("(//*[@title='NSE - NIFTY 50'])[1]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }
    public void clickonNifty() {

        // Click a link that opens a new window

        driver.findElement(niftyimage).click();

    }
    }


