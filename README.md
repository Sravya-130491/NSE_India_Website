# NSE India Stock Website - Test Automation

Selenium-based test automation framework for the [NSE India](https://www.nseindia.com/) website using the **Page Object Model (POM)** design pattern. The project automates stock search and validation of 52-week high/low data on NSE India's equity market pages.

## Tech Stack

| Component           | Technology               |
|---------------------|--------------------------|
| Language            | Java 11+                 |
| Build Tool          | Apache Maven             |
| Test Framework      | TestNG 7.7.0             |
| Browser Automation  | Selenium WebDriver 4.27.0|
| Driver Management   | WebDriverManager 5.9.2   |
| Reporting           | ExtentReports 5.1.1      |
| Data Handling       | Apache POI 5.2.3         |
| Logging             | Log4j2 2.22.1            |

## Project Structure

```
NSE_India_Website/
├── pom.xml                                    # Maven build configuration
├── src/
│   ├── main/java/
│   │   ├── pages/
│   │   │   ├── HomePage.java                  # NSE homepage interactions
│   │   │   └── StockPage.java                 # Stock search & 52-week data validation
│   │   └── utils/
│   │       ├── ConfigReader.java              # Properties file reader
│   │       └── ScreenshotUtil.java            # Screenshot capture utility
│   └── test/
│       ├── java/base/
│       │   └── BaseTest.java                  # Test setup, execution & teardown
│       └── resources/
│           ├── config.properties              # Test configuration (URL, stock name)
│           └── testng.xml                     # TestNG suite definition
└── README.md
```

## Page Object Model

The project follows the POM design pattern to separate test logic from page interaction logic:

- **`HomePage`** - Represents the NSE India landing page. Handles navigation to NIFTY 50 market data with fallback to direct URL navigation when elements are blocked.
- **`StockPage`** - Represents the stock quote page. Handles searching for stocks via the search bar, selecting from dropdown results, and validating 52-week high/low values.
- **`BaseTest`** - Orchestrates the test lifecycle: browser setup (`@BeforeMethod`), test execution (`@Test`), and cleanup (`@AfterMethod`).

## Test Flow

1. Open NSE India homepage (`https://www.nseindia.com/`)
2. Click on NIFTY 50 market data link
3. Search for a stock (default: TataMotors) using the search bar
4. Select the stock from the dropdown results
5. Validate 52-week high and 52-week low data is displayed
6. Assert the labels and values are present and correct

## Prerequisites

- **Java 11** or higher (JDK)
- **Apache Maven 3.6+**
- **Google Chrome** or **Microsoft Edge** browser installed

> WebDriverManager automatically downloads the matching chromedriver/edgedriver binary at runtime -- no manual driver setup needed.

## Configuration

Edit `src/test/resources/config.properties` to change the target URL or stock:

```properties
url=https://www.nseindia.com/
stockName=TATAMOTORS
```

Browser selection is configured in `src/test/resources/testng.xml`:

```xml
<parameter name="browser" value="chrome"/>
```

Supported values: `chrome`, `edge`

## Running Tests

### Run all tests via Maven

```bash
mvn clean test
```

### Run with a specific browser

```bash
mvn clean test -Dbrowser=chrome
```

### Run only compile (skip tests)

```bash
mvn clean compile -DskipTests
```

### View test reports

After test execution, reports are available at:

```
target/surefire-reports/
```

## Headless Mode

The framework runs in **headless mode** by default, making it suitable for CI/CD pipelines and servers without a display. To run with a visible browser, remove the `--headless=new` argument from `BaseTest.java`.

## Key Features

- **Cross-browser support** - Chrome and Edge via parameterized TestNG tests
- **Headless execution** - Runs without a GUI for CI/CD compatibility
- **Anti-bot bypass** - Custom user-agent and disabled automation flags to reduce detection
- **Resilient selectors** - Multiple XPath fallbacks per element to handle dynamic page changes
- **Graceful degradation** - Direct URL navigation fallback when interactive elements are blocked
- **Screenshot capture** - `ScreenshotUtil` saves timestamped screenshots to `screenshots/`
- **Externalized config** - Test parameters loaded from `config.properties`
- **Automatic driver management** - WebDriverManager handles browser driver binaries

## Troubleshooting

| Issue | Solution |
|-------|----------|
| `NoSuchElementException` | NSE India may have changed their page structure. Update XPath selectors in `HomePage.java` or `StockPage.java`. |
| `Access Denied` on stock page | NSE India has anti-bot protections. The framework falls back to URL-based navigation automatically. |
| `WebDriverException: unknown error: cannot find Chrome binary` | Install Google Chrome: `sudo apt install google-chrome-stable` (Debian/Ubuntu) or `sudo dnf install google-chrome-stable` (RHEL/Fedora). |
| CDP version warning | This is a non-fatal warning when Chrome version doesn't match Selenium's bundled CDP. Tests still execute normally. |
