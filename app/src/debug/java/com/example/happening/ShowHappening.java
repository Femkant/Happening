package com.example.happening;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happening.DbStuff.Data;
import com.example.happening.DbStuff.DbActions;
import com.example.happening.DbStuff.GetAttendRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowHappening extends Fragment {

    private Button attendButton;
    private ImageButton attendersButton;
    private boolean commentsShowing = false;
    private Happening happening;
    private AtomicBoolean attendRequestSent = new AtomicBoolean(false);
    private AtomicBoolean addCommentRequestSent = new AtomicBoolean(false);
    private AtomicBoolean getCommentRequestSent = new AtomicBoolean(false);
    private Button addCommentBtn;

    public ShowHappening() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_happening, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Happening");

        if (!MySharedPref.getInstance().getSharedPref().loadNightModeState()) {
            ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.bg_show);
            constraintLayout.setBackgroundResource(R.drawable.bg);
        }

        Bundle bundle = getArguments();
        happening = (Happening) bundle.getSerializable("object");

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(happening.getName());

        TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(happening.getDate());

        TextView time = (TextView) view.findViewById(R.id.time);
        time.setText(happening.getTime());

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(happening.getDescription());

        TextView city = (TextView) view.findViewById(R.id.city);
        city.setText(happening.getCity());

        description.setMovementMethod(new ScrollingMovementMethod());

        DbActions.getInstance().getComments(happening,getActivity(),getCommentRequestSent);
        attendButton = (Button) view.findViewById(R.id.attend);

        if(happening.isAttending()){
            attendButton.setText("Unattend");
            attendButton.setTextColor(Color.parseColor("#800000"));
        }

        else{
            attendButton.setText("Attend");
            attendButton.setTextColor(Color.parseColor("#008000"));
        }

        attendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendButtonClicked();
            }
        });

        attendersButton = (ImageButton) view.findViewById(R.id.attendersButton);
        attendersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendersButtonClicked();
            }
        });

        addCommentBtn = (Button) view.findViewById(R.id.addCommentBtn);
        addCommentBtn.setVisibility(View.INVISIBLE);

        final Button commentsTitle = (Button) view.findViewById(R.id.commentsTitle);
        commentsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentsShowing){
                    commentsTitle.setText("Show comments");
                    commentsShowing = false;
                    addCommentBtn.setVisibility(View.INVISIBLE);
                    hideComments();
                }else {
                    if(getCommentRequestSent.get()) {
                        Toast.makeText(getContext(),"Comments are downloading.. try in a second", Toast.LENGTH_LONG).show();
                    }
                    else {
                        commentsTitle.setText("Hide comments");
                        commentsShowing = true;
                        addCommentBtn.setVisibility(View.VISIBLE);
                        showComments();
                    }
                }
            }
        });



        return view;
    }

    /**
     * Attend button action
     */
    private void attendButtonClicked() {
        if(happening.getUserName().compareTo(FirebaseAuth.getInstance().getCurrentUser().getEmail()) != 0) {
            if (!attendRequestSent.get()) {
                GetAttendRequest attendRequest = new GetAttendRequest(
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        happening.getId());
                attendRequestSent.set(true);
                if (happening.isAttending()) {
                    DbActions.getInstance().deleteAttend(getActivity(), attendRequest, attendRequestSent, attendButton, happening);
                } else {
                    DbActions.getInstance().addAttend(getActivity(), attendRequest, attendRequestSent, attendButton, happening);
                }
            }
        }
        else{
            Toast.makeText(getContext(),"This is your Happening... cannot un-attend!",Toast.LENGTH_LONG).show();
        }
    }

    private void attendersButtonClicked() {

        String message = "";

        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Attenders");
        alert.setMessage(message);
        alert.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();

        if (myListAdapter == null) {
            //do nothing return null
            return;
        }

        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 60;
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        int height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        height = (height>2000)? 2000:height;
        params.height = height;
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }

    private void showComments() {
         ListView mListView = (ListView) getView().findViewById(R.id.commentsListView);
         Data.getInstance().acquireWrite(this.toString());
             ArrayList<Comment> commentsList = Data.getInstance().getComments();

             //Check if list is correct
             if(commentsList.size()>0){
                 if(happening.getId() != commentsList.get(0).getHappeningId()){
                     commentsList.clear();
                 }
             }
            final CommentListAdapter adapter = new CommentListAdapter(getContext(), R.layout.adapter_view_comment, commentsList);
        Data.getInstance().releaseWrite(this.toString());

        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getActivity(), adapter,"Enter comment");
            }
        });

        mListView.setAdapter(adapter);

        getListViewSize(mListView);
    }

    private void hideComments(){
        ListView mListView = (ListView) getView().findViewById(R.id.commentsListView);
        ArrayList<Comment> commentsList = new ArrayList<>();

        commentsList.clear();
        final CommentListAdapter adapter = new CommentListAdapter(getContext(), R.layout.adapter_view_comment, commentsList);
        mListView.setAdapter(adapter);

        getListViewSize(mListView);
    }


    private void showDialog(final Activity activity, final CommentListAdapter commentListAdapter, String msg){

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_postcomment);


        final TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        final EditText commentTxt = (EditText) dialog.findViewById(R.id.commentPopUpTextView);

        Button postBtn = (Button) dialog.findViewById(R.id.postCommentBtn);
        final ShowHappening showHappening = this;
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = commentTxt.getText().toString();

                if(text.trim().length()>0 && text.length()<500){
                    Comment comment = new Comment(happening.getId(), FirebaseAuth.getInstance().getCurrentUser().getEmail(),text,"Today","a while ago");
                    DbActions.getInstance().addComment(comment,activity,commentListAdapter);
                    dialog.dismiss();
                }
            }
        });

        Button dialogButton2 = (Button) dialog.findViewById(R.id.cancelCommentBtn);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}