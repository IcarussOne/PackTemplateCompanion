package com.barebonium.packcompanion.utils;

import com.barebonium.packcompanion.PackCompanion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RentryUploader {
    private static final String RENTRY_URL = "https://rentry.co/api/new";

    public static String uploadToRentry(String markdownContent) {
        try {
            URL url = new URL(RENTRY_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Referer", "https://rentry.co");

            String data = "text=" + URLEncoder.encode(markdownContent, "UTF-8");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = data.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }

                    String responseStr = response.toString();
                    if (responseStr.contains("\"url\":")) {
                        return responseStr.split("\"url\":")[1].split("\"")[1];
                    }
                }
            }
        } catch (Exception e) {
            PackCompanion.LOGGER.error("Failed to upload to Rentry", e);
        }
        return null;
    }
}
