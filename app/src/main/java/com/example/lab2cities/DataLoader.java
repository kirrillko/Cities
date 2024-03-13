package com.example.lab2cities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DataLoader {
    private static final String BASE_URL = "https://yctcrtvmnjhvbtewkeiu.supabase.co/rest/v1/citiesdata";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InljdGNydHZtbmpodmJ0ZXdrZWl1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTAxODY5NTQsImV4cCI6MjAyNTc2Mjk1NH0.rJPKSRaXdXrz6o1REjoa9h4X_FkzNgHteOGyt7JjaLI";

    public interface OnDataListLoadedListener {
        void onDataListLoaded(List<Place> placeList);
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(Place place);
    }

    public static void loadDataList(OnDataListLoadedListener listener) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL);

                // Открываем соединение
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("apikey", API_KEY);
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

                // Читаем ответ
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                Type productListType = new TypeToken<List<Place>>(){}.getType();
                List<Place> productList = gson.fromJson(response.toString(), productListType);

                // Закрываем соединение
                connection.disconnect();

                // Сообщаем слушателю о загруженных продуктах
                if (listener != null) {
                    listener.onDataListLoaded(productList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void loadData(int id, OnDataLoadedListener listener) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "?place_id=eq." + id);

                // Открываем соединение
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("apikey", API_KEY);
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

                // Читаем ответ
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                Type productListType = new TypeToken<List<Place>>(){}.getType();
                List<Place> productList = gson.fromJson(response.toString(), productListType);

                // Закрываем соединение
                connection.disconnect();

                // Сообщаем слушателю о загруженных продуктах
                if (listener != null) {
                    listener.onDataLoaded(productList.get(0));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void updateData(Place place) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "?place_id=eq." + place.getPlace_id());

                // Открываем соединение
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PATCH");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("apikey", API_KEY);
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
                connection.setDoOutput(true);

                // Параметры для обновления
                JSONObject productObject = new JSONObject();
                productObject.put("is_favorite", place.isIs_favorite());

                // Отправляем параметры
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(productObject.toString());
                out.close();

                // Получаем ответ
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //Log.d("Responce", String.valueOf(responseCode));
                } else {
                    //Log.d("Responce", String.valueOf(responseCode));
                }

                // Закрываем соединение
                connection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
