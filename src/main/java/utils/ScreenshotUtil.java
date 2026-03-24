package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private static final String SCREENSHOT_PATH = "screenshots/";

    // Method to capture and save screenshot
    public static void takeScreenshot(WebDriver driver, String testName) {
        // Create a timestamp to avoid duplicate filenames
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";

        try {
            // Convert WebDriver to TakesScreenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Create the screenshots directory if not exists
            File destDir = new File(SCREENSHOT_PATH);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            // Save the screenshot to the specified directory
            File destFile = new File(SCREENSHOT_PATH + fileName);
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("📸 Screenshot saved: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("❌ Failed to capture screenshot: " + e.getMessage());
        }
    }
}
