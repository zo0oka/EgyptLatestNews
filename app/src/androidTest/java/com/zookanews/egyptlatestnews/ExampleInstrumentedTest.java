package com.zookanews.egyptlatestnews;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zookanews.egyptlatestnews.RoomDB.DAO.ArticleDao;
import com.zookanews.egyptlatestnews.RoomDB.DB.FeedRoomDatabase;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private ArticleDao articleDao;
    private FeedRoomDatabase db;

    @Before
    public void createDB() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(appContext, FeedRoomDatabase.class).build();
        articleDao = db.articleDao();
    }

    @After
    public void closeDB() {
        db.close();
    }

    @Test
    public void testUpdateReadStatus() {
        for (int i = 0; i < 100; i++) {
            Article article = new Article(
                    String.valueOf(i),
                    String.valueOf(i),
                    String.valueOf(i),
                    Calendar.getInstance().getTime(),
                    String.valueOf(i),
                    String.valueOf(i),
                    String.valueOf(i),
                    false
            );
            articleDao.insertArticle(article);
        }
        for (int i = 0; i <= 30; i++) {
            articleDao.updateReadStatus(i, true);
        }
        List<Article> read = articleDao.getReadArticles();
        assertThat(read.size(), equalTo(30));
    }
}
