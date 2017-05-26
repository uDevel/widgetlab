package com.udevel.widgetlab.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new DemoViewPagerAdapter(getSupportFragmentManager()));
    }


    public class DemoViewPagerAdapter extends FragmentPagerAdapter {
        public DemoViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return ShowCaseFragment.newInstance();
                case 1:
                    return DemoAttributeFragment.newInstance();
                case 2:
                    return DemoAttribute2Fragment.newInstance();
                default:
                    throw new IllegalArgumentException("We only have 3 fragments");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Show Case";
                case 1:
                    return "Attributes Demo";
                case 2:
                    return "Attributes Demo 2";
                default:
                    throw new IllegalArgumentException("We only have 3 fragments");
            }
        }
    }
}
