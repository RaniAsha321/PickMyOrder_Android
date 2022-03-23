package com.pickmyorder.asharani.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Models.Category;
import com.pickmyorder.asharani.Models.ModelProductsCategory;
import com.pickmyorder.asharani.Models.ModelUserExist;
import com.pickmyorder.asharani.R;
import com.pickmyorder.asharani.Storage.databaseSqlite;

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

public class Next_Login_Page extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    List<Category> modelProductsCategories;
    List<Category> intentmodelProductsCategories;
    Button splashLogin;
    TextView user;
    Intent intentvalues;
    private FirebaseAnalytics mFirebaseAnalytics;
    String business_validity;
    final Handler handler = new Handler();
    final int delay = 1000; // 1000 milliseconds == 1 second
    long Todaymsecond;
    com.pickmyorder.asharani.Storage.databaseSqlite databaseSqlite;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_login);

        splashLogin = findViewById(R.id.splash_login);
        user = findViewById(R.id.user);


        /*********************************************** SETTING STATUS BAR WHITE ******************************************************************/

        if (android.os .Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        /***********************************************************End******************************************************************************/

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Welcome");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);


        intentvalues = getIntent();
        databaseSqlite = new databaseSqlite(getApplicationContext());

            SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            sharedPreferences.getString("role", "");
            String email = sharedPreferences.getString("email", "");
            String values = sharedPreferences.getString("username", "");
            sharedPreferences.getString("UserImage", "");
            sharedPreferences.getString("permission_cat", null);
            sharedPreferences.getString("permission_orders",null);
            sharedPreferences.getString("permission_pro_details",null);
            sharedPreferences.getString("permission_catelogues",null);
            sharedPreferences.getString("permission_all_orders",null);
            sharedPreferences.getString("permission_awaiting",null);
            sharedPreferences.getString("permission_see_cost",null);
            String business_name = sharedPreferences.getString("business_name",null);
            String Wholeseller_engineer_id = sharedPreferences.getString("Wholeseller_engineer_id",null);
            final String user_id =sharedPreferences.getString("unique_id",null);
            String Image =  sharedPreferences.getString("Image",null);
            business_validity =  sharedPreferences.getString("business_validity",null);

            sharedPreferences.getAll();

            Log.e("business_name",business_name+"");
            Paper.book().write("email", email);

            Paper.book().write("username", values);

           /* Paper.book().write("userid", user_id);*/
            /*Paper.book().write("Wholeseller_engineer_id", Wholeseller_engineer_id);*/
            Paper.book().write("search_product","");

            user.setText(values);

            splashLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Paper.book().write("menu_Van_Stock","");
                    Paper.book().write("menu_Brand","");
                    Paper.book().write("menu_Wholesaler","");
                    Paper.book().write("menu_Cat","1");
                    Paper.book().write("welcome","1");

                    User_Exist();
                }
            });
        }

    private void Business_Validity_Check(String business_validity) {

        if(business_validity != null && !business_validity.equals("")){

            processCurrentTime(business_validity);

        }
    }

    private void processCurrentTime(String business_validity) {

        if (!isDataConnectionAvailable(Next_Login_Page.this)) {
            showerrorDialog("No Network coverage!");
        } else {

            Log.e("datedd",business_validity+"");
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

            Log.e("Dated",What_Is_Today+"");
            SimpleDateFormat Dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String Today=Dateformat.format(What_Is_Today);
            Log.e("Dated2",Today+"");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date mDate = sdf.parse(Today);
            Todaymsecond = mDate.getTime();


        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("Dated3",timestampinMillies+"");
        Log.e("Dated4",Todaymsecond+"");

            if (timestampinMillies <= Todaymsecond) {
                showerrorDialog("Validity of Trial Version has been Expired");
            }

    }

    private void showerrorDialog(String data) {

        final Dialog dialog= new Dialog(Next_Login_Page.this);
        dialog.setContentView(R.layout.custom_dialog_trial);
        dialog.show();
        Button btn_continue = dialog.findViewById(R.id.btn_continue_trial);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);

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
                Paper .book().write("permission_wholeseller", "");
                Paper.book().write("ViewWholesellerPage", "100");
            }
        });

        dialog.setCancelable(false);
    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Next_Login_Page.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }

    private void getProducts() {

    /********************************************************  NEXT LOGIN API USING RETROFIT ******************************************************************/

    String user_id=Paper.book().read("unique_id");

    String isVan = "0";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

            service.Products(user_id,isVan).enqueue(new Callback<ModelProductsCategory>() {
                @Override
                public void onResponse(Call<ModelProductsCategory> call, Response<ModelProductsCategory> response) {

                    assert response.body() != null;
                    modelProductsCategories = response.body().getCategory();

                    if (response.body().getStatusCode().equals(200)) {

                        Log.e("dev_userID",user_id+"");
                        Log.e("dev_isVan",isVan+"");

                        Paper.book().write("awaiting_data",response.body().getNumberofawating());

                        Intent intent = new Intent(Next_Login_Page.this, Home.class);

                        startActivity(intent);

                        intentmodelProductsCategories = new ArrayList<>();

                        for (int i = 0; i < modelProductsCategories.size(); i++) {

                            Category category = new Category();
                            category.setCatId(response.body().getCategory().get(i).getCatId());
                            category.setCatImage(response.body().getCategory().get(i).getCatImage());
                            category.setCatName(response.body().getCategory().get(i).getCatName());
                            category.setStatus(response.body().getCategory().get(i).getStatus());
                            intentmodelProductsCategories.add(category);
                        }
                        Paper.book().write("categorylist", intentmodelProductsCategories);
                    }

                    else {

                        Toast.makeText(getApplicationContext(), "No Products Found", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ModelProductsCategory> call, Throwable t) {

                    Toast.makeText(Next_Login_Page.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });

    /************************************************************************************************************************************************/

    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Welcome");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        Business_Validity_Check(business_validity);

    }

    private void User_Exist() {

        String user_id = Paper.book().read("unique_id");

        if (!isDataConnectionAvailable(Next_Login_Page.this)) {
            showerrorDialog("No Network coverage!");
        } else {
            if (!(user_id.equals(""))) {
                GetUser(user_id);
            }
        }
    }

    private void GetUser(String user_id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLogin_Interface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

        service.USER_EXIST_CALL(user_id).enqueue(new Callback<ModelUserExist>() {
            @Override
            public void onResponse(Call<ModelUserExist> call, Response<ModelUserExist> response) {
                assert response.body() != null;
                if(response.body().getStatusCode().equals(200)) {

                    Log.e("Exist","200");
                    if (!(user_id.equals(""))) {

                        getProducts();         //API CALLING METHOD

                    } else {

                        Toast.makeText(getApplicationContext(), "No Product Found", Toast.LENGTH_SHORT).show();
                    }


                }
                else {

                    Log.e("Exist","401");

                    showerrorDialogUser();
                }

            }

            @Override
            public void onFailure(Call<ModelUserExist> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showerrorDialogUser() {


        final Dialog dialog= new Dialog(Next_Login_Page.this);
        dialog.setContentView(R.layout.custom_dialog_user_exist);
        dialog.show();
        Button btn_continue = dialog.findViewById(R.id.btn_continue_user_exist);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);

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
                Paper .book().write("permission_wholeseller", "");
                Paper.book().write("ViewWholesellerPage", "100");
            }
        });

        dialog.setCancelable(false);
    }


}



