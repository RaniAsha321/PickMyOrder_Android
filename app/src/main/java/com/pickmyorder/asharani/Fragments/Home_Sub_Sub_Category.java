package com.pickmyorder.asharani.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.Adapters.Adapter_Home_Sub_Sub_Category;
import com.pickmyorder.asharani.Adapters.Adapter_Super_Category;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.util.ItemOffsetDecoration;
import com.pickmyorder.asharani.Models.ModelDescription;
import com.pickmyorder.asharani.Models.ModelProductsSubSubCategory;
import com.pickmyorder.asharani.Models.Model_Home_Super_sub_Category;
import com.pickmyorder.asharani.Models.Product;
import com.pickmyorder.asharani.Models.SuperSubCategory;
import com.pickmyorder.asharani.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home_Sub_Sub_Category extends Fragment {

    String cat="2";
    Adapter_Super_Category home_super_category;
    List<Product> productlist;
    Context mcontext;
    RecyclerView recyclerviewHomeCategories;
    Adapter_Home_Sub_Sub_Category home_categories;
    List<Model_Home_Super_sub_Category> list;
    int spanCount = 2;
    int spacing = 25;
    private FirebaseAnalytics mFirebaseAnalytics;
    boolean includeEdge = true;
    List<SuperSubCategory> supersubcategories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home__sub__sub__category, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("l", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        Home.layout_banner.setVisibility(View.GONE);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sub-Sub-Category");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        recyclerviewHomeCategories=view.findViewById(R.id.recyclerview_home_sub_sub_categories);

        ((Home)getActivity()).hideView(false);

        list=new ArrayList<>();

        supersubcategories = Paper.book().read("superlist", new ArrayList<SuperSubCategory>());

        for (SuperSubCategory category : supersubcategories) {

            Model_Home_Super_sub_Category modelHomeSuperSubCategories = new Model_Home_Super_sub_Category();
            modelHomeSuperSubCategories.setSubcat(category.getSuperSubCatId());
            modelHomeSuperSubCategories.setSupertext(category.getSuperSubCatName());
            modelHomeSuperSubCategories.setSuperImage(category.getImage());
            list.add(modelHomeSuperSubCategories);
        }

        recyclerviewHomeCategories.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false));
        recyclerviewHomeCategories.addItemDecoration(new ItemOffsetDecoration(spanCount, spacing, includeEdge));

        String isVan = Paper.book().read("isVan");

        getProductSuperCat(isVan);

        return view;
    }

    private void getProductSuperCat(String isVan) {

        String user_id=Paper.book().read("unique_id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String supercatid = Paper.book().read("tempsubcategoryid", "");
        String String_status = Paper.book().read("substatus", "");

        int status = Integer.parseInt(String_status);

        Log.e("statuser",status+"");

        if (status != 0) {
            service.PRODUCTS_SUB_SUB_CATEGORY_CALL(user_id,supercatid,isVan).enqueue(new Callback<ModelProductsSubSubCategory>() {
                @Override
                public void onResponse(Call<ModelProductsSubSubCategory> call, Response<ModelProductsSubSubCategory> response) {
                    Log.e("statuser22","response");
                    assert response.body() != null;
                    if (response.body().getStatusCode().equals(200)) {

                        supersubcategories = (List<SuperSubCategory>) response.body().getSuperSubCategory();

                        home_categories = new Adapter_Home_Sub_Sub_Category(supersubcategories, mcontext);

                        recyclerviewHomeCategories.setAdapter(home_categories);
                    } else {

                        Toast.makeText(mcontext, "No Product Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelProductsSubSubCategory> call, Throwable t) {
                    Log.e("statuser22","failure");
                    Toast.makeText(mcontext, "No Product Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }

        else {

            service.PRODUCT_DESCRIPTION_CALL(user_id,supercatid,cat,isVan).enqueue(new Callback<ModelDescription>() {
                @Override
                public void onResponse(Call<ModelDescription> call, Response<ModelDescription> response) {

                    assert response.body() != null;
                    if (response.body().getStatusCode().equals(200)) {

                        productlist = (List<Product>) response.body().getProducts();

                        home_super_category = new Adapter_Super_Category(productlist, mcontext,response.body().getPublishkey());

                        recyclerviewHomeCategories.setAdapter(home_super_category);

                    } else {

                        Toast.makeText(mcontext, "No Product Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelDescription> call, Throwable t) {

                    Toast.makeText(getActivity(), t.getMessage()
                            , Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }

    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        mcontext=c;
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sub_Sub_Category");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }



}
