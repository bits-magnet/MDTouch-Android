package com.example.hsc.mdtouch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MedicalRecordsAdapter extends FragmentPagerAdapter {

    public String title[] = { "Ambulance Bill", "Blood Bank Bill", "Test Center Bill", "Prescription", "Hospital Bill" };

    public MedicalRecordsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch(position){
            case 0:
                fragment = new AmbulanceBillFragment();
                break;
            case 1:
                fragment = new BloodBankBillFragment();
                break;
            case 2:
                fragment = new TestCenterBillFragment();
                break;
            case 3:
                fragment = new PrescriptionFragment();
                break;
            case 4:
                fragment = new HospitalBillFragment();
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