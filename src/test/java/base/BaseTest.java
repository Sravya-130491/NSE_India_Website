package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import pages.HomePage;
import pages.StockPage;
import utils.ConfigReader;
import utils.ScreenshotUtil;

import java.time.Duration;

public class BaseTest {
    private WebDriver driver;
    private String stockName;

    @Parameters("browser")
    @BeforeMethod
    public void setup(@Optional("chrome") String browser) {
        stockName = ConfigReader.getProperty("stockName");

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setPageLoadStrategy(PageLoadStrategy.NONE);
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36");
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setPageLoadStrategy(PageLoadStrategy.NONE);
            driver = new EdgeDriver(options);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        String url = ConfigReader.getProperty("url");
        System.out.println("Navigating to: " + url);
        driver.get(url);

        // With NONE strategy, driver.get returns immediately. Wait for page to render.
        waitForPageReady(8000);
        System.out.println("Homepage loaded. Title: " + driver.getTitle());
        System.out.println("Cookies established: " + driver.manage().getCookies().size());
    }

    private void waitForPageReady(long maxMs) {
        long start = System.currentTimeMillis();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (System.currentTimeMillis() - start < maxMs) {
            try {
                String state = (String) js.executeScript("return document.readyState");
                if ("complete".equals(state) || "interactive".equals(state)) {
                    try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                    return;
                }
            } catch (Exception ignored) {}
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
    }

    @Test
    public void testStockSearch() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        homePage.clickonNifty();
        System.out.println("After NIFTY click - Page Title: " + driver.getTitle());

        StockPage stockPage = new StockPage(driver);
        Thread.sleep(2000);
        stockPage.searchStock(stockName);

        ScreenshotUtil.takeScreenshot(driver, "testStockSearch_final");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                ScreenshotUtil.takeScreenshot(driver, "teardown");
            } catch (Exception ignored) {}
            driver.quit();
        }
    }
}





