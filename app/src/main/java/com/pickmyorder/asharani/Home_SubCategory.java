package com.pickmyorder.asharani;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home_SubCategory extends Fragment {

    String cat="1";
    Context mcontext;
    List<SubCategory> subcategories;
    RecyclerView recyclerviewHomeCategories;
    Adapter_Home_Sub_Category home_categories;
    Adapter_Super_Category home_super_category;
    List<Model_Home_Sub_Category> list;
    List<Product> productlist;
    int spanCount = 2;
    int spacing = 25;
    private FirebaseAnalytics mFirebaseAnalytics;
    boolean includeEdge = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home__sub_category, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("home_category", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sub-Category");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        ((Home)getActivity()).hideView(false);

        recyclerviewHomeCategories = view.findViewById(R.id.recyclerview_home_sub_categories);

        list = new ArrayList<>();

        subcategories = (Paper.book().read("subcatlist", new ArrayList<SubCategory>()));

        Log.e("subcatlist",subcategories.size()+"");

        for (SubCategory subcategory1 : subcategories) {

            Model_Home_Sub_Category modelHomeCategories = new Model_Home_Sub_Category();
            modelHomeCategories.setSubcat(subcategory1.getSubCatId());
            modelHomeCategories.setSuperSubImage(subcategory1.getCatImage());
            modelHomeCategories.setSuperSubtext(subcategory1.getSubCatName());

            list.add(modelHomeCategories);
        }
        recyclerviewHomeCategories.addItemDecoration(new ItemOffsetDecoration(spanCount, spacing, includeEdge));

        recyclerviewHomeCategories.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        String isVan = Paper.book().read("isVan");

        getProductssubCat(isVan);
        return view;
    }
    private void getProductssubCat(String isVan) {

        String user_id=Paper.book().read("unique_id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String demo = Paper.book().read("tempcategoryid", "");
        String String_status = Paper.book().read("homestatus", "");

        Log.e("demo",demo+"");
        Log.e("demo1",isVan+"");

        int status= Integer.parseInt(String_status);
        if (status != 0) {

            service.PRODUCTS_SUB_CATEGORY_CALL(user_id,demo,isVan).enqueue(new Callback<ModelProductsSubCategory>() {

                @Override
                public void onResponse(Call<ModelProductsSubCategory> call, Response<ModelProductsSubCategory> response) {

                    assert response.body() != null;

                    if (response != null && response.isSuccessful() && response.body() != null) {

                        Log.e("statuser123","response_main_if");

                    if (response.body().getStatusCode().equals(200)) {

                        subcategories = (List<SubCategory>) response.body().getSubCategory();

                        home_categories = new Adapter_Home_Sub_Category(subcategories, mcontext);

                        recyclerviewHomeCategories.setAdapter(home_categories);

                    } else {

                        Toast.makeText(mcontext, "No Product Found", Toast.LENGTH_SHORT).show();
                    }

                }
                }

                @Override
                public void onFailure(Call<ModelProductsSubCategory> call, Throwable t) {

                    Log.e("statuser123","failure_main");
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }

        else {

            service.PRODUCT_DESCRIPTION_CALL(user_id,demo,cat,isVan).enqueue(new Callback<ModelDescription>() {
                @Override
                public void onResponse(Call<ModelDescription> call, Response<ModelDescription> response) {

                    assert response.body() != null;
                    if (response.body().getStatusCode().equals(200)) {

                        productlist = (List<Product>) response.body().getProducts();

                        Log.e("VanData",productlist.get(0).getVanStock()+"");

                        home_super_category = new Adapter_Super_Category(productlist, mcontext,response.body().getPublishkey());

                        recyclerviewHomeCategories.setAdapter(home_super_category);

                    } else {

                        Toast.makeText(mcontext, "No Product Found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ModelDescription> call, Throwable t) {

                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sub_Category");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }


}
