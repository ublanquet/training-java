package services;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


import static org.junit.Assert.assertEquals;

@Ignore
public class SeleniumTest {
  WebDriver driver;

  @Before
  public void setUp() {
    System.setProperty("webdriver.gecko.driver", "/home/ebiz/Téléchargements/geckodriver-v0.15");
    driver = new FirefoxDriver();
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void testDashboard() throws Exception {

    driver.get("http://localhost:8080/dashboard");
    // Alternatively the same thing can be done like this
    // driver.navigate().to("http://www.google.com");

    // Find the text input element by its name
    WebElement searchBox = driver.findElement(By.id("searchbox"));
    WebElement searchSubmit = driver.findElement(By.id("searchsubmit"));
    WebElement computerNb = driver.findElement(By.id("homeTitle"));
    WebElement results = driver.findElement(By.id("results"));
    WebElement result = results.findElement(By.tagName("a"));

    assertEquals("551 Computers found", computerNb.getText());
    assertEquals("MacBook Pro 15.4 inch", result.getText());
    System.out.println("Page title is: " + driver.getTitle());
    System.out.println("Page computer nb is: " + computerNb.getText());
    System.out.println("Page first result is: " + result.getText());

    // Enter something to search for
    searchBox.sendKeys("CM");
    searchSubmit.click();
    // Now submit the form. WebDriver will find the form for us from the element
    //searchBox.submit();
    //System.out.println("Page first result is: " + result.getText());

    computerNb = driver.findElement(By.id("homeTitle"));
    results = driver.findElement(By.id("results"));
    result = results.findElement(By.tagName("a"));

    System.out.println("Page first result is: " + result.getText());
    System.out.println("Page computer nb is: " + computerNb.getText());
    assertEquals("8 Computers found", computerNb.getText());
    assertEquals("CM-2a", result.getText());

    // Check the title of the page
/*
    (new WebDriverWait(driver, 10)).until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver d) {
        return (computerNb.getText().length() > 12);
      }
    });
*/
  }
}
