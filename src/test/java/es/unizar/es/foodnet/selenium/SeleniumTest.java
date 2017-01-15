package es.unizar.es.foodnet.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SeleniumTest {

    private WebDriver browser;

    @Before
    public void setup() {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("windows")) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        } else if(os.contains("ios")) {
            System.setProperty("webdriver.chrome.driver", "chromedriverM");
        } else {
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }

        browser = new ChromeDriver();
    }

    @After
    public void tearDown() {
        browser.close();
    }

    @Test
    public void startTest() throws Exception {
        browser.get("http://foodnetunizar.herokuapp.com/");
        Thread.sleep(3000);

        //Registro
        browser.findElement(By.linkText("Regístrate/Accede")).click();
        browser.findElement(By.id("register-form-link")).click();
        Thread.sleep(3000);
        browser.findElement(By.id("email")).sendKeys("pruebaAceptacion@gmail.com");
        browser.findElement(By.id("nombre")).sendKeys("Prueba");
        browser.findElement(By.id("apellidos")).sendKeys("Aceptacion");
        browser.findElement(By.id("direccion")).sendKeys("Unizar");
        Thread.sleep(3000);
        browser.findElement(By.id("register-submit")).click();
        Thread.sleep(2000);
        browser.findElement(By.id("password")).sendKeys("1234");
        browser.findElement(By.id("register-submit")).click();
        Thread.sleep(2000);

        //Login
        browser.findElement(By.linkText("Regístrate/Accede")).click();
        browser.findElement(By.id("emailLogin")).sendKeys("pruebaAceptacio@gmail.com");
        browser.findElement(By.id("passwordLogin")).sendKeys("1234");
        browser.findElement(By.id("login-submit")).click();
        Thread.sleep(2000);
        browser.findElement(By.id("emailLogin")).sendKeys("pruebaAceptacion@gmail.com");
        browser.findElement(By.id("passwordLogin")).sendKeys("1234");
        browser.findElement(By.id("login-submit")).click();
        Thread.sleep(2000);

        //Eliminar usuario
        browser.findElement(By.cssSelector("strong")).click();
        browser.findElement(By.linkText("Modificar Datos")).click();
        Thread.sleep(2000);
        browser.findElement(By.id("eliminate-submit")).click();
        Thread.sleep(1000);
        browser.findElement(By.cssSelector("button.btn.btn-primary")).click();
        Thread.sleep(3000);
    }
}
