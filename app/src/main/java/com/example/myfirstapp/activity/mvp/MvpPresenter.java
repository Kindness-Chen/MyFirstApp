package com.example.myfirstapp.activity.mvp;

import com.yitong.logs.Logs;

public class MvpPresenter implements MvpContract.Presenter {

    private MvpContract.View mView;

    public MvpPresenter(MvpContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void sendMessage() {
        mView.showMessage("success");
        Logs.e("MvpPresenter", "success");
    }
}
