package com.pickmyorder.asharani.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
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

import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.ModelQuote;
import com.pickmyorder.asharani.Models.QuotesDatum;
import com.pickmyorder.asharani.Adapters.Quote_Adapter;
import com.pickmyorder.asharani.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Quotes extends Fragment {

    List<QuotesDatum> dataorderlist;
    List<QuotesDatum> myorderlist;
    Quote_Adapter myadapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Home home;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quotes, container, false);


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    return true;
                }

                home.drawerLayout.openDrawer(Gravity.LEFT);

                return false;
            }
        });

        recyclerView=view.findViewById(R.id.recyclerview_quotes);
        home.search_layout.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        myorderlist=new ArrayList<>();

        OrderMenu();
        return view;
    }

    private void OrderMenu() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String userid= Paper.book().read("userid");

        final ProgressDialog progressDialogs = new ProgressDialog(getActivity(),R.style.AlertDialogCustom);
        progressDialogs.setCancelable(false);
        progressDialogs.setMessage("Please Wait.......");
        progressDialogs.show();


        service.GET_QUOTE(userid).enqueue(new Callback<ModelQuote>() {

            @Override
            public void onResponse(Call<ModelQuote> call, Response<ModelQuote> response) {

                if (response.body().getStatusCode().equals(200)){

                    dataorderlist=response.body().getQuotesData();

                    for (int i=0;i<dataorderlist.size();i++){

                        QuotesDatum orderDatum=new QuotesDatum();

                        orderDatum.setPoReffrence(dataorderlist.get(i).getPoReffrence());
                        orderDatum.setOrderId(dataorderlist.get(i).getOrderId());
                        orderDatum.setDate(dataorderlist.get(i).getDate());
                        orderDatum.setTotalIncVat(dataorderlist.get(i).getTotalIncVat());
                        orderDatum.setTotalExVat(dataorderlist.get(i).getTotalExVat());
                        orderDatum.setStatus(dataorderlist.get(i).getStatus());
                        orderDatum.setOrderDescrption(dataorderlist.get(i).getOrderDescrption());
                        orderDatum.setProject(dataorderlist.get(i).getProject());
                        orderDatum.setOrderStatus(dataorderlist.get(i).getOrderStatus());

                        myorderlist.add(orderDatum);

                        Paper.book().write("newReference",dataorderlist.get(i).getPoReffrence());
                        Paper.book().write("dateeed",dataorderlist.get(i).getDate());

                    }

                    myadapter=new Quote_Adapter(getActivity(),myorderlist);
                    recyclerView.setAdapter(myadapter);
                }

                else {

                    Toast.makeText(getActivity(),"You don't have any orders",Toast.LENGTH_SHORT).show();

                }

                progressDialogs.dismiss();
            }

            @Override
            public void onFailure(Call<ModelQuote> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressDialogs.dismiss();

            }
        });
    }

    public void onAttach (Activity activity) {
        super.onAttach(activity);
        if(activity.getClass()== Home.class){
            home = (Home) activity;
        }
    }

}
