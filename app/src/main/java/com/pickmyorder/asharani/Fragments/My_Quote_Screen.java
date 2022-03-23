package com.pickmyorder.asharani.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.pickmyorder.asharani.Adapters.Adapter_My_Order_Screen;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Storage.DatabaseChangedReceiver;
import com.pickmyorder.asharani.Activities.Home;
import com.pickmyorder.asharani.Models.ModelBuyQuote;
import com.pickmyorder.asharani.Models.ModelMyOrder;
import com.pickmyorder.asharani.Models.ModelSupplier;
import com.pickmyorder.asharani.Models.SingleOrderDatum;
import com.pickmyorder.asharani.Models.SupplierDatum;
import com.pickmyorder.asharani.R;
import com.pickmyorder.asharani.Storage.databaseSqlite;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


public class My_Quote_Screen extends Fragment {


    Context contextd;
    TextView sub_total,vat,total,tv_quote_supplier,delivery_date,collection_date;
    Button quote_place_order,btn_order;
    databaseSqlite db;
    TextView refernce,date;
    List<SingleOrderDatum> mylist,singleOrderList;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView_my_order_screen;
    Adapter_My_Order_Screen adapter;
    double vat1;
    List<SupplierDatum> supplierData,supplierDatumList;
    List<String> variation_drop_supplier;
    LinearLayout cartlayout,drop_supplier,layout_close,layout_close_bottom,layout_collection,layout_delivery;
    ListView my_list;
    PopupWindow popUp;
    Dialog dialog;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String col_date,col_time,del_date,del_time,reference,dated,dateeed,PO_number,sub_total1,total1;
    FragmentTransaction transaction;
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
        View view= inflater.inflate(R.layout.fragment_my__orders__screen_quotes, container, false);

        refernce=view.findViewById(R.id.reference);
        date=view.findViewById(R.id.date2);
        quote_place_order=view.findViewById(R.id.quote_place_order);
        sub_total=view.findViewById(R.id.order_sub_total);
        vat=view.findViewById(R.id.order_vat);
        total=view.findViewById(R.id.order_total);
        cartlayout=view.findViewById(R.id.cart_layout);
        


        db=new databaseSqlite(getActivity());
        variation_drop_supplier=new ArrayList<>();
        supplierData =new ArrayList<>();

        dated=Paper.book().read("date");
        dateeed=Paper.book().read("dateeed");
        PO_number=Paper.book().read("PO_Number");
        reference= Paper.book().read("newReference");

