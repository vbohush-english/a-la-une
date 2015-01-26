package net.bohush.android.alaune.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.UnderlinePageIndicator;

import net.bohush.android.alaune.R;
import net.bohush.android.alaune.data.ArticleHeader;
import net.bohush.android.alaune.data.Category;
import net.bohush.android.alaune.datamanagement.DataManager;
import net.bohush.android.alaune.fragments.ArticleFragment;

import java.util.List;


public class ArticleActivity extends FragmentActivity {

    public static final String EXTRA_CATEGORY_POSITION = "categoryIndex";
    public static final String EXTRA_ARTICLE_POSITION = "articlePosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        int categoryPosition = getIntent().getIntExtra(EXTRA_CATEGORY_POSITION, 0);
        int articlePosition = getIntent().getIntExtra(EXTRA_ARTICLE_POSITION, 0);

        Category category = DataManager.getInstance(this).getCategories().get(categoryPosition);
        setTitle(category.getName());

        setViewPager(categoryPosition, articlePosition);
    }

    private void setViewPager(int categoryPosition, int articlePosition) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_activity_article);
        PagerAdapter pagerAdapter =
                new ArticlePagerAdapter(getSupportFragmentManager(), categoryPosition);
        viewPager.setAdapter(pagerAdapter);
        UnderlinePageIndicator underlinePageIndicator = (UnderlinePageIndicator)
                findViewById(R.id.indicator_activity_article);
        underlinePageIndicator.setFades(false);
        underlinePageIndicator.setViewPager(viewPager, articlePosition);
    }

    private class ArticlePagerAdapter extends FragmentStatePagerAdapter {

        private int mCategoryPosition;
        private List<ArticleHeader> mArticleHeaders;

        public ArticlePagerAdapter(FragmentManager fm, int categoryPosition) {
            super(fm);
            DataManager dm = DataManager.getInstance(ArticleActivity.this);
            mArticleHeaders = dm.getArticleHeaders(dm.getCategories().get(categoryPosition));
            mCategoryPosition = categoryPosition;
        }

        @Override
        public Fragment getItem(int position) {
            return ArticleFragment.newInstance(mCategoryPosition, position);
        }

        @Override
        public int getCount() {
            return mArticleHeaders.size();
        }
    }

}