package app.touched.com.touched.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.touched.com.touched.R;

/**
 * Created by Anshul on 3/17/2018.
 */

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> data;
    LayoutInflater inflter;

    public SpinnerAdapter(Context context, ArrayList objects) {
        this.context = context;
        data = objects;
        inflter = (LayoutInflater.from(context));

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_layout, null);

        TextView names = (TextView) view.findViewById(R.id.txvName);

        names.setText(data.get(i));
        return view;
    }
}
