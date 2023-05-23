package com.example.besammen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//This class will control the rest of the fragments

public class PagerAdapter extends FragmentPagerAdapter {

    int tabCount;


    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount = behavior;


    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ChatFragment();


            case 1:
                return new GroupFragment();


            case 2:
                return new EventsFragment();


            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
