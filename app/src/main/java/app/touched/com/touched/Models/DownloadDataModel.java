package app.touched.com.touched.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rishabh.Nagpal on 5/6/2018.
 */

public class DownloadDataModel implements Parcelable {

    private String friendKey;
    private String myKey;
    private String msgId;

    public DownloadDataModel(String friendKey, String myKey, String msgId) {
        this.friendKey = friendKey;
        this.myKey = myKey;
        this.msgId = msgId;
    }

    public String getMyKey() {
        return myKey;
    }

    public void setMyKey(String myKey) {
        this.myKey = myKey;
    }

    public String getFriendKey() {
        return friendKey;
    }

    public void setFriendKey(String friendKey) {
        this.friendKey = friendKey;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.friendKey);
        dest.writeString(this.myKey);
        dest.writeString(this.msgId);
    }

    protected DownloadDataModel(Parcel in) {
        this.friendKey = in.readString();
        this.myKey = in.readString();
        this.msgId = in.readString();
    }

    public static final Parcelable.Creator<DownloadDataModel> CREATOR = new Parcelable.Creator<DownloadDataModel>() {
        @Override
        public DownloadDataModel createFromParcel(Parcel source) {
            return new DownloadDataModel(source);
        }

        @Override
        public DownloadDataModel[] newArray(int size) {
            return new DownloadDataModel[size];
        }
    };
}

