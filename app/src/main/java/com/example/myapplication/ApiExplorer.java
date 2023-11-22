package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;

public class ApiExplorer {
    public static Integer makeApiCall(String a) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/" + URLEncoder.encode("5267546b636173653830496e596141", "UTF-8")); /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); /*요청파일타입 (xml,xmlf,xls,json) */
        urlBuilder.append("/" + URLEncoder.encode("IotVdata017", "UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
        urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
        urlBuilder.append("/" + URLEncoder.encode("100", "UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
        // 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.
        urlBuilder.append("/" + URLEncoder.encode(a, "UTF-8"));/* 서비스별 추가 요청인자들*//* 서비스별 추가 요청인자들*/
        // 서비스별 추가 요청 인자이며 자세한 내용은 각 서비스별 '요청인자'부분에 자세히 나와 있습니다.

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        StringBuilder stringBuilder = new StringBuilder();
        int tmp = 0;
        try {
            JSONObject jsonObj = new JSONObject(sb.toString());
            JSONObject iotVdata017 = jsonObj.getJSONObject("IotVdata017");
            JSONArray jsonArray = iotVdata017.getJSONArray("row");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("AUTONOMOUS_DISTRICT");
                int temp = jsonObject.getInt("AVG_TEMP");
                if(name.equals("Dongjak-gu"))
                {
                    tmp += temp;
                }
                stringBuilder.append(" 구  : ").append(name).append("\n");
                //stringBuilder.append(" 구  : ").append(name).append(" 온도 : ").append(temp).append("\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            stringBuilder.append("Error parsing JSON: ").append(e.getMessage());
        }
        return tmp;
    }
}