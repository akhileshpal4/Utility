package com.autoworld.Utility;

import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.VideoFormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.testng.log4testng.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RecordScreen {
    private ScreenRecorder screenRecorder;
    private static final Logger logger=Logger.getLogger(ScreenRecorder.class);
    Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    int width;
    int height;
    Rectangle captureSize;
    GraphicsConfiguration gc;
    Format fileFormat;
    Format screenFormat;
    Format mouseFormat;
    Format audioFormat;

    public RecordScreen(String filePath) throws IOException, AWTException {
        this.height= screenSize.height;
        this.width= screenSize.width;
        this.captureSize=new Rectangle(0,0,width,height);
        this.gc=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        this.fileFormat=new Format(new Object[]{VideoFormatKeys.MediaTypeKey, FormatKeys.MediaType.FILE,VideoFormatKeys.MimeTypeKey,"video/avi"});
        this.screenFormat=new Format(new Object[]{VideoFormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO,VideoFormatKeys.EncodingKey,"tscc",VideoFormatKeys.CompressorNameKey,"tscc",VideoFormatKeys.DepthKey,24,VideoFormatKeys.FrameRateKey, Rational.valueOf(15.0D),VideoFormatKeys.QualityKey,1.0F,VideoFormatKeys.KeyFrameIntervalKey,900});
        this.mouseFormat=new Format(new Object[]{VideoFormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO,VideoFormatKeys.EncodingKey,"black",VideoFormatKeys.FrameRateKey,Rational.valueOf(30.0D)});
        this.audioFormat=null;
        this.screenRecorder=new ScreenRecorder(this.gc,this.captureSize,this.fileFormat,this.screenFormat,this.mouseFormat,this.audioFormat,new File(filePath));
    }

    public void startRecording(){
        try{
            this.screenRecorder.start();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    public void stopRecording(){
        try{
            this.screenRecorder.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
