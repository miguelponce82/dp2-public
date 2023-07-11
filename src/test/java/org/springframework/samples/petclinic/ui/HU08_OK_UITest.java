package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HU08_OK_UITest {
	
	@LocalServerPort
	private int port;
	
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeEach
  public void setUp() throws Exception {
	String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
	System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);
    driver = new FirefoxDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testHU08OKUI() throws Exception {
	  driver.get("http://localhost:"+port);
	  driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	  driver.findElement(By.id("username")).clear();
	  driver.findElement(By.id("username")).sendKeys("vet1");
	  driver.findElement(By.id("password")).click();
	  driver.findElement(By.id("password")).clear();
	  driver.findElement(By.id("password")).sendKeys("v3t");
	  driver.findElement(By.xpath("//button[@type='submit']")).click();
	  driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
	  driver.findElement(By.xpath("//table[@id='vetsTable']/tbody/tr[4]/td[4]/a")).click();
	  driver.findElement(By.linkText("Add Treatment")).click();
	  driver.findElement(By.id("recomendations")).clear();
	  driver.findElement(By.id("recomendations")).sendKeys("hola");
	  driver.findElement(By.id("startDate")).click();
	  driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
	  driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
	  driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
	  driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
	  driver.findElement(By.linkText("18")).click();
	  driver.findElement(By.id("endDate")).click();
	  driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
	  driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
	  driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
	  driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
	  driver.findElement(By.linkText("29")).click();
	  new Select(driver.findElement(By.id("medicine"))).selectByVisibleText("ENACARD 28 Comprimidos");
	  driver.findElement(By.xpath("//option[@value='ENACARD 28 Comprimidos']")).click();
	  driver.findElement(By.xpath("//button[@type='submit']")).click();
	  
	  try {
          assertEquals("hola", driver.findElement(By.xpath("//table[@id='diagnosisTable']/tbody/tr[3]/td[4]")).getText());
        } catch (Error e) {
          verificationErrors.append(e.toString());
        }
	  
  }

  @AfterEach
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
