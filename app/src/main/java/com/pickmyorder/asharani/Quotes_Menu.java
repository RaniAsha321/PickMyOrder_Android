package com.pickmyorder.asharani;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import io.paperdb.Paper;

public class Quotes_Menu extends Fragment {

    Adapter_Order_Awaiting dataa;
    Home home;
    TextView txt_Orders, txt_awaiting;
    static TextView approve_size;
    LinearLayout layout_awaiting_orders, layout_all_orders, layout_order_btn;
    List<AwatingOrderDatum> dataorderlist;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quote__menu, container, false);

        txt_Orders = view.findViewById(R.id.my_order);
        txt_awaiting = view.findViewById(R.id.awaiting);
        layout_all_orders = view.findViewById(R.id.layout_all_orders);
        layout_awaiting_orders = view.findViewById(R.id.layout_awaiting_orders);
        // approve_size = view.findViewById(R.id.ap_size);
        layout_order_btn = view.findViewById(R.id.order_btn_layout);

        txt_Orders.setBackgroundResource(R.color.bluetheme);
        txt_Orders.setTextColor(getResources().getColor(R.color.white));
        txt_awaiting.setBackgroundResource(R.color.shadegrey);
        txt_awaiting.setTextColor(getResources().getColor(R.color.themeblack));

        home.nav_search_layout.setVisibility(View.VISIBLE);

        if (Paper.book().read("datarole", "5").equals("4")) {

            if (Paper.book().read("permission_wholeseller", "5").equals("1")) {

                layout_all_orders.setVisibility(View.VISIBLE);
                layout_awaiting_orders.setVisibility(View.GONE);
                layout_order_btn.setVisibility(View.VISIBLE);
            }
        }

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Quotes");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.orders_container, new Quotes());
        transaction.commit();


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("category", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }

                home.drawerLayout.openDrawer(Gravity.START);
                return false;
            }
        });

        ((Home) getActivity()).hideView(true);


        txt_Orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_Orders.setBackgroundResource(R.color.bluetheme);
                txt_Orders.setTextColor(getResources().getColor(R.color.white));
                txt_awaiting.setBackgroundResource(R.color.shadegrey);
                txt_awaiting.setTextColor(getResources().getColor(R.color.themeblack));

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.orders_container, new Quotes());
                transaction.commit();
            }
        });

        txt_awaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_awaiting.setBackgroundResource(R.color.bluetheme);
                txt_awaiting.setTextColor(getResources().getColor(R.color.white));
                txt_Orders.setBackgroundResource(R.color.shadegrey);
                txt_Orders.setTextColor(getResources().getColor(R.color.themeblack));

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.orders_container, new My_Orders_Quotes());
                transaction.commit();
            }
        });

            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.setMargins(20, 0, 20, 0);
                params.gravity=Gravity.CENTER;
                params.height = 120;
                layout_all_orders.setLayoutParams(params);
                layout_order_btn.setVisibility(View.VISIBLE);
                layout_all_orders.setVisibility(View.VISIBLE);
                layout_awaiting_orders.setVisibility(View.GONE);
            }
                return view;
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
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Quotes");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

}

