package com.udevel.widgetlab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
