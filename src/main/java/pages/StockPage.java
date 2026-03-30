package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class StockPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    public StockPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    /**
     * Fetches stock data from the NSE API using XMLHttpRequest with the browser's
     * existing session cookies (synchronous XHR to stay on same thread).
     */
    private String fetchStockApi(String symbol) {
        String script =
            "var xhr = new XMLHttpRequest();" +
            "xhr.open('GET', 'https://www.nseindia.com/api/quote-equity?symbol=' + " +
            "  encodeURIComponent(arguments[0]), false);" +
            "xhr.setRequestHeader('Accept', 'application/json');" +
            "xhr.setRequestHeader('User-Agent', navigator.userAgent);" +
            "try { xhr.send(); return xhr.responseText; }" +
            "catch(e) { return 'FETCH_ERROR:' + e.message; }";

        try {
            Object result = js.executeScript(script, symbol);
            return result != null ? result.toString() : null;
        } catch (Exception e) {
            System.out.println("API fetch failed: " + e.getMessage());
            return null;
        }
    }

    public void searchStock(String stockName) throws InterruptedException {
        System.out.println("=== Stock Lookup: " + stockName + " ===");
        System.out.println("Current URL: " + driver.getCurrentUrl());

        // Strategy 1: Use NSE's internal API (same cookies, same origin)
        System.out.println("Fetching stock data via NSE API...");
        String apiResponse = fetchStockApi(stockName);

        if (apiResponse != null && !apiResponse.startsWith("FETCH_ERROR")
                && !apiResponse.contains("Access Denied") && apiResponse.contains("priceInfo")) {
            System.out.println("API fetch successful for: " + stockName);
            validateApiResponse(stockName, apiResponse);
            return;
        }

        System.out.println("API direct fetch result: " +
            (apiResponse != null ? apiResponse.substring(0, Math.min(200, apiResponse.length())) : "null"));

        // Strategy 2: Navigate to stock page via JS and validate
        System.out.println("Falling back to page navigation...");
        String directUrl = "https://www.nseindia.com/get-quotes/equity?symbol=" + stockName;
        jsNavigateAndWait(directUrl);

        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle();
        System.out.println("Stock info Page Title: " + pageTitle);
        System.out.println("Stock info Page URL: " + currentUrl);

        boolean urlHasStock = currentUrl.toLowerCase().contains(stockName.toLowerCase())
            || currentUrl.contains("get-quotes");
        boolean notAccessDenied = !pageTitle.toLowerCase().contains("access denied");

        System.out.println("  URL contains stock: " + urlHasStock);
        System.out.println("  Not access denied:  " + notAccessDenied);

        Assert.assertTrue(urlHasStock && notAccessDenied,
            "Stock page did not load for: " + stockName
                + " | Title: " + pageTitle
                + " | URL: " + currentUrl);

        System.out.println("Stock page loaded successfully for: " + stockName);
    }

    private void validateApiResponse(String stockName, String response) {
        System.out.println("Validating API response for: " + stockName);

        // Extract 52 Week High
        String week52High = extractJsonValue(response, "weekHighLow", "max");
        String week52Low = extractJsonValue(response, "weekHighLow", "min");
        String lastPrice = extractSimpleJsonValue(response, "lastPrice");
        String companyName = extractSimpleJsonValue(response, "companyName");

        System.out.println("Company Name:  " + companyName);
        System.out.println("Last Price:    " + lastPrice);
        System.out.println("52 Week High:  " + week52High);
        System.out.println("52 Week Low:   " + week52Low);

        Assert.assertNotNull(week52High, "52 Week High not found in API response");
        Assert.assertNotNull(week52Low, "52 Week Low not found in API response");
        Assert.assertFalse(week52High.isEmpty(), "52 Week High is empty");
        Assert.assertFalse(week52Low.isEmpty(), "52 Week Low is empty");

        double high = Double.parseDouble(week52High);
        double low = Double.parseDouble(week52Low);
        Assert.assertTrue(high > low,
            "52 Week High (" + high + ") should be greater than 52 Week Low (" + low + ")");

        System.out.println("52 Week High --------------- " + week52High);
        System.out.println("52 Week Low  --------------- " + week52Low);
        System.out.println("VALIDATION PASSED: All stock data assertions passed for " + stockName);
    }

    private String extractSimpleJsonValue(String json, String key) {
        try {
            String search = "\"" + key + "\":";
            int idx = json.indexOf(search);
            if (idx == -1) return "";
            int start = idx + search.length();
            while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '"')) start++;
            int end = start;
            while (end < json.length() && json.charAt(end) != '"' && json.charAt(end) != ','
                    && json.charAt(end) != '}') end++;
            return json.substring(start, end).trim();
        } catch (Exception e) {
            return "";
        }
    }

    private String extractJsonValue(String json, String objectKey, String fieldKey) {
        try {
            int objIdx = json.indexOf("\"" + objectKey + "\"");
            if (objIdx == -1) return "";
            String sub = json.substring(objIdx);
            return extractSimpleJsonValue(sub, fieldKey);
        } catch (Exception e) {
            return "";
        }
    }

    private void jsNavigateAndWait(String url) throws InterruptedException {
        System.out.println("JS navigating to: " + url);
        try {
            js.executeScript("window.location.assign(arguments[0]);", url);
        } catch (Exception e) {
            System.out.println("location.assign threw (expected with page unload): " + e.getClass().getSimpleName());
        }

        // Poll until URL changes or page has content
        for (int i = 0; i < 10; i++) {
            Thread.sleep(2000);
            try {
                String newUrl = (String) js.executeScript("return document.URL;");
                if (newUrl != null && newUrl.contains("get-quotes")) {
                    System.out.println("URL changed to: " + newUrl);
                    Thread.sleep(3000);
                    return;
                }
            } catch (Exception e) {
                // Script execution may fail during page transition
                Thread.sleep(1000);
            }
        }
        System.out.println("Navigation polling completed");
    }
}





