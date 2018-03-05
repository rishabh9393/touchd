package app.touched.com.touched.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

import static app.touched.com.touched.Utilities.Constants.EXPLORE_FRAGMENT;
import static app.touched.com.touched.Utilities.Constants.LEADERBOARD_FRAGMENT;

/**
 * Created by Anshul on 2/26/2018.
 */

public class Users_Adapter extends RecyclerView.Adapter<Users_Adapter.Users_ViewHolder> {
    private ArrayList<User_Details> user_details;
    private Activity activity;
    private String pageType;

    public Users_Adapter(Activity context, ArrayList<User_Details> user_details, String pageType) {
        this.user_details = user_details;
        activity = context;
        this.pageType = pageType;
    }

    @Override
    public Users_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = 0;
        if (pageType.equals(EXPLORE_FRAGMENT)) {
            resource = R.layout.explore_item_layout;
        } else if (pageType.equals(LEADERBOARD_FRAGMENT)) {
            resource = R.layout.explore_item_layout;
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new Users_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Users_ViewHolder holder, int position) {
        User_Details user_details = this.user_details.get(position);
        String photoUrl = "https://graph.facebook.com/" + user_details.getUser_id() + "/picture?height=500";

        Picasso.with(activity).load(photoUrl).placeholder(R.drawable.photo_placeholder).error(R.drawable.photo_placeholder).into(holder.userImage);
        holder.userName.setText(user_details.getFirst_name() + " " + user_details.getLast_name());
        String rating=user_details.getRating();
        if(rating!=null)
        holder.userRating.setText(user_details.getRating().isEmpty() ? "0" : user_details.getRating());
        else holder.userRating.setText("0");
        String age=user_details.getAge();
        if(age!=null)
        holder.userAge.setText(user_details.getAge().isEmpty() ? "18" : user_details.getAge());
        else
            holder.userAge.setText("18");
    }

    @Override
    public int getItemCount() {
        return user_details.size();
    }

    public class Users_ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userAge, userRating;
        public ImageView userImage;

        public Users_ViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.txvUserName);
            userAge = (TextView) view.findViewById(R.id.txvUserAge);
            userRating = (TextView) view.findViewById(R.id.txvUserRating);
            userImage = (ImageView) view.findViewById(R.id.imvUserPic);
        }
    }
}
