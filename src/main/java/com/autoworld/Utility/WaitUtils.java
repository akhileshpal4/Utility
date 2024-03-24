package com.autoworld.Utility;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {
    private WaitUtils(){
    }

    public static void waitForPageToLoad(WebDriver driver){
        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(60L));
        wait.until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
            }
        });
    }

    public static void sleep(long inMillis){
        try {
            Thread.sleep(inMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void sleep(WebDriver driver,String xPath){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xPath)));
    }
}
