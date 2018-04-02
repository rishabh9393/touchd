package app.touched.com.touched.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.touched.com.touched.Models.MessageModel;
import app.touched.com.touched.R;

/**
 * Created by Anshul on 4/2/2018.
 */

public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.ListViewHolder> {
    Context context;
    ArrayList<MessageModel> chatList;

    public MessagingAdapter(Context context, ArrayList<MessageModel> list) {
        this.context = context;
        this.chatList = list;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView smsTxt;
        TextView date, username, number, contactName;
        ImageView image, downloadImage, playImage, pauseImage;
        LinearLayout layout;
        ProgressBar progressBar;
        SeekBar seekBar;
        TextView invite, addContact;

        public ListViewHolder(View itemView) {
            super(itemView);

//            smsTxt = (TextView) itemView.findViewById(R.id.msgtxt_id);
//            number = (TextView) itemView.findViewById(R.id.number);
//            invite = (TextView) itemView.findViewById(R.id.invite);
//            addContact = (TextView) itemView.findViewById(R.id.addContact);
//            date = (TextView) itemView.findViewById(R.id.date_id);
//            image = (ImageView) itemView.findViewById(R.id.image_full_view);
//            contactName = (TextView) itemView.findViewById(R.id.contact_name);//error ni dikh ri
//            layout = (LinearLayout) itemView.findViewById(R.id.outgoing_layout_bubble);
//            downloadImage = (ImageView) itemView.findViewById(R.id.downloadImage);
//            playImage = (ImageView) itemView.findViewById(R.id.playImage);
//            username = (TextView) itemView.findViewById(R.id.user_name);
//            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_show_image);
//            seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
//            pauseImage = (ImageView) itemView.findViewById(R.id.pauseImage);
//            // time=(TextView)itemView.findViewById(R.id.name);

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
