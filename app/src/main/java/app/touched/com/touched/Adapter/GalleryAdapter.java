package app.touched.com.touched.Adapter;

import android.content.Context;
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

import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

/**
 * Created by Anshul on 3/18/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.Users_ViewHolder> {
    Context context;
    ArrayList<User_Details.Fb_Photos> fb_photos;

    public GalleryAdapter(Context context, ArrayList<User_Details.Fb_Photos> data) {
        this.context = context;
        fb_photos = data;
    }

    @Override
    public GalleryAdapter.Users_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_layout, parent, false);

        return new GalleryAdapter.Users_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.Users_ViewHolder holder, final int position) {
        Picasso.with(context).load(fb_photos.get(position).getData().getPicture()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(holder.image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(fb_photos.get(position).getData().getPicture()).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(holder.image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fb_photos.size();
    }

    public class Users_ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public Users_ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imvPhoto);
        }
    }
}
