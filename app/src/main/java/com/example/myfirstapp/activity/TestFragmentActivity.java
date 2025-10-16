package com.example.myfirstapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myfirstapp.R;
import com.example.myfirstapp.fragment.TestFragment;

public class TestFragmentActivity extends AppCompatActivity implements TestFragment.checkLife {

    private Button btnFragmentOne;

    private Button btnFragmentTwo;

    private TestFragment testFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        btnFragmentOne = findViewById(R.id.btn_fragment_one);
        btnFragmentTwo = findViewById(R.id.btn_fragment_two);

        btnFragmentOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testFragment == null) {
                    testFragment = TestFragment.newInstance("csr", "lyf");
                    getFragmentManager().beginTransaction()
                            .add(R.id.fl_container, testFragment, "f1")
                            .addToBackStack(null)
                            .commit();
                } else {
                    if (testFragment.isVisible()) {
                        getFragmentManager().beginTransaction()
                                .hide(testFragment)
                                .commit();
                    } else {
                        getFragmentManager().beginTransaction()
                                .show(testFragment)
                                .commit();
                    }

                }

            }
        });
        btnFragmentTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedInstanceState == null) {
                    getFragmentManager().beginTransaction()
                            .add(R.id.fl_container_two, TestFragment.newInstance("csr", "llz"), "f1")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    @Override
    public void initAgain() {
        testFragment = null;
    }
}