        refernce.setText(PO_number);
        date.setText(Paper.book().read("tempdate"));

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("quote", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        recyclerView_my_order_screen=view.findViewById(R.id.recyclerview_my_order_screen);

        ((Home)getActivity()).hideView(true);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView_my_order_screen.setLayoutManager(layoutManager);

        mylist=new ArrayList<SingleOrderDatum>();

        MyOrders();
        getsupplier();

        quote_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String supplier_name_old= Paper.book().read("supplier_name_old");
                String supplier_id_old= Paper.book().read("supplier_id_old");

                Paper.book().write("supplier_name",supplier_name_old);
                Paper.book().write("supplier_id",supplier_id_old);

                dropdownmenu_order();

            }
        });
        return view;
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

    private void dropdownmenu_order() {

        dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_quote_order_placed);

        btn_order = dialog.findViewById(R.id.purchase_order);
        drop_supplier = dialog.findViewById(R.id.drop_supplier);
        tv_quote_supplier = dialog.findViewById(R.id.tv_quote_supplier);
        layout_delivery = dialog.findViewById(R.id.layout_delivery);
        layout_collection = dialog.findViewById(R.id.layout_collection);
        delivery_date = dialog.findViewById(R.id.delivery_date);
        collection_date = dialog.findViewById(R.id.collection_date);
        

        DeliveryCollection();
        String supplier_name= Paper.book().read("supplier_name");
        tv_quote_supplier.setText(supplier_name);

        dialog.show();

        drop_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                dropdownmenu_supplier();
            }
        });


        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                place_order();
                dialog.dismiss();

            }
        });


    }

    private void place_order() {

        final ProgressDialog progressDialog=new ProgressDialog(getActivity(),R.style.AlertDialogCustom);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait.......");
        progressDialog.show();

        String supplier_id=Paper.book().read("supplier_id");
        String total1=Paper.book().read("orders_ssub_total1");

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

        service.Quote_Order(reference,supplier_id,del_time,del_date,col_date,col_time).enqueue(new Callback<ModelBuyQuote>() {
            @Override
            public void onResponse(Call<ModelBuyQuote> call, Response<ModelBuyQuote> response) {

                Log.e("qwerty_response",response.body().getStatusCode()+"");

                if(response.body().getStatusCode().equals(200)){

                    Integer orderid=response.body().getOrderid();
                    String string=String.valueOf(orderid);
                    final Dialog dialog= new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_dialog_order_success_quotes);
                    TextView order_id= dialog.findViewById(R.id.txt_order);
                    order_id.setText(string);
                    TextView limittext=dialog.findViewById(R.id.textlimit);


                    limittext.setText("Your order has been successfully placed.");


                    TextView total_price=dialog.findViewById(R.id.txtvw_total);

                    if(Paper.book().read("permission_see_cost","2").equals("1")) {

                        total_price.setText(total1);

                    }

                    else {

                            total_price.setText(total1);

                        }
                    if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                        total_price.setText(total1);

                    }

                    getActivity().sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));

                    Button continue_shopping=dialog.findViewById(R.id.btn_continue);

                    continue_shopping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            transaction=getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr, new Home_Categories());
                            transaction.commit();
                            getActivity().sendBroadcast(new Intent(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    progressDialog.dismiss();
                }

                else

                {
                    Toast.makeText(getActivity(),response.message(), Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<ModelBuyQuote> call, Throwable t) {

                Log.e("qwerty_failure",t.getMessage()+"");
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressDialog.dismiss();

            }
        });

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
                        delivery_date.setText(del_date);
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
                delivery_date.setText("Delivery");
                del_date= String.valueOf(0);
                del_time= String.valueOf(0);
                Paper.book().write("order_del_date",del_date);
                Paper.book().write("order_del_time",del_time);
            }

        });
    }

    private void dropdownmenu_supplier() {

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
               // tv_quote_supplier.setText(supplierData.get(position).getSuppliersName());
                Paper.book().write("supplier_id",supplierData.get(position).getId());
                Paper.book().write("supplier_name",supplierData.get(position).getSuppliersName());

                popUp.dismiss();

                dropdownmenu_order();


            }
        });
        //display the popup window
        popUp.showAtLocation(cartlayout, Gravity.CENTER, 0, 0);

        //close the popup window on button click
        layout_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
                dropdownmenu_order();
            }
        });

        layout_close_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.dismiss();
                dropdownmenu_order();
            }
        });

    }

    private void MyOrders() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        final ProgressDialog progressDialogs = new ProgressDialog(getActivity(),R.style.AlertDialogCustom);
        progressDialogs.setCancelable(false);
        progressDialogs.setMessage("Please Wait.......");
        progressDialogs.show();

        service.SINGLE_QUOTE(reference).enqueue(new Callback<ModelMyOrder>() {
            @Override
            public void onResponse(Call<ModelMyOrder> call, Response<ModelMyOrder> response) {

                if (response.body().getStatusCode().equals(200)) {

                    sub_total1=response.body().getTotalEx();
                    total1=response.body().getTotalInc();

                    Paper.book().write("orders_ssub_total1",sub_total1);
                    Paper.book().write("orders_ttotal1",total1);
                    Paper.book().write("supplier_id",response.body().getSupplierId());
                    Paper.book().write("supplier_name",response.body().getSupplierName());

                    Paper.book().write("supplier_name_old",response.body().getSupplierName());
                    Paper.book().write("supplier_id_old",response.body().getSupplierId());

                    vat1=Double.valueOf(total1)-Double.valueOf(sub_total1);

                    String data= String.format("%.2f", vat1);

                    if(Paper.book().read("permission_see_cost","2").equals("1")){

                        sub_total.setText(sub_total1);
                        total.setText(total1);
                        vat.setText(data);
                    }

                    else {

                        sub_total.setText("0.00");
                        total.setText("0.00");
                        vat.setText("0.00");

                    }
                        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                            sub_total.setText(sub_total1);
                            total.setText(total1);
                            vat.setText(data);
                        }

                    singleOrderList = response.body().getSingleOrderData();

                    for (int i = 0; i < singleOrderList.size(); i++) {

                        SingleOrderDatum singleOrderDatum = new SingleOrderDatum();

                        singleOrderDatum.setProductName(singleOrderList.get(i).getProductName());
                        singleOrderDatum.setLinenumber(singleOrderList.get(i).getLinenumber());
                        singleOrderDatum.setDate(singleOrderList.get(i).getDate());
                        singleOrderDatum.setQty(singleOrderList.get(i).getQty());
                        singleOrderDatum.setPrice(singleOrderList.get(i).getPrice());
                        singleOrderDatum.setImage(singleOrderList.get(i).getImage());
                        singleOrderDatum.setPendingItem(singleOrderList.get(i).getPendingItem());
                        singleOrderDatum.setVariationOptionName(singleOrderList.get(i).getVariationOptionName());

                        mylist.add(singleOrderDatum);

                        Paper.book().write("date",mylist.get(i).getDate());
                    }

                    adapter = new Adapter_My_Order_Screen(getActivity(), mylist);
                    recyclerView_my_order_screen.setAdapter(adapter);

                }

                else

                {
                    Toast.makeText(getActivity(), "Not Found", Toast.LENGTH_SHORT).show();

                }
                progressDialogs.dismiss();

            }

            @Override
            public void onFailure(Call<ModelMyOrder> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressDialogs.dismiss();

            }
        });

    }

}
