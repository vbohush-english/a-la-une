package net.bohush.android.alaune.data;

public class Article {

    private String mId;
    private String mAuthor;
    private String mCategoryId;
    private String mContent;
    private long mDate;

    public Article(String id, String author, String categoryId, String content, long date) {
        mId = id;
        mAuthor = author;
        mCategoryId = categoryId;
        mContent = content;
        mDate = date;
    }

    public String getId() {
        return mId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public String getContent() {
        return mContent;
    }

    public long getDate() {
        return mDate;
    }

}
