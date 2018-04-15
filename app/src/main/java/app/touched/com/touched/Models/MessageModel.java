package app.touched.com.touched.Models;

/**
 * Created by Anshul on 4/2/2018.
 */

public class MessageModel {
    private String localUri, msg_id, name, senderEmail, timestamp, isRead, isDelivered, deliveredTime, readTime, isSent, SentTime, isMetaData, metaType, msgContent;
    private boolean isMine;

    public MessageModel() {

    }

    public MessageModel(String name, String senderEmail, String timestamp, String isMetaData, String metaType, String msgContent) {
        this.name = name;
        this.senderEmail = senderEmail;
        this.timestamp = timestamp;
        this.isMetaData = isMetaData;
        this.metaType = metaType;
        this.msgContent = msgContent;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getLocalUri() {
        return localUri;
    }

    public void setLocalUri(String localUri) {
        this.localUri = localUri;
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

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
