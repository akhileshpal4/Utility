package com.autoworld.Utility;

import com.autoworld.ConfigProvider.ConfigProvider;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import org.testng.log4testng.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import java.security.spec.KeySpec;
import java.time.Duration;
import java.util.List;

public class BasePageObject {
    public static final String DESEDE_ENCRYPTION_SCHEME="DESede";
    private static final Logger logger=Logger.getLogger(BasePageObject.class);
    private static final int DEFAULT_IMPLICIT_WAIT=0;
    private static final String SET_INPUT="Set input: ";
    private static final String CLICK_ACTIOIN="Click Action";
    private static final String DROPDOWN="Select value from dropdown: ";
    private static final String SET_INPUT_COMMAND="arguments[0].value='%s';";
    private static final String CLICK_COMMAND="arguments[0].click();";
    private static final String JS_DISPLAY_COMMAND="arguments[0].style.display='block';";
    private static final String CLICK="Click: ";
    private static final String UNICODE_FORMAT="UTF8";
    private static String winID="";
    protected final JavascriptExecutor javascriptExecutor;
    private final Duration pollingInterval=Duration.ofMillis(ConfigProvider.getAsInt("POLLING_INTERVAL"));
    private final Duration fluentWaitDuration=Duration.ofSeconds(ConfigProvider.getAsInt("FLUENT_WAIT"));
    byte[] arrayByte;
    Actions action;
    Alert alert;
    private FluentWait<WebDriver> fluentWait;
    protected WebDriver driver;
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    private String myEncryptionKey;
    private String myEncryptionScheme;

    public BasePageObject(WebDriver driver){
        this.driver=driver;
        this.fluentWait=(new FluentWait(this.driver)).withTimeout(this.fluentWaitDuration).pollingEvery(pollingInterval).ignoring(StaleElementReferenceException.class).ignoring(ElementNotInteractableException.class).ignoring(NoSuchElementException.class);
        this.javascriptExecutor=(JavascriptExecutor) this.driver;
        PageFactory.initElements(this.driver,this);
        new AssertionLibrary(driver);
        this.action=new Actions(this.driver);
    }
    public BasePageObject() {
        this.javascriptExecutor = null;
    }
    public void get(String url){
        this.driver.get(url);
        Screenshots.addStepWithScreenshotInReport(this.driver,"Application launch: <a href=\""+url+"\">"+url+"</a>");
    }
    public void navigateTo(String url){
        this.driver.navigate().to(url);
        Screenshots.addStepWithScreenshotInReport(this.driver,"Application launch: <a href=\""+url+"\">"+url+"</a>");
    }

