// MainActivity.java
package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Region;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView mapImageView;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int[] tmp = new int[25];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapImageView = findViewById(R.id.iv_korea);

        // 'Jung-gu'의 '온도평균' 값을 가져와서 '중구'를 강조

        VectorChildFinder vector = new VectorChildFinder(this, R.drawable.seoul_districts, mapImageView);

        VectorDrawableCompat.VFullPath[] districts = new VectorDrawableCompat.VFullPath[25];

        String[] districtNames = {
                "도봉구", "동대문구", "동작구", "은평구", "강북구", "강동구", "강서구", "금천구", "구로구",
                "관악구", "광진구", "강남구", "종로구", "중구", "중랑구", "마포구", "노원구", "서초구",
                "서대문구", "성북구", "성동구", "송파구", "양천구", "영등포구", "용산구"
        };

        String[] RegionNames = {
                "Dobong-gu","Dongdaemun-gu","Dongjak-gu","Eunpyeong-gu","Gangbuk-gu","Gandong-gu", "Gangseo-gu","Geumcheon-gu","Guro-gu",
                "Gwanak-gu", "Gwangjin-gu", "Gangnam-gu", "Jongno-gu", "Jung-gu", "Jungnang-gu", "Mapo-gu", "Nowon-gu", "Seocho-gu",
                "Seodaemun-gu", "Seongbuk-gu", "Seongdong-gu", "Songpa-gu", "Yangcheon-gu", "Yeongdeungpo-gu", "Yongsan-gu"
        };

        for (int i = 0; i < districts.length; i++) {
            districts[i] = vector.findPathByName(districtNames[i]);
        }
        for (int i = 0; i < RegionNames.length; i++) {
            List<Integer> temperatures = MapHelper.getTemperaturesFromCSV(this, RegionNames[i]);
            tmp[i] = temperatures.isEmpty() ? 0 : temperatures.get(0);
        }

        for(int i=0;i<tmp.length;i++){
            color = Color.argb(tmp[i], 255, 0, 0);
            districts[i].setFillColor(color);
        }
        mapImageView.invalidate();
    }
}


