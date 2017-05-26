package com.udevel.widgetlab.sample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShowCaseFragment extends Fragment {
    public ShowCaseFragment() {
        // Required empty public constructor
    }

    public static ShowCaseFragment newInstance() {
        return new ShowCaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_case, container, false);
    }
}
