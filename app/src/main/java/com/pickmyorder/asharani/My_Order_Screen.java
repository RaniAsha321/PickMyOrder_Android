package com.pickmyorder.asharani;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class My_Order_Screen extends Fragment {


    LinearLayout layout_instruction,layout_customer;
    TextView sub_total,vat,total;
    Button more_detail,btn_add_stock_order;
    databaseSqlite db;
    String dated,dateeed,PO_number;
    String reference;
    TextView refernce,date;
    List<SingleOrderDatum> mylist;
    List<SingleOrderDatum> singleOrderList;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView_my_order_screen;
    Adapter_My_Order_Screen adapter;
    double vat1;
    FragmentTransaction transaction;
    List<ModelStock> modelStockList;

    String  more_details_supplier1,more_details_supplier_address1,more_details_order_date1,more_details_delivery_date1,more_details_collection_date,
            more_details_description1,more_details_city1,more_details_postcode1,more_details_delivery_customer1,
            more_details_delivery_address1,more_details_delivery_city1,more_details_delivery_postcode1,sub_total1,total1,more_details_instructions1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my__orders__screen, container, false);

        refernce=view.findViewById(R.id.reference);
        date=view.findViewById(R.id.date2);
        more_detail=view.findViewById(R.id.quote_place_order);
        sub_total=view.findViewById(R.id.order_sub_total);
        vat=view.findViewById(R.id.order_vat);
        total=view.findViewById(R.id.order_total);
        btn_add_stock_order=view.findViewById(R.id.btn_add_stock_order);

        modelStockList = new ArrayList<>();
        db=new databaseSqlite(getActivity());

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

                    getFragmentManager().popBackStack("o", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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


        String reorder_value_orders = Paper.book().read("reorder_value_orders");

        if (reorder_value_orders.equals("1")){

            btn_add_stock_order.setVisibility(View.VISIBLE);

        }
        else {

            btn_add_stock_order.setVisibility(View.GONE);
        }

        btn_add_stock_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Paper.book().write("Stock_List",modelStockList);
                transaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("order_screen").replace(R.id.containerr, new My_Order_AddStock());
                transaction.commit();

            }
        });

        MyOrders();

        more_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_more_details();

            }
        });
        return view;
    }

    private void get_more_details() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        service.MORE_DETAILS_CALL(reference).enqueue(new Callback<MoreDetails>() {
            @Override
            public void onResponse(Call<MoreDetails> call, Response<MoreDetails> response) {

                if (response.body().getStatusCode().equals(200)) {

                    more_details_collection_date=response.body().getOrdermoredetails().getCollectionDate();
                    more_details_supplier1=response.body().getOrdermoredetails().getSuppliers();
                    more_details_supplier_address1=response.body().getOrdermoredetails().getAddress();
                    more_details_order_date1=response.body().getOrdermoredetails().getOrderDate();
                    more_details_delivery_date1=response.body().getOrdermoredetails().getDeliveryDate();
                    more_details_description1=response.body().getOrdermoredetails().getOrderDescription();
                    more_details_city1=response.body().getOrdermoredetails().getCity();
                    more_details_postcode1=response.body().getOrdermoredetails().getPostcode();
                    more_details_delivery_customer1=response.body().getOrdermoredetails().getCustomer();
                    more_details_delivery_address1=response.body().getOrdermoredetails().getDeliveryAddress();
                    more_details_delivery_city1=response.body().getOrdermoredetails().getDeliveryCity();
                    more_details_delivery_postcode1=response.body().getOrdermoredetails().getDeliveryPostcode();
                    more_details_instructions1=response.body().getOrdermoredetails().getInstructions();

                    String orders_vanstock = Paper.book().read("orders_vanstock");
                    String orders_reorder = Paper.book().read("orders_reorder");

                    final Dialog dialog= new Dialog(getActivity());

                    dialog.setContentView(R.layout.custom_dialog_more_details);

                    TextView more_details_supplier= dialog.findViewById(R.id.more_details_supplier);
                    TextView more_details_supplier_address= dialog.findViewById(R.id.more_details_supplier_address);
                    TextView more_details_order_date= dialog.findViewById(R.id.more_details_order_date);
                    TextView more_details_delivery_date= dialog.findViewById(R.id.more_details_delivery_date);
                    TextView more_details_description= dialog.findViewById(R.id.more_details_description);
                    TextView more_details_city= dialog.findViewById(R.id.more_details_supplier_city);
                    TextView more_details_postcode= dialog.findViewById(R.id.more_details_supplier_postcode);
                    TextView more_details_delivery_customer= dialog.findViewById(R.id.more_details_delivery_customer);
                    TextView more_details_delivery_address= dialog.findViewById(R.id.more_details_delivery_address);
                    TextView more_details_delivery_city= dialog.findViewById(R.id.more_details_delivery_city);
                    TextView more_details_delivery_postcode= dialog.findViewById(R.id.more_details_delivery_postcode);
                    TextView txtvw_more_supplier= dialog.findViewById(R.id.txtvw_more_supplier);
                    TextView more_details_instructions= dialog.findViewById(R.id.more_details_instructions);
                    TextView txtvw_heading_details= dialog.findViewById(R.id.txtvw_heading_details);


                    layout_instruction = dialog.findViewById(R.id.layout_instruction);
                    layout_customer = dialog.findViewById(R.id.layout_customer);

                    TextView txt_more_detail=dialog.findViewById(R.id.txt_more_detail);
                    LinearLayout layoutmore=dialog.findViewById(R.id.layout_del_details);




                    if(orders_reorder != null) {

                        layout_customer.setVisibility(View.GONE);
                        layout_instruction.setVisibility(View.VISIBLE);

                    }
                    else {

                        layout_instruction.setVisibility(View.GONE);
                        layout_customer.setVisibility(View.VISIBLE);
                    }






                    if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                        txtvw_more_supplier.setText("Business Name :");

                    }

                    Log.e("Orders_van_value",orders_vanstock+"");

                   if(more_details_delivery_date1.equals(String.valueOf(0))){

                       if(orders_vanstock != null && orders_vanstock.equals("1")) {

                           Log.e("Orders_van_value","if");

                           layout_instruction.setVisibility(View.GONE);
                           layout_customer.setVisibility(View.VISIBLE);
                           layoutmore.setVisibility(View.VISIBLE);
                           txt_more_detail.setText("Delivery Date");
                           txtvw_heading_details.setText("Delivery Details");
                           more_details_supplier.setText(more_details_supplier1);
                           more_details_supplier_address.setText(more_details_supplier_address1);
                           more_details_order_date.setText(more_details_order_date1);
                           more_details_delivery_date.setText(more_details_delivery_date1);
                           more_details_description.setText(more_details_description1);
                           more_details_city.setText(more_details_city1);
                           more_details_postcode.setText(more_details_postcode1);
                           more_details_delivery_customer.setText(more_details_delivery_customer1);
                           more_details_delivery_address.setText(more_details_delivery_address1);
                           more_details_delivery_city.setText(more_details_delivery_city1);
                           more_details_delivery_postcode.setText(more_details_delivery_postcode1);
                           more_details_instructions.setText(more_details_instructions1);
                       }


                       else  if(orders_vanstock != null && orders_vanstock.equals("0") && orders_reorder != null && orders_reorder.equals("1")) {

                           Log.e("Orders_van_value","else-if");

                           layout_customer.setVisibility(View.GONE);
                           layout_instruction.setVisibility(View.VISIBLE);
                           layoutmore.setVisibility(View.VISIBLE);
                           txt_more_detail.setText("Delivery Date");
                           txtvw_heading_details.setText("Collection Details");
                           more_details_supplier.setText(more_details_supplier1);
                           more_details_supplier_address.setText(more_details_supplier_address1);
                           more_details_order_date.setText(more_details_order_date1);
                           //more_details_delivery_date.setText(more_details_delivery_date1);
                           more_details_description.setText(more_details_description1);
                           more_details_city.setText(more_details_city1);
                           more_details_postcode.setText(more_details_postcode1);
                           more_details_delivery_customer.setText(more_details_delivery_customer1);
                           more_details_delivery_address.setText(more_details_delivery_address1);
                           more_details_delivery_city.setText(more_details_delivery_city1);
                           more_details_delivery_postcode.setText(more_details_delivery_postcode1);
                           more_details_instructions.setText(more_details_instructions1);


                       }



                       else {

                           Log.e("Orders_van_value","else");

                           layout_customer.setVisibility(View.GONE);
                           layout_instruction.setVisibility(View.VISIBLE);
                           more_details_supplier.setText(more_details_supplier1);
                           more_details_supplier_address.setText(more_details_supplier_address1);
                           more_details_order_date.setText(more_details_order_date1);
                           more_details_delivery_date.setText(more_details_collection_date);
                           more_details_description.setText(more_details_description1);
                           more_details_city.setText(more_details_city1);
                           more_details_postcode.setText(more_details_postcode1);
                           txt_more_detail.setText("Collection Date");
                           more_details_instructions.setText(more_details_instructions1);
                           layoutmore.setVisibility(View.GONE);

                       }


                    }

                    else {


                       if(orders_vanstock != null && orders_vanstock.equals("0") && orders_reorder != null && orders_reorder.equals("1")) {

                           Log.e("Orders_van_value","else-if");

                           layout_customer.setVisibility(View.GONE);
                           layout_instruction.setVisibility(View.VISIBLE);
                           layoutmore.setVisibility(View.VISIBLE);
                           txt_more_detail.setText("Delivery Date");
                           txtvw_heading_details.setText("Collection Details");
                           more_details_supplier.setText(more_details_supplier1);
                           more_details_supplier_address.setText(more_details_supplier_address1);
                           more_details_order_date.setText(more_details_order_date1);
                           more_details_delivery_date.setText(more_details_delivery_date1);
                           more_details_description.setText(more_details_description1);
                           more_details_city.setText(more_details_city1);
                           more_details_postcode.setText(more_details_postcode1);
                           more_details_delivery_customer.setText(more_details_delivery_customer1);
                           more_details_delivery_address.setText(more_details_delivery_address1);
                           more_details_delivery_city.setText(more_details_delivery_city1);
                           more_details_delivery_postcode.setText(more_details_delivery_postcode1);
                           more_details_instructions.setText(more_details_instructions1);


                       }
                       else {


                           layoutmore.setVisibility(View.VISIBLE);
                           layout_instruction.setVisibility(View.GONE);
                           layout_customer.setVisibility(View.VISIBLE);
                           txt_more_detail.setText("Delivery Date");
                           txtvw_heading_details.setText("Delivery Details");
                           more_details_supplier.setText(more_details_supplier1);
                           more_details_supplier_address.setText(more_details_supplier_address1);
                           more_details_order_date.setText(more_details_order_date1);
                           more_details_delivery_date.setText(more_details_delivery_date1);
                           more_details_description.setText(more_details_description1);
                           more_details_city.setText(more_details_city1);
                           more_details_postcode.setText(more_details_postcode1);
                           more_details_delivery_customer.setText(more_details_delivery_customer1);
                           more_details_delivery_address.setText(more_details_delivery_address1);
                           more_details_delivery_city.setText(more_details_delivery_city1);
                           more_details_delivery_postcode.setText(more_details_delivery_postcode1);
                           more_details_instructions.setText(more_details_instructions1);

                       }



                    }

                    dialog.show();

                }
            }

            @Override
            public void onFailure(Call<MoreDetails> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();

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

        String pro_id=Paper.book().read("getProductId");

        final ProgressDialog progressDialogs = new ProgressDialog(getActivity(),R.style.AlertDialogCustom);
        progressDialogs.setCancelable(false);
        progressDialogs.setMessage("Please Wait.......");
        progressDialogs.show();

        service.MY_ORDER_CALL(reference).enqueue(new Callback<ModelMyOrder>() {
            @Override
            public void onResponse(Call<ModelMyOrder> call, Response<ModelMyOrder> response) {

                if (response.body().getStatusCode().equals(200)) {

                    sub_total1=response.body().getTotalEx();
                    total1=response.body().getTotalInc();

                    Paper.book().write("orders_ssub_total1",sub_total1);
                    Paper.book().write("orders_ttotal1",total1);

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
                       /* singleOrderDatum.setIncVat(singleOrderList.get(i).getIncVat());
                        singleOrderDatum.setExVat(singleOrderList.get(i).getExVat());*/
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
