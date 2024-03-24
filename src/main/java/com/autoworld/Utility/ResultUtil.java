package com.autoworld.Utility;

import com.autoworld.ConfigProvider.ConfigProvider;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultUtil {

    public ResultUtil(){}

    public static void createResultDir(){
        if(System.getProperty("resultDir")==null){
            File parentDir=new File(ConfigProvider.getAsString("mo.result.dir"));
            String timestamp=(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")).format(new Date());
            File resultDir=new File(parentDir+"/"+timestamp+"/Artifact");
            resultDir.mkdirs();
            System.setProperty("resultDir",parentDir+"/"+timestamp);
        }
    }

    public static String getScenarioArtifactName(String scenarioName){
        File artifactDir=new File(System.getProperty("resultDir")+"/Artifact");
        File[] listFiles=artifactDir.listFiles(new ArtifactNameFilter(scenarioName));
        String artifactfileName=scenarioName+"-Artifact_"+(listFiles.length+1);
        return artifactfileName;
    }

    public static class ArtifactNameFilter implements FilenameFilter{
        private String scenarioName;
        public ArtifactNameFilter(String scenarioName){this.scenarioName=scenarioName.toLowerCase();}
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().startsWith(this.scenarioName.toLowerCase()+"-artifact");
        }
    }
}
