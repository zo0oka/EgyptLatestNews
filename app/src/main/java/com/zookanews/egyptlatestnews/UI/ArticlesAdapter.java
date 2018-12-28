package com.zookanews.egyptlatestnews.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.ViewModels.ArticleViewModel;

import java.util.Calendar;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {

    private final ArticleViewModel articleViewModel;
    private List<Article> articles;
    private Context context;
    private SharedPreferences sharedPreferences;

    ArticlesAdapter(Context context, ArticleViewModel articleViewModel) {
        this.context = context;
        this.articleViewModel = articleViewModel;
    }

    @NonNull
    @Override
    public ArticlesAdapter.ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_list_item, viewGroup, false);
        return new ArticlesAdapter.ArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticlesAdapter.ArticlesViewHolder articlesViewHolder, int position) {
        final Article article = articles.get(position);
        articlesViewHolder.title.setText(Html.fromHtml(article.getArticleTitle()));
        if (article.getIsRead()) {
            articlesViewHolder.title.setTextColor(Color.DKGRAY);
            articlesViewHolder.isRead.setImageResource(R.drawable.ic_read);
        } else {
            articlesViewHolder.isRead.setImageResource(R.drawable.ic_unread);
            articlesViewHolder.title.setTextColor(Color.parseColor("#d32f2f"));
        }
        if (article.getIsFavorite()) {
            articlesViewHolder.isFavorite.setImageResource(R.drawable.ic_action_favorite_checked);
        } else {
            articlesViewHolder.isFavorite.setImageResource(R.drawable.ic_action_favorite_unchecked);
        }
        articlesViewHolder.description.setText(Html.fromHtml(article.getArticleDescription()));
        articlesViewHolder.description.setTextColor(Color.DKGRAY);
        articlesViewHolder.pubDate.setText(DateUtils.getRelativeTimeSpanString(
                article.getArticlePubDate().getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS));
        articlesViewHolder.pubDate.setTextColor(Color.LTGRAY);
        if (article.getArticleThumbnailUrl() != null) {
            Picasso.get().load(article.getArticleThumbnailUrl()).into(articlesViewHolder.thumb);
        } else {
            articlesViewHolder.thumb.setImageResource(R.drawable.thumb_placeholder);
        }
        articlesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                sharedPreferences.edit().putInt("article_id", article.getArticleId()).apply();
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra("articleId", article.getArticleId());
                context.startActivity(intent);
            }
        });
        articlesViewHolder.isFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (article.getIsFavorite()) {
                    articleViewModel.updateFavoriteStatus(article.getArticleId(), false);
                    articlesViewHolder.isFavorite.setImageResource(R.drawable.ic_action_favorite_unchecked);
                } else if (!article.getIsFavorite()) {
                    articleViewModel.updateFavoriteStatus(article.getArticleId(), true);
                    articlesViewHolder.isFavorite.setImageResource(R.drawable.ic_action_favorite_checked);
                }
            }
        });
    }

    void setArticles(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    public Article getArticleAtPosition(int position) {
        return articles.get(position);
    }

    @Override
    public int getItemCount() {
        if (articles != null) {
            return articles.size();
        } else return 0;
    }

    class ArticlesViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private TextView description;
        private TextView pubDate;
        private ImageView thumb, isRead, isFavorite;

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
}
