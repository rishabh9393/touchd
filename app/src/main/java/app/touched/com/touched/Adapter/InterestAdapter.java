package app.touched.com.touched.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.touched.com.touched.R;

/**
 * Created by Anshul on 3/18/2018.
 */

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.Users_ViewHolder> {
    Context con;
    ArrayList<String> data;
    public InterestAdapter(Context context, ArrayList<String> interestData)
    {
        con=context;
        data=interestData;
    }
    @Override
    public Users_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.interests_layout, parent, false);

        return new Users_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Users_ViewHolder holder, int position) {
holder.interestName.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Users_ViewHolder extends RecyclerView.ViewHolder {
        TextView interestName;
        public Users_ViewHolder(View itemView) {
            super(itemView);
            interestName=(TextView)itemView.findViewById(R.id.txvName);
        }
    }
}

