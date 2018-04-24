package app.touched.com.touched.Interfaces;

import app.touched.com.touched.Models.MessageModel;

/**
 * Created by Anshul on 4/3/2018.
 */

public interface IMessaging {
     void sendTextMsg(MessageModel msg);
     void deleteMsg(MessageModel msg);
     void updateMsg(MessageModel msg);
     void msgDelivered(MessageModel msg);
     void msgRead(MessageModel msg);

     void sendGiftMsg(MessageModel msg);
     void uploadImage(MessageModel msg);
}
