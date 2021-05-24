package com.pickmyorder.asharani;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
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
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.pickmyorder.asharani.Login.MY_PREFS_NAME;

public class Product_Detail extends Fragment {

    PopupWindow popUp;
    List<String> variation_drop;
    ArrayList<Variation> variations;
    LinearLayout variation,mainLayout,layout_close,layout_close_bottom,layout_product_stock;
    ListView list;
    List<Variation> variationList;
    Spinner spinner;
    Home homesize;
    List<Model_Product_Description> mlist;
    ImageView mainImage,first_image,second_image,Img_already_cart;
    TabLayout tabLayout;
    ViewPager viewPager;
    List<Product> modelProductsCategories;
    EditText number_of_items;
    Button btn_cart,btn_close_exist_item,btn_continue_exist_item;
    Home home;
    List<ModelCartMenu> cartMenuList;
    EditText quantity;
    TextView price,name,tv_drop,txtvw_product_stock,txtvw_outofStock;
    int hasornot;
    Description_Tab description_tab;
    String userid,pro_id,selectedItemText,variation_name,variation_id,product_id,product_first_img,product_second_img, product_name,product_description,product_specification,product_reviews,product_price,product_inc_vat;
    private databaseSqlite db;
    String vanstock,in_stock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product__detail, container, false);

        Paper.book().write("pop_cancel_up","1");

        quantity=view.findViewById(R.id.number_item);
        mainImage=view.findViewById(R.id.main_image);
        first_image=view.findViewById(R.id.image_one);
        second_image=view.findViewById(R.id.image_two);
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpagerr);
        price=view.findViewById(R.id.price);
        name=view.findViewById(R.id.pro_name);
        number_of_items=view.findViewById(R.id.number_item);
        btn_cart=view.findViewById(R.id.btn_cart);
        Img_already_cart=view.findViewById(R.id.Img_already);
        spinner=view.findViewById(R.id.variations_spinner);
        variation=view.findViewById(R.id.variationlayout);
        quantity.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        mainLayout=view.findViewById(R.id.product_layout);
        tv_drop=view.findViewById(R.id.tv_drop);
        txtvw_product_stock =view.findViewById(R.id.txtvw_product_stock);
        layout_product_stock =view.findViewById(R.id.layout_product_stock);
        txtvw_outofStock = view.findViewById(R.id.txtvw_outofStock);


        description_tab=new Description_Tab();

        db = new databaseSqlite(getActivity());

        vanstock = Paper.book().read("vanstock");
        in_stock = Paper.book().read("in_stock");

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {

                    getFragmentManager().popBackStack("mo", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    return true;
                }

                return false;
            }
        });




        if (vanstock != null && vanstock.equals("1")){


            if (in_stock != null && Integer.valueOf(in_stock) < 1 ){

                btn_cart.setVisibility(View.GONE);
                txtvw_outofStock.setVisibility(View.VISIBLE);
                layout_product_stock.setVisibility(View.GONE);


            }
            else {

                btn_cart.setVisibility(View.VISIBLE);
                txtvw_outofStock.setVisibility(View.GONE);
                layout_product_stock.setVisibility(View.VISIBLE);
                txtvw_product_stock.setText(in_stock);
            }




        }
        else {

            layout_product_stock.setVisibility(View.GONE);
        }





        variations=new ArrayList<>();
        variation_drop=new ArrayList<>();

        homesize.search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                homesize.search_txtvw.setFocusableInTouchMode(true);

            }
        });

        quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quantity.setFocusableInTouchMode(true);

            }
        });

        variation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dropdownmenu();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedItemText =  spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                Paper.book().write("selectedText",selectedItemText);

                TextView tv = (TextView) view;

                tv.setTextColor(Color.GRAY);
                tv.setText(variations.get(position).getOptionName());

                if(Paper.book().read("permission_see_cost","2").equals("1")){
                    price.setText(product_price);
                }

                else {

                    price.setText("0.00");

                }


                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    tv.setText(variations.get(position).getOptionName());


                    if(Paper.book().read("permission_see_cost","2").equals("1")) {
                        price.setText(product_price);
                    }

                    else {

                        price.setText("0.00");

                    }

                    Paper.book().write("tempid",variations.get(position).getVariationId());


                }
                else {
                    tv.setTextColor(Color.BLACK);
                   tv.setText(variations.get(position).getName());

                    if(Paper.book().read("permission_see_cost","2").equals("1")){
                        price.setText(variations.get(position).getPrice());

                        Paper.book().write("product_description",variations.get(position).getDescription());

                    }

                    else {

                        price.setText("0.00");

                    }

                    description_tab.description.setText(variations.get(position).getDescription());

                    Paper.book().write("product_price",variations.get(position).getPrice());
                    Paper.book().write("variation_name",variations.get(position).getName());
                    Paper.book().write("tempid",variations.get(position).getVariationId());
                    Paper.book().write("pro_inc_vat",variations.get(position).getIncPrice());
                    Paper.book().write("product_description",variations.get(position).getDescription());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cartMenuList=new ArrayList<>();
        cartMenuList=Paper.book().read("Menus",new ArrayList<ModelCartMenu>());

        userid= Paper.book().read("userid");
        pro_id=Paper.book().read("pro_id");

        Paper.book().write("no_item",number_of_items.getText().toString().trim());

        modelProductsCategories= Paper.book().read("products",new ArrayList<Product>());

        mlist=new ArrayList<>();

        getSuperDescription();
        getvariations();

        addtodatabase();

        viewPager.setAdapter(new Adapter_Viewpager(getFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void dropdownmenu() {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_dropdown_menu, null);

        layout_close = (LinearLayout) customView.findViewById(R.id.layout_popup_upper);
        layout_close_bottom=(LinearLayout) customView.findViewById(R.id.layout_popup_bottom);
        list = customView.findViewById(R.id.list);
        //instantiate popup window
        popUp = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, variation_drop);

        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = (TextView) view.findViewById(R.id.textview_drop);

                if(Paper.book().read("permission_see_cost","2").equals("1")){
                    price.setText(product_price);
                }

                else {

                    price.setText("0.00");

                }

                if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                    price.setText(product_price);

                }


                if(position == 0){

                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                    tv_drop.setText(variation_drop.get(position).toString());

                    position=position+1;

                    if(Paper.book().read("permission_see_cost","2").equals("1")) {

                        price.setText(variations.get(position).getPrice());
                    }

                    else  if(Paper.book().read("datarole", "5").equals("4")){

                        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                            price.setText(variations.get(position).getPrice());

                        }

                    }

                    else {

                        price.setText("0.00");

                    }

                    description_tab.description.setText(variations.get(position).getDescription());

                    Paper.book().write("product_price",variations.get(position).getPrice());
                    Paper.book().write("variation_name",variations.get(position).getName());
                    Paper.book().write("tempid",variations.get(position).getVariationId());
                    Paper.book().write("pro_inc_vat",variations.get(position).getIncPrice());
                    Paper.book().write("product_description",variations.get(position).getDescription());


                }
                else {

                    tv.setTextColor(Color.BLACK);
                    tv_drop.setText(variation_drop.get(position).toString());
                    position=position+1;

                    if(Paper.book().read("permission_see_cost","2").equals("1")){
                        price.setText(variations.get(position).getPrice());

                        Paper.book().write("product_description",variations.get(position).getDescription());

                    }


                    else  if(Paper.book().read("datarole", "5").equals("4")){

                        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                            price.setText(variations.get(position).getPrice());

                            Paper.book().write("product_description",variations.get(position).getDescription());

                        }

                    }

                    else {

                        price.setText("0.00");

                    }

                    description_tab.description.setText(variations.get(position).getDescription());

                    Paper.book().write("product_price",variations.get(position).getPrice());
                    Paper.book().write("variation_name",variations.get(position).getName());
                    Paper.book().write("tempid",variations.get(position).getVariationId());
                    Paper.book().write("pro_inc_vat",variations.get(position).getIncPrice());
                    Paper.book().write("product_description",variations.get(position).getDescription());

                }

                popUp.dismiss();
            }
        });
        //display the popup window
        popUp.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

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

    private void getvariations() {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiLogin_Interface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //Defining retrofit api service
            ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

            String productid= Paper.book().read("getProductId");
            service.VARIATIONS_CALL(productid).enqueue(new Callback<ModelVariations>() {

                @Override
                public void onResponse(Call<ModelVariations> call, Response<ModelVariations> response) {

                    Paper.book().write("hasornot",response.body().getHasornot());

                    if(response.body().getStatusCode().equals(200)) {

                        if (response.body().getHasornot().equals(1)) {

                            variationList = response.body().getVariation();

                            for (int i = 0; i < variationList.size(); i++) {

                                Variation variation = new Variation();

                                variation.setPlaseholder(response.body().getVariation().get(i).getPlaseholder());
                                variation.setOptionName(response.body().getVariation().get(i).getOptionName());
                                variation.setVariationId(response.body().getVariation().get(i).getVariationId());
                                variation.setPrice(response.body().getVariation().get(i).getPrice());
                                variation.setName(response.body().getVariation().get(i).getName());
                                variation.setIncPrice(response.body().getVariation().get(i).getIncPrice());
                                variation.setDescription(response.body().getVariation().get(i).getDescription());
                                variation.setName(response.body().getVariation().get(i).getName());

                                variations.add(variation);

                            }

                            for(int i = 1; i < variationList.size(); i++) {

                                Variation variation = new Variation();
                                variation.setName(response.body().getVariation().get(i).getName());
                                variation_drop.add(response.body().getVariation().get(i).getName());

                            }

                            final ArrayAdapter<Variation> spinnerArrayAdapter = new ArrayAdapter<Variation>(getActivity(), R.layout.spinner_item, variations) {

                                @Override
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
                                    View view = super.getDropDownView(position, convertView, parent);

                                    TextView tv = (TextView) view;

                                    tv.setTextColor(Color.GRAY);
                                    tv.setText(variations.get(position).getOptionName());

                                    if (position == 0) {

                                        tv.setTextColor(Color.BLACK);
                                        tv.setBackgroundResource(R.color.dropgrey);

                                    } else {
                                        tv.setTextColor(Color.BLACK);
                                        tv.setText(variations.get(position).getName());

                                    }

                                    return view;
                                }

                            };

                            spinner.setAdapter(spinnerArrayAdapter);
                        }

                        else if(response.body().getHasornot().equals(0)) {

                            variation.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelVariations> call, Throwable t) {

                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
    }
    private void getSuperDescription() {

        product_id= Paper.book().read("getProductId");
        product_name= Paper.book().read("product_name");
        product_first_img= Paper.book().read("product_first_img");
        product_second_img= Paper.book().read("product_second_img");
        product_description= Paper.book().read("product_description");
        product_reviews= Paper.book().read("product_pdf_info");
        product_specification= Paper.book().read("product_specification");
        product_price=Paper.book().read("product_price");

        if(!product_first_img.equals("")){

            Glide.with(getActivity()).load(product_first_img).into(first_image);
        }

        else {

            first_image.setVisibility(View.GONE);
        }

        if(!product_second_img.equals("")){

            Glide.with(getActivity()).load(product_second_img).into(second_image);
        }

        else {

            second_image.setVisibility(View.GONE);
        }

        Glide.with(getActivity()).load(product_first_img).into(mainImage);

        first_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getActivity()).load(product_first_img).into(mainImage);

            }
        });
        second_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getActivity()).load(product_second_img).into(mainImage);

            }
        });

        if(Paper.book().read("permission_see_cost","2").equals("1")){
            price.setText(product_price);
        }

        else {

            price.setText("0.00");

        }

        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

            price.setText(product_price);

        }


        name.setText(product_name);

        Paper.book().write("tempid","0");

        if(db.Exists(product_id)){

            Img_already_cart.setBackgroundResource(R.drawable.circle_view);

        }
    }

    private void addtodatabase() {

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Paper.book().write("pay_Card_status","0");
                String whichbusiness=Paper.book().read("whichbusiness");
                String publish_key=Paper.book().read("publish_key");


                String business_id=Paper.book().read("business_id");

                Log.e("Variation_get",variation_id+"");

                if((db.getNotesCount()==0) ){


                    if (vanstock != null && vanstock.equals("1") && !quantity.getText().toString().equals("") && Integer.valueOf(quantity.getText().toString()) <= Integer.valueOf(in_stock)) {

                        Log.e("suman","2");

                        Paper.book().write("cart_empty_key","10");

                        String business_id_2=Paper.book().read("business_id");
                        String whichbusiness_2=Paper.book().read("whichbusiness");
                        String wholeseller_bus_id=Paper.book().read("wholeseller_bus_id");

                        Log.e("sumandeep1",wholeseller_bus_id+"");
                        Log.e("sumandeep2",business_id_2+"");
                        Log.e("sumandeep3",whichbusiness_2+"");
                        Log.e("sumandeep4",publish_key+"");


                        if((business_id_2.equals(whichbusiness_2))){

                            Paper.book().write("stripe_publish_key","0");

                            Paper.book().write("save_business","0");
                        }

                        else{

                            SharedPreferences pref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("stripe_publish_key",publish_key);

                            editor.apply();
                            if (wholeseller_bus_id !=null){
                                Paper.book().write("save_business",wholeseller_bus_id);                        }

                        }

                  /*  SharedPreferences pref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("stripe_publish_key",publish_key);

                    editor.apply();*/


                        homesize.cart_size.setVisibility(View.VISIBLE);
                        product_price=Paper.book().read("product_price");
                        variation_id=Paper.book().read("tempid");
                        product_inc_vat=Paper.book().read("pro_inc_vat");
                        hasornot=Paper.book().read("hasornot");

                        if(hasornot==0){

                            String tempid=String.valueOf(0);

                            Paper.book().write("tempid",tempid);

                        }

                        String value=quantity.getText().toString();

                        if((!TextUtils.isEmpty(quantity.getText().toString()))) {
                            if ((!value.equals(String.valueOf(0)))) {

                                if (hasornot == 1) {

                                    if (!variation_id.equals(String.valueOf(0))) {

                                        variation_name=Paper.book().read("variation_name");

                                        if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {


                                            ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                            modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                            db.updateVNote(modelCartMenu1);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                        } else {

                                            ModelCartMenu modelCartMenu = new ModelCartMenu();

                                            if(Paper.book().read("permission_see_cost","2").equals("1")){

                                                modelCartMenu.setPrice(product_price);
                                            }

                                            else {

                                                modelCartMenu.setPrice(product_price);

                                            }

                                            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                                                modelCartMenu.setPrice(product_price);

                                            }

                                            modelCartMenu.setUsersid(userid);
                                            modelCartMenu.setProductid(product_id);
                                            modelCartMenu.setProductname(product_name);
                                            modelCartMenu.setQuantity(quantity.getText().toString());
                                            modelCartMenu.setProductimage(product_first_img);
                                            modelCartMenu.setVariationid(variation_id);
                                            modelCartMenu.setInc_vat(product_inc_vat);
                                            modelCartMenu.setVariation_name(variation_name);
                                            modelCartMenu.setAdd_product_code(String.valueOf(0));
                                            modelCartMenu.setBusinessid(whichbusiness);
                                            modelCartMenu.setProduct_type(vanstock);

                                            db.insertNote(modelCartMenu);

                                            homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                            homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                        }
                                    } else {

                                        Toast.makeText(getActivity(), "Please Select Variation", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {

                                    variation_name="";

                                    if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {

                                        ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                        modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                        db.updateVNote(modelCartMenu1);

                                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                    } else {

                                        ModelCartMenu modelCartMenu = new ModelCartMenu();
                                        modelCartMenu.setUsersid(userid);
                                        modelCartMenu.setProductid(product_id);
                                        modelCartMenu.setProductname(product_name);
                                        modelCartMenu.setQuantity(quantity.getText().toString());


                                        if(Paper.book().read("permission_see_cost","2").equals("1")){
                                            modelCartMenu.setPrice(product_price);
                                        }

                                        else {
                                            modelCartMenu.setPrice(product_price);

                                        }

                                        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                                            modelCartMenu.setPrice(product_price);

                                        }

                                        modelCartMenu.setAdd_product_code(String.valueOf(0));
                                        modelCartMenu.setProductimage(product_first_img);
                                        modelCartMenu.setVariationid(variation_id);
                                        modelCartMenu.setInc_vat(product_inc_vat);
                                        modelCartMenu.setVariation_name(variation_name);
                                        modelCartMenu.setBusinessid(whichbusiness);
                                        modelCartMenu.setProduct_type(vanstock);
                                        db.insertNote(modelCartMenu);

                                        homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                        homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            else {

                                Toast.makeText(getActivity(), "Zero is Not valid", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else
                        {
                            Toast.makeText(getActivity(),"Please Enter Quantity",Toast.LENGTH_SHORT).show();
                        }

                        quantity.setText("");
                        quantity.setFocusable(false);
                        homesize.search_txtvw.setFocusable(false);

                    }
                    else if (vanstock != null && vanstock.equals("0") ) {


                        Log.e("suman","2");

                        Paper.book().write("cart_empty_key","10");

                        String business_id_2=Paper.book().read("business_id");
                        String whichbusiness_2=Paper.book().read("whichbusiness");
                        String wholeseller_bus_id=Paper.book().read("wholeseller_bus_id");

                        Log.e("sumandeep1",wholeseller_bus_id+"");
                        Log.e("sumandeep2",business_id_2+"");
                        Log.e("sumandeep3",whichbusiness_2+"");
                        Log.e("sumandeep4",publish_key+"");


                        if((business_id_2.equals(whichbusiness_2))){

                            Paper.book().write("stripe_publish_key","0");

                            Paper.book().write("save_business","0");
                        }

                        else{

                            SharedPreferences pref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("stripe_publish_key",publish_key);

                            editor.apply();
                            if (wholeseller_bus_id !=null){
                                Paper.book().write("save_business",wholeseller_bus_id);                        }

                        }

                  /*  SharedPreferences pref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("stripe_publish_key",publish_key);

                    editor.apply();*/


                        homesize.cart_size.setVisibility(View.VISIBLE);
                        product_price=Paper.book().read("product_price");
                        variation_id=Paper.book().read("tempid");
                        product_inc_vat=Paper.book().read("pro_inc_vat");
                        hasornot=Paper.book().read("hasornot");

                        if(hasornot==0){

                            String tempid=String.valueOf(0);

                            Paper.book().write("tempid",tempid);

                        }

                        String value=quantity.getText().toString();

                        if((!TextUtils.isEmpty(quantity.getText().toString()))) {
                            if ((!value.equals(String.valueOf(0)))) {

                                if (hasornot == 1) {

                                    if (!variation_id.equals(String.valueOf(0))) {

                                        variation_name=Paper.book().read("variation_name");

                                        if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {


                                            ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                            modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                            db.updateVNote(modelCartMenu1);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                        } else {

                                            ModelCartMenu modelCartMenu = new ModelCartMenu();

                                            if(Paper.book().read("permission_see_cost","2").equals("1")){

                                                modelCartMenu.setPrice(product_price);
                                            }

                                            else {

                                                modelCartMenu.setPrice(product_price);

                                            }

                                            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                                                modelCartMenu.setPrice(product_price);

                                            }

                                            modelCartMenu.setUsersid(userid);
                                            modelCartMenu.setProductid(product_id);
                                            modelCartMenu.setProductname(product_name);
                                            modelCartMenu.setQuantity(quantity.getText().toString());
                                            modelCartMenu.setProductimage(product_first_img);
                                            modelCartMenu.setVariationid(variation_id);
                                            modelCartMenu.setInc_vat(product_inc_vat);
                                            modelCartMenu.setVariation_name(variation_name);
                                            modelCartMenu.setAdd_product_code(String.valueOf(0));
                                            modelCartMenu.setBusinessid(whichbusiness);
                                            modelCartMenu.setProduct_type(vanstock);

                                            db.insertNote(modelCartMenu);

                                            homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                            homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                        }
                                    } else {

                                        Toast.makeText(getActivity(), "Please Select Variation", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {

                                    variation_name="";

                                    if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {

                                        ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                        modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                        db.updateVNote(modelCartMenu1);

                                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                    } else {

                                        ModelCartMenu modelCartMenu = new ModelCartMenu();
                                        modelCartMenu.setUsersid(userid);
                                        modelCartMenu.setProductid(product_id);
                                        modelCartMenu.setProductname(product_name);
                                        modelCartMenu.setQuantity(quantity.getText().toString());


                                        if(Paper.book().read("permission_see_cost","2").equals("1")){
                                            modelCartMenu.setPrice(product_price);
                                        }

                                        else {
                                            modelCartMenu.setPrice(product_price);

                                        }

                                        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                                            modelCartMenu.setPrice(product_price);

                                        }

                                        modelCartMenu.setAdd_product_code(String.valueOf(0));
                                        modelCartMenu.setProductimage(product_first_img);
                                        modelCartMenu.setVariationid(variation_id);
                                        modelCartMenu.setInc_vat(product_inc_vat);
                                        modelCartMenu.setVariation_name(variation_name);
                                        modelCartMenu.setBusinessid(whichbusiness);
                                        modelCartMenu.setProduct_type(vanstock);
                                        db.insertNote(modelCartMenu);

                                        homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                        homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            else {

                                Toast.makeText(getActivity(), "Zero is Not valid", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else
                        {
                            Toast.makeText(getActivity(),"Please Enter Quantity",Toast.LENGTH_SHORT).show();
                        }

                        quantity.setText("");
                        quantity.setFocusable(false);
                        homesize.search_txtvw.setFocusable(false);
                    }

                    else {

                        Toast.makeText(getActivity(), quantity.getText().toString()+": Quantity is not in Stock "+"" ,Toast.LENGTH_SHORT).show();

                    }



                }



                else if ( vanstock.equals("0") && db.ExistsProType("0")){

                    if (!(db.Existsbusiness(whichbusiness)) ){

                        Log.e("numan","1");

                        final Dialog dialog= new Dialog(getActivity());
                        dialog.setContentView(R.layout.custom_dropdown_already_exists_data);
                        btn_close_exist_item=dialog.findViewById(R.id.btn_close_exist_item);
                        btn_continue_exist_item=dialog.findViewById(R.id.btn_continue_exist_item);
                        dialog.show();
                        btn_close_exist_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });

                        btn_continue_exist_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Paper.book().write("save_business",Paper.book().read("wholeseller_bus_id"));

                                SharedPreferences pref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString("stripe_publish_key",publish_key);

                                editor.apply();
                                db.deleteAll();

                                homesize.cart_size.setVisibility(View.VISIBLE);
                                product_price=Paper.book().read("product_price");
                                variation_id=Paper.book().read("tempid");
                                product_inc_vat=Paper.book().read("pro_inc_vat");
                                hasornot=Paper.book().read("hasornot");

                                if(hasornot==0){

                                    String tempid=String.valueOf(0);

                                    Paper.book().write("tempid",tempid);

                                }

                                String value=quantity.getText().toString();

                                if((!TextUtils.isEmpty(quantity.getText().toString()) )) {
                                    if ((!value.equals(String.valueOf(0)))) {

                                        if (hasornot == 1) {

                                            if (!variation_id.equals(String.valueOf(0))) {

                                                variation_name=Paper.book().read("variation_name");

                                                if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {


                                                    ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                                    modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                                    db.updateVNote(modelCartMenu1);

                                                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                                } else {

                                                    ModelCartMenu modelCartMenu = new ModelCartMenu();

                                                    if(Paper.book().read("permission_see_cost","2").equals("1")){

                                                        modelCartMenu.setPrice(product_price);
                                                    }

                                                    else {
                                                        modelCartMenu.setPrice(product_price);

                                                    }

                                                    if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                                                        modelCartMenu.setPrice(product_price);

                                                    }

                                                    modelCartMenu.setUsersid(userid);
                                                    modelCartMenu.setProductid(product_id);
                                                    modelCartMenu.setProductname(product_name);
                                                    modelCartMenu.setQuantity(quantity.getText().toString());
                                                    modelCartMenu.setProductimage(product_first_img);
                                                    modelCartMenu.setVariationid(variation_id);
                                                    modelCartMenu.setInc_vat(product_inc_vat);
                                                    modelCartMenu.setVariation_name(variation_name);
                                                    modelCartMenu.setAdd_product_code(String.valueOf(0));
                                                    modelCartMenu.setBusinessid(whichbusiness);
                                                    modelCartMenu.setProduct_type(vanstock);

                                                    db.insertNote(modelCartMenu);

                                                    homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                                    homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                                }
                                            } else {

                                                Toast.makeText(getActivity(), "Please Select Variation", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {

                                            variation_name="";


                                            if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {

                                                ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                                modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                                db.updateVNote(modelCartMenu1);

                                                Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                            } else {

                                                ModelCartMenu modelCartMenu = new ModelCartMenu();
                                                modelCartMenu.setUsersid(userid);
                                                modelCartMenu.setProductid(product_id);
                                                modelCartMenu.setProductname(product_name);
                                                modelCartMenu.setQuantity(quantity.getText().toString());


                                                if(Paper.book().read("permission_see_cost","2").equals("1")){
                                                    modelCartMenu.setPrice(product_price);
                                                }

                                                else {
                                                    modelCartMenu.setPrice(product_price);

                                                }

                                                if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                                                    modelCartMenu.setPrice(product_price);

                                                }

                                                modelCartMenu.setAdd_product_code(String.valueOf(0));
                                                modelCartMenu.setProductimage(product_first_img);
                                                modelCartMenu.setVariationid(variation_id);
                                                modelCartMenu.setInc_vat(product_inc_vat);
                                                modelCartMenu.setVariation_name(variation_name);
                                                modelCartMenu.setBusinessid(whichbusiness);
                                                modelCartMenu.setProduct_type(vanstock);
                                                db.insertNote(modelCartMenu);

                                                homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                                homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                                Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                    else {

                                        Toast.makeText(getActivity(), "Zero is Not valid", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                else
                                {
                                    Toast.makeText(getActivity(),"Please Enter Quantity",Toast.LENGTH_SHORT).show();
                                }

                                quantity.setText("");
                                quantity.setFocusable(false);
                                homesize.search_txtvw.setFocusable(false);
                                dialog.dismiss();

                                // Paper.book().write("whichbusiness","0");
                            }


                        });

                    }

                    else {


                        Log.e("numan","3");

                            homesize.cart_size.setVisibility(View.VISIBLE);

                            product_price=Paper.book().read("product_price");
                            variation_id=Paper.book().read("tempid");
                            product_inc_vat=Paper.book().read("pro_inc_vat");
                            hasornot=Paper.book().read("hasornot");

                            if(hasornot==0){

                                String tempid=String.valueOf(0);

                                Paper.book().write("tempid",tempid);

                            }

                            String value=quantity.getText().toString();

                            if((!TextUtils.isEmpty(quantity.getText().toString()) )) {
                                if ((!value.equals(String.valueOf(0)))) {

                                    Log.e("numan","51");
                                    Log.e("numanhas",hasornot+"");
                                    if (hasornot == 1) {
                                        Log.e("numan","521");
                                        if (!variation_id.equals(String.valueOf(0))) {

                                            Log.e("numan","53");

                                            variation_name=Paper.book().read("variation_name");

                                            if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {

                                                Log.e("numan","31");


                                                ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);
                                                modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                                db.updateVNote(modelCartMenu1);

                                                Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                            } else {

                                                Log.e("numan","32");

                                                ModelCartMenu modelCartMenu = new ModelCartMenu();

                                                if(Paper.book().read("permission_see_cost","2").equals("1")){

                                                    modelCartMenu.setPrice(product_price);
                                                }

                                                else {
                                                    modelCartMenu.setPrice(product_price);

                                                }

                                                if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                                                    modelCartMenu.setPrice(product_price);

                                                }

                                                modelCartMenu.setUsersid(userid);
                                                modelCartMenu.setProductid(product_id);
                                                modelCartMenu.setProductname(product_name);
                                                modelCartMenu.setQuantity(quantity.getText().toString());
                                                modelCartMenu.setProductimage(product_first_img);
                                                modelCartMenu.setVariationid(variation_id);
                                                modelCartMenu.setInc_vat(product_inc_vat);
                                                modelCartMenu.setVariation_name(variation_name);
                                                modelCartMenu.setAdd_product_code(String.valueOf(0));
                                                modelCartMenu.setProduct_type(vanstock);
                                                modelCartMenu.setBusinessid(whichbusiness);

                                                db.insertNote(modelCartMenu);

                                                homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                                homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                                Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                            }
                                        } else {

                                            Toast.makeText(getActivity(), "Please Select Variation", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {

                                        Log.e("nikhil_pid",product_id+"");
                                        Log.e("nikhil_vid",variation_id+"");

                                        Log.e("numan","522");
                                        variation_name="";

                                        Log.e("numan11",variation_id+"");

                                        if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {

                                            ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);
                                            modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                            db.updateVNote(modelCartMenu1);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                        } else {

                                            ModelCartMenu modelCartMenu = new ModelCartMenu();
                                            modelCartMenu.setUsersid(userid);
                                            modelCartMenu.setProductid(product_id);
                                            modelCartMenu.setProductname(product_name);
                                            modelCartMenu.setQuantity(quantity.getText().toString());


                                            if(Paper.book().read("permission_see_cost","2").equals("1")){
                                                modelCartMenu.setPrice(product_price);
                                            }

                                            else {
                                                modelCartMenu.setPrice(product_price);

                                            }

                                            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                                                modelCartMenu.setPrice(product_price);

                                            }

                                            modelCartMenu.setAdd_product_code(String.valueOf(0));
                                            modelCartMenu.setProductimage(product_first_img);
                                            modelCartMenu.setVariationid(variation_id);
                                            modelCartMenu.setInc_vat(product_inc_vat);
                                            modelCartMenu.setVariation_name(variation_name);
                                            modelCartMenu.setBusinessid(whichbusiness);
                                            modelCartMenu.setProduct_type(vanstock);
                                            db.insertNote(modelCartMenu);

                                            homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                            homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                else {

                                    Toast.makeText(getActivity(), "Zero is Not valid", Toast.LENGTH_SHORT).show();
                                }
                            }

                            else
                            {
                                Toast.makeText(getActivity(),"Please Enter Quantity",Toast.LENGTH_SHORT).show();
                            }

                            quantity.setText("");
                            quantity.setFocusable(false);
                            homesize.search_txtvw.setFocusable(false);


                        }


                }

                else if ( vanstock.equals("1") && db.ExistsProType("0")){


                    if (vanstock != null && vanstock.equals("1") && !quantity.getText().toString().equals("") && Integer.valueOf(quantity.getText().toString()) <= Integer.valueOf(in_stock)) {

                        Log.e("suman","3");

                        final Dialog dialog= new Dialog(getActivity());
                        dialog.setContentView(R.layout.custom_dropdown_already_exists_data);
                        btn_close_exist_item=dialog.findViewById(R.id.btn_close_exist_item);
                        btn_continue_exist_item=dialog.findViewById(R.id.btn_continue_exist_item);
                        dialog.show();
                        btn_close_exist_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });

                        btn_continue_exist_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Paper.book().write("save_business",Paper.book().read("wholeseller_bus_id"));

                                SharedPreferences pref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString("stripe_publish_key",publish_key);

                                editor.apply();
                                db.deleteAll();

                                homesize.cart_size.setVisibility(View.VISIBLE);
                                product_price=Paper.book().read("product_price");
                                variation_id=Paper.book().read("tempid");
                                product_inc_vat=Paper.book().read("pro_inc_vat");
                                hasornot=Paper.book().read("hasornot");

                                if(hasornot==0){

                                    String tempid=String.valueOf(0);

                                    Paper.book().write("tempid",tempid);

                                }

                                String value=quantity.getText().toString();

                                if((!TextUtils.isEmpty(quantity.getText().toString()) )) {
                                    if ((!value.equals(String.valueOf(0)))) {

                                        Log.e("hasornot",hasornot+"");

                                        if (hasornot == 1) {

                                            if (!variation_id.equals(String.valueOf(0))) {

                                                variation_name=Paper.book().read("variation_name");

                                                if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {


                                                    ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                                    modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                                    db.updateVNote(modelCartMenu1);

                                                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                                } else {

                                                    ModelCartMenu modelCartMenu = new ModelCartMenu();

                                                    if(Paper.book().read("permission_see_cost","2").equals("1")){

                                                        modelCartMenu.setPrice(product_price);
                                                    }

                                                    else {
                                                        modelCartMenu.setPrice(product_price);

                                                    }

                                                    if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                                                        modelCartMenu.setPrice(product_price);

                                                    }

                                                    modelCartMenu.setUsersid(userid);
                                                    modelCartMenu.setProductid(product_id);
                                                    modelCartMenu.setProductname(product_name);
                                                    modelCartMenu.setQuantity(quantity.getText().toString());
                                                    modelCartMenu.setProductimage(product_first_img);
                                                    modelCartMenu.setVariationid(variation_id);
                                                    modelCartMenu.setInc_vat(product_inc_vat);
                                                    modelCartMenu.setVariation_name(variation_name);
                                                    modelCartMenu.setAdd_product_code(String.valueOf(0));
                                                    modelCartMenu.setBusinessid(whichbusiness);
                                                    modelCartMenu.setProduct_type(vanstock);

                                                    db.insertNote(modelCartMenu);

                                                    homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                                    homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                                }
                                            } else {

                                                Toast.makeText(getActivity(), "Please Select Variation", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {

                                            variation_name="";
                                            variation_id = "0";

                                            if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {

                                                ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                                modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                                db.updateVNote(modelCartMenu1);

                                                Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                            } else {

                                                Log.e("praid11",product_id+"");
                                                Log.e("praid12",variation_id+"");
                                                Log.e("praid13",vanstock+"");

                                                ModelCartMenu modelCartMenu = new ModelCartMenu();
                                                modelCartMenu.setUsersid(userid);
                                                modelCartMenu.setProductid(product_id);
                                                modelCartMenu.setProductname(product_name);
                                                modelCartMenu.setQuantity(quantity.getText().toString());


                                                if(Paper.book().read("permission_see_cost","2").equals("1")){
                                                    modelCartMenu.setPrice(product_price);
                                                }

                                                else {
                                                    modelCartMenu.setPrice(product_price);

                                                }

                                                if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                                                    modelCartMenu.setPrice(product_price);

                                                }

                                                modelCartMenu.setAdd_product_code(String.valueOf(0));
                                                modelCartMenu.setProductimage(product_first_img);
                                                modelCartMenu.setVariationid(variation_id);
                                                modelCartMenu.setInc_vat(product_inc_vat);
                                                modelCartMenu.setVariation_name(variation_name);
                                                modelCartMenu.setBusinessid(whichbusiness);
                                                modelCartMenu.setProduct_type(vanstock);
                                                db.insertNote(modelCartMenu);

                                                homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                                homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                                Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                    else {

                                        Toast.makeText(getActivity(), "Zero is Not valid", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                else
                                {
                                    Toast.makeText(getActivity(),"Please Enter Quantity",Toast.LENGTH_SHORT).show();
                                }

                                quantity.setText("");
                                quantity.setFocusable(false);
                                homesize.search_txtvw.setFocusable(false);
                                dialog.dismiss();

                                // Paper.book().write("whichbusiness","0");
                            }


                        });


                    }
                    else {

                        Toast.makeText(getActivity(), quantity.getText().toString()+": Quantity is not in Stock "+"" ,Toast.LENGTH_SHORT).show();

                    }


                }


                else
                if ( vanstock.equals("1") && db.ExistsProType("1")){

                    Log.e("ankit","4");
                    if (vanstock != null && vanstock.equals("1") && !quantity.getText().toString().equals("") &&  Integer.valueOf(quantity.getText().toString()) <= Integer.valueOf(in_stock)) {

                        Log.e("ankit","41");


                        homesize.cart_size.setVisibility(View.VISIBLE);

                        product_price=Paper.book().read("product_price");
                        variation_id=Paper.book().read("tempid");
                        product_inc_vat=Paper.book().read("pro_inc_vat");
                        hasornot=Paper.book().read("hasornot");

                        if(hasornot==0){

                            String tempid=String.valueOf(0);

                            Paper.book().write("tempid",tempid);

                        }
                        Log.e("nikhil_pidrerrt",product_id+"");
                        Log.e("nikhil_pidrerrt",variation_id+"");
                        String value=quantity.getText().toString();

                        if((!TextUtils.isEmpty(quantity.getText().toString()) )) {

                            Log.e("ankit","42");

                            if ((!value.equals(String.valueOf(0)))) {

                                Log.e("ankit1",variation_id+"");

                                Log.e("ankitnot1",hasornot+"");
                                if (hasornot == 1) {

                                    Log.e("ankithas","if");

                                    if (!variation_id.equals(String.valueOf(0))) {

                                        variation_name=Paper.book().read("variation_name");

                                        if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {


                                            ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);
                                            modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                            db.updateVNote(modelCartMenu1);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                        } else {

                                            ModelCartMenu modelCartMenu = new ModelCartMenu();

                                            if(Paper.book().read("permission_see_cost","2").equals("1")){

                                                modelCartMenu.setPrice(product_price);
                                            }

                                            else {
                                                modelCartMenu.setPrice(product_price);

                                            }

                                            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                                                modelCartMenu.setPrice(product_price);

                                            }

                                            modelCartMenu.setUsersid(userid);
                                            modelCartMenu.setProductid(product_id)
                                            ;
                                            modelCartMenu.setProductname(product_name);
                                            modelCartMenu.setQuantity(quantity.getText().toString());
                                            modelCartMenu.setProductimage(product_first_img);
                                            modelCartMenu.setVariationid(variation_id);
                                            modelCartMenu.setInc_vat(product_inc_vat);
                                            modelCartMenu.setVariation_name(variation_name);
                                            modelCartMenu.setAdd_product_code(String.valueOf(0));
                                            modelCartMenu.setBusinessid(whichbusiness);
                                            modelCartMenu.setProduct_type(vanstock);

                                            db.insertNote(modelCartMenu);

                                            homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                            homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                        }
                                    } else {

                                        Toast.makeText(getActivity(), "Please Select Variation", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {


                                    Log.e("nikhil_pid",product_id+"");
                                    Log.e("nikhil_vid",variation_id+"");

                                    Log.e("ankithas","else");
                                    variation_name="";









                                    if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {

                                        Log.e("nikhiler","1");

                                        ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);
                                        modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                        db.updateVNote(modelCartMenu1);

                                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                    } else {

                                        Log.e("nikhiler","2");

                                        ModelCartMenu modelCartMenu = new ModelCartMenu();
                                        modelCartMenu.setUsersid(userid);
                                        modelCartMenu.setProductid(product_id);
                                        modelCartMenu.setProductname(product_name);
                                        modelCartMenu.setQuantity(quantity.getText().toString());


                                        if(Paper.book().read("permission_see_cost","2").equals("1")){
                                            modelCartMenu.setPrice(product_price);
                                        }

                                        else {
                                            modelCartMenu.setPrice(product_price);

                                        }

                                        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                                            modelCartMenu.setPrice(product_price);

                                        }

                                        modelCartMenu.setAdd_product_code(String.valueOf(0));
                                        modelCartMenu.setProductimage(product_first_img);
                                        modelCartMenu.setVariationid(variation_id);
                                        modelCartMenu.setInc_vat(product_inc_vat);
                                        modelCartMenu.setVariation_name(variation_name);
                                        modelCartMenu.setBusinessid(whichbusiness);
                                        modelCartMenu.setProduct_type(vanstock);
                                        db.insertNote(modelCartMenu);

                                        homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                        homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            else {

                                Toast.makeText(getActivity(), "Zero is Not valid", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else
                        {
                            Toast.makeText(getActivity(),"Please Enter Quantity",Toast.LENGTH_SHORT).show();
                        }

                        quantity.setText("");
                        quantity.setFocusable(false);
                        homesize.search_txtvw.setFocusable(false);

                    }
                    else {

                        Toast.makeText(getActivity(), quantity.getText().toString()+": Quantity is not in Stock "+"" ,Toast.LENGTH_SHORT).show();

                    }






                }


                else if  (vanstock.equals("0") && db.ExistsProType("1")) {


                    Log.e("suman","3");

                    final Dialog dialog= new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_dropdown_already_exists_data);
                    btn_close_exist_item=dialog.findViewById(R.id.btn_close_exist_item);
                    btn_continue_exist_item=dialog.findViewById(R.id.btn_continue_exist_item);
                    dialog.show();
                    btn_close_exist_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                        }
                    });

                    btn_continue_exist_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Paper.book().write("save_business",Paper.book().read("wholeseller_bus_id"));

                            SharedPreferences pref = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("stripe_publish_key",publish_key);

                            editor.apply();
                            db.deleteAll();

                            homesize.cart_size.setVisibility(View.VISIBLE);
                            product_price=Paper.book().read("product_price");
                            variation_id=Paper.book().read("tempid");
                            product_inc_vat=Paper.book().read("pro_inc_vat");
                            hasornot=Paper.book().read("hasornot");

                            if(hasornot==0){

                                String tempid=String.valueOf(0);

                                Paper.book().write("tempid",tempid);

                            }

                            String value=quantity.getText().toString();

                            if((!TextUtils.isEmpty(quantity.getText().toString()) )) {
                                if ((!value.equals(String.valueOf(0)))) {

                                    if (hasornot == 1) {

                                        if (!variation_id.equals(String.valueOf(0))) {

                                            variation_name=Paper.book().read("variation_name");

                                            if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {


                                                ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                                modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                                db.updateVNote(modelCartMenu1);

                                                Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                            } else {

                                                ModelCartMenu modelCartMenu = new ModelCartMenu();

                                                if(Paper.book().read("permission_see_cost","2").equals("1")){

                                                    modelCartMenu.setPrice(product_price);
                                                }

                                                else {
                                                    modelCartMenu.setPrice(product_price);

                                                }

                                                if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                                                    modelCartMenu.setPrice(product_price);

                                                }

                                                modelCartMenu.setUsersid(userid);
                                                modelCartMenu.setProductid(product_id);
                                                modelCartMenu.setProductname(product_name);
                                                modelCartMenu.setQuantity(quantity.getText().toString());
                                                modelCartMenu.setProductimage(product_first_img);
                                                modelCartMenu.setVariationid(variation_id);
                                                modelCartMenu.setInc_vat(product_inc_vat);
                                                modelCartMenu.setVariation_name(variation_name);
                                                modelCartMenu.setAdd_product_code(String.valueOf(0));
                                                modelCartMenu.setBusinessid(whichbusiness);
                                                modelCartMenu.setProduct_type(vanstock);

                                                db.insertNote(modelCartMenu);

                                                homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                                homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                                Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                            }
                                        } else {

                                            Toast.makeText(getActivity(), "Please Select Variation", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {

                                        variation_name="";

                                        if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {

                                            ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);

                                            modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                            db.updateVNote(modelCartMenu1);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                        } else {

                                            ModelCartMenu modelCartMenu = new ModelCartMenu();
                                            modelCartMenu.setUsersid(userid);
                                            modelCartMenu.setProductid(product_id);
                                            modelCartMenu.setProductname(product_name);
                                            modelCartMenu.setQuantity(quantity.getText().toString());


                                            if(Paper.book().read("permission_see_cost","2").equals("1")){
                                                modelCartMenu.setPrice(product_price);
                                            }

                                            else {
                                                modelCartMenu.setPrice(product_price);

                                            }

                                            if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                                                modelCartMenu.setPrice(product_price);

                                            }

                                            modelCartMenu.setAdd_product_code(String.valueOf(0));
                                            modelCartMenu.setProductimage(product_first_img);
                                            modelCartMenu.setVariationid(variation_id);
                                            modelCartMenu.setInc_vat(product_inc_vat);
                                            modelCartMenu.setVariation_name(variation_name);
                                            modelCartMenu.setBusinessid(whichbusiness);
                                            modelCartMenu.setProduct_type(vanstock);
                                            db.insertNote(modelCartMenu);

                                            homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                            homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                            Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                else {

                                    Toast.makeText(getActivity(), "Zero is Not valid", Toast.LENGTH_SHORT).show();
                                }
                            }

                            else
                            {
                                Toast.makeText(getActivity(),"Please Enter Quantity",Toast.LENGTH_SHORT).show();
                            }

                            quantity.setText("");
                            quantity.setFocusable(false);
                            homesize.search_txtvw.setFocusable(false);
                            dialog.dismiss();

                            // Paper.book().write("whichbusiness","0");
                        }


                    });

                }

                else {

                    Log.e("suman","4");

                    homesize.cart_size.setVisibility(View.VISIBLE);

                    product_price=Paper.book().read("product_price");
                    variation_id=Paper.book().read("tempid");
                    product_inc_vat=Paper.book().read("pro_inc_vat");
                    hasornot=Paper.book().read("hasornot");

                    if(hasornot==0){

                        String tempid=String.valueOf(0);

                        Paper.book().write("tempid",tempid);

                    }

                    String value=quantity.getText().toString();

                    if((!TextUtils.isEmpty(quantity.getText().toString()) )) {
                        if ((!value.equals(String.valueOf(0)))) {

                            if (hasornot == 1) {

                                if (!variation_id.equals(String.valueOf(0))) {

                                    variation_name=Paper.book().read("variation_name");

                                    if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {


                                        ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);
                                        modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                        db.updateVNote(modelCartMenu1);

                                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                    } else {

                                        ModelCartMenu modelCartMenu = new ModelCartMenu();

                                        if(Paper.book().read("permission_see_cost","2").equals("1")){

                                            modelCartMenu.setPrice(product_price);
                                        }

                                        else {
                                            modelCartMenu.setPrice(product_price);

                                        }

                                        if(Paper.book().read("permission_wholeseller", "5").equals("1")) {

                                            modelCartMenu.setPrice(product_price);

                                        }

                                        modelCartMenu.setUsersid(userid);
                                        modelCartMenu.setProductid(product_id);
                                        modelCartMenu.setProductname(product_name);
                                        modelCartMenu.setQuantity(quantity.getText().toString());
                                        modelCartMenu.setProductimage(product_first_img);
                                        modelCartMenu.setVariationid(variation_id);
                                        modelCartMenu.setInc_vat(product_inc_vat);
                                        modelCartMenu.setVariation_name(variation_name);
                                        modelCartMenu.setAdd_product_code(String.valueOf(0));
                                        modelCartMenu.setProduct_type(vanstock);
                                        modelCartMenu.setBusinessid(whichbusiness);

                                        db.insertNote(modelCartMenu);

                                        homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                        homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    Toast.makeText(getActivity(), "Please Select Variation", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {

                                variation_name="";

                                if (((db.Exists(product_id)) && (db.ExistsVariation(variation_id)))) {

                                    ModelCartMenu modelCartMenu1 = db.getModelVCartMenu(product_id, variation_id);
                                    modelCartMenu1.setQuantity(String.valueOf(Integer.valueOf(modelCartMenu1.getQuantity()) + Integer.valueOf(quantity.getText().toString())));
                                    db.updateVNote(modelCartMenu1);

                                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();

                                } else {

                                    ModelCartMenu modelCartMenu = new ModelCartMenu();
                                    modelCartMenu.setUsersid(userid);
                                    modelCartMenu.setProductid(product_id);
                                    modelCartMenu.setProductname(product_name);
                                    modelCartMenu.setQuantity(quantity.getText().toString());


                                    if(Paper.book().read("permission_see_cost","2").equals("1")){
                                        modelCartMenu.setPrice(product_price);
                                    }

                                    else {
                                        modelCartMenu.setPrice(product_price);

                                    }

                                    if(Paper.book().read("permission_wholeseller", "5").equals("1")) {
                                        modelCartMenu.setPrice(product_price);

                                    }

                                    modelCartMenu.setAdd_product_code(String.valueOf(0));
                                    modelCartMenu.setProductimage(product_first_img);
                                    modelCartMenu.setVariationid(variation_id);
                                    modelCartMenu.setInc_vat(product_inc_vat);
                                    modelCartMenu.setVariation_name(variation_name);
                                    modelCartMenu.setBusinessid(whichbusiness);
                                    modelCartMenu.setProduct_type(vanstock);
                                    db.insertNote(modelCartMenu);

                                    homesize.cart_size.setText(String.valueOf(db.getNotesCount()));
                                    homesize.cart_size.setBackgroundResource(R.drawable.circle_view);

                                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        else {

                            Toast.makeText(getActivity(), "Zero is Not valid", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else
                    {
                        Toast.makeText(getActivity(),"Please Enter Quantity",Toast.LENGTH_SHORT).show();
                    }

                    quantity.setText("");
                    quantity.setFocusable(false);
                    homesize.search_txtvw.setFocusable(false);

                }






            }
        });
    }

    public void onAttach (Activity activity) {
        super.onAttach(activity);
        if(activity.getClass()== Home.class){
            homesize = (Home) activity;
        }


    }
}

