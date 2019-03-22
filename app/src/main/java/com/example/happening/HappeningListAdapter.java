package com.example.happening;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class HappeningListAdapter extends ArrayAdapter<Happening> {

    private final static String TAG = "HappeningListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView city;
        TextView date;
    }

    /**
     *
     * @param context
     * @param resource
     * @param objects
     */
    public HappeningListAdapter(Context context, int resource, List<Happening> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String userName = getItem(position).getUserName();
        String name = getItem(position).getName();
        String date = getItem(position).getDate();
        String time = getItem(position).getTime();
        String city = getItem(position).getCity();
        String description = getItem(position).getDescription();

        Happening happening = new Happening(userName, name, date, time, city, description);

        final View result;

        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder=  new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.city = (TextView) convertView.findViewById(R.id.cityTextView);
            holder.date = (TextView) convertView.findViewById(R.id.dateTextView);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.name.setText(happening.getName());
        holder.city.setText(happening.getCity());
        holder.date.setText(happening.getDate());


        return convertView;

    }


}
