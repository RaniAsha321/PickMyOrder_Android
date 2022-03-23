package com.pickmyorder.asharani.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pickmyorder.asharani.Adapters.Adapter_Quote_Supplier;
import com.pickmyorder.asharani.Adapters.Adapter_cart_next;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Storage.DatabaseChangedReceiver;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.ModelBuyNow;
import com.pickmyorder.asharani.Models.ModelCartMenu;
import com.pickmyorder.asharani.Models.ModelGetQuote;
import com.pickmyorder.asharani.Models.ModelProjects;
import com.pickmyorder.asharani.Models.ModelSupplier;
import com.pickmyorder.asharani.Models.SupplierDatum;
import com.pickmyorder.asharani.Models.ProjectDatum;
import com.pickmyorder.asharani.R;
import com.pickmyorder.asharani.Storage.databaseSqlite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Cart extends Fragment {

    LinearLayout cartlayout,drop_project,drop_supplier,layout_close,layout_close_bottom,layout_quote,layout_col_del;
    List<String> variation_drop_project,variation_drop_supplier,my_quote,list_del_col;
    PopupWindow popUp;
    TextView sub_total,vat,total,tv_drop,tv_drop_project,txtvw_quote,txtvw_col_del;
    public static TextView tv_drop_supplier;
    EditText order_desc;
    List<SupplierDatum> supplierData, supplierDataQuote;
    List<SupplierDatum> supplierDatumList;
    Spinner spinner_supplier;
    RecyclerView recyclerView;
    List<ProjectDatum> mylist;
    List<ProjectDatum> projectlist;
    Spinner spinner;
    Adapter_cart_next adapter_cart_next;
    databaseSqlite databaseHelper;
    Button Proceed;
    FragmentTransaction transaction;
    List<ModelCartMenu> cartMenuList;
    LinearLayoutManager layoutManager;
    List<ProjectDatum> list;
    ListView my_list;
    String pop_cancel,devicetoken,vanstock,quotes,menu_Van_Stock,menu_Cat,menu_Wholesaler,Cart_Type;
    JSONArray jsonArray;
    Adapter_Quote_Supplier adapter_update_project;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView imgvw_arrow_quote;
    private FirebaseAnalytics mFirebaseAnalytics;

    private int minHour = 7;
    private int minMinute = 0;

    private int maxHour = 17;
    private int maxMinute = 61;

    private int currentHour = 0;
    private int currentMinute = 0;
    Context contextd;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("cart", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        projectlist=new ArrayList<>();

        databaseHelper=new databaseSqlite(getActivity());
        cartMenuList=databaseHelper.getAllModelCartMenu();
        recyclerView=view.findViewById(R.id.cart_recycle);
        order_desc=view.findViewById(R.id.edtxt_order_desc);
        spinner=view.findViewById(R.id.spinner_projects);
        spinner_supplier=view.findViewById(R.id.spinner_supplier);
        Proceed=view.findViewById(R.id.btn_proceed);
        sub_total=view.findViewById(R.id.cart_sub_total);
        vat=view.findViewById(R.id.cart_vat);
        total=view.findViewById(R.id.cart_total);
        cartlayout=view.findViewById(R.id.cart_layout);
        layout_quote=view.findViewById(R.id.layout_quote);
        drop_project=view.findViewById(R.id.drop_project);
        drop_supplier=view.findViewById(R.id.drop_supplier);
        tv_drop=view.findViewById(R.id.tv_drop);
        tv_drop_project=view.findViewById(R.id.tv_drop_project);
        tv_drop_supplier=view.findViewById(R.id.tv_drop_supplier);
        txtvw_quote = view.findViewById(R.id.txtvw_quote);
        layout_col_del = view.findViewById(R.id.layout_col_del);
        txtvw_col_del = view.findViewById(R.id.txtvw_col_del);
        imgvw_arrow_quote = view.findViewById(R.id.imgvw_arrow_quote);

        order_desc.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        jsonArray = new JSONArray();
        supplierData=new ArrayList<>();
        supplierDataQuote=new ArrayList<>();
        list= new ArrayList<>();
        mylist= new ArrayList<>();
        variation_drop_project=new ArrayList<>();
        variation_drop_supplier=new ArrayList<>();

        vanstock = Paper.book().read("vanstock");
        quotes = Paper.book().read("permission_Quote");
        my_quote = new ArrayList<>();
        list_del_col = new ArrayList<>();
       // my_quote.add("Van Stock");

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Cart");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        menu_Van_Stock = Paper.book().read("menu_Van_Stock");
        menu_Cat = Paper.book().read("menu_Cat");
        menu_Wholesaler = Paper.book().read("menu_Wholesaler");
        Cart_Type = Paper.book().read("Cart_Type");

        Log.e("menu_status1",menu_Van_Stock+"");
        Log.e("menu_status2",menu_Cat+"");
        Log.e("menu_status3",menu_Wholesaler+"");
        Log.e("menu_status4",quotes+"");
        Log.e("menu_status5",Paper.book().read("whole_seller")+"");
        Log.e("menu_status4",Cart_Type+"");


        if(quotes != null && quotes.equals("0")){
            Log.e("Condition","else if");

            imgvw_arrow_quote.setVisibility(View.GONE);
            layout_col_del.setVisibility(View.GONE);
            txtvw_quote.setText("Order");

        }




        ((Home)getActivity()).hideView(true);
        getsupplier();
        getProjects();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        spinner.setPrompt("Select Project");
        spinner_supplier.setPrompt("Select Supplier");


        layout_col_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select_col_del();

            }
        });


        layout_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String menu_Van_Stock=Paper.book().read("menu_Van_Stock");

                if ((quotes != null && quotes.equals("1")) || ((vanstock != null && vanstock.equals("1")))) {

                    if (txtvw_quote.getText().toString().equals("Order") || txtvw_quote.getText().toString().equals("Quote")) {

                        Quotes_invoice();
                    }
                }

            }
        });

        drop_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dropdownmenu_project();
            }
        });

        String business_id=Paper.book().read("business_id");
        String whichbusiness=Paper.book().read("whichbusiness");
        String business=Paper.book().read("save_business");
        String pop_cancel_up=Paper.book().read("pop_cancel_up");

        Log.e("tiger3",business+"");

        if(pop_cancel_up .equals("1")){

            pop_cancel=Paper.book().read("pop_cancel_up");
        }
        else {
            pop_cancel="0";
        }

            if(Paper.book().read("permission_wholeseller", "5").equals("1")){

                Log.e("tiger","1");
               // drop_supplier.setVisibility(View.GONE);

                Paper.book().write("supplier_id","0");

                Proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Paper.book().write("order_desc", order_desc.getText().toString().trim());

                        if (!txtvw_quote.getText().equals("Select Quote / Order")) {

                        if (!tv_drop_project.getText().equals("Select Project")) {

                            if (!TextUtils.isEmpty(order_desc.getText().toString().trim())) {

                                if (Proceed.getText().toString().equals("Proceed to Summary")) {

                                    transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("li").replace(R.id.containerr, new Cart_Summary());
                                    transaction.commit();
                                }

                                else  {
                                    jsonarray();
                                }


                            } else {

                                Toast.makeText(getActivity(), "Enter Order Description", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(getActivity(), "Select Project", Toast.LENGTH_SHORT).show();
                        }

                    }


                        else {

                            Toast.makeText(getActivity(), "Select Quote / Order", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

            else if(business !=null) {

                if(!(business.equals("0"))){


                    if (!(whichbusiness.equals(business_id))) {

                        Log.e("tigerqueen", business_id+"");

                        Log.e("tigerqueen", whichbusiness+"");

                        drop_supplier.setVisibility(View.GONE);

                        Paper.book().write("supplier_id", "0");

                        Proceed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Paper.book().write("order_desc", order_desc.getText().toString().trim());

                                if (!txtvw_quote.getText().equals("Select Quote / Order")) {

                                if (!tv_drop_project.getText().equals("Select Project")) {

                                    if (!TextUtils.isEmpty(order_desc.getText().toString().trim())) {


                                        if (Proceed.getText().toString().equals("Proceed to Summary")) {

                                            transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("li").replace(R.id.containerr, new Cart_Summary());
                                            transaction.commit();

                                        }

                                        else  {
                                            jsonarray();
                                        }

                                    } else {

                                        Toast.makeText(getActivity(), "Enter Order Description", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                    Toast.makeText(getActivity(), "Select Project", Toast.LENGTH_SHORT).show();
                                }

                                }

                                else {

                                    Toast.makeText(getActivity(), "Select Quote / Order", Toast.LENGTH_SHORT).show();
                                }

                            }



                        });




                    }

                    else {

                        Log.e("tiger", "565");
                        drop_supplier.setVisibility(View.VISIBLE);
                        Proceed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Paper.book().write("order_desc", order_desc.getText().toString().trim());

                                if (!txtvw_quote.getText().equals("Select Quote / Order")) {

                                if (!tv_drop_project.getText().equals("Select Project")) {

                                    if (!TextUtils.isEmpty(order_desc.getText().toString().trim())) {

                                        if (!tv_drop_supplier.getText().equals("Select Supplier")) {

                                            if (Proceed.getText().toString().equals("Proceed to Summary")) {

                                                transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("li").replace(R.id.containerr, new Cart_Summary());
                                                transaction.commit();

                                            }

                                            else  {
                                                jsonarray();
                                            }

                                        } else {

                                            Toast.makeText(getActivity(), "Select Supplier", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {

                                        Toast.makeText(getActivity(), "Enter Order Description", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                    Toast.makeText(getActivity(), "Select Project", Toast.LENGTH_SHORT).show();
                                }

                            }


                        else {

                                Toast.makeText(getActivity(), "Select Quote / Order", Toast.LENGTH_SHORT).show();
                            }

                            }
                        });


                    }

                }

                else {

                    drop_supplier.setVisibility(View.VISIBLE);
                    Proceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Paper.book().write("order_desc",order_desc.getText().toString().trim());

                            if (!txtvw_quote.getText().equals("Select Quote / Order")) {

                            if(!tv_drop_project.getText().equals("Select Project")){

                                if (!TextUtils.isEmpty(order_desc.getText().toString().trim())) {

                                    if(!tv_drop_supplier.getText().equals("Select Supplier")){

                                        if (Proceed.getText().toString().equals("Proceed to Summary")) {

                                            transaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("li").replace(R.id.containerr, new Cart_Summary());
                                            transaction.commit();

                                        }

                                        else  {
                                            jsonarray();
                                        }
                                    }

                                    else {

                                        Toast.makeText(getActivity(), "Select Supplier", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                else {

                                    Toast.makeText(getActivity(), "Enter Order Description", Toast.LENGTH_SHORT).show();
                                }
                            }

                            else {

                                Toast.makeText(getActivity(), "Select Project", Toast.LENGTH_SHORT).show();
                            }

                            }

                            else {

                                Toast.makeText(getActivity(), "Select Quote / Order", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }



        else{

                Log.e("tiger","55");
                drop_supplier.setVisibility(View.VISIBLE);
            Proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Paper.book().write("order_desc",order_desc.getText().toString().trim());

                    if (!txtvw_quote.getText().equals("Select Quote / Order")) {

                    if(!tv_drop_project.getText().equals("Select Project")){

                        if (!TextUtils.isEmpty(order_desc.getText().toString().trim())) {

                            if(!tv_drop_supplier.getText().equals("Select Supplier")){


                                if (Proceed.getText().toString().equals("Proceed to Summary")) {

                                    transaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("li").replace(R.id.containerr, new Cart_Summary());
                                    transaction.commit();

                                }

                                else  {
                                    jsonarray();
                                }
                            }

                            else {

                                Toast.makeText(getActivity(), "Select Supplier", Toast.LENGTH_SHORT).show();
                            }

                        }

                        else {

                            Toast.makeText(getActivity(), "Enter Order Description", Toast.LENGTH_SHORT).show();
                        }

                    }

                    else {

                        Toast.makeText(getActivity(), "Select Project", Toast.LENGTH_SHORT).show();
                    }

                }

                    else  if (Proceed.getText().toString().equals("Send Quotation")) {


                        jsonarray();
                    }

               else {

                    Toast.makeText(getActivity(), "Select Quote / Order", Toast.LENGTH_SHORT).show();
                }

                }
            });
        }

        String Whole_Seller_product=Paper.book().read("Whole_Seller_product");

        if((Whole_Seller_product.equals("1"))){

            drop_supplier.setVisibility(View.GONE);

            Paper.book().write("supplier_id","0");

            Proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                   public void onClick(View v) {

                    Paper.book().write("order_desc",order_desc.getText().toString().trim());

                    if (!txtvw_quote.getText().equals("Select Quote / Order")) {

                    if(!tv_drop_project.getText().equals("Select Project")){

                        if (!TextUtils.isEmpty(order_desc.getText().toString().trim())) {

                            if (Proceed.getText().toString().equals("Proceed to Summary")) {

                                transaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("li").replace(R.id.containerr, new Cart_Summary());
                                transaction.commit();
                            }

                            else  {
                                jsonarray();
                            }

                        }

                        else {

                            Toast.makeText(getActivity(), "Enter Order Description", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {

                        Toast.makeText(getActivity(), "Select Project", Toast.LENGTH_SHORT).show();
                    }

                    }

                    else {

                        Toast.makeText(getActivity(), "Select Quote / Order", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }




      else{

            Log.e("tiger","65");
            drop_supplier.setVisibility(View.VISIBLE);
            Proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Paper.book().write("order_desc",order_desc.getText().toString().trim());

                    if (!txtvw_quote.getText().equals("Select Quote / Order")) {

                    if(!tv_drop_project.getText().equals("Select Project")){

                        if (!TextUtils.isEmpty(order_desc.getText().toString().trim())) {

                                if (Proceed.getText().toString().equals("Proceed to Summary")) {

                                    if(!tv_drop_supplier.getText().equals("Select Supplier")){

                                    transaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("li").replace(R.id.containerr, new Cart_Summary());
                                    transaction.commit();

                                    }

                                    else {

                                        Toast.makeText(getActivity(), "Select Supplier", Toast.LENGTH_SHORT).show();
                                    }


                                }

                                else  {
                                    jsonarray();
                                }


                        }

                        else {

                            Toast.makeText(getActivity(), "Enter Order Description", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {

                        Toast.makeText(getActivity(), "Select Project", Toast.LENGTH_SHORT).show();
                    }

                }

               else {

                    Toast.makeText(getActivity(), "Select Quote / Order", Toast.LENGTH_SHORT).show();
                }

                }
            });
        }


        if(Paper.book().read("permission_wholeseller", "5").equals("1")){

            Log.e("tiger","1");
          //  drop_supplier.setVisibility(View.GONE);

            Paper.book().write("supplier_id","0");

            Proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Paper.book().write("order_desc",order_desc.getText().toString().trim());

                    if (!txtvw_quote.getText().equals("Select Quote / Order")) {

                    if(!tv_drop_project.getText().equals("Select Project")){

                        if (!TextUtils.isEmpty(order_desc.getText().toString().trim())) {

                            if (Proceed.getText().toString().equals("Proceed to Summary")) {

                                transaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("li").replace(R.id.containerr, new Cart_Summary());
                                transaction.commit();
                            }

                            else  {
                                jsonarray();
                            }

                        }

                        else {

                            Toast.makeText(getActivity(), "Enter Order Description", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {

                        Toast.makeText(getActivity(), "Select Project", Toast.LENGTH_SHORT).show();
                    }

                    }

                    else {

                        Toast.makeText(getActivity(), "Select Quote / Order", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


        if (menu_Van_Stock!= null && menu_Van_Stock.equals("1")){

            drop_supplier.setVisibility(View.GONE);

            Proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Paper.book().write("order_desc",order_desc.getText().toString().trim());

                    if (!txtvw_quote.getText().equals("Select Quote / Order")) {

                        if(!tv_drop_project.getText().equals("Select Project")){

                            if (!TextUtils.isEmpty(order_desc.getText().toString().trim())) {



                                    if (Proceed.getText().toString().equals("Proceed to Summary")) {

                                        transaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("li").replace(R.id.containerr, new Cart_Summary());
                                        transaction.commit();
                                    }

                                    else if (Proceed.getText().toString().equals("Purchase VanStock")) {
                                        jsonarray1();
                                    }

                                    else  {
                                        jsonarray();
                                    }

                            }

                            else {

                                Toast.makeText(getActivity(), "Enter Order Description", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else {

                            Toast.makeText(getActivity(), "Select Project", Toast.LENGTH_SHORT).show();
                        }

                    }

                    else {

                        Toast.makeText(getActivity(), "Select Quote / Order", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }




        drop_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Proceed.getText().toString().equals("Proceed to Summary")) {

                    dropdownmenu_supplier();

                }
                else if (Proceed.getText().toString().equals("Send Quotation")) {

                    dropdownmenu_supplier_Quotes();
                }

                else {

                    dropdownmenu_supplier();
                }
            }
        });

        adapter_cart_next=new Adapter_cart_next(getActivity(),cartMenuList);
        recyclerView.setAdapter(adapter_cart_next);

        double value=databaseHelper.getAllPrices();
        String data= String.format("%.2f", value);
        double sub=databaseHelper.getAllInc();
        String data1= String.format("%.2f", sub);
        double vat1=sub-value;
        String vatvalue= String.format("%.2f", vat1);

        if(Paper.book().read("permission_see_cost","2").equals("1")){

            sub_total.setText(data);
            total.setText(data1);
            vat.setText(vatvalue);
        }


        else {

            sub_total.setText("0.00");
            total.setText("0.00");
            vat.setText("0.00");

        }

            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                sub_total.setText(data);
                total.setText(data1);
                vat.setText(vatvalue);
            }


       /* Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Proceed.getText().toString().equals("Send Quotation")) {
                    jsonarray();
                }
            }
        });
*/


        if ((databaseHelper.Exists_Cart_Type("1") && databaseHelper.Exists_Cart_Type("3")) || databaseHelper.Exists_Cart_Type("1") ){


            Log.e("Condition","else1");
            my_quote.add("Quote");
            my_quote.add("Order");
            imgvw_arrow_quote.setVisibility(View.VISIBLE);
            layout_col_del.setVisibility(View.GONE);
            txtvw_quote.setText("Order");
            drop_supplier.setVisibility(View.VISIBLE);
            Proceed.setText("Proceed to Summary");

        }

        else if ((databaseHelper.Exists_Cart_Type("2") && databaseHelper.Exists_Cart_Type("3")) || databaseHelper.Exists_Cart_Type("2") ){
            Log.e("Condition","else2");
            my_quote.add("Quote");
            my_quote.add("Order");
            imgvw_arrow_quote.setVisibility(View.VISIBLE);
            layout_col_del.setVisibility(View.GONE);
            txtvw_quote.setText("Order");
            drop_supplier.setVisibility(View.GONE);
            Proceed.setText("Proceed to Summary");
        }
/*

        if (databaseHelper.Exists_Cart_Type("3")){

        }
*/

        else if (databaseHelper.Exists_Cart_Type("4")){


            Log.e("Condition","if");

            my_quote.add("Van Stock");
            my_quote.add("Quote");
            my_quote.add("Order");

            // layout_col_del.setVisibility(View.VISIBLE);
            imgvw_arrow_quote.setVisibility(View.GONE);
            txtvw_quote.setText("Van Stock");
            drop_supplier.setVisibility(View.GONE);
            Proceed.setText("Purchase VanStock");

        }

     /*   if (Cart_Type != null && Cart_Type.equals("2")){

            Log.e("Condition","if");

            my_quote.add("Van Stock");
            my_quote.add("Quote");
            my_quote.add("Order");

            // layout_col_del.setVisibility(View.VISIBLE);
            imgvw_arrow_quote.setVisibility(View.GONE);
            txtvw_quote.setText("Van Stock");
            drop_supplier.setVisibility(View.GONE);
            Proceed.setText("Purchase VanStock");
        }

        else if (Cart_Type != null && Cart_Type.equals("1")){

            Log.e("Condition","else1");
            my_quote.add("Quote");
            my_quote.add("Order");
            imgvw_arrow_quote.setVisibility(View.VISIBLE);
            layout_col_del.setVisibility(View.GONE);
            txtvw_quote.setText("Order");
            drop_supplier.setVisibility(View.VISIBLE);
            Proceed.setText("Proceed to Summary");

        }

        else if (Cart_Type != null && Cart_Type.equals("3")){

            Log.e("Condition","else2");
            my_quote.add("Quote");
            my_quote.add("Order");
            imgvw_arrow_quote.setVisibility(View.VISIBLE);
            layout_col_del.setVisibility(View.GONE);
            txtvw_quote.setText("Order");
            drop_supplier.setVisibility(View.GONE);
            Proceed.setText("Proceed to Summary");

        }
*/
        else {

            Log.e("Condition","else");
            my_quote.add("Quote");
            my_quote.add("Order");
            imgvw_arrow_quote.setVisibility(View.VISIBLE);
            layout_col_del.setVisibility(View.GONE);
            txtvw_quote.setText("Order");
            drop_supplier.setVisibility(View.VISIBLE);
            Proceed.setText("Proceed to Summary");

        }










        return view;

    }

    private void select_col_del() {


        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_dropdown_menu, null);

        layout_close = (LinearLayout) customView.findViewById(R.id.layout_popup_upper);
        layout_close_bottom=(LinearLayout) customView.findViewById(R.id.layout_popup_bottom);
        my_list = customView.findViewById(R.id.list);
        //instantiate popup window
        popUp = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        List <String> list = new ArrayList<>();
        list.add("Delivery");
        list.add("Collection");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list);

        my_list.setAdapter(arrayAdapter);

        my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                txtvw_col_del.setText(list.get(position));


                if(txtvw_col_del.getText().toString().equals("Collection")){

                    Collection();

                }

                popUp.dismiss();

            }
        });
        //display the popup window
        popUp.showAtLocation(cartlayout, Gravity.CENTER, 0, 0);

        //close the popup window on button click
        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        layout_close_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

    }

    private void Collection() {

        final Calendar calendar = Calendar.getInstance();


        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(),R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                currentHour = hourOfDay;
                currentMinute = minute;

                try {
                    Class<?> superclass = getClass().getSuperclass();
                    Field mTimePickerField = superclass.getDeclaredField("mTimePicker");
                    mTimePickerField.setAccessible(true);
                    TimePicker mTimePicker = (TimePicker) mTimePickerField.get(this);
                    mTimePicker.setOnTimeChangedListener((TimePicker.OnTimeChangedListener) contextd);
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }

                boolean validTime = true;
                if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)){
                    validTime = false;

                    Toast.makeText(getActivity(),"Please Select time between 7 am to 6 pm",Toast.LENGTH_SHORT).show();
                }

                if (hourOfDay  > maxHour || (hourOfDay == maxHour && minute > maxMinute)){
                    validTime = false;
                    Toast.makeText(getActivity(),"Please Select time between 7 am to 6 pm",Toast.LENGTH_SHORT).show();

                }


                if (validTime) {
                    currentHour = hourOfDay;
                    currentMinute = minute;
                  //  col_time=currentHour + ":" + currentMinute;
                  //  String temp_value=Paper.book().read("S_value");
                  //  Paper.book().write("order_col_time",col_time);
                    txtvw_col_del.setText(currentHour + ":" + currentMinute);
                }

            }
        }, mHour, mMinute, true);


        timePickerDialog.show();

    }

    private void jsonarray1() {

        String userid= Paper.book().read("userid");
        String order_desc=Paper.book().read("order_desc");
        String supplier_id=Paper.book().read("supplier_id");
        String project_id=Paper.book().read("project_id");

        for(int i=0;i<cartMenuList.size();i++) {

            String total= String.valueOf(Double.valueOf(cartMenuList.get(i).getPrice()));
            double value=Double.valueOf(total);
            String data= String.format("%.2f", value);

            JSONObject order1 = new JSONObject();

            try {

                order1.put("delivery_instruction","");
                order1.put("price",data);
                order1.put("user_id", userid);
                order1.put("variation_id",cartMenuList.get(i).getVariationid());
                order1.put("product_id", cartMenuList.get(i).getProductid());
                order1.put("quantity", cartMenuList.get(i).getQuantity());
                order1.put("order_desc",order_desc);
                order1.put("del_col","Delivery");
                order1.put("supplier_id",supplier_id);
                order1.put("project_id",project_id);
                order1.put("businessidlogo","pick");
                order1.put("procode",cartMenuList.get(i).getAdd_product_code());
                order1.put("procode_title_new",cartMenuList.get(i).getProductname());
                order1.put("vanstock","1");


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            jsonArray.put(order1);

        }

        JSONObject orderobj = new JSONObject();
        try {
            orderobj.put("Orderdetails", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String cart_string = orderobj.toString();

        Log.e("cartvan",cart_string+"");

        CartAPI_VAN(cart_string);
    }

    private void CartAPI_VAN(String cart_string) {



        final ProgressDialog progressDialog=new ProgressDialog(getActivity(),R.style.AlertDialogCustom);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Ordering.......");
        progressDialog.show();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

   //     devicetoken= FirebaseInstanceId.getInstance().getToken();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        // Get new Instance ID token
                        devicetoken = task.getResult();
                    }
                });
        service.CART_VAN_CALL(cart_string).enqueue(new Callback<ModelBuyNow>() {
            @Override
            public void onResponse(Call<ModelBuyNow> call, Response<ModelBuyNow> response) {

                if(response.body().getStatusCode().equals(200)){


                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.PRICE,total.getText().toString() );
                    bundle.putString(FirebaseAnalytics.Param.CURRENCY, "Pound");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Van_Pfroducts");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, bundle);

                    Integer orderid=response.body().getOrderid();
                    String string=String.valueOf(orderid);
                    final Dialog dialog= new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_dialog_order_success);
                    TextView order_id= dialog.findViewById(R.id.txt_order);
                    order_id.setText(string);
                    TextView limittext=dialog.findViewById(R.id.textlimit);

                    if(response.body().getLimitstatus().equals(0)){

                        limittext.setText("Your order has been successfully placed.");

                    }
                    else {

                        limittext.setText("The order has been sent to the supervisor for approval. ");

                    }

                    TextView total_price=dialog.findViewById(R.id.txtvw_total);

                    double value=Double.valueOf(databaseHelper.getAllInc());

                    String data= String.format("%.2f", value);


                    if(Paper.book().read("permission_see_cost","2").equals("1")){

                        total_price.setText(data);
                    }

                    else if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                        total_price.setText(data);
                    }

                    else {

                        total_price.setText("0.00");

                    }

                    databaseHelper.deleteAll();
                    getActivity().sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));

                    Button continue_shopping=dialog.findViewById(R.id.btn_continue);

                    continue_shopping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            databaseHelper.deleteAll();
                            transaction=getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr, new Home_Categories());
                            transaction.commit();
                            getActivity().sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                    dialog.setCancelable(false);
                      progressDialog.dismiss();
                }

                else

                {
                    Toast.makeText(getActivity(),response.message(), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ModelBuyNow> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                  progressDialog.dismiss();
            }
        });

    }

    private void dropdownmenu_supplier_Quotes() {

           Dialog dialog= new Dialog(getActivity(),R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.custom_dialog_update_project);

     /*   txt_update_cancel=dialog.findViewById(R.id.txt_update_cancel);
        txt_update_submit=dialog.findViewById(R.id.txt_update_submit);
*/
        RecyclerView recyclerView=dialog.findViewById(R.id.recyclerview_update);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

       // Log.e("data",AssignData.toString()+"");

        adapter_update_project=new Adapter_Quote_Supplier(getActivity(),supplierDataQuote,dialog);
        recyclerView.setAdapter(adapter_update_project);


        dialog.show();
    }


    public void jsonarray() {

        String userid= Paper.book().read("userid");
        String order_desc=Paper.book().read("order_desc");
        String supplier_id=Paper.book().read("supplier_id");
        String project_id=Paper.book().read("project_id");

        for(int i=0;i<cartMenuList.size();i++) {

            String total= String.valueOf(Double.valueOf(cartMenuList.get(i).getPrice()));
            double value=Double.valueOf(total);
            String data= String.format("%.2f", value);

            JSONObject order1 = new JSONObject();

            try {

                order1.put("delivery_instruction","");
                order1.put("price",data);
                order1.put("user_id", userid);
                order1.put("variation_id",cartMenuList.get(i).getVariationid());
                order1.put("product_id", cartMenuList.get(i).getProductid());
                order1.put("quantity", cartMenuList.get(i).getQuantity());
                order1.put("order_desc",order_desc);
                order1.put("supplier_id",supplier_id);
                order1.put("project_id",project_id);
                order1.put("businessidlogo","pick");
                order1.put("procode",cartMenuList.get(i).getAdd_product_code());
                order1.put("procode_title_new",cartMenuList.get(i).getProductname());
                order1.put("vanstock","0");


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            jsonArray.put(order1);

        }

        JSONObject orderobj = new JSONObject();
        try {
            orderobj.put("Orderdetails", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String cart_string = orderobj.toString();

        Log.e("cart",cart_string+"");

        CartAPI(cart_string);
    }

    private void CartAPI(final String json) {

        final ProgressDialog progressDialog=new ProgressDialog(getActivity(),R.style.AlertDialogCustom);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Ordering.......");
        progressDialog.show();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        // Get new Instance ID token
                        devicetoken = task.getResult();
                    }
                });

        service.SEND_QUOTE(json).enqueue(new Callback<ModelGetQuote>() {
            @Override
            public void onResponse(Call<ModelGetQuote> call, Response<ModelGetQuote> response) {

                if(response.body().getStatusCode().equals(200)){

                    Integer orderid=response.body().getQuotesid();
                    String string=String.valueOf(orderid);
                    final Dialog dialog= new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_dialog_order_success);
                    TextView order_id= dialog.findViewById(R.id.txt_order);
                    TextView txt_thanks= dialog.findViewById(R.id.txt_thanks);
                    TextView quote_id= dialog.findViewById(R.id.quote_id);


                    txt_thanks.setText("Thanks For Quotation");

                    order_id.setText(string);
                    TextView limittext=dialog.findViewById(R.id.textlimit);

                    if(response.body().getLimitstatus().equals(0)){

                        quote_id.setText("Quote No.");
                        limittext.setText("Your quotation has been Sent for your supplier .");

                    }
                    else {

                        limittext.setText("The order has been sent to the supervisor for approval. ");

                    }

                    TextView total_price=dialog.findViewById(R.id.txtvw_total);

                    double value=Double.valueOf(databaseHelper.getAllInc());

                    String data= String.format("%.2f", value);


                    if(Paper.book().read("permission_see_cost","2").equals("1")){

                        total_price.setText(data);
                    }

                    else if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                        total_price.setText(data);
                    }

                    else {

                        total_price.setText("0.00");

                    }

                    databaseHelper.deleteAll();
                    getActivity().sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));

                    Button continue_shopping=dialog.findViewById(R.id.btn_continue);

                    continue_shopping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            databaseHelper.deleteAll();
                            transaction=getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr, new Home_Categories());
                            transaction.commit();
                            getActivity().sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    dialog.setCancelable(false);
                    progressDialog.dismiss();
                }

                else

                {
                    Toast.makeText(getActivity(),response.message(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ModelGetQuote> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void Quotes_invoice() {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_dropdown_menu, null);

        layout_close = (LinearLayout) customView.findViewById(R.id.layout_popup_upper);
        layout_close_bottom=(LinearLayout) customView.findViewById(R.id.layout_popup_bottom);
        my_list = customView.findViewById(R.id.list);
        //instantiate popup window
        popUp = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, my_quote);

        my_list.setAdapter(arrayAdapter);

        my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    txtvw_quote.setText(my_quote.get(position));


                if(txtvw_quote.getText().toString().equals("Van Stock")){


                    drop_supplier.setVisibility(View.GONE);
                    //layout_col_del.setVisibility(View.VISIBLE);
                    Proceed.setText("Purchase VanStock");
                }

                 else  if(txtvw_quote.getText().toString().equals("Quote")){

                    tv_drop_supplier.setText("Select Supplier");
                     Paper.book().write("supplier_id","0");
                   // drop_supplier.setVisibility(View.VISIBLE);


                    if(Paper.book().read("permission_wholeseller", "5").equals("1")  && (Cart_Type!= null && Cart_Type.equals("3"))){
                        Paper.book().write("whole_seller","2");

                        drop_supplier.setVisibility(View.GONE);
                    }

                    else {

                        drop_supplier.setVisibility(View.VISIBLE);
                    }
                    layout_col_del.setVisibility(View.GONE);
                        Proceed.setText("Send Quotation");
                    }

                    else if (txtvw_quote.getText().toString().equals("Order")){

                    tv_drop_supplier.setText("Select Supplier");

                    if(Paper.book().read("permission_wholeseller", "5").equals("1")  && (Cart_Type!= null && Cart_Type.equals("3"))){
                        Paper.book().write("whole_seller","2");

                        drop_supplier.setVisibility(View.GONE);
                    }

                    else {

                        drop_supplier.setVisibility(View.VISIBLE);
                    }

                    layout_col_del.setVisibility(View.GONE);
                        Proceed.setText("Proceed to Summary");
                    }

                    else {

                        Proceed.setText("Proceed to Summary");
                    }


                popUp.dismiss();

            }
        });
        //display the popup window
        popUp.showAtLocation(cartlayout, Gravity.CENTER, 0, 0);

        //close the popup window on button click
        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        layout_close_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });
    }

    private void dropdownmenu_project() {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_dropdown_menu, null);

        layout_close = (LinearLayout) customView.findViewById(R.id.layout_popup_upper);
        layout_close_bottom=(LinearLayout) customView.findViewById(R.id.layout_popup_bottom);
        my_list = customView.findViewById(R.id.list);
        //instantiate popup window
        popUp = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, variation_drop_project);

        my_list.setAdapter(arrayAdapter);

        my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                    tv_drop_project.setText(mylist.get(position).getProjectName());
                    Paper.book().write("project_id",mylist.get(position).getId());
                }

                else {
                    position=position+1;
                    tv_drop_project.setText(mylist.get(position).getProjectName());
                    Paper.book().write("project_id",mylist.get(position).getId());

                }
                popUp.dismiss();

            }
        });
        //display the popup window
        popUp.showAtLocation(cartlayout, Gravity.CENTER, 0, 0);

        //close the popup window on button click
        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        layout_close_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

    }

    private void dropdownmenu_supplier() {

      /*  Dialog dialog= new Dialog(getActivity(),R.style.AlertDialogCustom);
        dialog.setContentView(R.layout.custom_dialog_update_project);

        *//*txt_update_cancel=dialog.findViewById(R.id.txt_update_cancel);
        txt_update_submit=dialog.findViewById(R.id.txt_update_submit);*//*

        RecyclerView recyclerView=dialog.findViewById(R.id.recyclerview_update);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

       // Log.e("data",AssignData.toString()+"");

        adapter_update_project=new Adapter_Quote_Supplier(getActivity(),supplierDataQuote,dialog);
        recyclerView.setAdapter(adapter_update_project);


        dialog.show();
*/


        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_dropdown_menu, null);

        layout_close = (LinearLayout) customView.findViewById(R.id.layout_popup_upper);
        layout_close_bottom=(LinearLayout) customView.findViewById(R.id.layout_popup_bottom);
        my_list = customView.findViewById(R.id.list);
        //instantiate popup window
        popUp = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, variation_drop_supplier);

        my_list.setAdapter(arrayAdapter);

        my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                position=position+1;
                tv_drop_supplier.setText(supplierData.get(position).getSuppliersName());
                Paper.book().write("supplier_id",supplierData.get(position).getId());
                popUp.dismiss();

            }
        });
        //display the popup window
        popUp.showAtLocation(cartlayout, Gravity.CENTER, 0, 0);

        //close the popup window on button click
        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

        layout_close_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
            }
        });

    }

    private void getsupplier() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String userid = Paper.book().read("userid");

        service.SUPPLIER_CALL(userid).enqueue(new Callback<ModelSupplier>() {
            @Override
            public void onResponse(Call<ModelSupplier> call, Response<ModelSupplier> response) {

                if (response.body().getStatusCode().equals(200)) {

                    supplierDatumList = response.body().getSupplierData();

                    for (int i = 0; i < supplierDatumList.size(); i++) {

                        SupplierDatum supplierDatum = new SupplierDatum();
                        supplierDatum.setSuppliersName(response.body().getSupplierData().get(i).getSuppliersName());
                        supplierDatum.setId(response.body().getSupplierData().get(i).getId());
                        supplierData.add(supplierDatum);
                    }


                    for (int i = 1; i < supplierDatumList.size(); i++) {

                        SupplierDatum supplierDatum = new SupplierDatum();
                        supplierDatum.setSuppliersName(response.body().getSupplierData().get(i).getSuppliersName());
                        supplierDatum.setId(response.body().getSupplierData().get(i).getId());
                        supplierDataQuote.add(supplierDatum);
                    }

                    for (int i = 1; i < supplierDatumList.size(); i++) {

                        SupplierDatum supplierDatum = new SupplierDatum();
                        supplierDatum.setSuppliersName(response.body().getSupplierData().get(i).getSuppliersName());
                        variation_drop_supplier.add(response.body().getSupplierData().get(i).getSuppliersName());
                    }
                }

            }

            @Override
            public void onFailure(Call<ModelSupplier> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void getProjects() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        final ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String userid= Paper.book().read("userid");

        service.PROJECTS_CALL(userid).enqueue(new Callback<ModelProjects>() {
            @Override
            public void onResponse(Call<ModelProjects> call, Response<ModelProjects> response) {

                if (response.body().getStatusCode().equals(200)){

                    projectlist=response.body().getProjectData();

                    for (int i=0; i<projectlist.size();i++) {

                        ProjectDatum datum = new ProjectDatum();
                        datum.setProjectName(response.body().getProjectData().get(i).getProjectName());
                        datum.setId(response.body().getProjectData().get(i).getId());
                        mylist.add(datum);
                    }

                    if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                        for (int i=0; i<projectlist.size();i++) {

                            ProjectDatum datum = new ProjectDatum();
                            datum.setProjectName(response.body().getProjectData().get(i).getProjectName());
                            variation_drop_project.add(response.body().getProjectData().get(i).getProjectName());
                        }
                    }

                    else {

                     for (int i=1; i<projectlist.size();i++) {

                        ProjectDatum datum = new ProjectDatum();
                        datum.setProjectName(response.body().getProjectData().get(i).getProjectName());
                        variation_drop_project.add(response.body().getProjectData().get(i).getProjectName());
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ModelProjects> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Cart");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

    }

}
