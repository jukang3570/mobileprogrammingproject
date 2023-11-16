package com.example.mainpage;

import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import kotlin.experimental.ExperimentalObjCRefinement;

public class MainActivity extends AppCompatActivity {

    frag1 frag1;
    frag2 frag2;
    frag3 frag3;
    rest rest;


    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //getSupportFragmentManager().beginTransaction().replace(R.id.containers, frag1).commit();
        mPager = findViewById(R.id.viewpager);
        mPager.setClipToOutline(true);

        pagerAdapter = new MyAdapter(this, num_page);
        mPager.setAdapter(pagerAdapter);

        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);



        mPager.setCurrentItem(1002);
        mPager.setOffscreenPageLimit(3);




        frag1 = new frag1();
        frag2 = new frag2();
        frag3 = new frag3();
        rest = new rest();

        NavigationBarView navigationBarView = findViewById(R.id.bottomNavi);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int itemId = item.getItemId();
                if (itemId==R.id.home)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, frag1).commit();
                    return true;
                } else if (itemId == R.id.umbrella) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, frag2).commit();
                    return true;
                } else if (itemId == R.id.comu) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, frag3).commit();
                    return true;
                } else if (itemId == R.id.rest) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, rest).commit();
                    return true;
                }

                return false;
            }
        });

    }
}