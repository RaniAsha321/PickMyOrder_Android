package com.pickmyorder.asharani.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.Adapters.Adapter_Searching;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.util.ItemOffsetDecoration;
import com.pickmyorder.asharani.Models.ModelSearching;
import com.pickmyorder.asharani.Models.SearchDatum;
import com.pickmyorder.asharani.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Search_items extends Fragment {

    Home home;
    RecyclerView recyclerView;
    Adapter_Searching adapter_searching;
    List<SearchDatum> mysearchlist;
    List<SearchDatum> mylist;
    int spanCount = 2;
    int spacing = 25;
    boolean includeEdge = true;
    String userid;
    Wholeseller wholeseller;
    private FirebaseAnalytics mFirebaseAnalytics;
    String search_selected_products,search_id,wholeseller_business_id ;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_items, container, false);

        recyclerView=view.findViewById(R.id.re_searching);
        wholeseller=new Wholeseller();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("cate", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Search");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        home.search_txtvw.setCursorVisible(false);
        userid=Paper.book().read("userid");
        home.layout_banner.setVisibility(View.GONE);

        search_selected_products=Paper.book().read("search");
        search_id=Paper.book().read("search_id");

        wholeseller_business_id=Paper.book().read("wholeseller_business_id");

        Log.e("search_id",search_id+"");
        Log.e("search_id1",userid+"");
        Log.e("search_id2",search_selected_products+"");
        Log.e("search_id3",wholeseller_business_id+"");


        if(search_id!=null){

            if(search_selected_products.equals("1")){

                Searching(wholeseller_business_id);
            }

            else if(search_selected_products.equals("3")){

                Searching(search_id);
            }
/*
            else if(search_selected_products.equals("2")){

                Searching(search_id);
            }*/

           else if(search_selected_products.equals("0")){

                Searching(userid);


            }

           else {

                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr, new Search_empty());
                fragmentTransaction.commit();

            }

        }

        else {

            Searching(userid);
        }



        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(spanCount, spacing, includeEdge));

        return view;
    }

    public void  Searching(String id) {

        mylist=new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);
        Paper.book().write("stringkey",home.search_txtvw.getText().toString().trim());


        String menu_Van_Stock=Paper.book().read("menu_Van_Stock");
        String menu_Brand=Paper.book().read("menu_Brand");

        String menu_Wholesaler=Paper.book().read("menu_Wholesaler");

       // Log.e("search_id4",menu_Van_Stock+"");
        Log.e("search_id41",home.search_txtvw.getText().toString().trim()+"");
        Log.e("search_id42",id+"");
        Log.e("search_id43",menu_Van_Stock+"");
        Log.e("search_id44",menu_Brand+"");
        Log.e("search_id45",menu_Wholesaler+"");


        if ((menu_Brand != null) && (menu_Brand.equals("1"))) {
            Log.e("search_id422","if");
            Paper.book().write("menu_Van_Stock","");

            service.SEARCHING_CALL_BRAND(home.search_txtvw.getText().toString().trim(),id).enqueue(new Callback<ModelSearching>() {
                @Override
                public void onResponse(Call<ModelSearching> call, Response<ModelSearching> response) {
                    Log.e("search_id422code",response.body().getStatusCode()+"");
                    assert response.body() != null;
                    if (response.body().getStatusCode().equals(200)) {

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, search_selected_products);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, search_id);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);


                        mysearchlist = response.body().getSearchData();
                        // Paper.book().write("stripe_publish_key",response.body().getPublishkey());
                        adapter_searching = new Adapter_Searching(getActivity(), mysearchlist,response.body().getPublishkey());
                        recyclerView.setAdapter(adapter_searching);
                    }

                    else {

                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr, new Search_empty());
                        fragmentTransaction.commit();

                    }
                }

                @Override
                public void onFailure(Call<ModelSearching> call, Throwable t) {

                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });


        }
          else  if ((menu_Van_Stock != null) && (menu_Van_Stock.equals("1"))){
            Log.e("search_id422","if else");
           // Paper.book().write("menu_Van_Stock","");
            Paper.book().write("menu_Brand","");

            service.SEARCHING_CALL_VAN(home.search_txtvw.getText().toString().trim(),id,menu_Van_Stock).enqueue(new Callback<ModelSearching>() {
                @Override
                public void onResponse(Call<ModelSearching> call, Response<ModelSearching> response) {

                    if (response.body().getStatusCode().equals(200)) {

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM,search_selected_products);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, search_id);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);

                        Log.e("search_id4","if");

                        mysearchlist = response.body().getSearchData();
                        // Paper.book().write("stripe_publish_key",response.body().getPublishkey());
                        adapter_searching = new Adapter_Searching(getActivity(), mysearchlist,response.body().getPublishkey());
                        recyclerView.setAdapter(adapter_searching);
                    }

                    else {
                        Log.e("search_id4","else");
                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr, new Search_empty());
                        fragmentTransaction.commit();

                    }
                }

                @Override
                public void onFailure(Call<ModelSearching> call, Throwable t) {

                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });


        }

        else if (menu_Wholesaler != null && menu_Wholesaler.equals("1") ){


            Log.e("search_id422","else");
            Paper.book().write("menu_Van_Stock","");
            Paper.book().write("menu_Brand","");

            service.SEARCHING_CALL_WHOLESALER(home.search_txtvw.getText().toString().trim(),id).enqueue(new Callback<ModelSearching>() {
                @Override
                public void onResponse(Call<ModelSearching> call, Response<ModelSearching> response) {

                    if (response.body().getStatusCode().equals(200)) {


                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, search_selected_products);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, search_id);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);


                        mysearchlist = response.body().getSearchData();
                        // Paper.book().write("stripe_publish_key",response.body().getPublishkey());
                        adapter_searching = new Adapter_Searching(getActivity(), mysearchlist,response.body().getPublishkey());
                        recyclerView.setAdapter(adapter_searching);
                    }

                    else {

                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr, new Search_empty());
                        fragmentTransaction.commit();

                    }
                }

                @Override
                public void onFailure(Call<ModelSearching> call, Throwable t) {

                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });


        }

        else {

            Log.e("search_id422","else");
            Paper.book().write("menu_Van_Stock","");
            Paper.book().write("menu_Brand","");

            service.SEARCHING_CALL(home.search_txtvw.getText().toString().trim(),id).enqueue(new Callback<ModelSearching>() {
                @Override
                public void onResponse(Call<ModelSearching> call, Response<ModelSearching> response) {

                    if (response.body().getStatusCode().equals(200)) {

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, search_selected_products);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, search_id);
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);


                        mysearchlist = response.body().getSearchData();
                        // Paper.book().write("stripe_publish_key",response.body().getPublishkey());
                        adapter_searching = new Adapter_Searching(getActivity(), mysearchlist,response.body().getPublishkey());
                        recyclerView.setAdapter(adapter_searching);
                    }

                    else {

                        FragmentTransaction fragmentTransaction;
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr, new Search_empty());
                        fragmentTransaction.commit();

                    }
                }

                @Override
                public void onFailure(Call<ModelSearching> call, Throwable t) {

                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }

    }

    public void onAttach (Activity activity) {
        super.onAttach(activity);
        if(activity.getClass()== Home.class){
            home = (Home) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Search");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }


}


