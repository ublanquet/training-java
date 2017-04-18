package services;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

@Ignore
public class SeleniumTest {
  WebDriver driver;
  protected ComputerService service;

  @Before
  public void setUp() {
    System.setProperty("webdriver.gecko.driver", "/home/ebiz/Téléchargements/geckodriver-v0.15");
    driver = new FirefoxDriver();
    //service = service;//.getInstance();
    driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
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
    // Find the elements
    WebElement searchBox = driver.findElement(By.id("searchbox"));
    WebElement searchSubmit = driver.findElement(By.id("searchsubmit"));
    WebElement computerNb = driver.findElement(By.id("homeTitle"));
    WebElement results = driver.findElement(By.id("results")); //get dashboard tab
    WebElement result = results.findElement(By.tagName("a")); //get first result text

    long count = service.getCount();
    assertEquals(count + " Computers found", computerNb.getText());
    assertEquals("MacBook Pro 15.4 inch", result.getText());
    System.out.println("Page title is: " + driver.getTitle());
    System.out.println("Page computer nb is: " + computerNb.getText());
    System.out.println("Page first result is: " + result.getText());

    //check elements/page

    List<WebElement> perPage = driver.findElements(By.className("nbEntries"));
    perPage.get(2).click();
    WebElement firstPage = driver.findElement(By.id("p0"));
    firstPage.findElement(By.tagName("a")).click();
    //GOTTA WAIT
    //driver.wait(1);
    (new WebDriverWait(driver, 10)).until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver d) {
        return (d.findElements(By.tagName("tr")).size() > 11);
      }
    });
    int pageSize = driver.findElements(By.tagName("tr")).size();
    System.out.println("Items per page: " + pageSize);
    assertEquals(101, pageSize);

    // Enter something to search for
    searchBox.sendKeys("CM");
    searchSubmit.click();
    // Now submit the form. WebDriver will find the form for us from the element
    //searchBox.submit();
    (new WebDriverWait(driver, 10)).until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver d) {
        return (d.findElement(By.id("filteredCount")) != null);
      }
    });

    //refresh our selectors after the search (js modify a lot of them)
    computerNb = driver.findElement(By.id("homeTitle"));
    results = driver.findElement(By.id("results"));
    result = results.findElement(By.tagName("a"));

    System.out.println("Page first result is: " + result.getText());
    System.out.println("Page computer nb is: " + computerNb.getText());
    assertEquals("8 Computers found", computerNb.getText());
    assertEquals("CM-2a", result.getText());
  }

  @Test
  public void testCreate () {
    long firstCount = service.getCount("test-selenium");
    driver.get("http://localhost:8080/addcomputer");

    // Find the elements
    WebElement computerName = driver.findElement(By.id("computerName"));
    WebElement introduced = driver.findElement(By.id("introduced"));
    WebElement discontinued = driver.findElement(By.id("discontinued"));
    Select companyId = new Select(driver.findElement(By.id("companyId")));

    computerName.sendKeys("test-selenium");
    introduced.sendKeys("11/11/2000");
    companyId.selectByVisibleText("RCA");
    computerName.submit();
    driver.get("http://localhost:8080/dashboard");
    long secondCount = service.getCount("test-selenium");
    assertEquals(firstCount + 1, secondCount);

    (new WebDriverWait(driver, 10)).until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver d) {
        return (d.findElement(By.id("searchbox")) != null);
      }
    });

    WebElement searchBox = driver.findElement(By.id("searchbox"));
    WebElement searchSubmit = driver.findElement(By.id("searchsubmit"));

    searchBox.sendKeys("test-selenium");
    searchSubmit.click();
    (new WebDriverWait(driver, 10)).until(new Function<WebDriver, Boolean>() {
      public Boolean apply(WebDriver d) {
        return (d.findElement(By.id("filteredCount")) != null);
      }
    });
    WebElement results = driver.findElement(By.id("results")); //get dashboard tab
    WebElement result = results.findElement(By.tagName("a")); //get first result text
    WebElement firstRow = results.findElement(By.tagName("tr"));
    List<WebElement> rowCells = firstRow.findElements(By.tagName("td"));

    assertEquals("test-selenium", result.getText());
    assertEquals("11/11/2000", rowCells.get(2).getText());
    assertEquals("", rowCells.get(3).getText());
    assertEquals("RCA", rowCells.get(4).getText());

    result.click();

    computerName = driver.findElement(By.id("computerName"));
    introduced = driver.findElement(By.id("introduced"));
    discontinued = driver.findElement(By.id("discontinued"));
    companyId = new Select(driver.findElement(By.id("companyId")));
    computerName.sendKeys("test-selenium-mod");
    introduced.sendKeys("11/11/2011");
    companyId.selectByVisibleText("Netronics");
    computerName.submit();

    driver.get("http://localhost:8080/dashboard");
    searchBox = driver.findElement(By.id("searchbox"));
    searchSubmit = driver.findElement(By.id("searchsubmit"));

    searchBox.sendKeys("test-selenium-mod");
    searchSubmit.click();
    results = driver.findElement(By.id("results")); //get dashboard tab
    result = results.findElement(By.tagName("a")); //get first result text
    firstRow = results.findElement(By.tagName("tr"));
    rowCells = firstRow.findElements(By.tagName("td"));

    assertEquals("test-selenium-mod", result.getText());
    assertEquals("11/11/2011", rowCells.get(2).getText());
    assertEquals("", rowCells.get(3).getText());
    assertEquals("Netronics", rowCells.get(4).getText());

    WebElement editButton = driver.findElement(By.id("editComputer"));
    editButton.click();
    WebElement checkbox = rowCells.get(0).findElement(By.name("cb"));
    checkbox.click();
    WebElement deleteButton = driver.findElement(By.id("deleteSelected"));
    deleteButton.click();
    driver.switchTo().alert().accept();
    driver.get("http://localhost:8080/dashboard");
    secondCount = service.getCount("test-selenium");
    assertEquals(firstCount, secondCount);
  }
}
