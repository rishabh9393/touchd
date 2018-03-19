package app.touched.com.touched.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.touched.com.touched.Activites.ProfileActivity;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

import static app.touched.com.touched.Utilities.Constants.EXPLORE_FRAGMENT;
import static app.touched.com.touched.Utilities.Constants.FACEBOOK_URL;
import static app.touched.com.touched.Utilities.Constants.LEADERBOARD_FRAGMENT;
import static app.touched.com.touched.Utilities.Constants.USERS_Details_NODE;

/**
 * Created by Anshul on 2/26/2018.
 */

public class Users_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<User_Details> user_details;
    private Activity activity;
    private String pageType;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public Users_Adapter(Activity context,  String pageType) {
        this.user_details = new ArrayList<>();
        activity = context;
        this.pageType = pageType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = 0;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ITEM:

                if (pageType.equals(EXPLORE_FRAGMENT)) {
                    resource = R.layout.explore_item_layout;
                } else if (pageType.equals(LEADERBOARD_FRAGMENT)) {
                    resource = R.layout.explore_item_layout;
                }
                View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

                viewHolder= new Users_ViewHolder(itemView);
                break;

            case LOADING:
                View v2 = inflater.inflate(R.layout.progress_bar, parent, false);
                viewHolder = new Loading_ViewHolder(v2);
                break;



        }
return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User_Details user_details = this.user_details.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                Users_ViewHolder viewHolder=(Users_ViewHolder) holder;
                String photoUrl = FACEBOOK_URL + user_details.getUser_id() + "/picture?height=500";

                Picasso.with(activity).load(photoUrl).placeholder(R.drawable.photo_placeholder).error(R.drawable.photo_placeholder).into(viewHolder.userImage);
                viewHolder.userName.setText(user_details.getFirst_name() + " " + user_details.getLast_name());
                String rating = user_details.getRanking();
                if (rating != null)
                    viewHolder.userRating.setText(user_details.getRanking().isEmpty() ? "0" : user_details.getRanking());
                else viewHolder.userRating.setText("0");
                String age = user_details.getAge();
                if (age != null)
                    viewHolder.userAge.setText(user_details.getAge().isEmpty() ? "18" : user_details.getAge());
                break;
            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return user_details.size();
    }

    public ArrayList<User_Details> getUser_details() {
        return user_details;
    }

    public void setUser_details(ArrayList<User_Details> user_details) {
        this.user_details = user_details;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == user_details.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

    }

    public void add(User_Details data) {
        user_details.add(data);
        notifyItemInserted(user_details.size() - 1);
    }

    public void addAll(List<User_Details> dataList) {
        for (User_Details data : dataList) {
            add(data);
        }
    }

    public void remove(User_Details city) {
        int position = user_details.indexOf(city);
        if (position > -1) {
            user_details.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new User_Details());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = user_details.size() - 1;
        User_Details item = getItem(position);

        if (item != null) {
            user_details.remove(position);
            notifyItemRemoved(position);
        }
    }

    public User_Details getItem(int position) {
        return user_details.get(position);
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent data=new Intent(view.getContext(), ProfileActivity.class);
                    data.putExtra(USERS_Details_NODE,user_details);
                }
            });
        }
    }
    public class Loading_ViewHolder extends RecyclerView.ViewHolder{

        public Loading_ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
