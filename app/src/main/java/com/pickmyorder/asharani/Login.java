package com.pickmyorder.asharani;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.firebase.messaging.FirebaseMessaging;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Button btnLogin,btn_signup;
    TextView txt_forgot_password;
    EditText user_email, user_password;
    String deviceid;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        user_email = findViewById(R.id.edtxt_email);
        user_password = findViewById(R.id.edtxt_password);
        txt_forgot_password = findViewById(R.id.forgot_password);
        user_email.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        user_password.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        btnLogin = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        user_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        user_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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


        txt_forgot_password.setPaintFlags(txt_forgot_password.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);  // SET UNDERLINE BELOW FORGOT PASSWORD
        forgot_Password();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://app.pickmyorder.co.uk/index";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(user_email.getText().toString())){     // VALIDATE EITHER EMAIL IS FILLED OR NOT

                    if(!TextUtils.isEmpty(user_password.getText().toString())){   // VALIDATE EITHER PASSWORD IS FILLED OR NOT


    /**************************************************Register Device to the Firebase************************************************************/


                        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("Token", "Fetching FCM registration token failed", task.getException());
                                            return;
                                        }

                                        Log.e("deviceid","1");
                                        // Get new FCM registration token
                                        String token = task.getResult();
                                        // Get new Instance ID token
                                        deviceid = task.getResult();

                                        Log.e("deviceid",deviceid+"");
                                        Paper.book().write("deviceid",deviceid);
                                        login();


                                    }
                                });
    /***************************************************************End***************************************************************************/
                    }
                    else{

                        Toast.makeText(getApplicationContext(),"Please Enter Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                    Toast.makeText(getApplicationContext(),"Please Enter Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void forgot_Password() {

        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  throw new RuntimeException();

                Intent intent = new Intent(Login.this, Forgot_Password.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this,R.style.AlertDialogCustom)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Login.super.onBackPressed();
                        finish();

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    private void login() {

                final String email = user_email.getText().toString();
                final String password1 = user_password.getText().toString();

                final ProgressDialog progressDialogs = new ProgressDialog(Login.this,R.style.AlertDialogCustom);
                progressDialogs.setCancelable(false);
                progressDialogs.setMessage("Please Wait.......");
                progressDialogs.show();

    /********************************************************  LOGIN API USING RETROFIT ******************************************************************/


        Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiLogin_Interface.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                //Defining retrofit api service
                ApiLogin_Interface service = retrofit.create(ApiLogin_Interface.class);

                Log.e("bname1","test");

                (service.getClient("application/x-www-form-urlencoded", user_email.getText().toString(),
                        user_password.getText().toString(),deviceid,"Android")).enqueue(new Callback<ModelLogin>() {

                    @Override
                    public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {

                        if (response.body().getStatusCode().equals(200)) {

                            mFirebaseAnalytics.setUserId(response.body().getId());

                            Log.e("business_name1",response.body().getBussinessName()+"");

                            if (response.body().getPermissionWholeseller().equals("0")) {

                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                Log.e("usernotfound",response.body().getMessage()+"");

                            if (response.body().getViewWholesellerPage().equals("1")) {


                                if (response.body().getPermissionWholeseller().equals("1")) {


                                    Intent intent = new Intent(Login.this, Next_Login_Page.class);

                                    Log.e("vanstock1",response.body().getVanstock());

                                    intent.putExtra("username", response.body().getUserName());
                                    intent.putExtra("unique_id", response.body().getId());
                                    intent.putExtra("role", response.body().getRole());
                                    intent.putExtra("permission_Quote", response.body().getQuotes());
                                    intent.putExtra("Image", response.body().getImage());
                                    intent.putExtra("permission_cat", response.body().getCategorie());
                                    intent.putExtra("permission_orders", response.body().getOrders());
                                    intent.putExtra("permission_pro_details", response.body().getProjectDetails());
                                    intent.putExtra("permission_catelogues", response.body().getCatalogues());
                                    intent.putExtra("permission_all_orders", response.body().getAllOrders());
                                    intent.putExtra("permission_awaiting", response.body().getOrdersToApprove());
                                    intent.putExtra("permission_see_cost", response.body().getSeecost());
                                    intent.putExtra("awaiting_data", response.body().getNumberofawating());
                                    intent.putExtra("permission_wholeseller", response.body().getPermissionWholeseller());
                                    intent.putExtra("permission_add_product", response.body().getAddproduct());
                                    intent.putExtra("Wholeseller_engineer_id", response.body().getWholesellerEngineerId());
                                    intent.putExtra("business_name", response.body().getBussinessName());
                                    intent.putExtra("ViewWholesellerPage", response.body().getViewWholesellerPage());
                                    intent.putExtra("stripe_publish_key", response.body().getPublishKey());
                                    intent.putExtra("email", email);
                                    startActivity(intent);

                                } else {


                                    Log.e("vanstock2",response.body().getVanstock());

                                    Intent intent = new Intent(Login.this, GetPostcode.class);
                                    intent.putExtra("username", response.body().getUserName());
                                    intent.putExtra("unique_id", response.body().getId());
                                    intent.putExtra("permission_Quote", response.body().getQuotes());
                                    intent.putExtra("role", response.body().getRole());
                                    intent.putExtra("Image", response.body().getImage());
                                    intent.putExtra("permission_cat", response.body().getCategorie());
                                    intent.putExtra("permission_orders", response.body().getOrders());
                                    intent.putExtra("permission_pro_details", response.body().getProjectDetails());
                                    intent.putExtra("permission_catelogues", response.body().getCatalogues());
                                    intent.putExtra("permission_all_orders", response.body().getAllOrders());
                                    intent.putExtra("permission_awaiting", response.body().getOrdersToApprove());
                                    intent.putExtra("permission_see_cost", response.body().getSeecost());
                                    intent.putExtra("awaiting_data", response.body().getNumberofawating());
                                    intent.putExtra("permission_wholeseller", response.body().getPermissionWholeseller());
                                    intent.putExtra("permission_add_product", response.body().getAddproduct());
                                    intent.putExtra("Wholeseller_engineer_id", response.body().getWholesellerEngineerId());
                                    intent.putExtra("business_name", response.body().getBussinessName());
                                    intent.putExtra("ViewWholesellerPage", response.body().getViewWholesellerPage());
                                    intent.putExtra("stripe_publish_key", response.body().getPublishKey());
                                    intent.putExtra("email", email);
                                    startActivity(intent);

                                }

                                Paper.book().write("vanstock", response.body().getVanstock());

                            }
                 else {

                                /***************************************************PASS VALUES TO "Next_Login_Page USING INTENT" ***************************************************************/

                                Log.e("vanstock3",response.body().getVanstock());

                                Intent intent = new Intent(Login.this, Next_Login_Page.class);
                                intent.putExtra("username", response.body().getUserName());
                                intent.putExtra("unique_id", response.body().getId());
                                intent.putExtra("role", response.body().getRole());
                                intent.putExtra("permission_Quote", response.body().getQuotes());
                                intent.putExtra("Image", response.body().getImage());
                                intent.putExtra("permission_cat", response.body().getCategorie());
                                intent.putExtra("permission_orders", response.body().getOrders());
                                intent.putExtra("permission_pro_details", response.body().getProjectDetails());
                                intent.putExtra("permission_catelogues", response.body().getCatalogues());
                                intent.putExtra("permission_all_orders", response.body().getAllOrders());
                                intent.putExtra("permission_awaiting", response.body().getOrdersToApprove());
                                intent.putExtra("permission_see_cost", response.body().getSeecost());
                                intent.putExtra("awaiting_data", response.body().getNumberofawating());
                                intent.putExtra("permission_wholeseller", response.body().getPermissionWholeseller());
                                intent.putExtra("permission_add_product", response.body().getAddproduct());
                                intent.putExtra("Wholeseller_engineer_id", response.body().getWholesellerEngineerId());
                                intent.putExtra("business_name", response.body().getBussinessName());
                                intent.putExtra("ViewWholesellerPage", response.body().getViewWholesellerPage());
                                intent.putExtra("stripe_publish_key", response.body().getPublishKey());
                                intent.putExtra("email", email);
                                startActivity(intent);

                                /************************************************************End************************************************************************************************/

                            }


                            /***************************************************USING PAPER DB LIBRARAY TO STORE DATA LOCALY" ***************************************************************/

                                Log.e("vanstock4",response.body().getVanstock());
                                Log.e("getcatalogues",response.body().getCatalogues());
                                Log.e("business_name22",response.body().getBussinessName()+"");

                            Paper.book().write("UserImage", response.body().getImage());
                            Paper.book().write("userid", response.body().getId());
                            Paper.book().write("unique_id", response.body().getId());
                            Paper.book().write("vanstock", response.body().getVanstock());
                            Paper.book().write("permission_Quote", response.body().getQuotes());
                            Paper.book().write("business_name", response.body().getBussinessName());
                            Paper.book().write("datarole", response.body().getRole());
                            Paper.book().write("permission_see_cost", response.body().getSeecost());
                            Paper.book().write("permission_cats", response.body().getCategorie());
                            Paper.book().write("permission_orderss", response.body().getOrders());
                            Paper.book().write("permission_pro_detailssse", response.body().getProjectDetails());
                            Paper.book().write("permission_cateloguess", response.body().getCatalogues());
                            Paper.book().write("permission_all_orders", response.body().getAllOrders());
                            Paper.book().write("permission_awaiting", response.body().getOrdersToApprove());
                            Paper.book().write("awaiting_data", response.body().getNumberofawating());
                            Paper.book().write("permission_add_product", response.body().getAddproduct());
                            Paper.book().write("permission_wholeseller", response.body().getPermissionWholeseller());
                            Paper.book().write("Wholeseller_engineer_id", response.body().getWholesellerEngineerId());
                            Paper.book().write("ViewWholesellerPage", response.body().getViewWholesellerPage());
                            Paper.book().write("business_id", response.body().getBusinessId());
                            Paper.book().write("stripe_publish_key", response.body().getPublishKey());
                            Paper.book().write("permission_Addtocart", response.body().getAddtocart());

                            /*************************************************************End***********************************************************************************************/


                            SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString("email", email);
                            editor.putString("password", password1);
                            editor.putString("stripe_publish_key", response.body().getPublishKey());

                            editor.putString("unique_id", response.body().getId());
                            editor.putString("username", response.body().getUserName());
                            editor.putString("role", response.body().getRole());
                            editor.putString("permission_Quote", response.body().getQuotes());
                            editor.putString("Image", response.body().getImage());
                            editor.putString("permission_cat", response.body().getCategorie());
                            editor.putString("permission_orders", response.body().getOrders());
                            editor.putString("permission_pro_details", response.body().getProjectDetails());
                            editor.putString("permission_catelogues", response.body().getCatalogues());
                            editor.putString("permission_all_orders", response.body().getAllOrders());
                            editor.putString("permission_awaiting", response.body().getOrdersToApprove());
                            editor.putString("permission_see_cost", response.body().getSeecost());
                            editor.putString("permission_add_product", response.body().getAddproduct());
                            editor.putInt("awaiting_data", response.body().getNumberofawating());
                            editor.putString("permission_wholeseller", response.body().getPermissionWholeseller());
                            editor.putString("Wholeseller_engineer_id", String.valueOf(response.body().getWholesellerEngineerId()));
                            editor.putString("business_name", response.body().getBussinessName());
                            editor.putString("ViewWholesellerPage", response.body().getViewWholesellerPage());
                            editor.putString("vanstock", response.body().getVanstock());
                            editor.putString("business_validity", response.body().getBusiness_validity());

                            editor.apply();
                        }

                            else {

                                Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_SHORT).show();
                            }

                        }


                    else

                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialogs.dismiss();


                    }

                    @Override
                    public void onFailure(Call<ModelLogin> call, Throwable t) {

                        Toast.makeText(Login.this,t.getMessage() , Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                        progressDialogs.dismiss();
                    }
                });

        }

     /************************************************************End********************************************************************************/


    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getClass().getName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }
}




