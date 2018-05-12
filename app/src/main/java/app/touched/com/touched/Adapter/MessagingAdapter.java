package app.touched.com.touched.Adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Locale;

import app.touched.com.touched.Interfaces.DownloadCallback;
import app.touched.com.touched.Models.MessageModel;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.CompressFile;
import app.touched.com.touched.Utilities.DialogBox;
import app.touched.com.touched.Models.MessageStatus;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.view.View.GONE;
import static app.touched.com.touched.Utilities.Constants.MY_IMAGE_MSG;
import static app.touched.com.touched.Utilities.Constants.MY_MESSAGE;
import static app.touched.com.touched.Utilities.Constants.OTHER_IMAGE_MSG;
import static app.touched.com.touched.Utilities.Constants.OTHER_MESSAGE;
import static app.touched.com.touched.Utilities.TimeUtils.getMsgTime;
import static app.touched.com.touched.Utilities.Utility.showToastForContentNotAvailable;

/**
 * Created by Anshul on 4/2/2018.
 */

public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.ListViewHolder> {
    private Activity context;
    private ArrayList<MessageModel> chatList;
    private DownloadManager downloadManager;
    public DownloadCallback downloadCallback;
    private String myProfilePic, friendProfilePic;

    public MessagingAdapter(Activity context, ArrayList<MessageModel> list, String myProfilePic, String friendProfilePic, DownloadCallback listener) {
        this.context = context;
        this.chatList = list;
        downloadCallback = listener;
        downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        this.myProfilePic = myProfilePic;
        this.friendProfilePic = friendProfilePic;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case MY_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mechat_item, parent, false);
                break;
            case OTHER_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youchat_item, parent, false);
                break;
            case MY_IMAGE_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_images, parent, false);
                break;
            case OTHER_IMAGE_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_opponent_image, parent, false);
                break;
        }
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case MY_MESSAGE:
                holder.profileImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(myProfilePic).networkPolicy(NetworkPolicy.OFFLINE).into(holder.profileImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(myProfilePic).into(holder.profileImage);
                    }
                });
                if (position<chatList.size()-1) {
                    if (chatList.get(position + 1).isMine()) {
                        holder.profileImage.setVisibility(View.INVISIBLE);
                    } else {
                        holder.profileImage.setVisibility(View.VISIBLE);

                    }
                }


                PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
                // String ago = prettyTime.format(new Date(chatList.get(position).getTimestamp()));
                // String myDate = getMsgTime(chatList.get(position).getSentTime());
                holder.date.setText(getMsgTime(chatList.get(position).getTimestamp()));
                holder.smsTxt.setText(chatList.get(position).getMsgContent());
                holder.username.setText(chatList.get(position).getName());
                if (chatList.get(position).getIsSent() != null && chatList.get(position).getIsSent().equals("true")) {
                    holder.msgStatus.setImageResource(R.drawable.message_sent);
                }
                if (chatList.get(position).getIsDelivered() != null && chatList.get(position).getIsDelivered().equals("true")) {
                    holder.msgStatus.setImageResource(R.drawable.message_delivered);
                }
                break;
            case OTHER_MESSAGE:
