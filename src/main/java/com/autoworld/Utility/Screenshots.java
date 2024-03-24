package com.autoworld.Utility;


import com.autoworld.Reports.ExtentTestManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.log4testng.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Screenshots {
    private static final Logger logger= Logger.getLogger(Screenshots.class);
    private static String screenshotsFolderPath;
    private static final String SCREENSHOTS_FOLDER="\\screenshots\\";
    private Screenshots(){}

    public static String getScreenshotsFolderPath(){return screenshotsFolderPath;}

    static {createDirectory();}
    private static void createDirectory(){
        screenshotsFolderPath="AutomationReports/screenshots/";
        File file=new File(screenshotsFolderPath);
        if(!file.exists() && !file.mkdir()){
            System.out.println("Failed to create directory!!");
            logger.warn("Failed to create directory!!");
        }
    }

    protected static String captureScreenshots(WebDriver driver,String screenshotName){
        String randomNumber= RandomStringUtils.randomNumeric(5);
        String destinationPath=screenshotsFolderPath+screenshotName+randomNumber+".png";

        TakesScreenshot ts=(TakesScreenshot) driver;
        File sourceFile=ts.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(sourceFile,new File(destinationPath));
        }catch (IOException e){
            logger.warn("Unable to capture due to error:"+e.getMessage());
        }
        return destinationPath.substring(destinationPath.indexOf("/")+1);
    }

    public static void addStepWithScreenshotInReport(WebDriver driver,String message){
        ExtentTest extentTest=ExtentTestManager.getTest();
        if(extentTest!=null){
            if(driver!=null){
                String path=captureScreenshots(driver,"screenshot");
                try{
                    extentTest.pass(message, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                extentTest.pass(message);
            }
        }
    }

    public static void addFailureStepWithScreenshotInReport(WebDriver driver,String message){
        ExtentTest extentTest=ExtentTestManager.getTest();
        if(extentTest!=null){
            if(driver!=null){
                String path=captureScreenshots(driver,"screenshot");
                try {
                    extentTest.fail(message,MediaEntityBuilder.createScreenCaptureFromPath(path).build());
                }catch (Exception e){
                    logger.warn(e.getMessage());
                }
            }else{
                extentTest.fail(message);
            }
        }
    }

    public static void addStepWithoutScreenshotInReport(String message,boolean res){
        ExtentTest extentTest=ExtentTestManager.getTest();
        if(extentTest!=null){
            if(res){
                extentTest.pass(message);
            }else{
                extentTest.fail(message);
            }
        }
    }
    public static void addStepWithoutScreenshotInReport(String message){
        ExtentTest extentTest= ExtentTestManager.getTest();
        if(extentTest!=null){
            extentTest.pass(message);
        }
    }

    protected static String captureDesktop(String screenshotName) throws AWTException {
        String randomNumber=RandomStringUtils.randomNumeric(5);
        String destPath=screenshotsFolderPath+screenshotName+randomNumber+".png";

        Robot rb=new Robot();
        Rectangle rectangle=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage image=rb.createScreenCapture(rectangle);
        try {
            ImageIO.write(image,"png",new File(destPath));
        }catch (IOException e){
            logger.warn("Unable to capture due to error:"+e.getMessage());
        }
        return destPath;
    }

    public static void addStepWithDesktopScreenshotInReport(String message) throws AWTException {
        ExtentTest extentTest=ExtentTestManager.getTest();
        String path=captureDesktop("screenshot");
        if(extentTest!=null){
            extentTest.pass(message,MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        }
    }

    public static void addJSONOutputToReport(String jsonString, Status status){
        ExtentTest extentTest=ExtentTestManager.getTest();
        Markup m= MarkupHelper.createCodeBlock(jsonString, CodeLanguage.JSON);
        if(status.equals(Status.PASS)){
            extentTest.pass(m);
        }else {
            extentTest.fail(m);
        }
    }

    public static void addXMLOutputToReport(String xmlString,Status status){
        ExtentTest extentTest=ExtentTestManager.getTest();
        Markup m=MarkupHelper.createCodeBlock(xmlString,CodeLanguage.XML);
        if(status.equals(Status.PASS)){
            extentTest.pass(m);
        }else {
            extentTest.fail(m);
        }
    }

    public static void addTableOutputToReport(String[][] tableData,Status status){
        ExtentTest extentTest=ExtentTestManager.getTest();
        Markup m=MarkupHelper.createTable(tableData);
        if(status.equals(Status.PASS)){
            extentTest.pass(m);
        }else {
            extentTest.fail(m);
        }
    }

    public static enum Status{
        PASS,
        FAIL;
        private Status(){}
    }
}
