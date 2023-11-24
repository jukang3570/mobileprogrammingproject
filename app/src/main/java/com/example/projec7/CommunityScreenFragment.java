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
import android.widget.ArrayAdapter;
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
    private PostAdapter postAdapter;
    Button bWrite;
    private Spinner localSpinner;
    private Spinner themeSpinner;
    private ArrayList<String> localList;
    private ArrayList<String> themeList;
    private ArrayAdapter<String> localAdapter;
    private ArrayAdapter<String> themeAdapter;
    private ArrayList<Post> postList = new ArrayList<>();


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

        localList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.local_array)));
        themeList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.theme_array)));

        // 어댑터 초기화
        localAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, localList);
        localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        themeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, themeList);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // 레이아웃 매니저 설정

        DBHelper dbHelper = new DBHelper(requireContext());



        // Update the RecyclerView with the loaded posts
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

// Load posts from the database
        ArrayList<Post> postArrayList = new ArrayList<>(); // 어떻게 리스트를 초기화했는지에 따라 코드가 달라집니다.
        postAdapter = new PostAdapter(postArrayList);
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






    private String convertStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }


    // Base64 문자열을 Bitmap으로 변환하는 메서드
    private Bitmap convertBase64ToBitmap(String base64String) {

        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Log.d("MyTag", "CommunityScreenFragment: convertBase64ToBitmap - Decoded String Length: " + decodedString.length);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

    }
}