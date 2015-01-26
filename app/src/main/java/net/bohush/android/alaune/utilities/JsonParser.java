package net.bohush.android.alaune.utilities;

import android.util.Log;

import net.bohush.android.alaune.data.Article;
import net.bohush.android.alaune.data.ArticleHeader;
import net.bohush.android.alaune.data.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    private static final String LOG_TAG = "JsonParser";

    public static List<Category> parseCategories(String jsonCategories) throws JSONException {
        Log.i(LOG_TAG, "Starting parsing list of categories");
        long startingTime = System.currentTimeMillis();

        List<Category> result = new ArrayList<>();
        JSONArray categoriesJson = new JSONArray(jsonCategories);
        for (int i = 0; i < categoriesJson.length(); i++) {
            JSONObject categoryJson = categoriesJson.getJSONObject(i);
            JSONArray subcategoriesJson = categoryJson.getJSONArray("subcategories");
            for (int j = 0; j < subcategoriesJson.length(); j++) {
                JSONObject subcategoryJson = subcategoriesJson.getJSONObject(j);
                String subcategoryId = subcategoryJson.getString("id");
                String subcategoryName = subcategoryJson.getString("name");
                result.add(new Category(subcategoryId, subcategoryName));
            }
        }

        String seconds = String.format("%.3f", (System.currentTimeMillis() - startingTime) * 0.001);
        Log.i(LOG_TAG, "Finished parsing list of categories in " + seconds + " seconds");

        return result;
    }

    public static List<ArticleHeader> parseArticleHeaders(String jsonArticleHeaders)
            throws JSONException {
        Log.i(LOG_TAG, "Starting parsing list of articleHeaders");
        long startingTime = System.currentTimeMillis();

        List<ArticleHeader> result = new ArrayList<>();
        JSONArray articleHeadersJson = (new JSONArray(jsonArticleHeaders)).getJSONArray(1);
        for (int i = 0; i < articleHeadersJson.length(); i++) {
            JSONObject articleHeaderJson = articleHeadersJson.getJSONObject(i);
            String id = articleHeaderJson.getString("id");
            long update = articleHeaderJson.getLong("update");
            long date = articleHeaderJson.getLong("date");
            int ranking = articleHeaderJson.getInt("ranking");
            String title = articleHeaderJson.getString("title");
            String subtitle = articleHeaderJson.getString("subtitle");
            String thumb = articleHeaderJson.getJSONObject("thumb").getString("link");
            ArticleHeader articleHeader = new ArticleHeader(
                    id,
                    update,
                    date,
                    ranking,
                    title,
                    subtitle,
                    thumb);
            result.add(articleHeader);
        }
        String seconds = String.format("%.3f", (System.currentTimeMillis() - startingTime) * 0.001);
        Log.i(LOG_TAG, "Finished parsing list of articleHeaders in " + seconds + " seconds");

        return result;
    }


    public static Article parseArticle(String jsonArticle) throws JSONException {
        Log.i(LOG_TAG, "Starting parsing list of articleHeaders");
        long startingTime = System.currentTimeMillis();

        JSONObject articleJson = new JSONObject(jsonArticle);
        String id = articleJson.getString("_id");
        String author = articleJson.getString("author");
        String categoryId = articleJson.getString("categoryId");
        String content = articleJson.getString("content");
        long date = articleJson.getLong("date");
        Article article = new Article(
                id,
                author,
                categoryId,
                content,
                date);

        String seconds = String.format("%.3f", (System.currentTimeMillis() - startingTime) * 0.001);
        Log.i(LOG_TAG, "Finished parsing list of articleHeaders in " + seconds + " seconds");

        return article;
    }
}
