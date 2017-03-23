package services;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Ignore
public class SeleniumTest {
  WebDriver driver;

  @Before
  public void setUp() {
    System.setProperty("webdriver.gecko.driver", "/home/ebiz/Téléchargements/geckodriver-v0.15");
    driver = new FirefoxDriver();

    /*System.setProperty("webdriver.gecko.driver","/home/ebiz/Téléchargements/geckodriver-v0.15");
    DesiredCapabilities capabilities=DesiredCapabilities.firefox();
    capabilities.setCapability("marionette", true);
    driver = new FirefoxDriver(capabilities);*/
  }

  @After
  public void tearDown() {
    driver.close();
    driver.quit();
  }

  @Test
  public void test() throws Exception {
    driver.get("http://localhost:8080/dashboard");
    // Alternatively the same thing can be done like this
    // driver.navigate().to("http://www.google.com");

    // Find the text input element by its name
    WebElement element = driver.findElement(By.id("searchbox"));

    // Enter something to search for
    element.sendKeys("CM");

    // Now submit the form. WebDriver will find the form for us from the element
    element.submit();

    // Check the title of the page
    System.out.println("Page title is: " + driver.getTitle());

    (new WebDriverWait(driver, 10)).until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver d) {

        return d.getTitle().toLowerCase().startsWith("cheese!");
      }
    });

    WebElement results = driver.findElement(By.id("results"));

    System.out.println("Page title is: " + driver.getTitle());

    //assertEquals(0, 0);
    //fail();
  }

  public WebElement getWhenVisible(By locator, int timeout) {
    WebElement element = null;
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    //element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    return element;
  }
  public void clickWhenReady(By locator, int timeout) {
    WebDriverWait wait = new WebDriverWait(driver, timeout);
    //WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
    //element.click();
  }
}
