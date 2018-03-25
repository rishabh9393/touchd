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
import java.util.List;

import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

/**
 * Created by AnshulMajoka on 18-Mar-18.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ArrayList<User_Details> user_details;
    private String pageType;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public LeaderboardAdapter(Activity context, String pageType) {
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
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_board_item, parent, false);
                viewHolder = new LeaderboardAdapter.Users_ViewHolder(itemView);
                break;

            case LOADING:
                View v2 = inflater.inflate(R.layout.progress_bar, parent, false);
                viewHolder = new LeaderboardAdapter.Loading_ViewHolder(v2);
                break;


        }
        return viewHolder;

    }//ZEnk2hpGfrivgGJOoiObfeiSFIY=

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User_Details user_details = this.user_details.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                LeaderboardAdapter.Users_ViewHolder viewHolder = (LeaderboardAdapter.Users_ViewHolder) holder;
               Picasso.with(activity).load(user_details.getPicture().getData().getUrl()).placeholder(R.drawable.photo_placeholder).error(R.drawable.photo_placeholder).into(viewHolder.userImage);
                viewHolder.userName.setText(user_details.getFirst_name() + " " + user_details.getLast_name());
                String rating = user_details.getRanking();
                if (rating != null)
                    viewHolder.userRating.setText(user_details.getRanking().isEmpty() ? "0" : user_details.getRanking());
                else viewHolder.userRating.setText("0");
                User_Details.Location location = user_details.getLocation();
                if (location != null)
                    viewHolder.location.setText(location.getName());
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
//        if(LeaderBoardFragment.ScrollType.equals("CityWise")){
//            user_details=new ArrayList<>();
//        }
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
        public TextView userName, location, userRating;
        public ImageView userImage;

        public Users_ViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.txt_Name);
            location = (TextView) view.findViewById(R.id.txt_Place);
            userRating = (TextView) view.findViewById(R.id.txt_Rank);
            userImage = (ImageView) view.findViewById(R.id.imv_Profile);
        }
    }

    public class Loading_ViewHolder extends RecyclerView.ViewHolder {

        public Loading_ViewHolder(View itemView) {
            super(itemView);
        }
    }

//    public  void sortbyAge(){
//        LeaderBoardFragment.ScrollType="Age";
//        Collections.sort(user_details, new Comparator<User_Details>() {
//            public int compare(User_Details p1, User_Details p2) {
//                return p1.getAge().compareTo(p2.getAge());
//            }
//        });
//        notifyDataSetChanged();
//    }


}