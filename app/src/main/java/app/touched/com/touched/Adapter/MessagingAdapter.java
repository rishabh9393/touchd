package app.touched.com.touched.Adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.touched.com.touched.Models.MessageModel;
import app.touched.com.touched.R;
import app.touched.com.touched.Utilities.DialogBox;
import app.touched.com.touched.Utilities.MessagingType;
import app.touched.com.touched.Utilities.Utility;

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

    public MessagingAdapter(Activity context, ArrayList<MessageModel> list) {
        this.context = context;
        this.chatList = list;
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
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case MY_MESSAGE:
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

                holder.date.setText(getMsgTime(chatList.get(position).getTimestamp()));
                holder.username.setText(chatList.get(position).getName());
                holder.smsTxt.setText(chatList.get(position).getMsgContent());

                break;
            case MY_IMAGE_MSG:
                holder.date.setText(getMsgTime(chatList.get(position).getTimestamp()));
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
                                showImage.ShowImage(chatList.get(holder.getAdapterPosition()).getLocalUri());
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
//                holder.date.setText(getMsgTime(chatList.get(position).getSentTime()));
//                holder.username.setText(chatList.get(position).getName());
//                if(oneTimearraylist.contains(chatList.get(position).getSms())){
//                    holder.progressBar.setVisibility(View.VISIBLE);
//                    holder.downloadImage.setVisibility(GONE);
//                }else {
//                    if (chatModels.get(position).getLocalUrl() == null || chatModels.get(position).getLocalUrl().equals("")) {
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
//                holder.image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            if (chatModels.get(position).getLocalUrl() != null || !chatModels.get(position).getLocalUrl().equals("")) {
//                                mCurrentPlayingPosition = -1;
//
//                                DialogBox showImage = new DialogBox(context);
//                                showImage.ShowImage(chatModels.get(position).getLocalUrl());
//                            } else {
//                                showToastForContentNotAvailable();
//                            }
//                        } catch (Exception e) {
//                            showToastForContentNotAvailable();
//                        }
//                    }
//                });
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
        LinearLayout layout;
        ProgressBar progressBar;
        SeekBar seekBar;
        TextView invite, addContact;

        public ListViewHolder(View itemView) {
            super(itemView);

            smsTxt = (TextView) itemView.findViewById(R.id.msgtxt_id);
            msgStatus = (ImageView) itemView.findViewById(R.id.user_reply_status);
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
