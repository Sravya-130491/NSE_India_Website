package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.HomePage;
import pages.StockPage;
import utils.ConfigReader;

import java.time.Duration;

public class BaseTest {
    private WebDriver driver;
    private WebDriverWait wait;
    String stockName = "TataMotors";

    @Test
    @Parameters("browser")
     @BeforeMethod
    public void setup(@Optional("chrome") String browser) throws InterruptedException {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            driver = new ChromeDriver(options);
        }
        else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            driver = new EdgeDriver(options);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(ConfigReader.getProperty("url"));
        HomePage homePage = new HomePage(driver);
        // Click a link that opens a new window
        homePage.clickonNifty();
        // Do something in the new window
        System.out.println(driver.getTitle());
        StockPage stockPage = new StockPage(driver);
        Thread.sleep(1000);
        stockPage.searchStock(stockName);
        Thread.sleep(1000);
        driver.quit();

    }

    }





