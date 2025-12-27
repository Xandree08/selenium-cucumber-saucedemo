package com.saucedemo.test.utils.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    
    public static WebDriver driver;

    /*  Definindo o driver globalmente, se o driver for chamado pelo comando getDriver ele vai verificar se o driver é nulo
    se caso for nulo, ele aplicará as configurações iniciais evitando abrir varios navegadores ao mesmo tempo, o webdriver fará
    configuração das dependencias do navegador e o start-maximized abrirá o navegador em tela cheia quando o teste for executado.
    */

    public static WebDriver getDriver() {
        if(driver == null){
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");

            driver = new ChromeDriver(options);
        }

        return driver;
    }    

    /* Quando desejar sair do driver ele vai verificar se o driver esta configurado ou inciado e se caso estiver ele vai
    sair e deixar as configurações como nulas análogo á uma limpeza. */

    public static void quitDriver (){
        if(driver != null){
            driver.quit();
            driver = null;
        }
    }
}