    protected void setImplicitWait(int duration){
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(duration));
    }

    protected WebElement getElement(final String locator){
        return fluentWait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.xpath(locator));
            }
        });
    }
    protected WebElement getElement(final By by){
        return this.fluentWait.until(new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(by);
            }
        });
    }

    protected List<WebElement> getElements(final String locator){
        return this.fluentWait.until(new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(WebDriver driver) {
                return driver.findElements(By.xpath(locator));
            }
        });
    }
    protected List<WebElement> getElements(final By by){
        return this.fluentWait.until(new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(WebDriver driver) {
                return driver.findElements(by);
            }
        });
    }

    protected boolean isElementOnPage(String locator){
        this.setImplicitWait(0);
        boolean flag=!this.getElements(locator).isEmpty();
        this.setImplicitWait(ConfigProvider.getAsInt("IMPLICIT_WAIT"));
        return flag;
    }

    protected boolean isElementOnPage(By by){
        this.setImplicitWait(0);
        boolean flag=!this.getElements(by).isEmpty();
        this.setImplicitWait(ConfigProvider.getAsInt("IMPLICIT_WAIT"));
        return flag;
    }

    protected boolean isEnabled(String locator){
        List<WebElement> elementList=this.getElements(locator);
        return !elementList.isEmpty()?elementList.get(0).isEnabled():false;
    }

    protected boolean isEnabled(By by){
        List<WebElement> elementList=this.getElements(by);
        return !elementList.isEmpty()?elementList.get(0).isEnabled():false;
    }

    protected boolean isDisplayed(String locator){
        List<WebElement> elementList=this.getElements(locator);
        return !elementList.isEmpty()?elementList.get(0).isDisplayed():false;
    }

    protected boolean isDisplayed(By by){
        List<WebElement> elementList=this.getElements(by);
        return !elementList.isEmpty()?elementList.get(0).isDisplayed():false;
    }

    protected boolean isSelected(String locator){
        List<WebElement> elementList=this.getElements(locator);
        return !elementList.isEmpty()?elementList.get(0).isSelected():false;
    }

    protected boolean isSelected(By by){
        List<WebElement> elementList=this.getElements(by);
        return !elementList.isEmpty()?elementList.get(0).isDisplayed():false;
    }

    protected int getElementSize(String locator){
        return this.isElementOnPage(locator)?this.getElements(locator).size():0;
    }

    protected int getElementSize(By by){
        return this.isElementOnPage(by)?this.getElements(by).size():0;
    }

    protected void setInputValue(String locator,String value,boolean clearInput){
        WebElement element=this.getElement(locator);
        if(clearInput){
            element.clear();
        }
        element.sendKeys(new CharSequence[]{value});
        Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
    }

    protected void setInputValue(String locator, String value, boolean clearInput, AssertionLibrary.Screenshot screenshot){
        WebElement element=this.getElement(locator);
        if(clearInput){
            element.clear();
        }
        element.sendKeys(new CharSequence[]{value});
        if(screenshot.equals(AssertionLibrary.Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,"Set Input: "+value);
        }else{
            Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
        }
    }

    protected void setInputValue(By by,String value,boolean clearInput){
        WebElement element=this.getElement(by);
        if(clearInput){
            element.clear();
        }
        element.sendKeys(new CharSequence[]{value});
        Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
    }

    protected void setInputValue(By by, String value, boolean clearInput, AssertionLibrary.Screenshot screenshot){
        WebElement element=this.getElement(by);
        if(clearInput){
            element.clear();
        }
        element.sendKeys(new CharSequence[]{value});
        if(screenshot.equals(AssertionLibrary.Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,"Set Input: "+value);
        }else{
            Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
        }
    }

    protected void setInputValue(String locator,String value){
        WebElement element=this.getElement(locator);
        element.clear();
        element.sendKeys(new CharSequence[]{value});
        Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
    }

    protected void setInputValue(String locator, String value, AssertionLibrary.Screenshot screenshot){
        WebElement element=this.getElement(locator);
        element.clear();
        element.sendKeys(new CharSequence[]{value});
        if(screenshot.equals(AssertionLibrary.Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,"Set Input: "+value);
        }else{
            Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
        }
    }

    protected void setInputValue(By by,String value){
        WebElement element=this.getElement(by);
        element.clear();
        element.sendKeys(new CharSequence[]{value});
        Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
    }

    protected void setInputValue(By by, String value, AssertionLibrary.Screenshot screenshot){
        WebElement element=this.getElement(by);
        element.clear();
        element.sendKeys(new CharSequence[]{value});
        if(screenshot.equals(AssertionLibrary.Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,"Set Input: "+value);
        }else{
            Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
        }
    }


    protected void setInputValueJS(String locator,String value,boolean clearInput){
        WebElement element=this.getElement(locator);
        if(clearInput){
            element.clear();
        }
       this.javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND,value),element);
        Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
    }

    protected void setInputValueJS(String locator, String value, boolean clearInput, AssertionLibrary.Screenshot screenshot){
        WebElement element=this.getElement(locator);
        if(clearInput){
            element.clear();
        }
        this.javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND,value),element);
        if(screenshot.equals(AssertionLibrary.Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,"Set Input: "+value);
        }else{
            Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
        }
    }

    protected void setInputValueJS(By by,String value,boolean clearInput){
        WebElement element=this.getElement(by);
        if(clearInput){
            element.clear();
        }
        this.javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND,value),element);
        Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
    }

    protected void setInputValueJS(By by, String value, boolean clearInput, AssertionLibrary.Screenshot screenshot){
        WebElement element=this.getElement(by);
        if(clearInput){
            element.clear();
        }
        this.javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND,value),element);
        if(screenshot.equals(AssertionLibrary.Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,"Set Input: "+value);
        }else{
            Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
        }
    }


    protected void setInputValueJS(String locator,String value){
        WebElement element=this.getElement(locator);
        element.clear();
        this.javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND,value),element);
        Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
    }

    protected void setInputValueJS(String locator, String value, AssertionLibrary.Screenshot screenshot){
        WebElement element=this.getElement(locator);
        element.clear();
        this.javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND,value),element);
        if(screenshot.equals(AssertionLibrary.Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,"Set Input: "+value);
        }else{
            Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
        }
    }

    protected void setInputValueJS(By by,String value){
        WebElement element=this.getElement(by);
        element.clear();
        this.javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND,value),element);
        Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
    }

    protected void setInputValueJS(By by, String value, AssertionLibrary.Screenshot screenshot){
        WebElement element=this.getElement(by);
        element.clear();
        this.javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND,value),element);
        if(screenshot.equals(AssertionLibrary.Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,"Set Input: "+value);
        }else{
            Screenshots.addStepWithoutScreenshotInReport("Set Input: "+value);
        }
    }

    protected void clearElement(String locator){
        this.getElement(locator).clear();
    }

    protected void clearElement(By by){
        this.getElement(by).clear();
    }

    protected String getText(String locator){return this.getElement(locator).getText();}

    protected String getText(By by){
        return this.getElement(by).getText();}

    protected String getAttribute(String locator,String attribute){return this.getElement(locator).getAttribute(attribute);}

    protected String getAttribute(By by,String attribute){return this.getElement(by).getAttribute(attribute);}

    protected String getCssValue(String locator,String attribute){
        return this.getElement(locator).getCssValue(attribute);
    }
    protected String getCssValue(By by,String attribute){
        return this.getElement(by).getCssValue(attribute);
    }

    protected void clickElement(String locator){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before Click:");
        javascriptExecutor.executeScript(CLICK_COMMAND,this.getElement(locator));
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
    }

    protected void clickElement(By by){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before Click:");
        javascriptExecutor.executeScript(CLICK_COMMAND,this.getElement(by));
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click Action");
    }

    protected void clickElement(String locator,String description){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before Click:");
        javascriptExecutor.executeScript(CLICK_COMMAND,this.getElement(locator));
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click: "+description);
    }

    protected void clickElement(By by,String description){
        Screenshots.addStepWithScreenshotInReport(this.driver,"Before Click:");
        javascriptExecutor.executeScript(CLICK_COMMAND,this.getElement(by));
        Screenshots.addStepWithScreenshotInReport(this.driver,"Click: "+description);
    }


}
