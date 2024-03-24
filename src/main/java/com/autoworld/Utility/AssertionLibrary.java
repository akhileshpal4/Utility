package com.autoworld.Utility;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Map;
import java.util.Set;

public class AssertionLibrary {
    private static WebDriver driver;
    private static final String ACTUAL="<br> Actual:";
    private static final String EXPECTED="<br> Expected";
    public static enum Screenshot{
        REQUIRED,
        NOT_REQUIRED;
        private Screenshot(){}
    }
    public AssertionLibrary(WebDriver driver){AssertionLibrary.driver=driver;}

    private static void attachScreenshotIfRequired(Screenshot screenshot, String message){
        if(screenshot.equals(Screenshot.REQUIRED)){
            Screenshots.addStepWithScreenshotInReport(driver,message);
        }else{
            Screenshots.addStepWithoutScreenshotInReport(message);
        }
    }


    public static void assertEquals(String actual, String expected, String message, Screenshot screenshot){
        String reporMessage=message+ACTUAL+actual+EXPECTED+expected;
        Assert.assertEquals(actual,expected,message);
        attachScreenshotIfRequired(screenshot,reporMessage);
    }
    public static void assertEquals(String actual,String expected,String message){
        assertEquals(actual,expected,message,Screenshot.REQUIRED);
    }

    public static void assertEquals(Double actual, Double expected, Double delta, String message, Screenshot screenshot){
        String reporMessage=message+ACTUAL+actual.toString()+EXPECTED+expected.toString();
        Assert.assertEquals(actual,expected,delta,message);
        attachScreenshotIfRequired(screenshot,reporMessage);
    }
    public static void assertEquals(Double actual,Double expected,Double delta,String message){
        assertEquals(actual,expected,delta,message,Screenshot.REQUIRED);
    }

    public static void assertEquals(Set<?> actual, Set<?> expected, String message, Screenshot screenshot){
        String reporMessage=message+ACTUAL+actual.toString()+EXPECTED+expected.toString();
        Assert.assertEqualsDeep(actual,expected,message);
        attachScreenshotIfRequired(screenshot,reporMessage);
    }
    public static void assertEquals(Set<?> actual, Set<?> expected,String message){
        assertEquals(actual,expected,message,Screenshot.REQUIRED);
    }

    public static void assertEquals(Map<?,?> actual, Map<?,?> expected, String message, Screenshot screenshot){
        String reporMessage=message+ACTUAL+actual.toString()+EXPECTED+expected.toString();
        Assert.assertEqualsDeep(actual,expected,message);
        attachScreenshotIfRequired(screenshot,reporMessage);
    }
    public static void assertEquals(Map<?,?> actual, Map<?,?> expected,String message){
        assertEquals(actual,expected,message,Screenshot.REQUIRED);
    }

    public static void assertTrue(boolean condition,String message,Screenshot screenshot){
        String reportMsg=message+"<br> Condition: "+condition;
        Assert.assertTrue(condition,message);
        attachScreenshotIfRequired(screenshot,reportMsg);
    }

    public static void assertTrue(boolean condition,String message){
        assertTrue(condition,message,Screenshot.REQUIRED);
    }

    public static void assertFalse(boolean condition,String message,Screenshot screenshot){
        String reportMsg=message+"<br> Condition: "+condition;
        Assert.assertFalse(condition,message);
        attachScreenshotIfRequired(screenshot,reportMsg);
    }

    public static void assertFalse(boolean condition,String message){
        assertFalse(condition,message,Screenshot.REQUIRED);
    }


}
