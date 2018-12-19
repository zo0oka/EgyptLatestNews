package com.zookanews.egyptlatestnews.UI;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zookanews.egyptlatestnews.R;
import com.zookanews.egyptlatestnews.RoomDB.DB.DateConverter;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {

    private List<Article> articles;
    private Context context;

    ArticlesAdapter(Context context) {
        this.context = context;
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
        articlesViewHolder.title.setText(article.getArticleTitle());
        articlesViewHolder.description.setText(article.getArticleDescription());
        articlesViewHolder.pubDate.setText(DateUtils.getRelativeTimeSpanString(DateConverter.toTimestamp(article.getArticlePubDate()),
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        if (article.getArticleThumbnailUrl() != null) {
            Picasso.get().load(article.getArticleThumbnailUrl()).into(articlesViewHolder.thumb);
        } else {
            articlesViewHolder.thumb.setImageResource(R.drawable.thumb_placeholder);
        }
        articlesViewHolder.category.setText(article.getCategoryName());
        articlesViewHolder.website.setText(article.getWebsiteName());
        articlesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra("articleId", article.getArticleId());
                context.startActivity(intent);
            }
        });
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
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
        private TextView category;
        private TextView website;
        private ImageView thumb;

        ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_textView);
            description = itemView.findViewById(R.id.description_textView);
            thumb = itemView.findViewById(R.id.article_imageView);
            pubDate = itemView.findViewById(R.id.pubDate_textView);
            category = itemView.findViewById(R.id.category_textView);
            website = itemView.findViewById(R.id.website_textView);
        }
    }
}
