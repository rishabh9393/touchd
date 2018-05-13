package app.touched.com.touched.Models;

/**
 * Created by Anshul on 4/2/2018.
 */

public class MessageModel {
    public String getMeta_no() {
        return meta_no;
    }

    public void setMeta_no(String meta_no) {
        this.meta_no = meta_no;
    }

    private String downloadStatus;
    private String downloadUid;
    private String meta_no, localUri, msg_id, name, senderEmail, timestamp, isRead, isDelivered, deliveredTime, readTime, isSent, SentTime, isMetaData, metaType, msgContent;
    private boolean isMine;
    private String thumbImage;

    public MessageModel() {

    }


    public MessageModel(String name, String senderEmail, String timestamp, String isMetaData, String metaType, String msgContent, String meta_no, boolean isMine) {
        this.name = name;
        this.senderEmail = senderEmail;
        this.timestamp = timestamp;
        this.isMetaData = isMetaData;
        this.metaType = metaType;
        this.msgContent = msgContent;
        this.meta_no = meta_no;
        this.isMine = isMine;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public String getDownloadUid() {
        return downloadUid;
    }

    public void setDownloadUid(String downloadUid) {
        this.downloadUid = downloadUid;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
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

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj == null || obj.getClass() != getClass()) {
            result = false;
        } else if (obj instanceof MessageModel) {
            MessageModel employee = (MessageModel) obj;
            if (this.msg_id.equals(employee.getMsg_id())) {
                result = true;
            }
        } else {
            return super.equals(obj);
        }
        return result;
    }
}
