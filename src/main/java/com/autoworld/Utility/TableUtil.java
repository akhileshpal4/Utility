package com.autoworld.Utility;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;


public class TableUtil {
    WebDriver driver;
    public TableUtil(WebDriver driver){this.driver=driver;}

    public List<String> readColumnDataByColumnName(String tableLocator,String columnName){
        List<String> columnDataList=new ArrayList<>();
        List<String> columnHeaderList=new ArrayList<>();
        int columnIndex=-1;

        try{
            this.driver.findElement(By.xpath(tableLocator));
        }catch (NoSuchElementException e){
            Screenshots.addStepWithoutScreenshotInReport("Invalid Input: Please provide valid table locaot. \n\n"+e.getMessage(),false);
        }
        String headerXpath=tableLocator+"//th";

        try{
            this.driver.findElements(By.xpath(headerXpath)).forEach((colHead)->{
                columnHeaderList.add(colHead.getText());
            });
            if(columnHeaderList.contains(columnName)){
                columnIndex=columnHeaderList.indexOf(columnName);
            }else{
                Screenshots.addStepWithoutScreenshotInReport("Invalid column name: "+columnName,false);
            }
        }catch (NoSuchElementException e){
            Screenshots.addStepWithoutScreenshotInReport("Please verify Table HTML tag. th tag is missing. Use column index instead of colum name",false);
        }

        String columnDataXpath=tableLocator+"//tr/td["+(columnIndex+1)+"]";
        try{
            this.driver.findElements(By.xpath(columnDataXpath)).forEach(data->{
                columnDataList.add(data.getText());
            });
        }catch (NoSuchElementException e){
            Screenshots.addStepWithoutScreenshotInReport("Invalid Input: Please provide valid table locaot. \n\n"+e.getMessage(),false);
        }
        return columnDataList;
    }

    public List<String> readColumnDataByColumnIndex(String tableLocator,int columnIndex){
        List<String> columnDataList=new ArrayList<>();

        try{
            this.driver.findElement(By.xpath(tableLocator));
        }catch (NoSuchElementException e){
            Screenshots.addStepWithoutScreenshotInReport("Invalid Input: Please provide valid table locaot. \n\n"+e.getMessage(),false);
        }

        String columnDataXpath=tableLocator+"//tr/td["+columnIndex+"]";
        try{
            this.driver.findElements(By.xpath(columnDataXpath)).forEach(data->{
                columnDataList.add(data.getText());
            });
            if(columnDataList.isEmpty()){
                Screenshots.addStepWithoutScreenshotInReport("Pleae enter vlaid column number. No data found");
            }
        }catch (NoSuchElementException e){
            Screenshots.addStepWithoutScreenshotInReport("Invalid Input: Please provide valid table locaot. \n\n"+e.getMessage(),false);
        }
        return columnDataList;
    }

    public void listComparator(List<String> list1,List<String> list2){
        if(list1.equals(list1)){
            Screenshots.addStepWithoutScreenshotInReport("List Data Matched: "+list1+"\n\n"+list2);
        }else{
            Screenshots.addStepWithoutScreenshotInReport("List Data does not match: "+list1+"\n\n"+list2,false);
        }
    }

    public void sortListAscending(List<String> columnData){
        columnData.sort(String.CASE_INSENSITIVE_ORDER);
    }
    public void sortListDescending(List<String> columnData){
        columnData.sort(String.CASE_INSENSITIVE_ORDER.reversed());
    }
}
