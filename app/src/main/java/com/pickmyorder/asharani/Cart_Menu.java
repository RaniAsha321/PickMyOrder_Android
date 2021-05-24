package com.pickmyorder.asharani;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;

public class Cart_Menu extends Fragment implements MyInterface{

    EditText add_product_description,add_product_quantity,add_product_price,add_product_product_code;
    LinearLayout layout_cart_menu,layout_add_product;
    Home home_activity;
    RecyclerView recyclerview;
    Adapter_Cart_Menu adapterCartMenu;
    List<ModelCartMenu> modelCartMenus;
    LinearLayoutManager layoutManager;
    TextView final_price;
    Button Continue,btn_add_product,btn_close_cart_popup,btn_add_cart_popup;
    private databaseSqlite databaseSqlite;
    Cart_Menu cart_menu;
    String userid,finalRWt;;
    FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart__menu, container, false);
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
        home_activity.nav_search_layout.setVisibility(View.VISIBLE);

        ((Home)getActivity()).hideView(true);
        cart_menu= new Cart_Menu();
        final_price=view.findViewById(R.id.finalprice);
        Continue=view.findViewById(R.id.btn_cart_continue);
        recyclerview=view.findViewById(R.id.recyclerview_Cart_Menu);
        btn_add_product=view.findViewById(R.id.pop_add_product_cart);
        layout_cart_menu=view.findViewById(R.id.layout_cart_menu);
        layout_add_product=view.findViewById(R.id.layout_add_product);
        databaseSqlite=new databaseSqlite(getActivity());

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        modelCartMenus=new ArrayList<>();



        modelCartMenus.addAll(databaseSqlite.getAllModelCartMenu());
        adapterCartMenu=new Adapter_Cart_Menu(getActivity(),modelCartMenus,cart_menu,this);
        recyclerview.setAdapter(adapterCartMenu);

        Paper.book().write("tempmodelcart",modelCartMenus);

        adapterCartMenu.notifyDataSetChanged();

        Paper.book().write("total",databaseSqlite.getAllModelCartMenu().size());

        double value=databaseSqlite.getAllPrices();

        String data= String.format("%.2f", value);

        if(Paper.book().read("permission_add_product", "5").equals("1")){

            layout_add_product.setVisibility(View.VISIBLE);
        }

        else {

            layout_add_product.setVisibility(View.GONE);
        }


        if(Paper.book().read("permission_see_cost","2").equals("1")){
            final_price.setText(data);
        }

        else {

            final_price.setText("0.00");

        }
            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                final_price.setText(data);
        }



        Paper.book().write("final_total",value);

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_product();
                adapterCartMenu.notifyDataSetChanged();

            }
        });

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String business_id=Paper.book().read("business_id");
                String whichbusiness=Paper.book().read("whichbusiness");

                Log.e("sumandeep1",business_id);
                Log.e("sumandeep1",whichbusiness);

                if(!((business_id.equals(whichbusiness)))){

                    Log.e("sumad","1");

                 /*   Log.e("suma","1");
                    Paper.book().write("stripe_publish_key_cart",Paper.book().read("stripe_publish_key"));
                    Paper.book().write("Whole_Seller_product","1");*/

                    if((databaseSqlite.Existsbusiness(whichbusiness))  && (!(business_id.equals(whichbusiness)))){
                        Log.e("suma","1");
                        Paper.book().write("stripe_publish_key_cart",Paper.book().read("stripe_publish_key"));
                        Paper.book().write("Whole_Seller_product","1");

                    }
                    else {
                        Log.e("sumad","11");

                        Paper.book().write("Whole_Seller_product","0");
                    }
                }

                else  if(((business_id.equals(whichbusiness)))) {
                    Log.e("sumad","1");

                 /*
                    Paper.book().write("stripe_publish_key_cart",Paper.book().read("stripe_publish_key"));
                    Paper.book().write("Whole_Seller_product","1");*/

                    if ((databaseSqlite.Existsbusiness(whichbusiness))) {

                        Paper.book().write("Whole_Seller_product","0");

                    }
                }
                else {
                    Log.e("sumad","2");

                    Paper.book().write("Whole_Seller_product","0");
                }

                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("cart").replace(R.id.containerr,new Cart());
                transaction.commit();
            }
        });

        return view;
    }

    private void add_product() {

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_drop_add_product_cart);

        btn_close_cart_popup=(Button) dialog.findViewById(R.id.btn_close_cart_popup);
        btn_add_cart_popup=(Button) dialog.findViewById(R.id.btn_add_cart_popup);
        add_product_description=(EditText)dialog.findViewById(R.id.edtxt_pop_cart_description);
        add_product_quantity=(EditText)dialog.findViewById(R.id.edtxt_pop_cart_quantity);
        add_product_price=(EditText)dialog.findViewById(R.id.edtxt_pop_cart_price);
        add_product_product_code=(EditText)dialog.findViewById(R.id.edtxt_pop_cart_product_code);

        userid= Paper.book().read("userid");
        dialog.show();

        btn_add_cart_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(add_product_description.getText().toString())){

                    if(!TextUtils.isEmpty(add_product_quantity.getText().toString())){

                        String price;

                        if (!(add_product_price.getText().toString()).equals("")){

                            float original_price = Float.parseFloat(add_product_price.getText().toString());
                            finalRWt = ((original_price * 6) / 5) + "";
                            String data= String.format("%.2f", original_price);
                                price = data;
                            }


                        else {
                            price = String.valueOf(0.00);
                            float original_price = Float.parseFloat(price);
                            finalRWt = ((original_price * 6) / 5) + "";
                            price = String.format("%.2f", original_price);


                        }

                        if ((databaseSqlite.ExistsCartProduct(add_product_description.getText().toString()))) {

                            ModelCartMenu modelCartMenu1 = databaseSqlite.getModelProductCartMenu(add_product_description.getText().toString());
                            modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(add_product_quantity.getText().toString())));

                            databaseSqlite.updateNote(modelCartMenu1);

                            if(!(String.valueOf(databaseSqlite.getNotesCount()).equals("0"))) {

                                fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home_category").replace(R.id.containerr, new Cart_Menu());
                                fragmentTransaction.commit();
                            }

                            else {
                                fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home_category").replace(R.id.containerr, new EmptyCart());
                                fragmentTransaction.commit();
                            }

                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                        }

                        else {

                            ModelCartMenu modelCartMenu = new ModelCartMenu();
                            modelCartMenu.setUsersid(userid);
                            modelCartMenu.setAdd_product_code(add_product_product_code.getText().toString());
                            modelCartMenu.setProductid("0");
                            modelCartMenu.setProductname(add_product_description.getText().toString());
                            modelCartMenu.setQuantity(add_product_quantity.getText().toString());

                            if(Paper.book().read("permission_see_cost","2").equals(1)){

                                modelCartMenu.setPrice(price);
                            }

                            else  if(Paper.book().read("datarole", "5").equals("4")){

                                if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                                    modelCartMenu.setPrice(price);

                                }

                            }
                            else {

                                modelCartMenu.setPrice(price);

                            }
                            modelCartMenu.setProductimage("");
                            modelCartMenu.setVariationid("0");
                            modelCartMenu.setInc_vat(finalRWt);
                            modelCartMenu.setVariation_name("");
                            databaseSqlite.insertNote(modelCartMenu);
                            home_activity.cart_size.setText(String.valueOf(databaseSqlite.getNotesCount()));
                            home_activity.cart_size.setBackgroundResource(R.drawable.circle_view);
                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                            if(!(String.valueOf(databaseSqlite.getNotesCount()).equals("0"))) {

                                fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home_category").replace(R.id.containerr, new Cart_Menu());
                                fragmentTransaction.commit();
                            }

                            else {
                                fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home_category").replace(R.id.containerr, new EmptyCart());
                                fragmentTransaction.commit();
                            }
                        }

                        dialog.dismiss();

                    }
                    else{

                        Toast.makeText(getActivity(), " Enter Quantity", Toast.LENGTH_SHORT).show();

                    }
                }

                else{

                    Toast.makeText(getActivity(), " Enter Product Description", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btn_close_cart_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


    }

    public void onAttach (Activity activity) {
        super.onAttach(activity);
        if(activity.getClass()== Home.class){
            home_activity = (Home) activity;
        }
    }
    @Override
    public void foo() {

        if(Paper.book().read("permission_see_cost","2").equals("1")){
            double value=databaseSqlite.getAllPrices();
            String data= String.format("%.2f", value);

            final_price.setText(data);
        }

        else  if(Paper.book().read("datarole", "5").equals("4")){

            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                double value=databaseSqlite.getAllPrices();
                String data= String.format("%.2f", value);

                final_price.setText(data);

            }

        }

        else {

            final_price.setText("0.00");

        }



    }
    @Override
    public void updateCart() {
    }

}
