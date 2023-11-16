package com.example.projec7;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


public class CommunityFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button bWaterCar;
    Button bCommunity;
    private String mParam1;
    private String mParam2;



    public CommunityFragment() {
        // Required empty public constructor
    }

    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
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



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);



        bWaterCar=rootView.findViewById(R.id.water_car_button);
        bCommunity=rootView.findViewById(R.id.community_button);
        bWaterCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                WaterCarFragment waterCarFragment= new WaterCarFragment();
                transaction.replace(R.id.container,waterCarFragment);
                transaction.commit();

            }
        });
        bCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                CommunityScreenFragment communityScreenFragment=new CommunityScreenFragment();
                transaction.replace(R.id.container,communityScreenFragment);
                transaction.commit();

            }
        });
        return rootView;

    }

}