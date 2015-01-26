package net.bohush.android.alaune.data;

public class ArticleHeader {

    private String mId;
    private long mUpdate;
    private long mDate;
    private int mRanking;
    private String mTitle;
    private String mSubtitle;
    private String mThumb;

    public ArticleHeader(String id, long update, long date,
                         int ranking, String title, String subtitle, String thumb) {
        mId = id;
        mUpdate = update;
        mDate = date;
        mRanking = ranking;
        mTitle = title;
        mSubtitle = subtitle;
        mThumb = thumb;
    }

    public String getId() {
        return mId;
    }

    public long getUpdate() {
        return mUpdate;
    }

    public long getDate() {
        return mDate;
    }

    public int getRanking() {
        return mRanking;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public String getThumb() {
        return mThumb;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        ArticleHeader articleHeader = (ArticleHeader) that;
        return mId.equals(articleHeader.mId);
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }
}
