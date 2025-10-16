package com.example.myfirstapp.activity.mvp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myfirstapp.R;

public class MVPActivity extends AppCompatActivity implements MvpContract.View {

    MvpPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        mPresenter = new MvpPresenter(this);
        mPresenter.sendMessage();
    }

    @Override
    public void setPresenter(MvpContract.Presenter presenter) {
        mPresenter = (MvpPresenter) presenter;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }
}