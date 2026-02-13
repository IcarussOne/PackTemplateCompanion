package com.barebonium.packcompanion.processors;

import com.barebonium.packcompanion.PackCompanion;
import com.barebonium.packcompanion.config.ConfigHandler;
import com.barebonium.packcompanion.entries.HTMLEntry;
import com.barebonium.packcompanion.htmlcompiler.HTMLGenerator;
import com.barebonium.packcompanion.utils.RentryUploader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import static java.nio.file.Files.readAllBytes;

public class OutputProcessor {
    public static File HTMLReportFile;
    public static File GlobalOutputLog;
    public static String lastRentryUrl = "";

    public static void runPackCompanionChecks() {
        boolean isSuccess = false;
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        String timeStampFile = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
        String fileName = "companionOutput-" + timeStampFile + ".md";

        try {
            File outputLog = new File(PackCompanion.outputDir, fileName);
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputLog)));
            ArrayList<HTMLEntry> htmlEntries = new ArrayList<>();

            writer.println("# Pack Companion Report");
            writer.println("");
            writer.printf("### Generated on: %s%n",timeStamp);
            writer.println("");

            if(ConfigHandler.enableModAnalysis) {
                isSuccess |= ModlistProcessor.checkModList(writer, htmlEntries);
            }
            if(ConfigHandler.enableConfigAnalysis) {
                isSuccess |= ConfigProcessor.checkConfigs(writer);
            }

            PackCompanion.LOGGER.info("Loaded mods Successfully Analysed");
            PackCompanion.LOGGER.info("Please consult the Output log at {}", outputLog.getPath());
            writer.close();

            if(isSuccess) {
                cleanupOldReports(PackCompanion.outputDir, ".md", ConfigHandler.reportFilesCountLimit);
                cleanupOldReports(PackCompanion.outputDir, ".html", ConfigHandler.reportFilesCountLimit);
                if (ConfigHandler.debugMode) {
                    PackCompanion.LOGGER.info("Analysis confirmed, Beginning HTML compilation");
                }
                File htmlOutput = new File(PackCompanion.outputDir, fileName.replace(".md", ".html"));
                HTMLGenerator.saveAsHtml(htmlEntries, htmlOutput, timeStamp);
                if (ConfigHandler.enableUploadToRentry) {
                    try {
                        byte[] encoded = readAllBytes(outputLog.toPath());
                        String content = new String(encoded, StandardCharsets.UTF_8);
                        String rentryUrl = RentryUploader.uploadToRentry(content);

                        if (rentryUrl != null) {
                            PackCompanion.LOGGER.info("Report uploaded to Rentry: {}", rentryUrl);
                            lastRentryUrl = rentryUrl;
                        }
                    } catch (IOException e) {
                        PackCompanion.LOGGER.error("Failed to read MD file for upload", e);
                    }
                }
            } else if (ConfigHandler.debugMode) {
                PackCompanion.LOGGER.info("Analysis Failed, Skipping HTML compilation");
            }
        } catch (Exception e) {
            PackCompanion.LOGGER.error("Encountered an exception while compiling HTML",e);
        }
    }

    public static File verifyCachedFile(String fileName) throws FileNotFoundException {
        File file = new File(PackCompanion.cacheDir, fileName);
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("modList JSON could not be fetched at %s", file.getPath()));
        }
        return file;
    }

    public static File initializeDirectory(File parentDir, String childDir) {
        File directory = new File(parentDir, childDir);
        if (!directory.exists()) directory.mkdir();
        return directory;
    }
    public static void cleanupOldReports(File directory, String format, int maxFilesPresent){
        if(!directory.exists()){
            return;
        }
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(format));
        if(files!=null &&  files.length>maxFilesPresent){
            Arrays.sort(files, Comparator.comparing(File::lastModified));
            int filesToDelete = files.length-maxFilesPresent;
            for(int i=0; i<filesToDelete; i++){
                if(files[i].delete()){
                    PackCompanion.LOGGER.warn("Report deleted from output folder: {}", files[i].getPath());
                }
            }
        }
    }
}
