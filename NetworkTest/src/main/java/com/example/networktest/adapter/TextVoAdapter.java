package com.example.networktest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.networktest.R;
import com.example.networktest.vo.TextVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Date：2025/10/13
 * Time：17:30
 * Author：chenshengrui
 */
public class TextVoAdapter extends RecyclerView.Adapter<TextVoAdapter.ViewHolder> {

    List<TextVo.DataDTO> data = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_net, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextVo.DataDTO dataDTO = data.get(position);
        if (dataDTO == null) return;
        holder.title.setText(dataDTO.getTitle() == null ? "" : dataDTO.getTitle());
        holder.content.setText(dataDTO.getUserName() == null ? "" : dataDTO.getUserName());

        ImageView iv_cover = holder.itemView.findViewById(R.id.img_logo);
        Glide.with(holder.itemView.getContext()).load("http://192.168.1.9:9102" + dataDTO.getCover()).into(iv_cover);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<TextVo.DataDTO> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
        }
    }
}
