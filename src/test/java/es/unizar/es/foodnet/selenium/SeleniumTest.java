package es.unizar.es.foodnet.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SeleniumTest {

    private WebDriver browser;

    @Before
    public void setup() {
        browser = new ChromeDriver();
    }

    @After
    public void tearDown() {
        browser.close();
    }

    @Test
    public void startTest() throws Exception {
        browser.get("http://foodnetunizar.herokuapp.com/");
        Thread.sleep(10000);
    }
}
