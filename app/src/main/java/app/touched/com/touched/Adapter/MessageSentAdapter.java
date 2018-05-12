package app.touched.com.touched.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.touched.com.touched.Activites.MessagingActivity;
import app.touched.com.touched.MainApplicationClass;
import app.touched.com.touched.Models.MessageSentModel;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

import static app.touched.com.touched.Utilities.Constants.FRIENDS_TAG;

/**
 * Created by Anshul on 4/29/2018.
 */

public class MessageSentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<MessageSentModel> user_details;
    private User_Details myDetails;

    public MessageSentAdapter(Activity activity, ArrayList<MessageSentModel> user_details) {
        this.activity = activity;
        this.user_details = user_details;
        myDetails = ((MainApplicationClass) activity.getApplication()).getProfileUsersDetail();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_board_item, parent, false);

        return new MessageSent_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MessageSentModel data = user_details.get(position);
        final MessageSent_ViewHolder viewHolder = (MessageSent_ViewHolder) holder;
        Picasso.with(activity).load(data.getPicture().getData().getUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.photo_placeholder).error(R.drawable.photo_placeholder).into(viewHolder.userImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(activity).load(data.getPicture().getData().getUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.photo_placeholder).error(R.drawable.photo_placeholder).into(viewHolder.userImage);
            }
        });
        viewHolder.userName.setText(data.getFirst_name() + " " + data.getLast_name());
        viewHolder.msgTime.setText(data.getSentTime());
        if (data.getSenderEmail().equals(myDetails.getEmail())) {
            if (data.getIsMetaData().equals("true"))
                viewHolder.location.setText("you: Photo sent by you");
            else
                viewHolder.location.setText("you: " + data.getMsg_content());
        } else {
            if (data.getIsMetaData().equals("true"))
                viewHolder.location.setText("Photo received by you");
            else
                viewHolder.location.setText(data.getMsg_content());

        }
        if (data.getIsMessagesUnread().equals("true")) {
            viewHolder.imv_Status_icon.setVisibility(View.VISIBLE);
            viewHolder.imv_Status_icon.setImageResource(R.drawable.unread);
            viewHolder.location.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            viewHolder.location.setTextColor(Color.GRAY);

            viewHolder.imv_Status_icon.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return user_details.size();
    }

    public class MessageSent_ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, location, userRating, msgTime;
        public ImageView userImage, imv_Status_icon;

        public MessageSent_ViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.txt_Name);
            location = (TextView) view.findViewById(R.id.txt_Place);
            userRating = (TextView) view.findViewById(R.id.txt_Rank);
            userRating.setVisibility(View.GONE);
            userImage = (ImageView) view.findViewById(R.id.imv_Profile);
            msgTime = (TextView) view.findViewById(R.id.txt_Time);
            imv_Status_icon = (ImageView) view.findViewById(R.id.imv_Icon);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageSentModel data = user_details.get(getAdapterPosition());
                    User_Details myFriendData = new User_Details();
                    myFriendData.setEmail(data.getEmail());
                    myFriendData.setFirst_name(data.getFirst_name());
                    myFriendData.setId(data.getId());
                    myFriendData.setKey(data.getKey());
                    myFriendData.setLast_name(data.getLast_name());
                    myFriendData.setPicture(data.getPicture());
                    activity.startActivity(new Intent(activity, MessagingActivity.class).putExtra(FRIENDS_TAG, myFriendData));

                }
            });
        }
    }
}
