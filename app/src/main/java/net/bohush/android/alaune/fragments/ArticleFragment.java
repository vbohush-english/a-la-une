package net.bohush.android.alaune.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.bohush.android.alaune.R;
import net.bohush.android.alaune.data.Article;
import net.bohush.android.alaune.data.ArticleHeader;
import net.bohush.android.alaune.data.Category;
import net.bohush.android.alaune.datamanagement.DataManager;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArticleFragment extends Fragment {

    private static final String ARGS_CATEGORY_POSITION = "categoryPosition";
    private static final String ARGS_ARTICLE_POSITION = "articlePosition";
    private View mRootView;
    private Context mContext;

    public static ArticleFragment newInstance(int categoryPosition, int articlePosition) {
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CATEGORY_POSITION, categoryPosition);
        args.putInt(ARGS_ARTICLE_POSITION, articlePosition);
        articleFragment.setArguments(args);
        return articleFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article, container, false);
        mContext = getActivity();

        int categoryPosition = getArguments().getInt(ARGS_CATEGORY_POSITION);
        int articlePosition = getArguments().getInt(ARGS_ARTICLE_POSITION);
        ArticleHeader articleHeader = getArticleHeader(categoryPosition, articlePosition);
        setArticleHeaderViews(articleHeader);

        new LoadArticleAsyncTask().execute(articleHeader);
        return mRootView;
    }

    //get ArticleHeader from positions
    private ArticleHeader getArticleHeader(int categoryPosition, int articlePosition) {
        DataManager dm = DataManager.getInstance(mContext);
        Category category = dm.getCategories().get(categoryPosition);
        List<ArticleHeader> articleHeaders = dm.getArticleHeaders(category);
        return articleHeaders.get(articlePosition);
    }

    private void setArticleHeaderViews(ArticleHeader articleHeader) {
        //title
        TextView articleTitleTextView = (TextView)
                mRootView.findViewById(R.id.article_title_fragment_article);
        articleTitleTextView.setText(articleHeader.getTitle());

        //subtitle
        TextView articleSubtitleTextView = (TextView)
                mRootView.findViewById(R.id.article_subtitle_fragment_article);
        String subtitle = articleHeader.getSubtitle();
        if ((subtitle == null) || (subtitle.equals(""))) {
            articleSubtitleTextView.setVisibility(View.GONE);
        } else {
            articleSubtitleTextView.setText(articleHeader.getSubtitle());
        }

        //thumb
        final ImageView thumbImageView = (ImageView)
                mRootView.findViewById(R.id.thumb_fragment_article);
        if ((articleHeader.getThumb() == null) || (articleHeader.getThumb().equals(""))) {
            thumbImageView.setVisibility(View.GONE);
        } else {
            thumbImageView.setVisibility(View.INVISIBLE);
            //server return max available image
            int maxSize = 1000;
            String thumbUrl = String.format(articleHeader.getThumb(), maxSize, maxSize);
            ImageLoader.getInstance().displayImage(thumbUrl, thumbImageView,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String arg0, View arg1) {
                        }

                        @Override
                        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                            thumbImageView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                            thumbImageView.setVisibility(View.VISIBLE);
                            //change size of ImageView
                            double aspectRatio = ((double) arg2.getHeight()) / arg2.getWidth();
                            int width = thumbImageView.getWidth();
                            int height = (int) (width * aspectRatio);
                            thumbImageView.setMinimumHeight(height);
                            thumbImageView.setMaxHeight(height);
                            thumbImageView.getLayoutParams().height = height;
                            thumbImageView.requestLayout();
                        }

                        @Override
                        public void onLoadingCancelled(String arg0, View arg1) {
                            thumbImageView.setVisibility(View.GONE);
                        }
                    });
        }

    }

    private void setArticleViews(Article article) {
        //author
        TextView authorTextView = (TextView) mRootView.findViewById(R.id.author_fragment_article);
        authorTextView.setText(article.getAuthor());

        //date
        TextView dateTextView = (TextView) mRootView.findViewById(R.id.date_fragment_article);
        Date date = new Date(article.getDate() * 1000); //timestamp in seconds?
        DateFormat dateFormat = DateFormat
                .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.FRENCH);
        dateTextView.setText(", " + dateFormat.format(date));

        //content
        WebView contentWebView = (WebView) mRootView.findViewById(R.id.web_view_fragment_article);
        WebSettings webSettings = contentWebView.getSettings();
        webSettings.setTextZoom(80);
        contentWebView.loadData(article.getContent(), "text/html; charset=UTF-8", null);
    }

    class LoadArticleAsyncTask extends AsyncTask<ArticleHeader, Void, Article> {
        @Override
        protected Article doInBackground(ArticleHeader... params) {
            return DataManager.getInstance(mContext).getArticle(params[0]);
        }

        @Override
        protected void onPostExecute(Article result) {
            super.onPostExecute(result);
            if (result != null) {
                setArticleViews(result);
            }
        }
    }

}