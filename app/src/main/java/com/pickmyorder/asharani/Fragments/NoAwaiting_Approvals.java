package com.pickmyorder.asharani.Fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.R;

public class NoAwaiting_Approvals extends Fragment {

    Home home;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_no_awaiting__approvals, container, false);
        home.layout_banner.setVisibility(View.GONE);

        return view;
    }


    public void onAttach (Activity activity) {
        super.onAttach(activity);
        if(activity.getClass()== Home.class){
            home = (Home) activity;
        }
    }

}
