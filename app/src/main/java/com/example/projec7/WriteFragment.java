package com.example.projec7;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WriteFragment extends Fragment {



    private Spinner localSpinner;
    private Spinner themeSpinner;
    private ArrayList<String> localList;
    private ArrayList<String> themeList;
    private ArrayAdapter<String> localAdapter;
    private ArrayAdapter<String> themeAdapter;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 2;
    private ImageView imageView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Button bCancel;
    private ImageButton bCamera;
    private Button bSubmit;
    private EditText contentEditText;
    private EditText titleEditText;
    private boolean dataAddedSuccessfully = false;
    private String mParam1;
    private String mParam2;




    public WriteFragment() {
        // Required empty public constructor
    }

    public static WriteFragment newInstance(String param1, String param2) {
        WriteFragment fragment = new WriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        View view = inflater.inflate(R.layout.fragment_write, container, false);
        bCamera = view.findViewById(R.id.CameraButton);
        bCancel = view.findViewById(R.id.cancelButton);
        bSubmit=view.findViewById(R.id.registerButton);
        contentEditText = view.findViewById(R.id.contentEditText);
        titleEditText=view.findViewById(R.id.titleText);
        imageView = new ImageView(getActivity());

        // Spinner 참조 얻기
        localSpinner = view.findViewById(R.id.localSpinner);
        themeSpinner = view.findViewById(R.id.themeSpinner);

        // Spinner에 어댑터 설정
        localSpinner.setAdapter(localAdapter);
        themeSpinner.setAdapter(themeAdapter);





        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                CommunityScreenFragment communityscreenFragment= new CommunityScreenFragment();
                transaction.replace(R.id.container,communityscreenFragment);
                transaction.commit();

            }

        });

        //사진 첨부하는 기능으로
        //사진 버튼을 누르면 dialog가 열리면서 카메라어플로 넘어갈건지 갤러리로 넘어갈건지 선택하는 창이 나옴

        bCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showOptionsDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 예외 처리 로직 추가
                }
            }
        });

        //등록 버튼을 누르면 게시글이 데이터베이스에 추가되도록 설정

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String title = titleEditText.getText().toString();
                        String content = contentEditText.getText().toString();
                        String localCategory = localSpinner.getSelectedItem().toString();
                        String themeCategory = themeSpinner.getSelectedItem().toString();
                        String imageData = convertImageToBase64();

                        User user = new User(title, content, localCategory, themeCategory, imageData);
                        ArrayList<User> users = new ArrayList<>();
                        users.add(user);
                        Query query = new Query("add", users);
                        send(query);
                        Log.d("WriteFragment", "데이터베이스에 데이터 저장 성공");

                        // bSubmit을 클릭하면 CommunityFragment로 이동
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 이동할 Fragment 객체 생성
                                CommunityScreenFragment communityScreenFragment = new CommunityScreenFragment();

                                // FragmentTransaction을 사용하여 Fragment 교체
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.container, communityScreenFragment);
                                transaction.commit();
                            }
                        });
                    }
                }).start();
            }
        });
        return view;

    }
    public void send(Query query) {
        int portNumber =5000;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket sock = new Socket("10.0.2.2", portNumber);
                    ObjectOutputStream outStream = new ObjectOutputStream(sock.getOutputStream());
                    outStream.writeObject(query);
                    outStream.flush();

                    ObjectInputStream inStream = new ObjectInputStream(sock.getInputStream());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getContext(), CommunityFragment.class);
                            try {
                                if (query.header.equals("add")) {
                                    String responseStr = "" + inStream.readObject();
                                    intent.putExtra("STRING", responseStr);
                                    startActivity(intent);
                                } else if (query.header.equals("checkAll")) {
                                    Query responseQuery = (Query) inStream.readObject();
                                    ArrayList<User> users = responseQuery.users;
                                    intent.putExtra("user", users);
                                    startActivity(intent);
                                }
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                                // 데이터베이스 작업 중 예외 발생
                                Log.e("WriteFragment", "데이터베이스 작업 중 오류 발생: " + e.getMessage());
                            } finally {
                                try {
                                    sock.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.e("WriteFragment", "소켓 닫기 중 오류 발생: " + e.getMessage());
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    // 소켓 연결 오류
                    Log.e("WriteFragment", "소켓 연결 중 오류 발생: " + e.getMessage());
                }
            }
        }).start();
    }



    private void showOptionsDialog() {
        String[] options = {"카메라", "갤러리"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("사진 가져오기")
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            openCamera();
                        } else if (which == 1) {
                            openGallery();
                        }
                    }
                });

        builder.create().show();
    }
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    insertImageIntoBody(imageBitmap);
                }
            } else if (requestCode == REQUEST_GALLERY_IMAGE) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    insertImageIntoBody(selectedImage);
                }
            }
        }
    }
    private void insertImageIntoBody(Bitmap imageBitmap) {
        if (imageView != null) {
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageBitmap(imageBitmap);

            LinearLayout linearLayout = getView().findViewById(R.id.PhotoZone);
            linearLayout.removeAllViews();
            linearLayout.addView(imageView);
        } else {
            Log.e("WriteFragment", "ImageView is null in insertImageIntoBody");
        }
    }
    private void insertImageIntoBody(Uri selectedImage) {
        // Uri를 Bitmap으로 변환 (이 부분을 추가해야 함)
        Bitmap imageBitmap = getBitmapFromUri(selectedImage);

        if (imageView != null) {
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageBitmap(imageBitmap);

            LinearLayout linearLayout = getView().findViewById(R.id.PhotoZone);
            linearLayout.removeAllViews();
            linearLayout.addView(imageView);
        } else {
            Log.e("WriteFragment", "ImageView is null in insertImageIntoBody");
        }
    }


    // Uri를 Bitmap으로 변환하는 메서드
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // 이미지를 Base64 문자열로 변환하는 메서드
    private String convertImageToBase64() {
        if (imageView != null && imageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        return null;
    }
}