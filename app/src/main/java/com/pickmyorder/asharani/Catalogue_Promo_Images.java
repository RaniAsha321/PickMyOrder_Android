package com.pickmyorder.asharani;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Catalogue_Promo_Images extends Fragment {

    Home homes;
    RecyclerView recyclerView_cat_promo;
    Adapter_Cat_Promo adapter_catalogues;
    List<Cetalouge> my_list;
    ImageView img_main_cat_promo;
    Catalogue_Promo_Images catalogue_promo_images;
    TextView txt_desc_catalog;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.catalogue_promo_image_screen, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("catalogs", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    return true;
                }

              //  homes.drawerLayout.openDrawer(Gravity.START);

                return false;
            }
        });

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Catalogs_Images");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);


        recyclerView_cat_promo = view.findViewById(R.id.recyclervw_cat_promo_img);
        img_main_cat_promo = view.findViewById(R.id.img_main_cat_promo);

        homes.nav_search_layout.setVisibility(View.GONE);

        my_list = (Paper.book().read("CatImageList", new ArrayList<Cetalouge>()));

       // Glide.with(getActivity()).load(my_list.get(0)).into(img_main_cat_promo);

        adapter_catalogues=new Adapter_Cat_Promo(getActivity(),my_list,Catalogue_Promo_Images.this,img_main_cat_promo);
        recyclerView_cat_promo.setLayoutManager(new LinearLayoutManager(getActivity(),  RecyclerView.HORIZONTAL, false));
        recyclerView_cat_promo.setAdapter(adapter_catalogues);

        return view;
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
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Catalogs_Images");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

    }

}
