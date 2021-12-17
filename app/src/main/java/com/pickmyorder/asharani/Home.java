package com.pickmyorder.asharani;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.Payment_Stripe.AddPayment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , MyInterface, Nav_Drawer {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private FirebaseAnalytics mFirebaseAnalytics;
    AddPayment addPayment;
    LinearLayout Search_postcode_layout,Search_product_layout,layout_search_view,layout_post_drop_home;
    Orders_Menu sizedata;
    int awaiting_data;
    TextView heading,search_postcode,textview_version;
    com.pickmyorder.asharani.databaseSqlite databaseSqlite;
    Adapter_Cart_Menu adapterCartMenu;
    Search_items items;
    EditText search_txtvw;
    List<ModelCartMenu> modelCartMenuList;
    List<String> mydatalist;
    LinearLayout search_layout,idlayout,nav_search_layout;
    Intent intent;
    Context context;
    ImageView menuIcon,Img_cart,Img_Search_icon,img_icon;
    public static DrawerLayout drawerLayout;
    NavigationView navigationView;
    public FragmentTransaction fragmentTransaction;
    TextView cart_size,txt_blink;
    List<Category> intentmodelProductsCategories;
    List<Category> modelProductsCategories;
    Animation animation;
    Wholeseller wholeseller;
    List<CityList> myList;
    List<CityList> myCityList;
    Adapter_Select_City adapter_select_city;
    SharedPreferences sharedPreferences;
    final Handler handler = new Handler();
    final int delay = 1000; // 1000 milliseconds == 1 second
    long Todaymsecond;
    String business_validity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        search_txtvw = findViewById(R.id.search_txtvw);
        textview_version = findViewById(R.id.textview_version);
        search_postcode= findViewById(R.id.search_postcode);
        menuIcon = findViewById(R.id.menu_icon);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Img_cart = findViewById(R.id.nav_cart);
        cart_size = findViewById(R.id.cart_size);
        Img_Search_icon = findViewById(R.id.search_icon);
        idlayout = findViewById(R.id.idlayout);
        heading = findViewById(R.id.heading);
        img_icon=findViewById(R.id.img_icon);
        Search_product_layout=findViewById(R.id.Search_product_layout);
        Search_postcode_layout=findViewById(R.id.Search_postcode_layout);
        layout_search_view=findViewById(R.id.layout_search_view);
        wholeseller=new Wholeseller();
        nav_search_layout=findViewById(R.id.navsearchlayout);
        sizedata=new Orders_Menu();
        search_txtvw.setRawInputType(InputType.TYPE_CLASS_TEXT);
        search_layout = findViewById(R.id.searchview_layout);
        layout_post_drop_home = findViewById(R.id.layout_post_drop_home);
        databaseSqlite = new databaseSqlite(getApplicationContext());
        addPayment=new AddPayment();
        myList=new ArrayList<>();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

      //  search_postcode.setTextColor(getResources().getColor(R.color.bluetheme));
      //  search_txtvw.setTextColor(getResources().getColor(R.color.bluetheme));
        /*********************************************** SETTING STATUS BAR WHITE ******************************************************************/

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        /************************************************************End*****************************************************************************/

        SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        business_validity =  sharedPreferences.getString("business_validity",null);
        sharedPreferences.getAll();

        Paper.book().write("isVan","0");

        sharedPreferences =getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        sharedPreferences.getString("role", "");
        sharedPreferences.getString("permission_cat", "");
        sharedPreferences.getString("permission_orders", "");
        sharedPreferences.getString("permission_pro_details", "");
        sharedPreferences.getString("permission_catelogues", "");
        sharedPreferences.getString("permission_all_orders", "");
        sharedPreferences.getString("permission_awaiting", "");

            search_postcode.setText(sharedPreferences.getString("city", ""));

            String container_app_payment=Paper.book().read("container_app_payment");

    /************************************************ SET PERMISSIONS ON NAVIGATION BUTTON CLICK****************************************************************************************/

   /* String Project_add = Paper.book().read("Project_add");

    if (Project_add != null && Project_add.equals("1")){

        fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("projects").replace(R.id.containerr,new Projects());
        fragmentTransaction.commit();
        search_txtvw.setText("");
    }
*/

        String vanstock = Paper.book().read("vanstock");

        Log.e("reyansh",vanstock+"");
        int versionCode = BuildConfig.VERSION_CODE;

        textview_version.setText(String.valueOf(versionCode));
        Paper.book().write("menu_Van_Stock","");
        /*Paper.book().write("vanstock","");*/

        Paper.book().write("whole_seller","2");

         if (Paper.book().read("vanstock", "5").equals("1")) {

             Menu nav_Menu = navigationView.getMenu();
             nav_Menu.findItem(R.id.nav_van_stock).setVisible(true);
             nav_Menu.findItem(R.id.nav_stock_order).setVisible(true);

         }
         else {

             Menu nav_Menu = navigationView.getMenu();
             nav_Menu.findItem(R.id.nav_van_stock).setVisible(false);
             nav_Menu.findItem(R.id.nav_stock_order).setVisible(false);

         }


        if (Paper.book().read("datarole", "5").equals("1")) {

            Menu nav_Menu = navigationView.getMenu();

            if(Paper.book().read("ViewWholesellerPage", "5").equals("1")) {


                if(Paper.book().read("permission_wholeseller", "5").equals("1")){


                    nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                    nav_Menu.findItem(R.id.nav_order).setVisible(true);
                    nav_Menu.findItem(R.id.nav_wholesellers).setVisible(false);
                    nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                    nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                    nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);

                }
                else {

                    nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                    nav_Menu.findItem(R.id.nav_order).setVisible(true);
                    nav_Menu.findItem(R.id.nav_wholesellers).setVisible(true);
                    nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                    nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                    nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);

                }
            }

            else {

                nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                nav_Menu.findItem(R.id.nav_order).setVisible(true);
                nav_Menu.findItem(R.id.nav_wholesellers).setVisible(false);
                nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);
            }

        }

        else if (Paper.book().read("datarole", "5").equals("2")) {

            Menu nav_Menu = navigationView.getMenu();

            if(Paper.book().read("ViewWholesellerPage", "5").equals("1")) {

                if(Paper.book().read("permission_wholeseller", "5").equals("1")){


                    nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                    nav_Menu.findItem(R.id.nav_order).setVisible(true);
                    nav_Menu.findItem(R.id.nav_wholesellers).setVisible(false);
                    nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                    nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                    nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);

                }
                else {

                    nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                    nav_Menu.findItem(R.id.nav_order).setVisible(true);
                    nav_Menu.findItem(R.id.nav_wholesellers).setVisible(true);
                    nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                    nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                    nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);
                }
            }

            else {

                nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                nav_Menu.findItem(R.id.nav_order).setVisible(true);
                nav_Menu.findItem(R.id.nav_wholesellers).setVisible(false);
                nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);
            }

        }
        else if (Paper.book().read("datarole", "5").equals("3")) {

            Menu nav_Menu = navigationView.getMenu();
            if(Paper.book().read("ViewWholesellerPage", "5").equals("1")) {

                if(Paper.book().read("permission_wholeseller", "5").equals("1")){

                    nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                    nav_Menu.findItem(R.id.nav_order).setVisible(true);
                    nav_Menu.findItem(R.id.nav_wholesellers).setVisible(false);
                    nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                    nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                    nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);

                }
                else {

                    nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                    nav_Menu.findItem(R.id.nav_order).setVisible(true);
                    nav_Menu.findItem(R.id.nav_wholesellers).setVisible(true);
                    nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                    nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                    nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);
                }
            }

            else {

                nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                nav_Menu.findItem(R.id.nav_order).setVisible(true);
                nav_Menu.findItem(R.id.nav_wholesellers).setVisible(false);
                nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);
            }

        }

        else if (Paper.book().read("datarole", "5").equals("4")){

            Menu nav_Menu = navigationView.getMenu();

            if(Paper.book().read("permission_cats", "5").equals("1")){

                nav_Menu.findItem(R.id.nav_categories).setVisible(true);

            }

            else if(Paper.book().read("permission_cats", "5").equals("0")) {

                nav_Menu.findItem(R.id.nav_categories).setVisible(false);

            }

             if(Paper.book().read("permission_orderss", "5").equals("1")){

                nav_Menu.findItem(R.id.nav_order).setVisible(true);

            }

            else if(Paper.book().read("permission_orderss", "5").equals("0")){

                nav_Menu.findItem(R.id.nav_order).setVisible(false);

            }

            if(Paper.book().read("permission_pro_detailssse", "5").equals("1")){

                nav_Menu.findItem(R.id.nav_project_details).setVisible(true);

            }

            else if(Paper.book().read("permission_pro_detailssse", "5").equals("0")){

                nav_Menu.findItem(R.id.nav_project_details).setVisible(false);

            }

            String catalog = Paper.book().read("permission_cateloguess");
            Log.e("catalog",catalog+"");

            if(Paper.book().read("permission_cateloguess", "5").equals("1")){

                nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);

            }

            else if(Paper.book().read("permission_cateloguess", "5").equals("0")){

                nav_Menu.findItem(R.id.nav_catalogue).setVisible(false);
            }

          //  if(Paper.book().read("ViewWholesellerPage", "5").equals("1")) {


                if(Paper.book().read("permission_wholeseller", "5").equals("1")){


                nav_Menu.findItem(R.id.nav_categories).setVisible(true);
                nav_Menu.findItem(R.id.nav_order).setVisible(true);
                nav_Menu.findItem(R.id.nav_wholesellers).setVisible(false);
                nav_Menu.findItem(R.id.nav_project_details).setVisible(true);
                nav_Menu.findItem(R.id.nav_account_detail).setVisible(true);
                nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);


                }
                else {


                    if(Paper.book().read("permission_cats", "5").equals("1")){

                        nav_Menu.findItem(R.id.nav_categories).setVisible(true);

                    }

                    else if(Paper.book().read("permission_cats", "5").equals("0")) {

                        nav_Menu.findItem(R.id.nav_categories).setVisible(false);

                    }

                    if(Paper.book().read("permission_orderss", "5").equals("1")){

                        nav_Menu.findItem(R.id.nav_order).setVisible(true);

                    }

                    else if(Paper.book().read("permission_orderss", "5").equals("0")){

                        nav_Menu.findItem(R.id.nav_order).setVisible(false);

                    }

                    if(Paper.book().read("permission_pro_detailssse", "5").equals("1")){

                        nav_Menu.findItem(R.id.nav_project_details).setVisible(true);

                    }

                    else if(Paper.book().read("permission_pro_detailssse", "5").equals("0")){

                        nav_Menu.findItem(R.id.nav_project_details).setVisible(false);

                    }

                    if(Paper.book().read("permission_cateloguess", "5").equals("1")){

                        nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);

                    }

                    else if(Paper.book().read("permission_cateloguess", "5").equals("0")){

                        nav_Menu.findItem(R.id.nav_catalogue).setVisible(false);
                    }

                    if(Paper.book().read("permission_Quote", "5").equals("1")){

                        nav_Menu.findItem(R.id.nav_quotes).setVisible(true);

                    }

                    else if(Paper.book().read("permission_Quote", "5").equals("0")){

                        nav_Menu.findItem(R.id.nav_quotes).setVisible(false);
                    }
                }
           // }

        }

        Menu nav_Menu = navigationView.getMenu();

        if(Paper.book().read("permission_cateloguess", "5").equals("1")){

            nav_Menu.findItem(R.id.nav_catalogue).setVisible(true);

        }

        else if(Paper.book().read("permission_cateloguess", "5").equals("0")){

            nav_Menu.findItem(R.id.nav_catalogue).setVisible(false);
        }


        if(Paper.book().read("permission_Quote", "5").equals("1")){

            nav_Menu.findItem(R.id.nav_quotes).setVisible(true);

        }

        else if(Paper.book().read("permission_Quote", "5").equals("0")){

            nav_Menu.findItem(R.id.nav_quotes).setVisible(false);
        }


        if(Paper.book().read("permission_wholeseller", "5").equals("1")){

            nav_Menu.findItem(R.id.nav_wholesellers).setVisible(true);

        }

        else if(Paper.book().read("permission_wholeseller", "5").equals("0")){

            nav_Menu.findItem(R.id.nav_wholesellers).setVisible(false);
        }


        /*******************************************************************End**************************************************************************/


        if(!(String.valueOf(databaseSqlite.getNotesCount()).equals("0"))) {

           cart_size.setBackgroundResource(R.drawable.circle_view);
           cart_size.setText(String.valueOf(databaseSqlite.getNotesCount()));              // SET NUMBER OF ITEM IN THE CART USING SQLITE DATABASE "getNotesCount() METHOD"
       }

       else{

           cart_size.setText(String.valueOf(databaseSqlite.getNotesCount()));
       }

       String name=Paper.book().read("username");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_header_name);
        navUsername.setText(name);

       registerReceiver(mReceiver,new IntentFilter(DatabaseChangedReceiver.ACTION_DATABASE_CHANGED));

        items=new Search_items();

        adapterCartMenu = new Adapter_Cart_Menu(getApplicationContext());

        mydatalist=new ArrayList<>();

        modelCartMenuList=new ArrayList<>();

        img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("category").replace(R.id.containerr,new Home_Categories());
                fragmentTransaction.commit();
                search_txtvw.setText("");
            }
        });


        Paper.book().write("search","0");
        fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.containerr, new Home_Categories());
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(this);

        modelCartMenuList=Paper.book().read("Menus", new ArrayList<ModelCartMenu>());

        Img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(String.valueOf(databaseSqlite.getNotesCount()).equals("0"))) {

                    fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("home_category").replace(R.id.containerr, new Cart_Menu());
                    fragmentTransaction.commit();
                }

                else {
                    fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("home_category").replace(R.id.containerr, new EmptyCart());
                    fragmentTransaction.commit();
                }
            }
        });

        search_txtvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    search_txtvw.setFocusableInTouchMode(true);
                    layout_search_view.setVisibility(View.GONE);
                    Search_postcode_layout.setVisibility(View.GONE);
                    Search_product_layout.setVisibility(View.VISIBLE);

            }
        });

        layout_post_drop_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_search_view.setVisibility(View.GONE);
                Search_product_layout.setVisibility(View.GONE);
                Search_postcode_layout.setVisibility(View.VISIBLE);


                getCity();
            }
        });


        idlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_id=Paper.book().read("search_id");
                Paper.book().write("pay_Card_status","0");

                String menu_Van_Stock=Paper.book().read("menu_Van_Stock");

                //  Log.e("qwerty_search","1");

                if ((Search_postcode_layout.getVisibility() == View.GONE) &&(Search_product_layout.getVisibility() == View.VISIBLE) ){
                    Log.e("qwerty_search","1");
                    if(search_id!=null){
                        search_txtvw.requestFocus();
                        search_txtvw.requestFocusFromTouch();
                        fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("cate").replace(R.id.containerr, new Search_items());
                        fragmentTransaction.commit();
                    }

                    else{

                        search_txtvw.requestFocus();
                        search_txtvw.requestFocusFromTouch();
                        fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("cate").replace(R.id.containerr, new Search_items());
                        fragmentTransaction.commit();

                    }
                }

                if ((Search_postcode_layout.getVisibility() == View.VISIBLE) &&(Search_product_layout.getVisibility() == View.GONE) ){

                    Log.e("qwerty_search","2");
                        search_txtvw.requestFocus();
                        search_txtvw.requestFocusFromTouch();
                   // Paper.book().write("city",search_postcode.getText().toString());

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("city", search_postcode.getText().toString());
                    editor.apply();

                    fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("cate").replace(R.id.containerr, new Wholeseller());
                        fragmentTransaction.commit();

                }


                if  ((Search_product_layout.getVisibility() == View.GONE) &&(Search_postcode_layout.getVisibility() == View.VISIBLE)) {

                    Log.e("qwerty_search","3");
                    // Either gone or invisible
                    if(!(search_postcode.getText().toString().equals(""))){

                        Log.e("qwerty_search","4");
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("city", search_postcode.getText().toString());
                        editor.apply();

                        Log.e("qwerty_search2",search_postcode.getText().toString()+"");
                       // Paper.book().write("city",search_postcode.getText().toString());
                                fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("cate").replace(R.id.containerr, new Wholeseller());
                                fragmentTransaction.commit();
                    }

                    else if(search_txtvw.getHint().toString().equals("Search Products")){

                        Log.e("qwerty_search","5");
                        search_txtvw.requestFocus();
                        search_txtvw.requestFocusFromTouch();
                        fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("cate").replace(R.id.containerr, new Search_items());
                        fragmentTransaction.commit();
                    }
                }
            }
        });
        menuIcon();
    }

    private void getCity() {

        Paper.book().write("city","0");

        final ProgressDialog progressDialogs = new ProgressDialog(Home.this,R.style.AlertDialogCustom);
        progressDialogs.setCancelable(false);
        progressDialogs.setMessage("Please Wait.......");
        progressDialogs.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        final ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String userid= Paper.book().read("unique_id");

        service.MODEL_CITY_LIST_CALL(userid).enqueue(new Callback<ModelCityList>() {
            @Override
            public void onResponse(Call<ModelCityList> call, Response<ModelCityList> response) {

                if (response.body().getStatusCode().equals(200)) {

                    myCityList = response.body().getCityList();

                    for (int i = 0; i < myCityList.size(); i++) {

                        CityList cityList = new CityList();
                        cityList.setTown(response.body().getCityList().get(i).getTown());
                        myList.add(cityList);
                    }

                    final Dialog dialog= new Dialog(Home.this);
                    dialog.setContentView(R.layout.custom_dropdown_select_city);

                    EditText editText=dialog.findViewById(R.id.editTextSearch);
                    RecyclerView recyclerView=dialog.findViewById(R.id.recyclerView_city);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);

                    adapter_select_city=new Adapter_Select_City(Home.this,myList,dialog,search_postcode);
                    recyclerView.setAdapter(adapter_select_city);

                    //adding a TextChangedListener
                    //to call a method whenever there is some change on the EditText

                    TextWatcher edittw = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            filter(editable.toString());

                        }
                    };

                    editText.addTextChangedListener(edittw);

                    dialog.show();

                    if((Paper.book().read("selected_city")!=null)){

                        search_postcode.setText(Paper.book().read("selected_city"));

                    }

                    else {
                        search_postcode.setText("Select City");

                    }

                    progressDialogs.dismiss();

                }
            }

            @Override
            public void onFailure(Call<ModelCityList> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressDialogs.dismiss();
            }
        });

    }

    private void filter(String text) {

        //new array list that will hold the filtered data
        ArrayList<CityList> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (CityList s : myList) {
            //if the existing elements contains the search input
            if (s.getTown().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter_select_city.filterList(filterdNames);

    }

    private void menuIcon() {

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){

            case R.id.nav_categories:

                Paper.book().write("save_business","");
                Paper.book().write("whole_seller","2");
                Paper.book().write("menu_Van_Stock","");
                Paper.book().write("wholeseller_bus_id","0");
                Paper.book().write("pop_cancel_up","1");
                Paper.book().write("pay_Card_status","0");
                Paper.book().write("search","0");
                getProducts(Paper.book().read("unique_id"),"0");
                layout_search_view.setVisibility(View.GONE);
                Search_product_layout.setVisibility(View.VISIBLE);
                Search_postcode_layout.setVisibility(View.GONE);
                search_txtvw.setText("");

                break;


            case R.id.nav_van_stock:

                Paper.book().write("whole_seller","2");
                Paper.book().write("save_business","");
                Paper.book().write("menu_Van_Stock","1");
                Paper.book().write("wholeseller_bus_id","0");
                Paper.book().write("pop_cancel_up","1");
                Paper.book().write("pay_Card_status","0");
                Paper.book().write("search","0");
                getProducts(Paper.book().read("unique_id"),"1");
                layout_search_view.setVisibility(View.GONE);
                Search_product_layout.setVisibility(View.VISIBLE);
                Search_postcode_layout.setVisibility(View.GONE);
                search_txtvw.setText("");

                break;



            case R.id.nav_wholesellers:

                search_postcode.setText(sharedPreferences.getString("city", ""));
                Paper.book().write("search","1");
                Paper.book().write("whole_seller","1");
                fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("category").replace(R.id.containerr,new Wholeseller());
                fragmentTransaction.commit();

                layout_search_view.setVisibility(View.GONE);
                Search_product_layout.setVisibility(View.GONE);
                Search_postcode_layout.setVisibility(View.VISIBLE);

                break;

            case R.id.nav_quotes:

                fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.containerr,new Quotes_Menu());
                fragmentTransaction.commit();
                search_txtvw.setText("");

                break;

            case R.id.nav_order:

                Paper.book().write("reorder_value_orders","0");
                fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.containerr,new Orders_Menu());
                fragmentTransaction.commit();
                search_txtvw.setText("");

                break;

            case R.id.nav_stock_order:

                Paper.book().write("reorder_value_orders","1");
                fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.containerr,new Orders_Menu());
                fragmentTransaction.commit();
                search_txtvw.setText("");

                break;

            case R.id.nav_project_details:

                if (Paper.book().read("permission_wholeseller", "5").equals("1")) {
                    Paper.book().write("dataset", "1");
                }
                fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("projects").replace(R.id.containerr,new Projects());
                fragmentTransaction.commit();
                search_txtvw.setText("");

                break;

            case R.id.nav_account_detail:

                fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.containerr,new AccountDetails());
                fragmentTransaction.commit();
                search_txtvw.setText("");

                break;

            case R.id.nav_catalogue:

                fragmentTransaction=getSupportFragmentManager().beginTransaction().replace(R.id.containerr,new Dynamic_Catalogous());
                fragmentTransaction.commit();
                search_txtvw.setText("");

                break;

            case R.id.nav_logout:

                new AlertDialog.Builder(this,R.style.AlertDialogCustom)
                        .setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout();
                            }
                        })

                        .setNegativeButton("No", null)
                        .show();

                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void logout() {

        final ProgressDialog progressDialog=new ProgressDialog(Home.this,R.style.AlertDialogCustom);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait.......");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        String userid= Paper.book().read("userid");     // Retrieve id from Login Api
        String deviceid=Paper.book().read("deviceid");

        service.LOGOUT_CALL("application/x-www-form-urlencoded",userid,deviceid,"Android" +
                "").enqueue(new Callback<ModelLogout>() {
            @Override
            public void onResponse(Call<ModelLogout> call, Response<ModelLogout> response) {

                if(response.body().getStatusCode().equals(200)){

                    Intent intent= new Intent(Home.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.clear();

                    editor.commit();

                    startActivity(intent);

                    databaseSqlite.deleteAll();

                    Paper.book().write("datarole","");
                    Paper.book().write("permission_see_cost","");
                    Paper.book().write("permission_cat","");
                    Paper.book().write("permission_orders","");
                    Paper.book().write("permission_pro_detailsss","");
                    Paper.book().write("permission_catelogues","");
                    Paper.book().write("permission_all_orders","");
                    Paper.book().write("permission_awaiting","");
                    Paper.book().write("deviceid","");
                    Paper.book().write("permission_wholeseller", "");
                    Paper.book().write("ViewWholesellerPage", "100");

                }

                else

                    {

                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ModelLogout> call, Throwable t) {

                Toast.makeText(Home.this, t.getMessage(),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }


    public void getProducts(String id, String isVan) {

        layout_search_view.setVisibility(View.GONE);
        Search_product_layout.setVisibility(View.VISIBLE);
        Search_postcode_layout.setVisibility(View.GONE);

        Log.e("id_id",id+"");
        Log.e("isVan",isVan+"");

        Paper.book().write("isVan",isVan);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        service.Products(id,isVan).enqueue(new Callback<ModelProductsCategory>() {
            @Override
            public void onResponse(Call<ModelProductsCategory> call, Response<ModelProductsCategory> response) {

                assert response.body() != null;
                modelProductsCategories = response.body().getCategory();

                if (response.body().getStatusCode().equals(200)) {

                    Paper.book().write("awaiting_data", response.body().getNumberofawating());



                    intentmodelProductsCategories = new ArrayList<>();

                    for (int i = 0; i < modelProductsCategories.size(); i++) {

                        Category category = new Category();
                        category.setCatId(response.body().getCategory().get(i).getCatId());
                        category.setCatImage(response.body().getCategory().get(i).getCatImage());
                        category.setCatName(response.body().getCategory().get(i).getCatName());
                        category.setStatus(response.body().getCategory().get(i).getStatus());
                        intentmodelProductsCategories.add(category);
                    }

                    fragmentTransaction=getSupportFragmentManager().beginTransaction().addToBackStack("category").replace(R.id.containerr,new Home_Categories());
                    fragmentTransaction.commit();


                    Paper.book().write("categorylist", intentmodelProductsCategories);
                } else {

                    Toast.makeText(getApplicationContext(), "No Products Found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ModelProductsCategory> call, Throwable t) {

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }
        });

    }

        @Override
    public void foo() {
    }

    @Override
    public void updateCart() {
   }

    private DatabaseChangedReceiver mReceiver = new DatabaseChangedReceiver() {

        public void onReceive(Context context, Intent intent) {

           if((databaseSqlite.getNotesCount()) != 0)
           {
               cart_size.setVisibility(View.VISIBLE);
               cart_size.setBackgroundResource(R.drawable.circle_view);
               cart_size.setText(String.valueOf(databaseSqlite.getNotesCount()));
           }

           else {

               cart_size.setText(String.valueOf(databaseSqlite.getNotesCount()));

               cart_size.setVisibility(View.GONE);
           }
        }

    };

    public void hideView(boolean b) {

        if(b){

            search_layout.setVisibility(View.GONE);
        }
        else {

            search_layout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void drawer_nav() {

        drawerLayout.openDrawer(Gravity.START);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        Business_Validity_Check(business_validity);

    }


    private void Business_Validity_Check(String business_validity) {

        if(business_validity != null && !business_validity.equals("")){

            processCurrentTime(business_validity);

        }
    }

    private void processCurrentTime(String business_validity) {

        if (!isDataConnectionAvailable(Home .this)) {
            showerrorDialog("No Network coverage!");
        } else {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date mDate = sdf.parse(business_validity);
                long timeInMilliseconds = mDate.getTime();

                Log.e("timeInMilliseconds",timeInMilliseconds+"");
                checkExpiry(timeInMilliseconds);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public static boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null)
            return false;

        return connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void checkExpiry(long timestampinMillies) {

        Date What_Is_Today= Calendar.getInstance().getTime();
        SimpleDateFormat Dateformat = new SimpleDateFormat("yyyy/MM/dd");
        String Today=Dateformat.format(What_Is_Today);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date mDate = sdf.parse(Today);
            Todaymsecond = mDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (timestampinMillies <= Todaymsecond) {
            showerrorDialog("Validity of Trial Version has been Expired");
        }

    }

    private void showerrorDialog(String data) {

        final Dialog dialog= new Dialog(Home.this);
        dialog.setContentView(R.layout.custom_dialog_trial);
        dialog.show();
        Button btn_continue = dialog.findViewById(R.id.btn_continue_trial);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });

        dialog.setCancelable(false);
    }


}