//                PrettyTime prettyTimeO = new PrettyTime(Locale.getDefault());
//                String ago0 = prettyTimeO.format(new Date(getMsgTime(chatList.get(position).getTimestamp())));
                holder.profileImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(friendProfilePic).networkPolicy(NetworkPolicy.OFFLINE).into(holder.profileImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(friendProfilePic).into(holder.profileImage);
                    }
                });
                if (position<chatList.size()-1) {
                    if (!chatList.get(position + 1).isMine()) {
                        holder.profileImage.setVisibility(View.INVISIBLE);
                    } else {
                        holder.profileImage.setVisibility(View.VISIBLE);

                    }
                }
                holder.date.setText(getMsgTime(chatList.get(position).getTimestamp()));
                holder.username.setText(chatList.get(position).getName());
                holder.smsTxt.setText(chatList.get(position).getMsgContent());

                break;
            case MY_IMAGE_MSG:
                holder.profileImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(myProfilePic).networkPolicy(NetworkPolicy.OFFLINE).into(holder.profileImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(myProfilePic).into(holder.profileImage);
                    }
                });
                if (position<chatList.size()-1) {
                    if (chatList.get(position + 1).isMine()) {
                        holder.profileImage.setVisibility(View.INVISIBLE);
                    } else {
                        holder.profileImage.setVisibility(View.VISIBLE);

                    }
                }
                holder.date.setText(chatList.get(position).getTimestamp());
                holder.username.setText(chatList.get(position).getName());
                if (chatList.get(position).getLocalUri() != null && !chatList.get(position).getLocalUri().isEmpty())
                    holder.image.setImageURI(Uri.parse(chatList.get(position).getLocalUri()));
                if (chatList.get(position).getMsgContent() == null || (chatList.get(position).getMsgContent().isEmpty())) {
                    holder.progressBar.setVisibility(View.VISIBLE);

                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(holder.progressBar, "progress", 0, 5);
                    objectAnimator.setDuration(5000);
                    objectAnimator.setInterpolator(new DecelerateInterpolator());
                    objectAnimator.start();
                } else {
                    holder.progressBar.clearAnimation();
                    holder.progressBar.setVisibility(GONE);

                }
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            if (!chatList.get(holder.getAdapterPosition()).getLocalUri().isEmpty()) {
                                DialogBox showImage = new DialogBox(context);
                                showImage.ShowImage(chatList.get(holder.getAdapterPosition()).getLocalUri(), chatList.get(holder.getAdapterPosition()).getMsg_id());
                            } else {
                                showToastForContentNotAvailable(context);
                            }
                        } catch (Exception e) {
                            showToastForContentNotAvailable(context);
                        }
                    }
                });
                break;
            case OTHER_IMAGE_MSG:
                holder.profileImage.setVisibility(View.VISIBLE);
                Picasso.with(context).load(friendProfilePic).networkPolicy(NetworkPolicy.OFFLINE).into(holder.profileImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(friendProfilePic).into(holder.profileImage);
                    }
                });
                if (position<chatList.size()-1) {
                    if (!chatList.get(position + 1).isMine()) {
                        holder.profileImage.setVisibility(View.INVISIBLE);
                    } else {
                        holder.profileImage.setVisibility(View.VISIBLE);

                    }
                }
                holder.date.setText(chatList.get(position).getSentTime());
                holder.username.setText(chatList.get(position).getName());
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.downloadImage.setVisibility(GONE);
                if (chatList.get(position).getDownloadStatus() == null) {
                    Picasso.with(context).load(chatList.get(position).getThumbImage()).placeholder(R.drawable.photo_placeholder).networkPolicy(NetworkPolicy.OFFLINE).into(holder.image, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.downloadImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
                            Picasso.with(context).load(chatList.get(position).getThumbImage()).placeholder(R.drawable.photo_placeholder).into(holder.image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    holder.progressBar.setVisibility(View.GONE);
                                    holder.downloadImage.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError() {
                                    holder.progressBar.setVisibility(View.GONE);
                                    holder.downloadImage.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                } else if (chatList.get(position).getDownloadStatus().equals(MessageStatus.PROGRESS.toString())) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.downloadImage.setVisibility(View.GONE);
                } else if (chatList.get(position).getDownloadStatus().equals(MessageStatus.DOWNLOADED.toString())) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.downloadImage.setVisibility(View.GONE);
                    if (CompressFile.checkForImageStatus(context, chatList.get(position).getMeta_no()))
                        holder.image.setImageURI(Uri.parse(CompressFile.myStoragePath(context) + "/" + chatList.get(position).getMeta_no()));
                    else {// else means user deleted the image
                        holder.image.setImageResource(R.drawable.photo_placeholder);

                        chatList.get(position).setDownloadStatus(MessageStatus.REMOVED.toString());
                    }


                } else {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.downloadImage.setVisibility(View.GONE);
                }
                holder.downloadImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        holder.progressBar.setVisibility(View.VISIBLE);
                        Uri downloadReference = Uri.parse(chatList.get(position).getMsgContent());
                        DownloadManager.Request request = new DownloadManager.Request(downloadReference);

                        //Restrict the types of networks over which this download may proceed.
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                        //Set whether this download may proceed over a roaming connection.
                        request.setAllowedOverRoaming(true);
                        //Set the title of this download, to be displayed in notifications (if enabled).
                        request.setTitle(context.getString(R.string.app_name));
                        //Set a description of this download, to be displayed in notifications (if enabled)
//                        request.setDescription("Android Data download using DownloadManager.");
                        //Set the local destination for the downloaded file to a path within the application's external files directory

                        request.setDestinationInExternalPublicDir(context.getString(R.string.app_name), chatList.get(position).getMeta_no());


                        //Enqueue a new download and same the referenceId
                        long downloadId = downloadManager.enqueue(request);
                        downloadCallback.downloadStartSetValue(position, downloadId, chatList.get(position));


                    }
                });
