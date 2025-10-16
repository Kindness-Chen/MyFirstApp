package com.example.myfirstapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;

import com.example.myfirstapp.R;
import com.example.myfirstapp.view.StepView;

public class StepViewActivity extends AppCompatActivity {

    private StepView svCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_view);

        svCount = findViewById(R.id.sv_count);

        svCount.setMax(4000);

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 4000);
        valueAnimator.setDuration(2000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                svCount.setProgress((int)progress);
            }
        });
    }
}