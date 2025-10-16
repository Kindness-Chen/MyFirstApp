package com.example.myfirstapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myfirstapp.R;
import com.example.myfirstapp.model.ViewFlowModel;
import com.yitong.android.adapter.YTBaseAdapter;

/**
 * 轮播图片的适配器
 *
 * @author Charley Chen 2015-1-7 下午4:15:34
 * @company Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
 */
public class DynamicBannersViewFlowAdapter extends YTBaseAdapter {

    private final int defaultResId;

    public DynamicBannersViewFlowAdapter(Activity activity, int defaultResId) {
        this.context = activity;
        this.defaultResId = defaultResId;
    }

    @Override
    public int getCount() {
        // 返回很大的值使得getView中的position不断增大来实现
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position % items.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.imgViewAd);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ViewFlowModel viewFlowModel = (ViewFlowModel) getItem(position);
        //加载图片到imageView
        Glide.with(context)
                .load(viewFlowModel.getImgUrl())
                .placeholder(defaultResId)
                .error(defaultResId)
                .into(viewHolder.imageView);

        return convertView;
    }

    public class ViewHolder {
        public ImageView imageView;
    }
}
