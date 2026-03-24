package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.Assertion;

public class StockPage  {
    private WebDriver driver;
    private WebDriverWait wait;
    private By dropdownlist = By.xpath("(//div[@id=\"header-search-input_listbox\"]//div/div)[1]");
    private By week52highVal = By.xpath("//span[@id='week52highVal']");
    private By week52lowVal = By.xpath("//span[@id='week52lowVal']");
    private By texthighvalue= By.xpath("//*[text()='52 Week High ']");
    private By textlowvalue= By.xpath("//*[text()='52 Week Low']");

    public StockPage(WebDriver driver) {
        this.driver = driver;
    }

    public void searchStock(String stockName) throws InterruptedException {


       WebElement searchInput = driver.findElement(By.id("header-search-input")) ;
       System.out.println("clicked-------------------------->");
        searchInput.clear();
        searchInput.click();
        Thread.sleep(1000);
        searchInput.sendKeys(stockName);
        Thread.sleep(1000);
       System.out.println(stockName + "serchrd stock");
        WebElement dropdown = driver.findElement(dropdownlist) ;
       dropdown.click();
      System.out.println( "Stock info Page Title---->" +driver.getTitle());
      Thread.sleep(1000);
      String texthighvalue1 = driver.findElement(texthighvalue).getText();
      Assert.assertTrue(texthighvalue1.contentEquals("52 Week High"));
        String week52highValinfo = driver.findElement(week52highVal).getText();
        System.out.println(texthighvalue1 +" ---------------"+week52highValinfo);


      String week52lowValtext = driver.findElement(textlowvalue).getText();
     Assert.assertTrue(week52lowValtext.contentEquals("52 Week Low"));
        String week52lowValinfo = driver.findElement(week52lowVal).getText();
      System.out.println(week52lowValtext +" ----------------"+week52lowValinfo);
    }
}





