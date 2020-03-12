import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Nokia 7 Plus");
        capabilities.setCapability("platformVersion", "10");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("unicodeKeyboard", true);
        capabilities.setCapability("app", "/Users/melnik/Documents/GitHub/MobileAutoHomeTask03/apks/wikipedia.apk");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void checkTextInSearchField() {
        String textInSearchField = waitForElementAndGetText(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@class='android.widget.TextView']"),
                "Cannot find text in search field ",
                20);
        System.out.println(textInSearchField);
        Assert.assertEquals("We see unexpected text in search field", "Search Wikipedia", textInSearchField);
    }

    @Test
    public void checkSearchResultAfterClickCancelButton() {
        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_container']//*[@class='android.widget.TextView']"),
                "Cannot find search button",
                10);
        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot send text in search field",
                10);
        int countOfArticles = waitAndReturnListOfElements(
                By.id("org.wikipedia:id/page_list_item_title"),
                "Cannot find the articles",
                15).size();
        Assert.assertTrue("Count of articles less than 2", countOfArticles >= 2);
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X button",
                10);
        boolean resultAfterClickCancelButton = waitForElementNotPresent(
                By.id("org.wikipedia:id/page_list_item_title"),
                "Articles is still present on the page",
                5);
        Assert.assertTrue("Count of articles less than 2", resultAfterClickCancelButton);
    }

    private boolean waitForElementNotPresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private List<WebElement> waitAndReturnListOfElements(By by, String errorMessage, long timeoutInSeconds) {
        List<WebElement> listOfElements = waitForElementsPresent(by, errorMessage, timeoutInSeconds);
        return listOfElements;
    }

    private List<WebElement> waitForElementsPresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private WebElement waitForElementPresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(errorMessage + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementPresent(By by, String errorMessage) {
        return waitForElementPresent(by, errorMessage, 5);
    }

    private WebElement waitForElementAndClick(By by, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
        element.click();
        return element;
    }

    private String waitForElementAndGetText(By by, String errorMessage, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, errorMessage, timeoutInSeconds);
        return element.getAttribute("text");
    }

}