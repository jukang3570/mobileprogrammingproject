package com.example.projec7;

import android.content.Context;
import android.content.SharedPreferences;
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
public class CommunityScreenFragment extends Fragment {


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



        getParentFragmentManager().setFragmentResultListener("writeKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                // WriteFragment에서 전달된 결과를 받아서 처리
                String title = result.getString("title", "");
                String content = result.getString("content", "");
                String localCategory = result.getString("localCategory", "");
                String themeCategory = result.getString("themeCategory", "");
                String imageData = result.getString("imageData", "");

                // 새로운 게시글 생성
                Post newPost = new Post(title, content, localCategory, themeCategory, imageData);

                // 리스트에 추가
                postList.add(0, newPost);

                // 어댑터에 변경사항 알림
                postAdapter.notifyItemInserted(0);

                // 스크롤 이동
                recyclerView.scrollToPosition(0);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_community_screen, container, false);
        localSpinner =  rootView.findViewById(R.id.local_category);
        themeSpinner =  rootView.findViewById(R.id.theme_category);

        // Spinner에 어댑터 설정
        localSpinner.setAdapter(localAdapter);
        themeSpinner.setAdapter(themeAdapter);

        bWrite = rootView.findViewById(R.id.WriteButton);

        // RecyclerView 초기화 및 레이아웃 매니저 설정
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // 레이아웃 매니저 설정

        // PostAdapter 생성 시 postList를 전달
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

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
        loadPostsFromDatabase();


        return rootView;
    }
    private void loadPostsFromDatabase() {
        List<Post> databasePosts = getPostsFromDatabase();
        postList.addAll(databasePosts);
        postAdapter.notifyDataSetChanged();
    }
    private List<Post> getPostsFromDatabase() {
        List<Post> postList = new ArrayList<>();

        try {
            // 서버에서 데이터를 가져오는 로직
            String serverUrl = "http://your-server-url/data-api"; // 실제 서버 URL로 변경
            URL url = new URL(serverUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertStreamToString(in);

                // JSON 파싱
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonPost = jsonArray.getJSONObject(i);

                    // Post 객체로 변환
                    String title = jsonPost.getString("title");
                    String content = jsonPost.getString("content");
                    String localCategory=jsonPost.getString("localCategory");
                    String themeCategory=jsonPost.getString("themeCategory");
                    String imageData=jsonPost.getString("imageData");

                    Post post = new Post(title, content,localCategory,themeCategory,imageData);
                    postList.add(post);
                }

            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return postList;
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