//                if(oneTimearraylist.contains(chatList.get(position).getSms())){
//                    holder.progressBar.setVisibility(View.VISIBLE);
//                    holder.downloadImage.setVisibility(GONE);
//                }else {
//                    if (chatList.get(position).getLocalUri() == null || chatList.get(position).getLocalUri().equals("")) {
//                        if (isAutoDownload != null && isAutoDownload.equals("true")) {
//                            holder.progressBar.setVisibility(View.VISIBLE);
//                            holder.downloadImage.setVisibility(GONE);
//                            Intent objIntent = new Intent(context, DownloadFileService.class);
//                            objIntent.putExtra("fileURL", chatModels.get(position).getSms());
//                            objIntent.putExtra("fileName", chatModels.get(position).getSms().substring(chatModels.get(position).getSms().lastIndexOf("/") + 1));
//                            objIntent.putExtra("type", chatModels.get(position).getSelecteModel().toString());
//                            objIntent.putExtra("download", "original");
//                            context.startService(objIntent);
//                        } else {
//                            holder.downloadImage.setVisibility(View.VISIBLE);
//                            holder.progressBar.setVisibility(GONE);
//                            // holder.playImage.setVisibility(View.GONE);
//                            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.defaultimage));
//                        }
//                    } else {
//                        holder.downloadImage.setVisibility(GONE);
//
//                        holder.image.setImageURI(Uri.parse(chatModels.get(position).getLocalUrl()));
//                        holder.progressBar.setVisibility(GONE);
//                        //holder.playImage.setVisibility(View.VISIBLE);
//                    }
//                }
//                holder.downloadImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                            builder.setPositiveButton("Download original", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    oneTimearraylist.add(chatModels.get(position).getSms());
//                                    holder.progressBar.setVisibility(View.VISIBLE);
//                                    holder.downloadImage.setVisibility(GONE);
//
//                                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(holder.progressBar, "progress", 0, 5);
//                                    objectAnimator.setDuration(5000);
//                                    objectAnimator.setInterpolator(new DecelerateInterpolator());
//                                    objectAnimator.start();
//                                    Intent objIntent = new Intent(context, DownloadFileService.class);
//                                    objIntent.putExtra("fileURL", chatModels.get(position).getSms());
//                                    objIntent.putExtra("fileName", chatModels.get(position).getSms().substring(chatModels.get(position).getSms().lastIndexOf("/") + 1));
//                                    objIntent.putExtra("type", chatModels.get(position).getSelecteModel().toString());
//                                    objIntent.putExtra("download", "original");
//                                    context.startService(objIntent);
//                                }
//                            });
//                            builder.setNegativeButton("Download compressed", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    oneTimearraylist.add(chatModels.get(position).getSms());
//                                    holder.progressBar.setVisibility(View.VISIBLE);
//                                    holder.downloadImage.setVisibility(GONE);
//                                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(holder.progressBar, "progress", 0, 5);
//                                    objectAnimator.setDuration(5000);
//                                    objectAnimator.setInterpolator(new DecelerateInterpolator());
//                                    objectAnimator.start();
//                                    Intent objIntent = new Intent(context, DownloadFileService.class);
//                                    objIntent.putExtra("fileURL", chatModels.get(position).getSms());
//                                    objIntent.putExtra("fileName", chatModels.get(position).getSms().substring(chatModels.get(position).getSms().lastIndexOf("/") + 1));
//                                    objIntent.putExtra("type", chatModels.get(position).getSelecteModel().toString());
//                                    objIntent.putExtra("download", "compressed");
//                                    context.startService(objIntent);
//                                }
//                            });
//                            builder.show();
//                        } catch (Exception e) {
//                            holder.progressBar.setVisibility(View.GONE);
//                            holder.downloadImage.setVisibility(View.VISIBLE);
//                            showToastForContentNotAvailable();
//                        }
//
//                    }
//                });
//
//
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (!chatList.get(holder.getAdapterPosition()).getDownloadStatus().equals(MessageStatus.REMOVED.toString())) {
                                if (chatList.get(holder.getAdapterPosition()).getLocalUri() != null || !chatList.get(holder.getAdapterPosition()).getLocalUri().equals("")) {
                                    DialogBox showImage = new DialogBox(context);
                                    showImage.ShowImage(chatList.get(holder.getAdapterPosition()).getLocalUri(), chatList.get(holder.getAdapterPosition()).getMsg_id());
                                } else {
                                    // showToastForContentNotAvailable();
                                }
                            } else {
                                Toast.makeText(context, "no file found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            //showToastForContentNotAvailable();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel item = chatList.get(position);
        switch (item.getMetaType()) {
            case "TEXT":
                if (item.isMine()) return MY_MESSAGE;
                else return OTHER_MESSAGE;

            case "IMAGE":
                if (item.isMine()) return MY_IMAGE_MSG;
                else return OTHER_IMAGE_MSG;
        }
        return 0;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView smsTxt;
        TextView date, username, number, contactName;
        ImageView image, downloadImage, playImage, pauseImage, msgStatus;
        CircleImageView profileImage;
        LinearLayout layout;
        ProgressBar progressBar;
        SeekBar seekBar;
        TextView invite, addContact;

        public ListViewHolder(View itemView) {
            super(itemView);

            smsTxt = (TextView) itemView.findViewById(R.id.msgtxt_id);
            msgStatus = (ImageView) itemView.findViewById(R.id.user_reply_status);
            profileImage = (CircleImageView) itemView.findViewById(R.id.imv_ProfileImage);
//            number = (TextView) itemView.findViewById(R.id.number);
//            invite = (TextView) itemView.findViewById(R.id.invite);
//            addContact = (TextView) itemView.findViewById(R.id.addContact);
            date = (TextView) itemView.findViewById(R.id.date_id);
            image = (ImageView) itemView.findViewById(R.id.image_full_view);
//            contactName = (TextView) itemView.findViewById(R.id.contact_name);//error ni dikh ri
            layout = (LinearLayout) itemView.findViewById(R.id.outgoing_layout_bubble);
            downloadImage = (ImageView) itemView.findViewById(R.id.downloadImage);
//            playImage = (ImageView) itemView.findViewById(R.id.playImage);
            username = (TextView) itemView.findViewById(R.id.user_name);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_show_image);
//            seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
//            pauseImage = (ImageView) itemView.findViewById(R.id.pauseImage);
            // time=(TextView)itemView.findViewById(R.id.name);

            itemView.setOnLongClickListener(this);
            if (image != null)
                image.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
