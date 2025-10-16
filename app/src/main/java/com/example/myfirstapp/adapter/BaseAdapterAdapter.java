package com.example.myfirstapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myfirstapp.R;
import com.example.myfirstapp.model.BaseAdapterModel;

import java.util.List;

public class BaseAdapterAdapter extends BaseQuickAdapter<BaseAdapterModel, BaseViewHolder> {

    public BaseAdapterAdapter(int layoutResId, @Nullable List<BaseAdapterModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseAdapterModel item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_content, item.getContent());
        helper.addOnClickListener(R.id.tv_content);
    }
}
