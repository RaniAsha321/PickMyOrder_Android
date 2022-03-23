package com.pickmyorder.asharani.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.pickmyorder.asharani.R;
import com.pickmyorder.asharani.Storage.databaseSqlite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.paperdb.Paper;

import static com.pickmyorder.asharani.Activities.Next_Login_Page.MY_PREFS_NAME;

public class Product_Zoom_Image extends AppCompatActivity implements View.OnTouchListener{

    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    public static String fileNAME;
    public static int framePos = 0;

    private float scale = 0;
    private float newDist = 0;

    // Fields
    private String TAG = this.getClass().getSimpleName();

    // We can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    // Remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;

    ImageView zoom,zoom_cross;
    final Handler handler = new Handler();
    final int delay = 1000; // 1000 milliseconds == 1 second
    long Todaymsecond;
    String business_validity;
    com.pickmyorder.asharani.Storage.databaseSqlite databaseSqlite;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_zoom_image);
        zoom = findViewById(R.id.zoom_image);
        zoom_cross = findViewById(R.id.zoom_cross);
        databaseSqlite = new databaseSqlite(getApplicationContext());
        SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        business_validity =  sharedPreferences.getString("business_validity",null);
        sharedPreferences.getAll();

        String image = Paper.book().read("zoomImage");
        Glide.with(getApplicationContext()).load(image).into(zoom);

        zoom.setOnTouchListener(this);

        zoom_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product_Zoom_Image.this.finish();
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        // Dump touch event to log
        dumpEvent(event);

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: //first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG" );
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;

            case MotionEvent.ACTION_UP: //first finger lifted
            case MotionEvent.ACTION_POINTER_UP: //second finger lifted
                mode = NONE;
                Log.d(TAG, "mode=NONE" );
                break;


            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM && event.getPointerCount() == 2) {
                float newDist = spacing(event);
                matrix.set(savedMatrix);
                if (newDist > 10f) {
                    scale = newDist / oldDist;
                    matrix.postScale(scale, scale, mid.x, mid.y);
                }
                if (lastEvent != null) {
                    newRot = rotation(event);
                    float r = newRot - d;
                    matrix.postRotate(r, view.getMeasuredWidth() / 2,
                            view.getMeasuredHeight() / 2);
                }
            }
                break;

        }
        // Perform the transformation
        view.setImageMatrix(matrix);

        return true; // indicate event was handled

    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);

        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);

    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x/2, y/2);

    }


    /** Show an event in the LogCat view, for debugging */

    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
                "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_" ).append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid " ).append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")" );
        }

        sb.append("[" );

        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#" ).append(i);
            sb.append("(pid " ).append(event.getPointerId(i));
            sb.append(")=" ).append((int) event.getX(i));
            sb.append("," ).append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())

                sb.append(";" );
        }

        sb.append("]" );
        Log.d(TAG, sb.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();

        Business_Validity_Check(business_validity);

    }


    private void Business_Validity_Check(String business_validity) {

        if(business_validity != null && !business_validity.equals("")){

            processCurrentTime(business_validity);

        }
    }

    private void processCurrentTime(String business_validity) {

        if (!isDataConnectionAvailable(Product_Zoom_Image.this)) {
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

        final Dialog dialog= new Dialog(Product_Zoom_Image.this);
        dialog.setContentView(R.layout.custom_dialog_trial);
        dialog.show();
        Button btn_continue = dialog.findViewById(R.id.btn_continue_trial);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Login.class);
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
