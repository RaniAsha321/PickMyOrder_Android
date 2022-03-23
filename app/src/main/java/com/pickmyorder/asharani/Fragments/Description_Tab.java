package com.pickmyorder.asharani.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pickmyorder.asharani.R;

import io.paperdb.Paper;

public class Description_Tab extends Fragment {

    public static TextView description;
    Context context;
    WebView webView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_description__tab, container, false);
        description=view.findViewById(R.id.fg_description);
        webView = view.findViewById(R.id.web_desc);


        String desc= Paper.book().read("product_description");

        webView.loadDataWithBaseURL(null, desc, "text/html", "utf-8", null);

        //  description.setText(Html.fromHtml(desc));
        return view;
    }
}
