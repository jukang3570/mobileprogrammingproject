package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView mapImageView;
    private ImageView mapImageView2;
    private int color;
    private boolean flag = false;
    private static final String[] ITEMS = {"온도", "습도"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapImageView = findViewById(R.id.iv_korea);
        mapImageView2 = findViewById(R.id.iv_tmp);
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ITEMS[position];

                if (selectedItem.equals("온도")) {
                    if (!flag) {
                        updateImageView(R.drawable.seoul_districts, mapImageView);
                        flag = true;
                    }

                    toggleImageViews(true);
                    Log.d("Myapp", "1번 사진임");
                } else {
                    toggleImageViews(false);
                    Log.d("Myapp", "2번 사진임");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected (if needed)
            }
        });
    }

    private void updateImageView(int drawableId, ImageView imageView) {
        VectorChildFinder vector = new VectorChildFinder(MainActivity.this, drawableId, imageView);

        VectorDrawableCompat.VFullPath[] districts = new VectorDrawableCompat.VFullPath[25];

        String[] districtNames = {
                "도봉구", "동대문구", "동작구", "은평구", "강북구", "강동구", "강서구", "금천구", "구로구",
                "관악구", "광진구", "강남구", "종로구", "중구", "중랑구", "마포구", "노원구", "서초구",
                "서대문구", "성북구", "성동구", "송파구", "양천구", "영등포구", "용산구"
        };

        String[] regionNames = {
                "Dobong-gu","Dongdaemun-gu","Dongjak-gu","Eunpyeong-gu","Gangbuk-gu","Gandong-gu", "Gangseo-gu","Geumcheon-gu","Guro-gu",
                "Gwanak-gu", "Gwangjin-gu", "Gangnam-gu", "Jongno-gu", "Jung-gu", "Jungnang-gu", "Mapo-gu", "Nowon-gu", "Seocho-gu",
                "Seodaemun-gu", "Seongbuk-gu", "Seongdong-gu", "Songpa-gu", "Yangcheon-gu", "Yeongdeungpo-gu", "Yongsan-gu"
        };

        for (int i = 0; i < districts.length; i++) {
            districts[i] = vector.findPathByName(districtNames[i]);
        }

        int[] tmp = new int[regionNames.length];
        for (int i = 0; i < regionNames.length; i++) {
            double temperatures = MapHelper.getTemperaturesFromCSV(this, regionNames[i]);
            //tmp[i] = temperatures.isEmpty() ? 0 : temperatures.get(0);
            tmp[i] = (int)temperatures * 8;
        }

        for (int i = 0; i < tmp.length; i++) {
            color = Color.argb(tmp[i], 255, 0, 0);
            districts[i].setFillColor(color);
        }

        imageView.invalidate();
    }

    private void toggleImageViews(boolean isImageView1Visible) {
        mapImageView.setVisibility(isImageView1Visible ? View.VISIBLE : View.GONE);
        mapImageView2.setVisibility(isImageView1Visible ? View.GONE : View.VISIBLE);
    }
}