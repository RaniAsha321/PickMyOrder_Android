package com.pickmyorder.asharani;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class My_Order_AddStock extends Fragment  {


    LinearLayout layout_instruction,layout_customer;
    TextView sub_total,vat,total;
    Button btn_stock_history,btn_add_stock;

    String dated,dateeed,PO_number;
    String reference;
    TextView refernce,date;
    List<SingleOrderDatum> mylist;
    List<SingleOrderDatum> singleOrderList;
    LinearLayoutManager layoutManager,layoutManager_stock;
    RecyclerView recyclerview_order_addstock;
    Adapter_Order_AddStock adapter;
    double vat1;
    List<ModelStock> modelStockList;
    List<ModelStock> modelStockList2;
    database_stock database_stock2;
    JSONArray jsonArray;
    List<VanReorder> vanReorders;
    List<VanReorder> vanReorderList;
    AdapterStockHistory adapterStockHistory;


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
        View view= inflater.inflate(R.layout.fragment_order_addstock, container, false);

        refernce=view.findViewById(R.id.reference_add_stock);
        date=view.findViewById(R.id.date_add_stock);
        btn_add_stock=view.findViewById(R.id.btn_add_stock);
        btn_stock_history = view.findViewById(R.id.btn_stock_history);
        database_stock2= new database_stock(getActivity());
       // modelStockList=database_stock2.getAllModelStockMenu();
        modelStockList = new ArrayList<>();
        dated=Paper.book().read("date");
        dateeed=Paper.book().read("dateeed");
        PO_number=Paper.book().read("PO_Number");
        reference= Paper.book().read("newReference");
        jsonArray = new JSONArray();
        refernce.setText(PO_number);
        date.setText(Paper.book().read("tempdate"));

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("order_screen", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;


                }

                return false;
            }
        });

        recyclerview_order_addstock=view.findViewById(R.id.recyclerview_order_addstock);

        ((Home)getActivity()).hideView(true);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerview_order_addstock.setLayoutManager(layoutManager);

        mylist=new ArrayList<SingleOrderDatum>();

        btn_stock_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Stock_History();

            }
        });


        btn_add_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String reference= Paper.book().read("newReference");
                modelStockList = Paper.book().read("Stock_List");



                if (modelStockList != null && modelStockList.size() != 0){

                    Log.e("papaya",modelStockList.size()+"");

                    String project_id=Paper.book().read("project_id");

                    for(int i=0;i<modelStockList.size();i++) {

                        JSONObject stock = new JSONObject();

                        try {

                            stock.put("product_Id",modelStockList.get(i).getPro_id());
                            stock.put("Order_Id",reference);
                            stock.put("product_Qnty",modelStockList.get(i).getReorder_qty());

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        jsonArray.put(stock);

                    }

                    JSONObject orderobj = new JSONObject();
                    try {
                        orderobj.put("ReorderData", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String reorder_data = orderobj.toString();

                    Log.e("cartvan",reorder_data+"");

                    Stock_API(reorder_data);
                    Log.e("Stock_List95",modelStockList.size()+"");

                }
                else {

                    Toast.makeText(getActivity(),"Please Enter quantity",Toast.LENGTH_SHORT).show();
                }



/*

                  modelStockList2 = Paper.book().read("Stock_List",new ArrayList<ModelStock>());

                  if (modelStockList2.size() != 0){

                      for (int i = 0; i<modelStockList2.size();i++){

                          Log.e("Stock_List9",modelStockList2.get(i).getPro_id()+"");
                          Log.e("Stock_List9",modelStockList2.get(i).getReorder_qty()+"");
                      }

                   */
/*   Log.e("Stock_List",modelStockList2.get(0).getPro_id()+"");
                      Log.e("Stock_List",modelStockList2.size()+"");*//*


                  }
*/





            }
        });


        MyOrders();

        return view;
    }

    private void Stock_History() {

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

        service.STOCK_HISTORY(reference).enqueue(new Callback<StockDetails>() {
            @Override
            public void onResponse(Call<StockDetails> call, Response<StockDetails> response) {


                assert response.body() != null;

                vanReorders = response.body().getVanReorder();

                if(response.body().getStatusCode().equals(200)) {

                    vanReorderList = new ArrayList<>();

                    for (int i = 0; i < vanReorderList.size(); i++) {

                        VanReorder vanReorder = new VanReorder();
                        vanReorder.setProductname(response.body().getVanReorder().get(i).getProductname());
                        vanReorder.setQty(response.body().getVanReorder().get(i).getQty());
                        vanReorder.setDateTime(response.body().getVanReorder().get(i).getDateTime());

                        vanReorders.add(vanReorder);
                    }

                    final Dialog dialog= new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_stock_history);
                    RecyclerView recyclerView_stock = dialog.findViewById(R.id.review_stock_history);

                    layoutManager_stock = new LinearLayoutManager(getActivity());
                    recyclerView_stock.setLayoutManager(layoutManager_stock);
                    Button btn_close = dialog.findViewById(R.id.btn_stock_history);

                    adapterStockHistory = new AdapterStockHistory(getActivity(), vanReorders);
                    recyclerView_stock.setAdapter(adapterStockHistory);

                    dialog.show();

                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });

                    dialog.setCancelable(false);


                }

                else

                {
                    Toast.makeText(getActivity(),"No Stock History", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<StockDetails> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }
        });


       /* final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_stock_history);
        RecyclerView recyclerView_stock = dialog.findViewById(R.id.review_stock_history);

        dialog.show();

        dialog.setCancelable(false);*/
    }

    private void Stock_API(String reorder_data) {

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

        service.ADDSTOCK(reorder_data).enqueue(new Callback<AddVanStock>() {
            @Override
            public void onResponse(Call<AddVanStock> call, Response<AddVanStock> response) {

                assert response.body() != null;
                if(response.body().getStatusCode().equals(200)){

                    Toast.makeText(getActivity(), "Added Successfully", Toast.LENGTH_SHORT).show();

                    if (response.body().getOrderComplete().equals(1)){

                        Paper.book().write("reorder_value_orders","1");
                        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containerr,new Orders_Menu());
                        fragmentTransaction.commit();
                    }
                    else {

                        FragmentTransaction transactions = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("o").replace(R.id.containerr, new My_Order_Screen());
                        transactions.commit();
                    }


                }

            }

            @Override
            public void onFailure(Call<AddVanStock> call, Throwable t) {

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
                        singleOrderDatum.setProductId(singleOrderList.get(i).getProductId());
                        singleOrderDatum.setPendingItem(singleOrderList.get(i).getPendingItem());
                        singleOrderDatum.setVariationOptionName(singleOrderList.get(i).getVariationOptionName());

                        mylist.add(singleOrderDatum);

                        Paper.book().write("date",mylist.get(i).getDate());
                    }

                    My_Order_AddStock my_order_addStock =new My_Order_AddStock();

                    adapter = new Adapter_Order_AddStock(getActivity(), mylist,my_order_addStock);
                    recyclerview_order_addstock.setAdapter(adapter);

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


    public void getData(String productId, String qty, String reorder, EditText edtxt_add_qty, Integer position, Integer size, List<ModelStock> modelStockListada) {

        Log.e("order23423",reorder.length()+"");
        Log.e("productId",productId+"");

        database_stock db=new database_stock(getActivity());

        String reference= Paper.book().read("newReference");

        modelStockList2 = Paper.book().read("Stock_List");

        if (reorder.length() != 0 ){

            Log.e("orange", "1");

            Log.e("mango1", modelStockList2.size()+"");

            if (modelStockList2 != null && modelStockList2.size() != 0) {

                Log.e("orange", "2");

                for (int i=0;i<modelStockList2.size();i++) {

                Log.e("mango2", modelStockList2.size() + "");


                if (modelStockList2.get(i).getPro_id() != null) {

                    Log.e("orange1", "1");

                    if (!(modelStockList2.get(i).getPro_id().equals(productId))) {

                        Log.e("orange1", "2");

                        if (modelStockList2 != null && modelStockList2.size() != 0) {

                            Log.e("orange1", "3");

                            ModelStock modelStock = new ModelStock();
                            modelStock.setPro_id(productId);
                            modelStock.setOrdered_qty(qty);
                            modelStock.setReorder_qty(reorder);

                            modelStockList2.addAll(Arrays.asList(modelStock));

                            Log.e("mango3", modelStockList2.size() + "");

                            Paper.book().write("Stock_List", modelStockList2);

                        } else {

                            Log.e("orange1", "4");

                            ModelStock modelStock = new ModelStock();
                            modelStock.setPro_id(productId);
                            modelStock.setOrdered_qty(qty);
                            modelStock.setReorder_qty(reorder);

                            modelStockList2.add(modelStock);

                            Log.e("mango4", modelStockList2.size() + "");

                            Paper.book().write("Stock_List", modelStockList2);
                        }
                    }

                    else {

                        Log.e("orange", "33");

                        ModelStock modelStock = new ModelStock();
                        modelStock.setPro_id(productId);
                        modelStock.setOrdered_qty(qty);
                        modelStock.setReorder_qty(reorder);

                        modelStockListada.add(modelStock);

                        Log.e("mango5", modelStockListada.size() + "");

                        Paper.book().write("Stock_List", modelStockListada);



                    }

                }

            }

            }

            else {

                Log.e("orange", "3");

                ModelStock modelStock = new ModelStock();
                modelStock.setPro_id(productId);
                modelStock.setOrdered_qty(qty);
                modelStock.setReorder_qty(reorder);

                modelStockListada.add(modelStock);

                Log.e("mango6", modelStockListada.size() + "");

                Paper.book().write("Stock_List", modelStockListada);
            }



        }

       /* else {

            Toast.makeText(getActivity(),"Please Enter quantity",Toast.LENGTH_SHORT).show();
        }
*/





    }
}
