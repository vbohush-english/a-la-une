package net.bohush.android.alaune.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.viewpagerindicator.UnderlinePageIndicator;

import net.bohush.android.alaune.R;
import net.bohush.android.alaune.data.Category;
import net.bohush.android.alaune.datamanagement.DataManager;
import net.bohush.android.alaune.fragments.CategoryFragment;

import java.util.List;


public class CategoryActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        new LoadCategoriesAsyncTask().execute();
    }

    private void setViewPager(List<Category> categories) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_activity_category);
        PagerAdapter pagerAdapter =
                new CategoryPagerAdapter(getSupportFragmentManager(), categories);
        viewPager.setAdapter(pagerAdapter);
        UnderlinePageIndicator underlinePageIndicator =
                (UnderlinePageIndicator) findViewById(R.id.indicator_activity_category);
        underlinePageIndicator.setFades(false);
        underlinePageIndicator.setViewPager(viewPager);
    }

    private class LoadCategoriesAsyncTask extends AsyncTask<Void, Void, List<Category>> {
        @Override
        protected List<Category> doInBackground(Void... params) {
            return DataManager.getInstance(CategoryActivity.this).getCategories();
        }

        @Override
        protected void onPostExecute(List<Category> result) {
            super.onPostExecute(result);
            if (result != null) {
                setViewPager(result);
            } else {
                Toast.makeText(CategoryActivity.this,
                        getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private class CategoryPagerAdapter extends FragmentStatePagerAdapter {

        private List<Category> mCategories;

        public CategoryPagerAdapter(FragmentManager fm, List<Category> categories) {
            super(fm);
            mCategories = categories;
        }

        @Override
        public Fragment getItem(int position) {
            return CategoryFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mCategories.size();
        }
    }

}