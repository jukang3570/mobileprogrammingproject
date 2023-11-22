package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

public class MainActivity extends AppCompatActivity {

    private ImageView mapImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ivSeoul  = findViewById(R.id.iv_korea);


        VectorChildFinder vector = new VectorChildFinder(this, R.drawable.seoul_districts,ivSeoul);
        VectorDrawableCompat.VFullPath Dobonggu = vector.findPathByName("도봉구");
        VectorDrawableCompat.VFullPath Dongdaemungu = vector.findPathByName("동대문구");
        VectorDrawableCompat.VFullPath Dongjak = vector.findPathByName("동작구");
        VectorDrawableCompat.VFullPath EunPyeong = vector.findPathByName("은평구");
        VectorDrawableCompat.VFullPath Gangbuk = vector.findPathByName("강북구");
        VectorDrawableCompat.VFullPath Gangdong = vector.findPathByName("강동구");
        VectorDrawableCompat.VFullPath Gangseo = vector.findPathByName("강서구");
        VectorDrawableCompat.VFullPath Geumcheon = vector.findPathByName("금천구");
        VectorDrawableCompat.VFullPath Guro = vector.findPathByName("구로구");
        VectorDrawableCompat.VFullPath Gwanak = vector.findPathByName("관악구");
        VectorDrawableCompat.VFullPath Gwangjin = vector.findPathByName("광진구");
        VectorDrawableCompat.VFullPath Gangnam = vector.findPathByName("강남구");
        VectorDrawableCompat.VFullPath Jonglo = vector.findPathByName("종로구");
        VectorDrawableCompat.VFullPath Jungku = vector.findPathByName("중구");
        VectorDrawableCompat.VFullPath Junglang = vector.findPathByName("중랑구");
        VectorDrawableCompat.VFullPath Mapo = vector.findPathByName("마포구");
        VectorDrawableCompat.VFullPath Nowon = vector.findPathByName("노원구");
        VectorDrawableCompat.VFullPath Seocho = vector.findPathByName("서초구");
        VectorDrawableCompat.VFullPath Seodaemoon = vector.findPathByName("서대문구");
        VectorDrawableCompat.VFullPath Seongbuk = vector.findPathByName("성북구");
        VectorDrawableCompat.VFullPath Sedongdong = vector.findPathByName("성동구");
        VectorDrawableCompat.VFullPath Songpa = vector.findPathByName("송파구");
        VectorDrawableCompat.VFullPath Yangcheo = vector.findPathByName("양천구");
        VectorDrawableCompat.VFullPath Younddeungpo = vector.findPathByName("영등포구");
        VectorDrawableCompat.VFullPath Yongsan = vector.findPathByName("용산구");

        new ApiCallAsyncTask().execute();
        int color = Color.argb( 244,255, 0, 0);
        Dongjak.setFillColor(color);
        ivSeoul.invalidate();
    }

}
