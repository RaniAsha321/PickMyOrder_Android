package com.pickmyorder.asharani.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.Adapters.Adapter_Shelves;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.ModelShelves;
import com.pickmyorder.asharani.Models.Shelf;
import com.pickmyorder.asharani.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dynamic_Catalogous extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;
    Home homes;
    Adapter_Shelves adapter_shelves;
    int spanCount = 3;
    int spacing = 25;
    boolean includeEdge = true;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    List<Shelf> shelvesdata;
    List<Shelf> mylist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.dynamic_catalog, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    return true;
                }

                homes.drawerLayout.openDrawer(Gravity.START);


                return false;
            }
        });

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sheleves");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        recyclerView=view.findViewById(R.id.recyclerview_shelves);
        homes.nav_search_layout.setVisibility(View.VISIBLE);

        mylist = new ArrayList<>();
        ((Home)getActivity()).hideView(true);

        getShelves();

        ((Home)getActivity()).hideView(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);



        return view;
    }

    private void getShelves() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String userid= Paper.book().read("userid");

        service.SHELVES_CALL(userid).enqueue(new Callback<ModelShelves>() {
            @Override
            public void onResponse(Call<ModelShelves> call, Response<ModelShelves> response) {

                if(response.body().getStatusCode().equals(200)) {

                    shelvesdata = response.body().getShelves();

                    for (int i = 0; i < shelvesdata.size(); i++) {

                        Shelf shelf = new Shelf();
                        shelf.setId(response.body().getShelves().get(i).getId());
                        shelf.setHeading(response.body().getShelves().get(i).getHeading());
                        shelf.setShelvesdata(response.body().getShelves().get(i).getShelvesdata());

                        mylist.add(shelf);
                    }

                    adapter_shelves=new Adapter_Shelves(getActivity(),mylist);

                    recyclerView.setAdapter(adapter_shelves);

                }
                else{

                    Toast.makeText(getActivity(),"Not Found",Toast.LENGTH_LONG).show();
                    FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr,new NoCatelogues());
                    fragmentTransaction.commit();

                }


            }

            @Override
            public void onFailure(Call<ModelShelves> call, Throwable t) {

                t.printStackTrace();
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("failure",t.getMessage()+"");

            }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity.getClass() == Home.class) {
            homes = (Home) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Shelves");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }


}
