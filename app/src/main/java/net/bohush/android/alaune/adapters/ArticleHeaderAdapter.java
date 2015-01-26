package net.bohush.android.alaune.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.bohush.android.alaune.R;
import net.bohush.android.alaune.data.ArticleHeader;

import java.util.List;

public class ArticleHeaderAdapter extends ArrayAdapter<ArticleHeader> {

    private static final int TYPE_FIRST = 0;
    private static final int TYPE_OTHER = 1;
    private static final int TYPE_MAX_COUNT = TYPE_OTHER + 1;
    private static final int THUMB_DEFAULT_SIZE = 300;

    private Context mContext;
    private List<ArticleHeader> mArticleHeaders;

    public ArticleHeaderAdapter(Context context, List<ArticleHeader> articleHeaders) {
        super(context, 0, articleHeaders);
        mContext = context;
        mArticleHeaders = articleHeaders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            int layout;
            if (getItemViewType(position) == TYPE_FIRST) {
                layout = R.layout.item_article_header_first;
            } else {
                layout = R.layout.item_article_header;
            }
            rowView = LayoutInflater.from(mContext).inflate(layout, parent, false);
            holder = new ViewHolder();
            holder.mTitleTextView = (TextView)
                    rowView.findViewById(R.id.title_item_article_header);
            holder.mSubtitleTextView = (TextView)
                    rowView.findViewById(R.id.subtitle_item_article_header);
            holder.mThumbImageView = (ImageView)
                    rowView.findViewById(R.id.thumb_item_article_header);
            holder.mThumbContainerFrameLayout = (FrameLayout)
                    rowView.findViewById(R.id.layout_thumb_item_article_header);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        ArticleHeader articleHeader = mArticleHeaders.get(position);
        holder.mTitleTextView.setText(articleHeader.getTitle());
        holder.mSubtitleTextView.setText(articleHeader.getSubtitle());
        final FrameLayout frameLayout = holder.mThumbContainerFrameLayout;
        if ((articleHeader.getThumb() == null) || (articleHeader.getThumb().equals(""))) {
            frameLayout.setVisibility(View.GONE);
        } else {
            frameLayout.setVisibility(View.INVISIBLE);
            String thumbUrl = String.format(articleHeader.getThumb(),
                    THUMB_DEFAULT_SIZE, THUMB_DEFAULT_SIZE);
            ImageLoader.getInstance().displayImage(thumbUrl, holder.mThumbImageView,
                    new ImageLoadingListener() {

                        @Override
                        public void onLoadingStarted(String arg0, View arg1) {
                        }

                        @Override
                        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                            frameLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                            frameLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingCancelled(String arg0, View arg1) {
                            frameLayout.setVisibility(View.GONE);
                        }
                    });
        }
        return rowView;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 ? TYPE_FIRST : TYPE_OTHER);
    }

    private static class ViewHolder {
        public TextView mTitleTextView;
        public TextView mSubtitleTextView;
        public ImageView mThumbImageView;
        public FrameLayout mThumbContainerFrameLayout;
    }
}
