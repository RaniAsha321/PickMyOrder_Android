package com.pickmyorder.asharani.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.Adapters.Adapter_Home_Categories;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.util.ItemOffsetDecoration;
import com.pickmyorder.asharani.Models.Category;
import com.pickmyorder.asharani.Models.Model_Home_Categories;
import com.pickmyorder.asharani.R;

import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;


public class Home_Categories extends Fragment {

    Home home;
    private FirebaseAnalytics mFirebaseAnalytics;
    int i;
    Intent intent;
    List<Model_Home_Categories> mlist;
    ArrayList<Category> categories;
    Context context;
    RecyclerView recyclerviewHomeCategories;
    Adapter_Home_Categories home_categories;
    int spanCount = 2;
    int spacing = 25;
    boolean includeEdge = true;
    Search_items search_items;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home__categories, container, false);
        search_items=new Search_items();

        String menu_Brand = Paper.book().read("menu_Brand");

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("hoome", FragmentManager.POP_BACK_STACK_INCLUSIVE);

/*
                    if (menu_Brand != null && menu_Brand.equals("1")){
                        getFragmentManager().popBackStack("brands_category", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    else{
                        getFragmentManager().popBackStack("hoome", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }

                    */
                    return true;
                }

                home.drawerLayout.openDrawer(Gravity.START);

                return false;

            }
        });

        String menu_Van_Stock = Paper.book().read("menu_Van_Stock");

        if (menu_Van_Stock != null  && menu_Van_Stock.equals("1")){
            Home.layout_banner.setVisibility(View.GONE);
        }
        else {
            Home.layout_banner.setVisibility(View.VISIBLE);
        }


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Category");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        home.nav_search_layout.setVisibility(View.VISIBLE);
        home.search_txtvw.setHint("Search Products");
       // home.layout_banner.setVisibility(View.VISIBLE);

        String tab_type = Paper.book().read("tab_type");

        if (tab_type != null && !tab_type.equals("4")){
            Paper.book().write("tab_type","1");
        }


        recyclerviewHomeCategories = view.findViewById(R.id.recyclerview_home_categories);

        mlist = new ArrayList<>();

        ((Home)getActivity()).hideView(false);

        categories = Paper.book().read("categorylist", new ArrayList<Category>());

        for (Category category : categories) {

            Model_Home_Categories modelHomeCategories = new Model_Home_Categories();
            modelHomeCategories.setCat_id(category.getCatId());
            modelHomeCategories.setProduct_img(category.getCatImage());
            modelHomeCategories.setProduct_text(category.getCatName());
            modelHomeCategories.setStatus(category.getStatus());
            mlist.add(modelHomeCategories);
        }

        Paper.book().write("home_key",mlist);

        home_categories = new Adapter_Home_Categories(mlist, getActivity());

        recyclerviewHomeCategories.addItemDecoration(new ItemOffsetDecoration(spanCount, spacing, includeEdge));

        recyclerviewHomeCategories.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        recyclerviewHomeCategories.setAdapter(home_categories);

        return view;
    }


    public void setKeyListenerOnView(View v){

        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK :
                }

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setKeyListenerOnView(getView());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    public void onAttach (Activity activity) {
        super.onAttach(activity);
        if(activity.getClass()== Home.class){
            home = (Home) activity;
        }
    }

}

