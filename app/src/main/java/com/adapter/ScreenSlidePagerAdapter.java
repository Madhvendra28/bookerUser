package com.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fragment.FlipkartFragment;
import com.fragment.MiStoreFragment;
import com.model.confirmclaim.Datum;

import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    List<Datum> data;

    public ScreenSlidePagerAdapter(@NonNull FragmentManager fm, List<Datum> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MiStoreFragment(data);
            case 1:
                return new FlipkartFragment(data);
            default:
                return new MiStoreFragment(data);
        }

    }

    @Override
    public int getCount() {
        return 2;
    }
}
