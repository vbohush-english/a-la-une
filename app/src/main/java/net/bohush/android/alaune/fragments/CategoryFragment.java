package net.bohush.android.alaune.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.bohush.android.alaune.R;
import net.bohush.android.alaune.activities.ArticleActivity;
import net.bohush.android.alaune.adapters.ArticleHeaderAdapter;
import net.bohush.android.alaune.data.ArticleHeader;
import net.bohush.android.alaune.data.Category;
import net.bohush.android.alaune.datamanagement.DataManager;

import java.util.List;

public class CategoryFragment extends Fragment {

    private static final String ARGS_CATEGORY_INDEX = "categoryIndex";
    private ListView mListView;
    private Context mContext;

    public static CategoryFragment newInstance(int categoryIndex) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CATEGORY_INDEX, categoryIndex);
        categoryFragment.setArguments(args);
        return categoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_category, container, false);
        mContext = getActivity();

        final int categoryIndex = getArguments().getInt(ARGS_CATEGORY_INDEX, 0);
        final Category category = DataManager.getInstance(mContext)
                .getCategories().get(categoryIndex);

        TextView categoryNameTextView = (TextView)
                rootView.findViewById(R.id.name_fragment_category);
        categoryNameTextView.setText(category.getName().toUpperCase());

        mListView = (ListView) rootView.findViewById(R.id.articles_fragment_category);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra(ArticleActivity.EXTRA_CATEGORY_POSITION, categoryIndex);
                intent.putExtra(ArticleActivity.EXTRA_ARTICLE_POSITION, position);
                startActivity(intent);
            }
        });

        new LoadArticleHeadersAsyncTask().execute(category);

        return rootView;
    }

    private class LoadArticleHeadersAsyncTask
            extends AsyncTask<Category, Void, List<ArticleHeader>> {
        @Override
        protected List<ArticleHeader> doInBackground(Category... params) {
            return DataManager.getInstance(mContext).getArticleHeaders(params[0]);
        }

        @Override
        protected void onPostExecute(List<ArticleHeader> result) {
            super.onPostExecute(result);
            if (result != null) {
                mListView.setAdapter(new ArticleHeaderAdapter(mContext, result));
            }
        }
    }

}