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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pickmyorder.asharani.Adapters.Adapter_new_promotions;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.Catalogdatum;
import com.pickmyorder.asharani.Models.ModelGetSections;
import com.pickmyorder.asharani.Models.Shelvesdatum;
import com.pickmyorder.asharani.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragments_GetShelves extends Fragment {

    Home homes;
    Adapter_new_promotions adapter_home_categories;
    int spanCount = 3;
    int spacing = 25;
    boolean includeEdge = true;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView_shelves;
    List<Shelvesdatum> shelvesdata;
    List<Catalogdatum> mylist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.rowlayout_home_categories, container, false);

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

        recyclerView_shelves=view.findViewById(R.id.recyclerview_home_categories);
        homes.nav_search_layout.setVisibility(View.VISIBLE);

        mylist = new ArrayList<>();
        ((Home)getActivity()).hideView(true);

        getShelves();

       // layoutManager = new LinearLayoutManager(getActivity());
       // recyclerView_shelves.setLayoutManager(layoutManager);


        return view;
    }

    private void getShelves() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String Shelve_id= Paper.book().read("Shelve_id");


        service.GETSHELVES(Shelve_id).enqueue(new Callback<ModelGetSections>() {
            @Override
            public void onResponse(Call<ModelGetSections> call, Response<ModelGetSections> response) {

                if(response.body().getStatusCode().equals(200)) {

                    shelvesdata = response.body().getShelvesdata();

                    for (int i = 0; i < shelvesdata.size(); i++) {

                        Catalogdatum datum1=new Catalogdatum();
                        datum1.setId(shelvesdata.get(i).getId());
                        datum1.setSectionName(shelvesdata.get(i).getHeading());
                        datum1.setCoverImage(shelvesdata.get(i).getSectionImage());
                       // datum1.setCatalog(shelvesdata.get(i).getShelves());

                        mylist.add(datum1);
                    }


                   /* adapter_new_promotions=new Adapter_new_promotions(getActivity(),mylist);

                    recyclerview_promotions.addItemDecoration(new ItemOffsetDecoration(spanCount, spacing, includeEdge));

                    recyclerview_promotions.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));

                    recyclerview_promotions.setAdapter(adapter_new_promotions);*/


                }

                else{

                    Toast.makeText(getActivity(),"Not Found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ModelGetSections> call, Throwable t) {

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
}
