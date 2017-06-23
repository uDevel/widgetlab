package com.udevel.widgetlab.sample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DemoAttributeFragment extends Fragment {
    public DemoAttributeFragment() {
        // Required empty public constructor
    }

    public static DemoAttributeFragment newInstance() {
        return new DemoAttributeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo_attribute, container, false);
    }
}
