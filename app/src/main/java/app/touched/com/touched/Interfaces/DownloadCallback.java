package app.touched.com.touched.Interfaces;

import app.touched.com.touched.Models.MessageModel;

/**
 * Created by Rishabh.Nagpal on 5/6/2018.
 */

public interface DownloadCallback {
    void downloadStartSetValue(int position, long downloadId, MessageModel msg);
}
