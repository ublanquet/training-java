package services;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SeleniumTests {
  WebDriver driver;

  @Before
  public void setUp() {
    System.setProperty("webdriver.gecko.driver", "/home/ebiz/Téléchargements/geckodriver-v0.14");
    //driver = new FirefoxDriver();
  }

  @After
  public void tearDown() {
  }

  @Test
  public void test() throws Exception {
    // Create a new instance of the Firefox driver
    // Notice that the remainder of the code relies on the interface,
    // not the implementation.


    // And now use this to visit Google
    driver.get("http://www.google.com");
    // Alternatively the same thing can be done like this
    // driver.navigate().to("http://www.google.com");

    // Find the text input element by its name
    WebElement element = driver.findElement(By.name("q"));

    // Enter something to search for
    element.sendKeys("Cheese!");

    // Now submit the form. WebDriver will find the form for us from the element
    element.submit();

    // Check the title of the page
    System.out.println("Page title is: " + driver.getTitle());

    // Google's search is rendered dynamically with JavaScript.
    // Wait for the page to load, timeout after 10 seconds
    /*(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        return d.getTitle().toLowerCase().startsWith("cheese!");
      }
    });*/

    (new WebDriverWait(driver, 10)).until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver d) {
        return d.getTitle().toLowerCase().startsWith("cheese!");
      }
    });
    //(new WebDriverWait(driver, 10)).until(d -> d.getTitle().toLowerCase().startsWith("cheese!"));

    // Should see: "cheese! - Google Search"
    System.out.println("Page title is: " + driver.getTitle());

    //Close the browser
    driver.quit();

    assertEquals(0, 0);
    fail();
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
