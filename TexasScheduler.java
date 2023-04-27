package org.example;


import org.json.JSONArray;
import java.awt.*;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TexasScheduler {
    public static void main(String[] args) throws Exception {
        int count=0;
        while (true) {
            String url = "https://publicapi.txdpsscheduler.com/api/AvailableLocation";
            String body = "{\"TypeId\":71,\"ZipCode\":\"78726\",\"CityName\":\"\",\"PreferredDay\":0}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Accept-Language", "en-US,en-IN;q=0.9,en;q=0.8,en-GB;q=0.7,kn;q=0.6,hi;q=0.5")
                    // .header("Connection", "keep-alive")
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .header("Origin", "https://public.txdpsscheduler.com")
                    .header("Referer", "https://public.txdpsscheduler.com/")
                    .header("Sec-Fetch-Dest", "empty")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Site", "same-site")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36")
                    .header("sec-ch-ua", "\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("sec-ch-ua-platform", "\"Windows\"")
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


          //  System.out.println("Response status code: " + response.statusCode()+ " count number "+ count);
            count++;
            String responseBody = response.body();
         //   System.out.println("Response body: " + responseBody);
            //Add the date you want
            ChronoLocalDate chronoLocalDate = LocalDate.parse("2023-04-29");
            System.out.println("Target date is "+ chronoLocalDate);
            JSONArray jsonArray = new JSONArray(responseBody);
            for (int index = 0; index < jsonArray.length(); index++)
            {
                //HashMap<String, LocalDate> nameDateMap = new HashMap<>();
                String Name = jsonArray.getJSONObject(index).getString("Name");
                String NextAvailableDate = jsonArray.getJSONObject(index).getString("NextAvailableDate");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
                LocalDate date = LocalDate.parse(NextAvailableDate, formatter);
                System.out.println(Name+ "  "+ date); // 2010-01-02
                if(date.isBefore(chronoLocalDate)){
                    System.out.println("found");
                    final Runnable runnable =(Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
                    while (runnable != null) {
                        runnable.run();
                        Thread.sleep(30000);
                    }
                }
                //nameDateMap.put(Name,date);
               // System.out.println(Name + "    "+ NextAvailableDate);
            }
            System.out.println();
            //set the time interval for subsequent requests
            Thread.sleep(30000);
        }
    }
}
