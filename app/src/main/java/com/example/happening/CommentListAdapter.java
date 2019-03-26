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

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private final static String TAG = "CommentListAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;


    public CommentListAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    private static class ViewHolder {
        TextView userName;
        TextView comment;
        TextView date;
        TextView time;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String userName = getItem(position).getUserName();
        String comment = getItem(position).getComment();
        String date = getItem(position).getDate();
        String time = getItem(position).getTime();

        Comment mComment = new Comment(userName, comment, date, time);

        final View result;

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder=  new CommentListAdapter.ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.cUserName);
            holder.comment = (TextView) convertView.findViewById(R.id.cComment);
            holder.date = (TextView) convertView.findViewById(R.id.cDate);
            holder.time = (TextView) convertView.findViewById(R.id.cTime);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (CommentListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.userName.setText(mComment.getUserName());
        holder.comment.setText(mComment.getComment());
        holder.date.setText(mComment.getDate());
        holder.time.setText(mComment.getTime());



        return convertView;

    }

}
