// MapHelper.java
package com.example.myapplication;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapHelper {

    public static List<Integer> getTemperaturesFromCSV(Context context, String districtName) {
        List<Integer> temperatures = new ArrayList<>();

        try {
            InputStream inputStream = context.getAssets().open("nature.csv");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // CSV 파일 구조가 "지역, 온도"와 같다고 가정합니다.
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].trim().equals(districtName)) {
                    float temperature = Float.parseFloat(parts[1].trim());
                    // 반올림하여 가장 가까운 정수로 변환 후 리스트에 추가합니다.
                    int roundedTemperature = Math.round(temperature);
                    temperatures.add(roundedTemperature);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temperatures;
    }

    // 나머지 메서드들...
}
