package com.zookanews.egyptlatestnews.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.ArticleViewModel;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteArticlesAdapter extends RecyclerView.Adapter<ArticlesViewHolder> {

    private final Context context;
    private final ArticleViewModel articleViewModel;
    private List<Article> articles;
    private SharedPreferences sharedPreferences;

    FavoriteArticlesAdapter(Context context, ArticleViewModel articleViewModel) {
        this.context = context;
        this.articleViewModel = articleViewModel;
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticlesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticlesViewHolder holder, int position) {
        final Article article = articles.get(position);
        holder.title.setText(Html.fromHtml(article.getArticleTitle()));
        if (article.getIsRead()) {
            holder.title.setTextColor(Color.DKGRAY);
            holder.isRead.setImageResource(R.drawable.ic_read);
        } else {
            holder.isRead.setImageResource(R.drawable.ic_unread);
            holder.title.setTextColor(Color.parseColor("#d32f2f"));
        }
        if (article.getIsFavorite()) {
            holder.isFavorite.setImageResource(R.drawable.ic_action_favorite_checked);
        } else {
            holder.isFavorite.setImageResource(R.drawable.ic_action_favorite_unchecked);
        }
        holder.description.setText(Html.fromHtml(article.getArticleDescription()));
        holder.description.setTextColor(Color.DKGRAY);
        holder.pubDate.setText(DateUtils.getRelativeTimeSpanString(
                article.getArticlePubDate().getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS));
        holder.pubDate.setTextColor(Color.LTGRAY);
        if (article.getArticleThumbnailUrl() != null) {
            Picasso.get().load(article.getArticleThumbnailUrl()).into(holder.thumb);
        } else {
            holder.thumb.setImageResource(R.drawable.thumb_placeholder);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                sharedPreferences.edit().putInt("article_id", article.getArticleId()).apply();
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra("articleId", article.getArticleId());
                context.startActivity(intent);
            }
        });
        holder.isFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (article.getIsFavorite()) {
                    articleViewModel.updateFavoriteStatus(article.getArticleId(), false);
                    holder.isFavorite.setImageResource(R.drawable.ic_action_favorite_unchecked);
                } else if (!article.getIsFavorite()) {
                    articleViewModel.updateFavoriteStatus(article.getArticleId(), true);
                    holder.isFavorite.setImageResource(R.drawable.ic_action_favorite_checked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (articles != null) {
            return articles.size();
        } else {
            return 0;
        }
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    Article getArticleAt(int position) {
        return articles.get(position);
    }
}
