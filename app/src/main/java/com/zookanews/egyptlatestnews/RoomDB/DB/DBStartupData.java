package com.zookanews.egyptlatestnews.RoomDB.DB;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Category;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Feed;
import com.zookanews.egyptlatestnews.RoomDB.Entities.Website;

import java.util.ArrayList;

class DBStartupData {
    static final ArrayList<Category> categories = new ArrayList<Category>() {
        {
            add(new Category("latest_news", "أخر الأخبار"));
            add(new Category("politics", "الأخبار السياسية"));
            add(new Category("accidents", "حوادث و قضايا"));
            add(new Category("finance", "إقتصاد و بنوك"));
            add(new Category("sports", "أخبار الرياضة"));
            add(new Category("woman", "عالم المرأة و المنوعات"));
            add(new Category("arts", "أخبار الفن"));
            add(new Category("technology", "علوم و تكنولوجيا"));
            add(new Category("videos", "فيديوهات"));
            add(new Category("automotive", "عالم السيارات"));
            add(new Category("investigations", "تحقيقات و حوارات"));
            add(new Category("culture", "الثقافة"));
            add(new Category("travel", "سياحة و طيران"));
            add(new Category("health", "الصحة و اللياقة"));
        }
    };
    static final ArrayList<Feed> feeds = new ArrayList<Feed>() {
        {
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds", "latest_news", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=7", "accidents", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=4", "finance", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=8", "sports", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=69", "woman", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=12", "woman", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=10", "arts", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=9", "technology", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=96", "woman", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=98", "woman", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=107", "woman", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=86", "automotive", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=5", "investigations", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=6", "culture", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=61", "finance", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=22", "travel", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=109", "health", "almasry_alyoum"));
            add(new Feed("AlMasryAlYoum", "https://www.almasryalyoum.com/rss/rssfeeds?sectionId=110", "health", "almasry_alyoum"));
            add(new Feed("AlWatan", "https://www.elwatannews.com/home/rssfeeds", "latest_news", "alwatan"));
            add(new Feed("AlDostour", "https://www.dostor.org/rss.aspx", "latest_news", "aldostour"));
            add(new Feed("Akhbarak", "http://www.akhbarak.net/api/top_news.rss", "latest_news", "akhbarak"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/important", "latest_news", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/26", "accidents", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/27", "accidents", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/29", "finance", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/20", "sports", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/21", "sports", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/25", "sports", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/82", "woman", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/44", "arts", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/34", "technology", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/37", "technology", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/639", "videos", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/175", "videos", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/49", "automotive", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/9", "investigations", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/48", "culture", "alwafd"));
//            add(new Feed("AlWafd", "https://alwafd.news/feed/54", "travel", "alwafd"));
            add(new Feed("BBCArabic", "http://feeds.bbci.co.uk/arabic/rss.xml", "latest_news", "bbc_arabic"));
            add(new Feed("RoseAlYousef", "http://www.rosaelyoussef.com/rss", "latest_news", "rose_alyousef"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=39", "latest_news", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=45", "accidents", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=42", "finance", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=38", "sports", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=46", "woman", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=37", "arts", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=48", "technology", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=51", "woman", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=1138", "automotive", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=61", "investigations", "alfagr"));
            add(new Feed("AlGagr", "https://www.elfagr.com/rss.aspx?id=41", "videos", "alfagr"));
            add(new Feed("AkhbarAlHawadeth", "https://hawadeth.akhbarelyom.com/newsfeed.aspx?g=4&id=65", "latest_news", "akhbar_alhawadeth"));
            add(new Feed("AkhbarAlHawadeth", "https://hawadeth.akhbarelyom.com/newsfeed.aspx?g=4&id=63", "accidents", "akhbar_alhawadeth"));
            add(new Feed("AkhbarAlHawadeth", "https://hawadeth.akhbarelyom.com/newsfeed.aspx?g=4&id=60", "investigations", "akhbar_alhawadeth"));
            add(new Feed("AkhbarAlHawadeth", "https://hawadeth.akhbarelyom.com/newsfeed.aspx?g=4&id=62", "accidents", "akhbar_alhawadeth"));
            add(new Feed("AkhbarAlHawadeth", "https://hawadeth.akhbarelyom.com/newsfeed.aspx?g=4&id=66", "accidents", "akhbar_alhawadeth"));
            add(new Feed("SadaElBalad", "https://www.elbalad.news/rss.aspx", "latest_news", "sada_albalad"));
            add(new Feed("BawabetVeto", "https://www.vetogate.com/rss.aspx", "latest_news", "bawabet_veto"));
            add(new Feed("AlMogaz", "http://almogaz.com/rss", "latest_news", "almogaz"));
            add(new Feed("AlMogaz", "http://almogaz.com/rss/politics", "politics", "almogaz"));
            add(new Feed("AlMogaz", "http://almogaz.com/rss/sports", "sports", "almogaz"));
            add(new Feed("AlMogaz", "http://almogaz.com/rss/art-culture", "culture", "almogaz"));
            add(new Feed("AlMogaz", "http://almogaz.com/rss/art-culture", "arts", "almogaz"));
            add(new Feed("AlMogaz", "http://almogaz.com/rss/crime", "accidents", "almogaz"));
            add(new Feed("AlMogaz", "http://almogaz.com/rss/economy", "finance", "almogaz"));
            add(new Feed("AlMogaz", "http://almogaz.com/rss/tech", "technology", "almogaz"));
        }
    };
    static final ArrayList<Website> websites = new ArrayList<Website>() {
        {
            add(new Website("almasry_alyoum", "المصرى اليوم", "https://www.almasryalyoum.com/", "https://www.almasryalyoum.com/rss/rssfeeds"));
            add(new Website("alwatan", "الوطن", "https://www.elwatannews.com/", "https://www.elwatannews.com/home/rssfeeds"));
            add(new Website("aldostour", "الدستور", "https://www.dostor.org/", "https://www.dostor.org/rss.aspx"));
            add(new Website("akhbarak", "أخبارك", "http://www.akhbarak.net/", "http://www.akhbarak.net/api/top_news.rss"));
            add(new Website("alwafd", "بوابة الوفد الإلكترونية", "https://alwafd.news/", "https://alwafd.news/feed/important"));
            add(new Website("bbc_arabic", "BBC", "http://www.bbc.com/arabic", "http://feeds.bbci.co.uk/arabic/rss.xml"));
            add(new Website("alfagr", "بوابة الفجر الإلكترونية", "https://www.elfagr.com/", "https://www.elfagr.com/rss.aspx?id=39"));
            add(new Website("rose_alyousef", "جريدة روز اليوسف", "http://www.rosaelyoussef.com/", "http://www.rosaelyoussef.com/rss"));
            add(new Website("akhbar_alhawadeth", "أخبار الحوادث", "https://hawadeth.akhbarelyom.com/", "https://hawadeth.akhbarelyom.com/newsfeed.aspx?g=4&id=65"));
            add(new Website("sada_albalad", "صدى البلد", "https://www.elbalad.news/", "https://www.elbalad.news/rss.aspx"));
            add(new Website("bawabet_veto", "بوابة فيتو", "https://www.vetogate.com/", "https://www.vetogate.com/rss.aspx"));
            add(new Website("almogaz", "الموجز", "http://www.almogaz.com/", "http://almogaz.com/rss"));
        }
    };

}
