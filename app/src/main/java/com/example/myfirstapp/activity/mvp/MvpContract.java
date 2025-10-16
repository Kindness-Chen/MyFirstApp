package com.example.myfirstapp.activity.mvp;

import com.example.myfirstapp.base.mvp.BasePresenter;
import com.example.myfirstapp.base.mvp.BaseView;

public class MvpContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
    }

    interface Presenter extends BasePresenter {
        void sendMessage();
    }
}
