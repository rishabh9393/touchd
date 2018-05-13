package app.touched.com.touched.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rishabh.Nagpal on 4/30/2018.
 */

public class MessageSentModel implements Parcelable {
    private String email;
    private String first_name;
    private String last_name;
    private String key;
    private String id;
    private User_Details.Picture picture;
    private String msg_count;
    private String senderEmail, timestamp, isRead, isDelivered, deliveredTime, readTime, isSent, SentTime, isMetaData, metaType, msg_content;
    private String isMessagesUnread;
    private String isRejected;
    private String timeToExpire;
    private String giftType;

    public String getGiftType() {
        return giftType;
    }

    public void setGiftType(String giftType) {
        this.giftType = giftType;
    }

    public String getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(String isRejected) {
        this.isRejected = isRejected;
    }

    public String getTimeToExpire() {
        return timeToExpire;
    }

    public void setTimeToExpire(String timeToExpire) {
        this.timeToExpire = timeToExpire;
    }

    public String getIsMessagesUnread() {
        return isMessagesUnread;
    }

    public void setIsMessagesUnread(String isMessagesUnread) {
        this.isMessagesUnread = isMessagesUnread;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(String isDelivered) {
        this.isDelivered = isDelivered;
    }

    public String getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(String deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getIsSent() {
        return isSent;
    }

    public void setIsSent(String isSent) {
        this.isSent = isSent;
    }

    public String getSentTime() {
        return SentTime;
    }

    public void setSentTime(String sentTime) {
        SentTime = sentTime;
    }

    public String getIsMetaData() {
        return isMetaData;
    }

    public void setIsMetaData(String isMetaData) {
        this.isMetaData = isMetaData;
    }

    public String getMetaType() {
        return metaType;
    }

    public void setMetaType(String metaType) {
        this.metaType = metaType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User_Details.Picture getPicture() {
        return picture;
    }

    public void setPicture(User_Details.Picture picture) {
        this.picture = picture;
    }

    public String getMsg_count() {
        return msg_count;
    }

    public void setMsg_count(String msg_count) {
        this.msg_count = msg_count;
    }

    public MessageSentModel() {
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj == null || obj.getClass() != getClass()) {
            result = false;
        } else if (obj instanceof MessageSentModel) {
            MessageSentModel employee = (MessageSentModel) obj;
            if (this.key.equals(employee.getKey())) {
                result = true;
            }
        } else {
            return super.equals(obj);
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.key);
        dest.writeString(this.id);
        dest.writeParcelable(this.picture, flags);
        dest.writeString(this.msg_count);
        dest.writeString(this.senderEmail);
        dest.writeString(this.timestamp);
        dest.writeString(this.isRead);
        dest.writeString(this.isDelivered);
        dest.writeString(this.deliveredTime);
        dest.writeString(this.readTime);
        dest.writeString(this.isSent);
        dest.writeString(this.SentTime);
        dest.writeString(this.isMetaData);
        dest.writeString(this.metaType);
        dest.writeString(this.msg_content);
        dest.writeString(this.isMessagesUnread);
    }

    protected MessageSentModel(Parcel in) {
        this.email = in.readString();
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.key = in.readString();
        this.id = in.readString();
        this.picture = in.readParcelable(User_Details.Picture.class.getClassLoader());
        this.msg_count = in.readString();
        this.senderEmail = in.readString();
        this.timestamp = in.readString();
        this.isRead = in.readString();
        this.isDelivered = in.readString();
        this.deliveredTime = in.readString();
        this.readTime = in.readString();
        this.isSent = in.readString();
        this.SentTime = in.readString();
        this.isMetaData = in.readString();
        this.metaType = in.readString();
        this.msg_content = in.readString();
        this.isMessagesUnread = in.readString();
    }

    public static final Creator<MessageSentModel> CREATOR = new Creator<MessageSentModel>() {
        @Override
        public MessageSentModel createFromParcel(Parcel source) {
            return new MessageSentModel(source);
        }

        @Override
        public MessageSentModel[] newArray(int size) {
            return new MessageSentModel[size];
        }
    };
}
