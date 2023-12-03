package com.example.votepage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("투표 결과");

        Intent intent=getIntent();
        int[] voteResult = intent.getIntArrayExtra("VoteCount");
        String[] addName = intent.getStringArrayExtra("addName");
        for(int i=0;i<25;i++){
            Log.d("Test",addName[i]+" : "+voteResult[i]+"표");
        }

    }
}
