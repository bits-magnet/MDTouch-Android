package com.example.hsc.mdtouch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MedicalViewPagerAdapter extends FragmentPagerAdapter {

    public String title[] = { "Hospitals", "Despensary", "Blood Banks", "Test Centers", "Emergency" };

    public MedicalViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch(position){
            case 0:
                fragment = new HospitalsFragment();
                break;
            case 1:
                fragment = new DespensaryFragment();
                break;
            case 2:
                fragment = new BloodBanksFragment();
                break;
            case 3:
                fragment = new TestCentersFragment();
                break;
            case 4:
                fragment = new EmergencyFragment();
                break;
        }

        return fragment;

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

}