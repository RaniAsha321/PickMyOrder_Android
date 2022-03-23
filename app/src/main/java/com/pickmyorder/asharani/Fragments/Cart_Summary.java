package com.pickmyorder.asharani.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pickmyorder.asharani.Adapters.Adapter_cart_next;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Storage.DatabaseChangedReceiver;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.ModelBuyNow;
import com.pickmyorder.asharani.Models.ModelCartMenu;
import com.pickmyorder.asharani.Activities.Payment_Stripe.AddPayment;
import com.pickmyorder.asharani.R;
import com.pickmyorder.asharani.Storage.databaseSqlite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Cart_Summary extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;
    Context context;
    Adapter_cart_next adapter_cart_summary;
    RecyclerView recyclerView;
    List<ModelCartMenu> cartMenuList;
    LinearLayoutManager layoutManager;
    databaseSqlite myDb;
    LinearLayout layout_delivery,layout_collection;
    TextView delivery_Date,collection_date,sub_total,vat,total;
    Button Purchase_order,btn_payment;
    public int mYear, mMonth, mDay, mHour, mMinute,awaiting_data;
    String col_date,col_time,del_date,del_time,stripe_token,devicetoken,delivery_instruction,Cart_Type;
    FragmentTransaction transaction;
    JSONArray jsonArray;
    EditText del_address;
    AddPayment add_payment;
    private int minHour = 7;
    private int minMinute = 0;
    private int maxHour = 17;
    private int maxMinute = 61;
    private int currentHour = 0;
    private int currentMinute = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart__summary, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("li", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        Purchase_order=view.findViewById(R.id.purchase_order);
        delivery_Date=view.findViewById(R.id.delivery_date);
        collection_date=view.findViewById(R.id.collection_date);
        layout_collection=view.findViewById(R.id.layout_collection);
        layout_delivery=view.findViewById(R.id.layout_delivery);
        recyclerView=view.findViewById(R.id.cart_summary_recycle);
        del_address=view.findViewById(R.id.edtxt_del_ins);
        sub_total=view.findViewById(R.id.summary_sub_total);
        vat=view.findViewById(R.id.summary_vat);
        total=view.findViewById(R.id.summary_total);
        del_address.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        btn_payment=view.findViewById(R.id.btn_payment);

        Cart_Type = Paper.book().read("Cart_Type");

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        jsonArray = new JSONArray();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Cart Summary");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        myDb=new databaseSqlite(getActivity());
        cartMenuList=myDb.getAllModelCartMenu();

        Paper.book().write("order_del_address",del_address.getText().toString().trim());

        ((Home)getActivity()).hideView(true);

        adapter_cart_summary=new Adapter_cart_next(getActivity(),cartMenuList);
        recyclerView.setAdapter(adapter_cart_summary);

        String Whole_Seller_product=Paper.book().read("Whole_Seller_product");

        if(Whole_Seller_product.equals("0")){
            btn_payment.setVisibility(View.GONE);
        }

        else if(Paper.book().read("permission_wholeseller", "5").equals("1")  || Paper.book().read("ViewWholesellerPage", "5").equals("1")  ) {
           btn_payment.setVisibility(View.VISIBLE);
        }

        else {

            btn_payment.setVisibility(View.GONE);

        }

        String menu_Cat = Paper.book().read("menu_Cat");
        String menu_Wholesaler = Paper.book().read("menu_Wholesaler");


        double value=myDb.getAllPrices();
        String data= String.format("%.2f", value);
        double sub=myDb.getAllInc();
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
                btn_payment.setVisibility(View.VISIBLE);
            }


        if (Cart_Type!= null && Cart_Type.equals("1")){

            Purchase_order.setVisibility(View.VISIBLE);
            btn_payment.setVisibility(View.GONE);
        }
        else if (Cart_Type!= null && Cart_Type.equals("3")){

            Purchase_order.setVisibility(View.GONE);
            btn_payment.setVisibility(View.VISIBLE);
        }
        else {
            Purchase_order.setVisibility(View.VISIBLE);
            btn_payment.setVisibility(View.VISIBLE);
        }



        DeliveryCollection();

            btn_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("deleveryboy",delivery_instruction+"");
                    if((!delivery_Date.getText().toString().equals("Delivery")) || (!collection_date.getText().toString().equals("Collection")) )
                    {
                        delivery_instruction=del_address.getText().toString().trim();
                        Log.e("deleveryboy",delivery_instruction+"");
                        jsonarray_payment(delivery_instruction);
                        Intent intent = new Intent(getActivity(), AddPayment.class);
                        startActivity(intent);

                        if(!Paper.book().read("awaiting_data").equals(""))
                        {
                            awaiting_data=Paper.book().read("awaiting_data");
                            Paper.book().write("awaiting_data",awaiting_data+1);
                        }

                        else{

                            Paper.book().write("awaiting_data",1 );
                        }

                    }
                    else
                    {

                        Toast.makeText(getActivity(), "Select Either Delivery or Collection", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        Purchase_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if((!delivery_Date.getText().toString().equals("Delivery")) || (!collection_date.getText().toString().equals("Collection")) )
                {
                    Paper.book().write("saved","");
                     jsonarray();


                    if(!Paper.book().read("awaiting_data").equals(""))
                    {
                        awaiting_data=Paper.book().read("awaiting_data");
                        Paper.book().write("awaiting_data",awaiting_data+1);
                    }

                    else{

                        Paper.book().write("awaiting_data",1 );
                    }

                }
                else
                    {

                    Toast.makeText(getActivity(), "Select Either Delivery or Collection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == resultCode){

            btn_payment.setVisibility(View.VISIBLE);
            stripe_token = data.getStringExtra("stripe_token");

            Toast.makeText(getActivity(),stripe_token, Toast.LENGTH_LONG).show();

            if(stripe_token.length()>1) {


        }

        else {

            btn_payment.setVisibility(View.GONE);
        }
    }
    }

    private void DeliveryCollection() {

        layout_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();


                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                /*TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {

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

                            del_time=currentHour + ":" + currentMinute;

                            String temp_value=Paper.book().read("S_value");
                            delivery_Date.setText(temp_value +"  " +del_time);
                            Paper.book().write("order_del_time",del_time);
                        }
                    }


                }, mHour, mMinute, true);


                timePickerDialog.show();*/
               // String temp_value=Paper.book().read("S_value");


                del_time = "";
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
                        del_date = dateFormat.format(calendar.getTime());
                        Paper.book().write("S_value",del_date);
                        Paper.book().write("order_del_date",del_date);

                        delivery_Date.setText(del_date);
                        Paper.book().write("order_del_time","");

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
                datePickerDialog.show();

                layout_collection.setVisibility(View.GONE);
                collection_date.setText("Collection");
                col_date= String.valueOf(0);
                col_time= String.valueOf(0);
                Paper.book().write("order_col_date",col_date);
                Paper.book().write("order_col_time",col_time);
            }
        });
        
            layout_collection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

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
                            mTimePicker.setOnTimeChangedListener((TimePicker.OnTimeChangedListener) context);
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
                            col_time=currentHour + ":" + currentMinute;
                            String temp_value=Paper.book().read("S_value");
                            Paper.book().write("order_col_time",col_time);
                            collection_date.setText(temp_value +"  " +col_time);
                        }

                    }
                }, mHour, mMinute, true);


                timePickerDialog.show();

                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
                        col_date = dateFormat.format(calendar.getTime());
                        Paper.book().write("S_value",col_date);
                        Paper.book().write("order_col_date",col_date);

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();

                layout_delivery.setVisibility(View.GONE);
                delivery_Date.setText("Delivery");
                del_date= String.valueOf(0);
                del_time= String.valueOf(0);
                Paper.book().write("order_del_date",del_date);
                Paper.book().write("order_del_time",del_time);
            }

            });
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

        service.CART_CALL(json).enqueue(new Callback<ModelBuyNow>() {
            @Override
            public void onResponse(Call<ModelBuyNow> call, Response<ModelBuyNow> response) {

                if(response.body().getStatusCode().equals(200)){

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.PRICE,total.getText().toString() );
                    bundle.putString(FirebaseAnalytics.Param.CURRENCY, "Pound");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Products");
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

                    double value=Double.valueOf(myDb.getAllInc());

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

                    myDb.deleteAll();
                    getActivity().sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));

                    Button continue_shopping=dialog.findViewById(R.id.btn_continue);

                    continue_shopping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            myDb.deleteAll();
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

                order1.put("delivery_instruction",del_address.getText().toString().trim());
                order1.put("price",data);
                order1.put("user_id", userid);
                order1.put("variation_id",cartMenuList.get(i).getVariationid());
                order1.put("product_id", cartMenuList.get(i).getProductid());
                order1.put("quantity", cartMenuList.get(i).getQuantity());
                order1.put("collection_time",col_time);
                order1.put("collection_date",col_date);
                order1.put("delivery_time",del_time);
                order1.put("delivery_Date",del_date);
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

    public void jsonarray_payment(String delivery_instruction) {

        String userid= Paper.book().read("userid");
        String order_desc=Paper.book().read("order_desc");
        String supplier_id=Paper.book().read("supplier_id");
        String project_id=Paper.book().read("project_id");


        for(int i=0;i<cartMenuList.size();i++) {

            String total= String.valueOf(Double.valueOf(cartMenuList.get(i).getPrice()));
            double value=Double.valueOf(total);
            String data= String.format("%.2f", value);

            Log.e("instruction",del_address.getText().toString().trim());

            JSONObject order1 = new JSONObject();

            try {

                order1.put("delivery_instruction",delivery_instruction);
                order1.put("price",data);
                order1.put("user_id", userid);
                order1.put("variation_id",cartMenuList.get(i).getVariationid());
                order1.put("product_id", cartMenuList.get(i).getProductid());
                order1.put("quantity", cartMenuList.get(i).getQuantity());
                order1.put("collection_time",col_time);
                order1.put("collection_date",col_date);
                order1.put("delivery_time",del_time);
                order1.put("delivery_Date",del_date);
                order1.put("order_desc",order_desc);
                order1.put("supplier_id",supplier_id);
                order1.put("project_id",project_id);
                order1.put("businessidlogo","pick");
                order1.put("procode",cartMenuList.get(i).getAdd_product_code());
                order1.put("procode_title_new",cartMenuList.get(i).getProductname());


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

        Log.e("cart_string",cart_string+"");

        Paper.book().write("Cart_Array_for_PAyment",cart_string);
        Paper.book().write("Cart_Payment",cart_string);
    }

    public void onAttach (Activity activity) {
        super.onAttach(activity);
        if(activity.getClass()== AddPayment.class){
            add_payment = (AddPayment) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Cart Summary");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

    }

}
