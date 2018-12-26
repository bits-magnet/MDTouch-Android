package com.example.hsc.mdtouch;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PagerActivity extends AppCompatActivity {

    int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        assert mViewPager != null;
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(page);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void Finish(View v){
        Utils.saveSharedSetting(PagerActivity.this,MainActivity.PREF,"false");
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceHolderFragment extends Fragment{

        private static final String ARGS = "section number";

        ImageView view;

        int[] img = new int[]{ R.mipmap.p1, R.mipmap.p2,R.mipmap.p3 };
        String[] txt = new String[]{ "Book an appointment with the right doctor.",
                                     "Too busy to see a doctor, Chat online instead.",
                                     "Keep your medical history handy." };

        public PlaceHolderFragment(){

        }

        public static PlaceHolderFragment newInstance(int s){
            PlaceHolderFragment f = new PlaceHolderFragment();
            Bundle args = new Bundle();
            args.putInt(ARGS, s);
            f.setArguments(args);
            return f;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_pager,container,false);

            TextView text = (TextView) root.findViewById(R.id.text_label);
            text.setText(txt[getArguments().getInt(ARGS)-1]);

            ImageView image = (ImageView) root.findViewById(R.id.image_label);
            image.setBackgroundResource(img[getArguments().getInt(ARGS)-1]);

            return root;

        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceHolderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

}
