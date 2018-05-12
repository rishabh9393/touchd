package app.touched.com.touched.Services;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import app.touched.com.touched.Models.DownloadDataModel;
import app.touched.com.touched.Models.MessageStatus;
import app.touched.com.touched.Utilities.PersistanceManager;

import static app.touched.com.touched.Utilities.Constants.MSG_NODE;

/**
 * Created by Rishabh.Nagpal on 5/4/2018.
 */

public class DownloadFilesReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String downloadDataModelString = PersistanceManager.getInstance(context).getPersistanceJsonObjectValue(String.valueOf(referenceId));
            if (downloadDataModelString != null) {
                DownloadDataModel dataModel = new Gson().fromJson(downloadDataModelString, DownloadDataModel.class);
                DatabaseReference dbToMyNode = FirebaseDatabase.getInstance().getReference().child(MSG_NODE).child(dataModel.getMyKey()).child(dataModel.getFriendKey()).child(dataModel.getMsgId());
                Map<String, Object> childUpdate = new HashMap<>();
                childUpdate.put("downloadStatus", MessageStatus.DOWNLOADED.toString());
                dbToMyNode.updateChildren(childUpdate);
//                Intent intent1 = new Intent("app.touched.com.touched.Activites.UpdateToUI");
//                intent.putExtra("downloadedDetails", dataModel);
//                context.sendBroadcast(intent);

            }
        }
    }
}
