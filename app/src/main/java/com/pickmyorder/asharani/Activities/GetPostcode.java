package com.pickmyorder.asharani.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pickmyorder.asharani.Adapters.Adapter_Select_City;
import com.pickmyorder.asharani.ApiLogin_Interface;
import com.pickmyorder.asharani.Models.CityList;
import com.pickmyorder.asharani.Models.ModelCityList;
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

public class GetPostcode extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Button btn_submit;
    LinearLayout layout_city,layout_post_drop;
    List<CityList> myList;
    List<CityList> myCityList;
    private FirebaseAnalytics mFirebaseAnalytics;
    Adapter_Select_City adapter_select_city;
    EditText tv_drop_city;
    final Handler handler = new Handler();
    final int delay = 1000; // 1000 milliseconds == 1 second
    long Todaymsecond;
    String business_validity;
    com.pickmyorder.asharani.Storage.databaseSqlite databaseSqlite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_get_postcode);

        btn_submit=findViewById(R.id.btn_submit_postcode);
        layout_city=findViewById(R.id.layout_city);
        tv_drop_city=findViewById(R.id.tv_drop_city);
        layout_post_drop = findViewById(R.id.layout_post_drop);
        myList=new ArrayList<>();

        tv_drop_city.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        databaseSqlite = new databaseSqlite(getApplicationContext());
        tv_drop_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        /*********************************************** SETTING STATUS BAR WHITE ******************************************************************/

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        /************************************************************End*****************************************************************************/

       // tv_drop_city.setTextColor(getResources().getColor(R.color.bluetheme));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Postcode");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        layout_post_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCity();
            }
        });

        SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        business_validity =  sharedPreferences.getString("business_validity",null);
        sharedPreferences.getAll();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(tv_drop_city.getText().toString().equals(""))){

                    SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("city", tv_drop_city.getText().toString());
                    editor.apply();

                    Intent intent= new Intent(getApplicationContext(), Next_Login_Page.class);
                    startActivity(intent);

                }

                else {

                    Toast.makeText(getApplicationContext(),"Please Type City or Search",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void getCity() {

        final ProgressDialog progressDialogs = new ProgressDialog(GetPostcode.this,R.style.AlertDialogCustom);
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

                    final Dialog dialog= new Dialog(GetPostcode.this);
                    dialog.setContentView(R.layout.custom_dropdown_select_city);

                    EditText editText=dialog.findViewById(R.id.editTextSearch);
                    RecyclerView recyclerView=dialog.findViewById(R.id.recyclerView_city);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);

                    adapter_select_city=new Adapter_Select_City(GetPostcode.this,myList,dialog,tv_drop_city);
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

                        tv_drop_city.setText(Paper.book().read("selected_city"));

                    }

                   /* else {
                        tv_drop_city.setText("Select City");

                    }*/

                   // progressDialogs.dismiss();
                }

                progressDialogs.dismiss();

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

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Postcode");
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

        if (!isDataConnectionAvailable(GetPostcode.this)) {
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

        final Dialog dialog= new Dialog(GetPostcode.this);
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
                Paper.book().write("permission_wholeseller", "");
                Paper.book().write("ViewWholesellerPage", "100");
            }
        });

        dialog.setCancelable(false);
    }


}
