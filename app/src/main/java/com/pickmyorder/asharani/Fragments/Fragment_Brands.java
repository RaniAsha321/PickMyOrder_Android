package com.pickmyorder.asharani.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Adapters.Adapter_Brands;
import com.pickmyorder.asharani.Adapters.Adapter_wholesellers;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Models.Brand;
import com.pickmyorder.asharani.Models.CityList;
import com.pickmyorder.asharani.Models.ModelBrands;
import com.pickmyorder.asharani.Models.Wholselear;
import com.pickmyorder.asharani.R;
import com.pickmyorder.asharani.util.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_Brands extends Fragment {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    RecyclerView recyclerview_brands;
    int spanCount = 2;
    int spacing = 25;
    boolean includeEdge = true;
    List<Brand> brands_list;
    List<Brand> my_list;
    Adapter_Brands adapter_brands;
    private FirebaseAnalytics mFirebaseAnalytics;
    Home home;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home__categories, container, false);

        recyclerview_brands = view.findViewById(R.id.recyclerview_home_categories);
        ((Home) getActivity()).hideView(false);
        my_list = new ArrayList<>();

        home.search_txtvw.setHint("Search Brands");

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("hoome", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }

                home.drawerLayout.openDrawer(Gravity.LEFT);

                return false;

            }

        });


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Brands");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

       // home.search_txtvw.setText("Search Brands");
        getBrands();


        TextWatcher edittw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());

            }
        };
        home.search_txtvw.addTextChangedListener(edittw);

        return view;
    }

    private void filter(String text) {

        //new array list that will hold the filtered data
        ArrayList<Brand> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Brand brands : my_list) {
            //if the existing elements contains the search input
            if (brands.getBusinessName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(brands);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter_brands.filterList(filterdNames);

    }


    private void getBrands() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        service.BRANDS_CALL().enqueue(new Callback<ModelBrands>() {
            @Override
            public void onResponse(Call<ModelBrands> call, Response<ModelBrands> response) {


                assert response.body() != null;
                if(response.body().getStatusCode().equals(200)){

                    brands_list=response.body().getBrands();

                    for (int i=0;i<brands_list.size();i++) {

                        Brand brand = new Brand();
                        brand.setId(brands_list.get(i).getId());
                        brand.setBusinessName(brands_list.get(i).getBusinessName());
                        brand.setBussiness_logo(brands_list.get(i).getBussiness_logo());

                        my_list.add(brand);
                    }

                    recyclerview_brands.addItemDecoration(new ItemOffsetDecoration(spanCount, spacing, includeEdge));

                    recyclerview_brands.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

                    adapter_brands = new Adapter_Brands(my_list, getActivity());
                    recyclerview_brands.setAdapter(adapter_brands);

                }

                else {

                    Toast.makeText(getActivity(), "No Brands Found", Toast.LENGTH_SHORT).show();

                   /* FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr,new NoWholeseller());
                    fragmentTransaction.commit();*/
                }

            }

            @Override
            public void onFailure(Call<ModelBrands> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

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
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Brands");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }


}
