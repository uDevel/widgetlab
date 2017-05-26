package com.udevel.widgetlab.sample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DemoAttribute2Fragment extends Fragment {
    public DemoAttribute2Fragment() {
        // Required empty public constructor
    }

    public static DemoAttribute2Fragment newInstance() {
        return new DemoAttribute2Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo_attribute2, container, false);
    }
}
