package com.autoworld.Utility;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PDFUtils {
    public PDFUtils(){}

    public String readSavedPDF(String fileName,String filePath) throws IOException {
        File file=new File(filePath+File.separator+fileName);
        PDDocument pdDoc=PDDocument.load(file);
        PDFTextStripper strip=new PDFTextStripper();
//        strip.setStartPage(1);
//        strip.setEndPage(pdDoc.getNumberOfPages());
        String data=strip.getText(pdDoc);
        pdDoc.close();
        return data;
    }

    public String readPDFInURL(String urlname) throws IOException {
        URL url=new URL(urlname);
        InputStream is=url.openStream();
        BufferedInputStream fileToParse=new BufferedInputStream(is);
        PDDocument document=null;
        String output;
        try{
            document=PDDocument.load(fileToParse);
            output=(new PDFTextStripper().getText(document));
        }finally {
            if(document!=null){
                document.close();
            }
            fileToParse.close();
            is.close();
        }
return output;
    }
}
