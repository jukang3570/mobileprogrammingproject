package com.example.projec7;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projec7.Post;
import com.example.projec7.PostAdapter;
import com.example.projec7.WriteFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityScreenFragment extends Fragment  {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private RecyclerView recyclerView;
    ImageButton bWrite;
    private Spinner localSpinner;
    private Spinner themeSpinner;
    private ArrayList<String> localList;
    private ArrayList<String> themeList;
    private ArrayAdapter<String> localAdapter;
    private ArrayAdapter<String> themeAdapter;
    private List<Post> postList;
    private PostAdapter adapter;


    public CommunityScreenFragment() {
        // Required empty public constructor
    }


    public static CommunityScreenFragment newInstance(String param1, String param2) {
        CommunityScreenFragment fragment = new CommunityScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        localList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.local_array)));
        themeList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.theme_array)));

        // 어댑터 초기화
        localAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, localList);
        localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        themeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, themeList);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_community_screen, container, false);
        localSpinner = rootView.findViewById(R.id.local_category);
        themeSpinner = rootView.findViewById(R.id.theme_category);

        // Spinner에 어댑터 설정
        localSpinner.setAdapter(localAdapter);
        themeSpinner.setAdapter(themeAdapter);

        bWrite = rootView.findViewById(R.id.WriteButton);

        // RecyclerView 초기화 및 레이아웃 매니저 설정
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // DBHelper에서 데이터 가져오기
        DBHelper dbHelper = new DBHelper(getActivity());

        // PostAdapter 객체 초기화 및 데이터 설정
        postList = new ArrayList<>(); // postList 초기화
        postList.addAll(dbHelper.getAllPosts());
        adapter = new PostAdapter(getActivity(), postList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        localSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 선택된 지역에 따라 postList 필터링
                filterPosts(localList.get(position), themeSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때의 처리
                showAllPosts();
            }
        });

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 선택된 테마에 따라 postList 필터링
                filterPosts(localSpinner.getSelectedItem().toString(), themeList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때의 처리
                showAllPosts();
            }
        });




        // WriteButton 클릭 이벤트 설정
        bWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WriteFragment로 이동하는 코드 작성
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                WriteFragment writeFragment = new WriteFragment();
                transaction.replace(R.id.container, writeFragment);
                transaction.commit();
                requireActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });



        return rootView;
    }

    private void filterPosts(String selectedLocal, String selectedTheme) {
        // DBHelper에서 해당 지역과 테마에 맞는 데이터 가져오기
        DBHelper dbHelper = new DBHelper(getActivity());
        List<Post> filteredPosts = dbHelper.getFilteredPosts(selectedLocal, selectedTheme);

        // Adapter에 데이터 설정 및 갱신
        adapter.setPosts(filteredPosts);
        adapter.notifyDataSetChanged();
    }
    private void showAllPosts() {
        // Implement the logic to show all posts
        // For example, you can call filterPosts with "전체" for both local and theme categories
        filterPosts("구 선택", "전체");
    }

}