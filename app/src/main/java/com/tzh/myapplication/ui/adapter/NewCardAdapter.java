package com.tzh.myapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tzh.myapplication.R;
import com.tzh.myapplication.ui.dto.CardDto;

import java.util.List;

public class NewCardAdapter extends RecyclerView.Adapter<NewCardAdapter.CardHolder> {

    private List<CardDto> mCardBeanList;
    private RequestOptions mRequestOptions;

    public NewCardAdapter(List<CardDto> cardBeanList) {
        mCardBeanList = cardBeanList;
        mRequestOptions = new RequestOptions();
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_card, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardHolder holder, final int position) {
        final CardDto bean = mCardBeanList.get(position);
        Glide.with(holder.itemView).load(bean.getUrl()).apply(mRequestOptions).into(holder.img);
        holder.text.setText(bean.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "click " + bean.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(),"点击了 img",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardBeanList.size();
    }

    static class CardHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView text;

        public CardHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.card_img);
            text = itemView.findViewById(R.id.card_txt);
        }
    }
}
