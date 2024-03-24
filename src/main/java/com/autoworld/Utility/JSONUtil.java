package com.autoworld.Utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.log4testng.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONUtil {

    private static final Logger logger=Logger.getLogger(JSONUtil.class);
    private JSONUtil(){}

    public static String getJSONInString(String jsonFilePath){
        String jsonAsString="";
        try{
            jsonAsString=new String(Files.readAllBytes(Paths.get(jsonFilePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.warn("JSON file not found: "+jsonFilePath);
            logger.warn(e.getMessage());
        }
        return jsonAsString;
    }
    public static void CompareTwoJson(String expectedJsonPth,String actualJsonPath) throws IOException {
        String diffJsonRaw=null;
        FileWriter writer=null;
        try{
            String failureMessage;
            try {
                ObjectMapper mapper=new ObjectMapper();
                failureMessage="Error: ";
                File expecteFile=new File(expectedJsonPth);
                File actualFile=new File(actualJsonPath);
                writer=new FileWriter("JsonDiff.txt",false);
                JsonNode expecteNode=mapper.readTree(expecteFile);
                JsonNode actualNode=mapper.readTree(actualFile);
                boolean statusAfterCompare=actualNode.equals(expecteNode);
                if(statusAfterCompare){
                    logger.info("Both JSON's are equal");
                    writer.write("Both JSON's are equal");
                    writer.close();
                }else{
                    logger.info("Both JSON's are not equal");
                    logger.info("please check JsonDiff.txt file for more info");
                    JSONAssert.assertEquals(failureMessage,expecteNode.toString(),actualNode.toString(), JSONCompareMode.STRICT);
                }

            }catch (AssertionError e){
                diffJsonRaw=e.getMessage();
                failureMessage=diffJsonRaw.replace("Error: ","");
                String[] diffRaw=failureMessage.split(" ; ");
                int i=0;
                writer.write("Total number of differences are: "+diffRaw.length+"\n");
                while (i<diffRaw.length){
                    writer.write("=====================================================\n");
                    writer.write("Key("+i+"): "+diffRaw[i]);
                    writer.write("\n");
                    i++;
                }
                writer.close();
            }

            catch (IOException e) {
                logger.warn(e.getMessage());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }finally {
            logger.info("-- Comparison Done --");
        }
    }
}
