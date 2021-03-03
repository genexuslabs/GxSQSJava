package sqs;

public class GxMessageContent {

    private String mId;
    private String mContents;

    public void setContents(String contents) {
        mContents = contents;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getId() {
        return mId;
    }

    public String getContents() {
        return mContents;
    }
    
}
