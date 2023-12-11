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

    private ImageView mapImageView3;
    private int color;
    private boolean flag = false;
    private static final String[] ITEMS = {"온도", "습도", "열섬"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapImageView = findViewById(R.id.iv_korea);
        mapImageView2 = findViewById(R.id.iv_tmp);
        mapImageView3 = findViewById(R.id.iv_hotisland);
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
                        MapUpdater.updateImageView(MainActivity.this, R.drawable.seoul_districts, mapImageView, selectedItem);
                        flag = true;
                    }

                    toggleImageViews(true, false,false);
                    Log.d("Myapp", "1번 사진임");
                } else if(selectedItem.equals("습도")) {
                    MapUpdater_water.updateImageView(MainActivity.this, R.drawable.seoul_districts, mapImageView2, selectedItem);
                    toggleImageViews(false,true, false);
                    Log.d("Myapp", "2번 사진임");
                }else if (selectedItem.equals("열섬")) {
                    // "열섬"이 선택되면 mapImageView3를 보이게 설정
                    MapUpdater_hotisland.updateImageView(MainActivity.this, R.drawable.seoul_districts, mapImageView3, selectedItem);
                    toggleImageViews(false, false,true);
                    Log.d("Myapp", "열섬 사진임");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected (if needed)
            }
        });
    }

    private void toggleImageViews(boolean isImageView1Visible,boolean isImageView2Visible, boolean isImageView3Visible) {
        mapImageView.setVisibility(isImageView1Visible ? View.VISIBLE : View.GONE);
        mapImageView2.setVisibility(isImageView2Visible ? View.VISIBLE : View.GONE);
        mapImageView3.setVisibility(isImageView3Visible ? View.VISIBLE : View.GONE);
    }

}