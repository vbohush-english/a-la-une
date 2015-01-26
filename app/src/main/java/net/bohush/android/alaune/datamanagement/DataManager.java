package net.bohush.android.alaune.datamanagement;

import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.bohush.android.alaune.data.Article;
import net.bohush.android.alaune.data.ArticleHeader;
import net.bohush.android.alaune.data.Category;
import net.bohush.android.alaune.utilities.HttpHelper;
import net.bohush.android.alaune.utilities.JsonParser;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private static final String LOG_TAG = "DataManager";
    private static final String URL = "http://figaro.service.yagasp.com/article/";
    private static final String URL_CATEGORIES = URL + "categories";
    private static final String URL_ARTICLE_HEADERS = URL + "header/";
    private static final String URL_ARTICLE = URL;

    private static DataManager sInstance;

    private List<Category> mCategories;
    private Map<Category, List<ArticleHeader>> mMapOfArticleHeaders;
    private Map<ArticleHeader, Article> mMapOfArticles;

    private DataManager(Context context) {
        mMapOfArticleHeaders = new HashMap<>();
        mMapOfArticles = new HashMap<>();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        ImageLoader.getInstance().init(config);
    }

    public static synchronized DataManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataManager(context.getApplicationContext());
        }
        return sInstance;
    }

    public List<Category> getCategories() {
        Log.i(LOG_TAG, "getCategories");
        if (mCategories == null) {
            Log.i(LOG_TAG, "new articleHeaders");
            try {
                String jsonCategories = HttpHelper.loadData(URL_CATEGORIES);
                if (jsonCategories == null) {
                    return null;
                } else {
                    mCategories = JsonParser.parseCategories(jsonCategories);
                    return mCategories;
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed to parse data", e);
                return null;
            }
        } else {
            Log.i(LOG_TAG, "mCategories is already downloaded");
            return mCategories;
        }
    }

    public List<ArticleHeader> getArticleHeaders(Category category) {
        Log.i(LOG_TAG, "getArticleHeaders");
        List<ArticleHeader> articleHeaders = mMapOfArticleHeaders.get(category);
        if (articleHeaders == null) {
            Log.i(LOG_TAG, "new articleHeaders");
            try {
                String articleHeadersUrl = URL_ARTICLE_HEADERS + category.getId();
                String jsonArticleHeaders = HttpHelper.loadData(articleHeadersUrl);
                if (jsonArticleHeaders == null) {
                    return null;
                } else {
                    articleHeaders = JsonParser.parseArticleHeaders(jsonArticleHeaders);
                    mMapOfArticleHeaders.put(category, articleHeaders);
                    return articleHeaders;
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed to parse data", e);
                return null;
            }
        } else {
            Log.i(LOG_TAG, "articleHeaders is already in map");
            return articleHeaders;
        }
    }

    public Article getArticle(ArticleHeader articleHeader) {
        Log.i(LOG_TAG, "getArticle");
        Article article = mMapOfArticles.get(articleHeader);
        if (article == null) {
            Log.i(LOG_TAG, "new article");
            try {
                String articleUrl = URL_ARTICLE + articleHeader.getId();
                String jsonArticle = HttpHelper.loadData(articleUrl);
                if (jsonArticle == null) {
                    return null;
                } else {
                    article = JsonParser.parseArticle(jsonArticle);
                    mMapOfArticles.put(articleHeader, article);
                }
                return article;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Failed to parse data", e);
                return null;
            }
        } else {
            Log.i(LOG_TAG, "Article is already in map");
            return article;
        }
    }

}
