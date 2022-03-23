package com.pickmyorder.asharani.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pickmyorder.asharani.Fragments.Description_Tab;
import com.pickmyorder.asharani.Fragments.Pdf_indo_Tab;
import com.pickmyorder.asharani.Fragments.Specification_Tab;

public class Adapter_Viewpager extends FragmentStatePagerAdapter {

    int tabcount;

    public Adapter_Viewpager(FragmentManager fragmentManager, int tabCount) {

        super(fragmentManager);
        this.tabcount=tabCount;

    }

    @Override
    public Fragment getItem(int i) {

        switch ( i) {

            case 0:
        {
            return new Description_Tab();
        }
            case 1:
        {
            return new Specification_Tab();
        }
            case 2:
        {
            return new Pdf_indo_Tab();
        }

        default:

        return null;

      }
    }

    @Override
    public int getCount() {
        return tabcount;
    }

}
