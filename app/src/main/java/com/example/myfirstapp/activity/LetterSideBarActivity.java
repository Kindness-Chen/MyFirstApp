package com.example.myfirstapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.view.LetterSideBar;

public class LetterSideBarActivity extends AppCompatActivity {

    private LetterSideBar letterSideBar;
    private TextView tvTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lettet_side_bar);

        letterSideBar = findViewById(R.id.ls_guile);
        tvTouch = findViewById(R.id.tv_touch);
        letterSideBar.setTouchTextListener(new LetterSideBar.TouchTextListener() {
            @Override
            public void onShowTouchText(String touchText, boolean isDown) {
                if (isDown) {
                    tvTouch.setVisibility(View.VISIBLE);
                    tvTouch.setText(touchText);
                } else {
                    letterSideBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvTouch.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        letterSideBar.setTouchTextListener(null);
    }
}