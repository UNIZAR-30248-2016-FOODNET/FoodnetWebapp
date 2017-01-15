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
        } else {
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }

        browser = new ChromeDriver();
    }

    @Test
    public void startTestRegistro() throws Exception {
        browser.get("http://foodnetunizar.herokuapp.com/");
        Thread.sleep(1000);

        //Registro
        browser.findElement(By.linkText("Regístrate/Accede")).click();
        browser.findElement(By.id("register-form-link")).click();
        Thread.sleep(1000);
        browser.findElement(By.id("email")).sendKeys("pruebaAceptacion@gmail.com");
        browser.findElement(By.id("nombre")).sendKeys("Prueba");
        browser.findElement(By.id("apellidos")).sendKeys("Aceptacion");
        browser.findElement(By.id("direccion")).sendKeys("Unizar");
        browser.findElement(By.id("register-submit")).click();
        Thread.sleep(1000);
        browser.findElement(By.id("password")).sendKeys("1234");
        browser.findElement(By.id("register-submit")).click();
        Thread.sleep(2000);

        //Login
        browser.findElement(By.linkText("Regístrate/Accede")).click();
        Thread.sleep(1000);
        browser.findElement(By.id("emailLogin")).sendKeys("pruebaAceptacio@gmail.com");
        browser.findElement(By.id("passwordLogin")).sendKeys("1234");
        browser.findElement(By.id("login-submit")).click();
        Thread.sleep(2000);
        browser.findElement(By.id("emailLogin")).sendKeys("pruebaAceptacion@gmail.com");
        browser.findElement(By.id("passwordLogin")).sendKeys("1234");
        browser.findElement(By.id("login-submit")).click();
        Thread.sleep(1000);

        //Eliminar usuario
        browser.findElement(By.cssSelector("strong")).click();
        Thread.sleep(1000);
        browser.findElement(By.linkText("Modificar Datos")).click();
        Thread.sleep(2000);
        browser.findElement(By.id("eliminate-submit")).click();
        Thread.sleep(1000);
        browser.findElement(By.cssSelector("button.btn.btn-primary")).click();
        Thread.sleep(1500);
    }

    @Test
    public void startTestPedido() throws Exception {
        browser.get("http://foodnetunizar.herokuapp.com/");
        Thread.sleep(1000);

        //Registro
        browser.findElement(By.linkText("Regístrate/Accede")).click();
        browser.findElement(By.id("register-form-link")).click();
        Thread.sleep(1000);
        browser.findElement(By.id("email")).sendKeys("pruebaAceptacion@gmail.com");
        browser.findElement(By.id("nombre")).sendKeys("Prueba");
        browser.findElement(By.id("apellidos")).sendKeys("Aceptacion");
        browser.findElement(By.id("direccion")).sendKeys("Unizar");
        browser.findElement(By.id("password")).sendKeys("1234");
        browser.findElement(By.id("register-submit")).click();
        Thread.sleep(1000);

        //Login
        browser.findElement(By.linkText("Regístrate/Accede")).click();
        Thread.sleep(1000);
        browser.findElement(By.id("emailLogin")).sendKeys("pruebaAceptacion@gmail.com");
        browser.findElement(By.id("passwordLogin")).sendKeys("1234");
        browser.findElement(By.id("login-submit")).click();
        Thread.sleep(1000);

        //Añadir producto a carro
        browser.findElement(By.xpath("//a[@onclick=\"javascript:aumentar('Filete de ternera,6.0,http://placehold.it/650x450&text=Filete de ternera,587671911cd1971ca02b9649');\"]")).click();
        browser.findElement(By.xpath("//div[@id='bs-example-navbar-collapse-1']/ul[2]/li[4]/a")).click();
        Thread.sleep(1000);
        browser.findElement(By.linkText("Ver carro")).click();
        Thread.sleep(1500);

        //Finalizar pago
        browser.findElement(By.linkText("Finalizar pago")).click();
        Thread.sleep(1000);
        browser.findElement(By.xpath("//input[@value='Pagar']")).click();
        Thread.sleep(1500);

        //Ver historial y cancelar pedido
        browser.findElement(By.linkText("Historial de pedidos")).click();
        Thread.sleep(1000);
        browser.findElement(By.cssSelector("h4.panel-title > a")).click();
        Thread.sleep(2000);
        browser.findElement(By.cssSelector("form > button.btn.btn-danger")).click();
        Thread.sleep(2000);

        //Eliminar usuario
        browser.findElement(By.cssSelector("strong")).click();
        Thread.sleep(1000);
        browser.findElement(By.linkText("Modificar Datos")).click();
        Thread.sleep(1000);
        browser.findElement(By.id("eliminate-submit")).click();
        Thread.sleep(1000);
        browser.findElement(By.cssSelector("button.btn.btn-primary")).click();
        Thread.sleep(1000);
    }

    @After
    public void tearDown() {
        browser.close();
    }
}
