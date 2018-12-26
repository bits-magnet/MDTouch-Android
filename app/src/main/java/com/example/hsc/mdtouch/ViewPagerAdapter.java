package com.example.hsc.mdtouch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    public String title[] = { "Personal", "Medical", "LifeStyle" };

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch(position){
            case 0:
                fragment = new PersonalFragment();
                break;
            case 1:
                fragment = new MedicalFragment();
                break;
            case 2:
                fragment = new LifestyleFragment();
                break;
        }

        return fragment;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
