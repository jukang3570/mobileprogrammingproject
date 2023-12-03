package com.example.votepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements onListItemSelectedInterface {

    RecyclerviewAdapter adapter;
    ArrayList<address> listdata, filteredList;
    EditText searchET;

    final int voteCount[] = new int[25];

    String addName[]={"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getData();

        for (int i = 0; i<25;i++)

        {
            voteCount[i] = 0;
        }

        Button btnRes = findViewById(R.id.button);
        btnRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("VoteCount",voteCount);
                intent.putExtra("addName",addName);
                startActivity(intent);


                DBHelper helper = new DBHelper(MainActivity.this);
                for (int i=0;i <addName.length;i++){
                    int countValue = voteCount[i];
                    long result = helper.insertAddress(addName[i],countValue);
                }

            }
        });

    }
    private void init() {
        ConstraintLayout mainLayout = findViewById(R.id.mainLayout);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);


        searchET = findViewById(R.id.searchaddress);
        filteredList = new ArrayList<>();
        listdata = new ArrayList<>();
        adapter = new RecyclerviewAdapter(listdata, this);


        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), 1));
        recyclerView.addItemDecoration(new RecyclerDecoration(45));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);




        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String searchText = searchET.getText().toString();
                searchFilter(searchText);

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void searchFilter(String searchText) {
        filteredList.clear();

        for (int i = 0; i < listdata.size(); i++) {
            if (listdata.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(listdata.get(i));
            }
        }

        adapter.filterList(filteredList);
    }
    public void onItemSelected(View v, int position){
        Toast.makeText(getApplicationContext(),"pos : "+position,Toast.LENGTH_SHORT).show();
        voteCount[position]++;
    }

    private void getData(){
        address data = new address("강남구");
        adapter.addItem(data);
        data = new address("강동구");
        adapter.addItem(data);
        data = new address("강북구");
        adapter.addItem(data);
        data = new address("강서구");
        adapter.addItem(data);
        data = new address("관악구");
        adapter.addItem(data);
        data = new address("광진구");
        adapter.addItem(data);
        data = new address("구로구");
        adapter.addItem(data);
        data = new address("금천구");
        adapter.addItem(data);
        data = new address("노원구");
        adapter.addItem(data);
        data = new address("도봉구");
        adapter.addItem(data);
        data = new address("동대문구");
        adapter.addItem(data);
        data = new address("동작구");
        adapter.addItem(data);
        data = new address("마포구");
        adapter.addItem(data);
        data = new address("서대문구");
        adapter.addItem(data);
        data = new address("서초구");
        adapter.addItem(data);
        data = new address("성동구");
        adapter.addItem(data);
        data = new address("성북구");
        adapter.addItem(data);
        data = new address("송파구");
        adapter.addItem(data);
        data = new address("양천구");
        adapter.addItem(data);
        data = new address("영등포구");
        adapter.addItem(data);
        data = new address("용산구");
        adapter.addItem(data);
        data = new address("은평구");
        adapter.addItem(data);
        data = new address("종로구");
        adapter.addItem(data);
        data = new address("중구");
        adapter.addItem(data);
        data = new address("중랑구");
        adapter.addItem(data);
    }

}