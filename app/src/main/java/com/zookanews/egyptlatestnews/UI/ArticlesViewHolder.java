package com.zookanews.egyptlatestnews.UI;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zookanews.egyptlatestnews.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticlesViewHolder extends RecyclerView.ViewHolder {

    public final TextView title;
    public TextView description;
    public TextView pubDate;
    public ImageView thumb;
    public ImageView isRead;
    public ImageView isFavorite;

    ArticlesViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title_textView);
        description = itemView.findViewById(R.id.description_textView);
        thumb = itemView.findViewById(R.id.article_imageView);
        pubDate = itemView.findViewById(R.id.pubDate_textView);
        isRead = itemView.findViewById(R.id.isRead_imageView);
        isFavorite = itemView.findViewById(R.id.isFavorite_imageView);
    }
}