package com.udevel.widgetlab.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.udevel.widgetlab.TypingIndicatorView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TypingIndicatorView typingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typingIndicatorView = ((TypingIndicatorView) findViewById(R.id.typing_indicator_view));
        typingIndicatorView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (typingIndicatorView.isAnimationStarted()) {
            typingIndicatorView.stopDotAnimation();
        } else {
            typingIndicatorView.startDotAnimation();
        }
    }
}
