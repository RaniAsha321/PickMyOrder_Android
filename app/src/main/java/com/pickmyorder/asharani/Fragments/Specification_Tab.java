package com.pickmyorder.asharani.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.pickmyorder.asharani.R;

import io.paperdb.Paper;


public class Specification_Tab extends Fragment {

    TextView specification;
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_specification__tab, container, false);

        specification=view.findViewById(R.id.specification);
        webView = view.findViewById(R.id.web_specification);

        String spec= Paper.book().read("product_specification");

        Log.e("spec",spec+"");

        String data = "<table style=\"max-width: 100%; background-color: #ffffff; border-spacing: 0px; width: 515.406px; margin-bottom: 20px; border-radius: 0px; border: none; color: #393536; font-family: Roboto, Arial, Helvetica, sans-serif; font-size: 14px;\">\n<tbody>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Colour</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">White</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Current Rating</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">13A</td>\n</tr>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Dimensions</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">(H)86 mm x (W)146 mm</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Dual Earth</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">Yes</td>\n</tr>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Fixing Centres</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">120.6mm</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Frequency</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">50/60 Hz</td>\n</tr>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Guarantee</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">1 Year</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Height</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">86mm</td>\n</tr>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Material</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">Moulded</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Mounting</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">Wall</td>\n</tr>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Number of Gangs</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">2</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Number of Poles</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">Double Pole</td>\n</tr>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Packaging Types</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">Each</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Product Range</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">Sollysta</td>\n</tr>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Standards</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">BS 1363</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Switched Unswitched</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">Switched</td>\n</tr>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Unspsc</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">39121406</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Voltage Rating</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">230V</td>\n</tr>\n<tr style=\"background: #edf1f3;\">\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Weight</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">0.248kg</td>\n</tr>\n<tr>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c; font-weight: 600; width: 215.922px;\">Width</td>\n<td style=\"padding: 8px; line-height: 20px; vertical-align: top; border: none; color: #60686c;\">146mm</td>\n</tr>\n</tbody>\n</table>";

        webView.loadDataWithBaseURL(null, spec, "text/html", "utf-8", null);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            specification.setText(Html.fromHtml(spec, Html.FROM_HTML_MODE_LEGACY));
        } else {
            specification.setText(Html.fromHtml(spec));
        }*/
       // specification.setText(Html.fromHtml(spec));

        return view;
    }

}
