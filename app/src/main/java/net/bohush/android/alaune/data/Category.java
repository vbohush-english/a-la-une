package net.bohush.android.alaune.data;

public class Category {
    private String mId;
    private String mName;

    public Category(String id, String name) {
        mName = name;
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        Category category = (Category) that;
        return mId.equals(category.mId);
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }

}
