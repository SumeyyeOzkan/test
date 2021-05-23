/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sumeyye.testt;

import java.io.File;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import jdk.jfr.Timespan;
import org.junit.Assert;
import org.junit.Test;
// import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author sumey
 */
public class Main {

    @Test
    public static void main(String[] args) throws InterruptedException {
        //Setup();
        String email = "email@giriniz.com";
        String password = "şifregiriniz";
        String username = "username_giriniz";
        //google tarayıcımızı ekledik
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        
        driver.manage().window().maximize();

        //www.gittigidiyor sayfası açılır
        driver.get("https://www.gittigidiyor.com/");

        //zamanlayıcı
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //Ana sayfanın açılıp açılmadığı kontrol edilir
        if (driver.getCurrentUrl().contains("https://www.gittigidiyor.com/")) {
            System.out.println("Gittigidiyor sayfasina giris yapildi");
            System.out.println("-----------------------------");
        } else {
            throw new WebDriverException("Gittigidiyor sayfasi acilamadi");
        }

        // Bağlantıya gidilir
        driver.navigate().to("https://www.gittigidiyor.com/uye-girisi");
        // Siteye login olunur
        driver.findElement(By.xpath("//*[@id=\'L-UserNameField\']")).sendKeys(email);
        driver.findElement(By.xpath("//*[@id=\'L-PasswordField\']")).sendKeys(password); 
        System.out.println("bekleme : ");
        Thread.sleep(5000);
        driver.findElement(By.xpath("//input[@id='gg-login-enter']")).sendKeys(Keys.ENTER);
        System.out.println("bekleme bitiş");
        

        //    Giriş sayfası kontrolü
        WebElement userCheck = driver.findElement(By.xpath("//span[contains(text(),'"+username+"')]")); 
        if (userCheck.getText().contains(username)) { 
            System.out.println("Login islemi basariyla gerceklesti");
            System.out.println("-----------------------------");

            //Arama kutucuğuna bilgisayar kelimesi girilir
            driver.findElement(
                    By.xpath("//*[@id=\"main-header\"]/div[3]/div/div/div/div[2]/form/div/div[1]/div[2]/input"))
                    .sendKeys("bilgisayar");
            driver.findElement(
                    By.xpath("//*[@id=\"main-header\"]/div[3]/div/div/div/div[2]/form/div/div[2]/button/span")).click();

            //Arama sonuçları sayfasından ikinci sayfa açılır
            ((JavascriptExecutor) driver).executeScript("scroll(0,9500)");
            driver.findElement(By.xpath("//*[@id=\"best-match-right\"]/div[5]/ul/li[7]/a")).click();

            //2. sayfanın açıldığı kontrol edilir
            if (driver.getCurrentUrl().contains("sf=2")) {
                System.out.println("2. sayfaya gecis yaptiniz");
                System.out.println("-----------------------------");

            } else {
                System.out.println("2. sayfaya gidemediniz");
            }

            //Sonuca göre sergilenen rastgele 2 ürün sepete eklenir
            ((JavascriptExecutor) driver).executeScript("scroll(0,400)");
            Thread.sleep(1000);
            driver.findElement(By.cssSelector(
                    "body.desktop:nth-child(2) div.gray-content:nth-child(5) div.container:nth-child(8) div.clearfix div.ggfound-x-times.padding-none.mrb10.gg-uw-19.gg-w-18.gg-d-18.gg-t-24.gg-m-24.padding-none div.blueWrapper.clearfix:nth-child(3) div.clearfix:nth-child(4) ul.catalog-view.clearfix.products-container:nth-child(1) li.gg-uw-6.gg-w-8.gg-d-8.gg-t-8.gg-m-24.gg-mw-12.catalog-seem-cell.srp-item-list.browser:nth-child(2) a.product-link div.cell-border-css div.product-info-con.clearfix div.gg-w-24.gg-d-24.gg-t-24.gg-m-24.pl0.pr0.product-info-details:nth-child(1) div.gg-w-24.gg-d-24.gg-t-24.gg-m-24.product-title-info:nth-child(1) h3.product-title > span:nth-child(1)"))
                    //".product-link nth-child(1)"))
                    .click();
            
            WebElement productPriceInDetails = driver.findElement(By.xpath("/html//div[@id='sp-price-lowPrice']"));
            String listPrice = productPriceInDetails.getText();

            //Seçilen ürün sepete eklenir
            ((JavascriptExecutor) driver).executeScript("scroll(0,400)");
            driver.findElement(By.xpath("/html//button[@id='add-to-basket']")).click();

            //sepetim sayfasına gidilir
            driver.findElement(By.xpath("/html//div[@id='header_wrapper']/div/div/div//div[@class='gg-d-12 pl0']/a[@href='https://www.gittigidiyor.com/sepetim']")).click();
             driver.navigate().to("https://www.gittigidiyor.com/sepetim");

            //Ürün sayfasındaki fiyat ile sepette  yer alan ürün fiyatının doğruluğu kontrol edilir
            WebElement productPriceInBasket = driver.findElement(By.xpath(".//*[@id='cart-price-container']/div[3]/p"));
            String basketPrice = productPriceInBasket.getText();
            Assert.assertEquals(listPrice, basketPrice);
            System.out.println("Urunun listelenen fiyati ile sepetteki fiyati esittir ve " + basketPrice + "'dir.");
            System.out.println("-----------------------------");

            //Adet arttırılarak 2 yapılır
            WebElement productQuantity = driver.findElement(By.xpath(
                    "//body/div[@id='main-content']/div[2]/div[1]/div[1]/form[1]/div[1]/div[2]/div[2]/div[1]/div[2]/div[6]/div[2]/div[2]/div[4]/div[1]/div[2]/select[1]"));
            productQuantity.click();
            productQuantity.sendKeys("2");
            productQuantity.sendKeys(Keys.ENTER);
            Thread.sleep(1000);


            //ürün adetinin 2 olduğu doğrulanır
            WebElement productCount = driver.findElement(By.xpath(
                    "//*[@id=\'submit-cart\']/div/div[2]/div[3]/div/div[1]/div/div[5]/div[1]/div/ul/li[1]/div[1]"));
            System.out.println(productCount.getText());
            System.out.println("-----------------------------");

            if (productCount.getText().contains("2 Adet")) {
                System.out.println("Sepetteki urun adedi 2'dir.");
                System.out.println("-----------------------------");

            } else {
                System.out.println("Sepetteki urun adedi 2 degildir.");
            }

            //Ürün sepeti silinerek sepetin boş olduğu kontrol edilir
            driver.findElement(By.xpath(
                    "/html//div[@id='cart-items-container']/div[@class='products-container']/div/div//div[@class='row']/a[@title='Sil']/i[@class='gg-icon gg-icon-bin-medium']"))
                    .click();

            //Taracı kapatılır
            driver.quit();

        }
        else{
            System.out.println("Login işlemi başarısız");
        }
    }
}
