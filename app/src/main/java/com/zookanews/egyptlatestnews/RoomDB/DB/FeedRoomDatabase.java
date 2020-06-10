package com.zookanews.egyptlatestnews.RoomDB.DB;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zookanews.egyptlatestnews.Parser.SaxXmlParser;
import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DAO.CategoryDao;
import com.zookanews.egyptlatestnews.RoomDB.DAO.FeedDao;
import com.zookanews.egyptlatestnews.RoomDB.DAO.WebsiteDao;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Category;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Website;

@Database(entities = {Article.class, Category.class, Feed.class, Website.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class FeedRoomDatabase extends RoomDatabase {
    private static volatile FeedRoomDatabase INSTANCE;
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(INSTANCE).execute();
        }

//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            new SyncDBAsync(INSTANCE).execute();
//        }
    };

    public static FeedRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FeedRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FeedRoomDatabase.class, "feed_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract ArticleDao articleDao();

    public abstract CategoryDao categoryDao();

    public abstract FeedDao feedDao();

    public abstract WebsiteDao websiteDao();

    static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final CategoryDao categoryDao;
        private final FeedDao feedDao;
        private final WebsiteDao websiteDao;
        private final ArticleDao articleDao;

        PopulateDbAsync(FeedRoomDatabase db) {
            categoryDao = db.categoryDao();
            feedDao = db.feedDao();
            websiteDao = db.websiteDao();
            articleDao = db.articleDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (Category category : DBStartupData.categories) {
                categoryDao.insertCategory(category);
            }

            for (Website website : DBStartupData.websites) {
                websiteDao.insertWebsite(website);
            }

            for (Feed feed : DBStartupData.feeds) {
                feedDao.insertFeed(feed);
                for (Article article : SaxXmlParser.parse(feed.getFeedRssLink())) {
                    articleDao.insertArticle(new Article(
                            article.getArticleTitle(),
                            article.getArticleLink(),
                            article.getArticleDescription(),
                            article.getArticlePubDate(),
                            article.getArticleThumbnailUrl(),
                            feed.getWebsiteName(),
                            feed.getCategoryName(),
                            false,
                            false
                    ));
                }
            }
            return null;
        }
    }
}
