package com.example.testbtl2.services;


import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class APIService {

    public static JSONObject getBookInfo(String isbn) throws Exception {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");

        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        StringBuilder result = new StringBuilder();
        int read;
        while ((read = reader.read()) != -1) {
            result.append((char) read);
        }

        return new JSONObject(result.toString());
    }
}

