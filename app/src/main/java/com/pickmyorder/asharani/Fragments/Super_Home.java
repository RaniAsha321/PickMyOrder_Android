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
import com.pickmyorder.asharani.Adapters.Adapter_Super_Category;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.util.ItemOffsetDecoration;
import com.pickmyorder.asharani.Models.ModelDescription;
import com.pickmyorder.asharani.Models.Product;
import com.pickmyorder.asharani.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Super_Home extends Fragment {

    String cat="4";
    RecyclerView recyclerviewSuperCategories;
    Adapter_Super_Category home_categories;
    List<Product> list;
    List<Product> supersubcategories;
    Context mcontext;
    List<Product> modelProductsCategories;
    int spanCount = 2;
    int spacing = 25;
    boolean includeEdge = true;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super__home, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("m", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        recyclerviewSuperCategories = view.findViewById(R.id.recyclerview_super_home);

        Home.layout_banner.setVisibility(View.GONE);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Products");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);


        ((Home) getActivity()).hideView(false);

        list = new ArrayList<>();

        modelProductsCategories = Paper.book().read("products", new ArrayList<Product>());

        for (Product category : modelProductsCategories) {

            Product product = new Product();

            product.setProductId(category.getProductId());
            product.setProductName(category.getProductName());
            product.setPrice(category.getPrice());
            product.setImage(category.getImage());

            list.add(product);
        }

        recyclerviewSuperCategories.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerviewSuperCategories.addItemDecoration(new ItemOffsetDecoration(spanCount, spacing, includeEdge));
        String isVan = Paper.book().read("isVan");
        getProduct(isVan);

        return view;
    }

    private void getProduct(String isVan) {

        String user_id=Paper.book().read("unique_id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String productid=(Paper.book().read("tempSuperSubCategory",""));
        String isVan2 = Paper.book().read("isVan");
        Log.e("user1",user_id+"");
        Log.e("user2",productid+"");
        Log.e("user3",cat+"");
        Log.e("user4",isVan2+"");
        service.PRODUCT_DESCRIPTION_CALL(user_id,productid,cat,isVan2).enqueue(new Callback<ModelDescription>() {
            @Override
            public void onResponse(Call<ModelDescription> call, Response<ModelDescription> response) {

                if (response != null && response.isSuccessful() && response.body() != null) {

                    Log.e("statuser123","response_main_if");
                    Log.e("statuser1234",response.body().getStatusCode()+"");
                assert response.body() != null;
                if (response.body().getStatusCode().equals(200)) {

                    supersubcategories = (List<Product>) response.body().getProducts();

                  //  Paper.book().write("stripe_publish_key",response.body().getPublishkey());
                    Log.e("VanData",supersubcategories.get(0).getVanStock()+"");
                    home_categories = new Adapter_Super_Category(supersubcategories, mcontext,response.body().getPublishkey());

                    recyclerviewSuperCategories.setAdapter(home_categories);
                }

                else {

                    Toast.makeText(mcontext, "No Product Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelDescription> call, Throwable t) {

                Log.e("statuser123","failure_main");
                Toast.makeText(mcontext, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

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
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Products");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }


}
