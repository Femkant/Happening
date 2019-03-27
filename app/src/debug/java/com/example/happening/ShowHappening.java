package com.example.happening;


import android.app.Dialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

        TextView organizer = (TextView) view.findViewById(R.id.organizer);
        organizer.setText(happening.getUserName());

        description.setMovementMethod(new ScrollingMovementMethod());

        //DbActions.getInstance().getComments(happening,getActivity(),getCommentRequestSent);
        attendButton = (Button) view.findViewById(R.id.attend);

        if(happening.isAttending()){
            attendButton.setText("Unattend");
            //attendButton.setTextColor(Color.parseColor("#800000"));
        }

        else{
            attendButton.setText("Attend");
            //attendButton.setTextColor(Color.parseColor("#008000"));
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
                showAttenders();
            }
        });

        addCommentBtn = (Button) view.findViewById(R.id.addCommentBtn);
        addCommentBtn.setVisibility(View.INVISIBLE);

        final Button commentsTitle = (Button) view.findViewById(R.id.commentsTitle);
        commentsTitle.setText("Show comments");

        commentsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComments();
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

    /**
     * Show attenders of happening
     */
    private void showAttenders() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_attenders);


        ArrayList<String> list = new ArrayList<>();

        Data.getInstance().acquireRead(this.toString());
        Data.getInstance().setUpdateAttendersList(list);

        //Get listview
        final AttendersListAdapter adapter = new AttendersListAdapter(getContext(), R.layout.adapter_view_users, list);
        Data.getInstance().releaseRead(this.toString());

        DbActions.getInstance().getAttenders(happening,getActivity(),adapter);

        ListView mListView = (ListView) dialog.findViewById(R.id.attendersListView);
        mListView.setAdapter(adapter);

        final EditText commentTxt = (EditText) dialog.findViewById(R.id.commentPopUpTextView);

        Button closeAttendersBtn = (Button) dialog.findViewById(R.id.closeAttendersBtn);
        final ShowHappening showHappening = this;

        closeAttendersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    /**
     * Shows comments
     */
    private void showComments(){

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_comments);



        Data.getInstance().acquireRead(this.toString());
            ArrayList<Comment> list = Data.getInstance().getComments();
            Data.getInstance().setUpdateCommentList(list);
            //Get listview
            final CommentListAdapter adapter = new CommentListAdapter(getContext(), R.layout.adapter_view_comment, list);
        Data.getInstance().releaseRead(this.toString());

        DbActions.getInstance().getComments(happening,getActivity(),adapter);

        ListView mListView = (ListView) dialog.findViewById(R.id.commentsListView);
        mListView.setAdapter(adapter);

        final EditText commentTxt = (EditText) dialog.findViewById(R.id.commentPopUpTextView);

        Button postBtn = (Button) dialog.findViewById(R.id.postCommentBtn);
        final ShowHappening showHappening = this;

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addCommentRequestSent.get()) {
                    Toast.makeText(getContext(),"In progress.",Toast.LENGTH_LONG).show();
                }
                else{
                    String text = commentTxt.getText().toString();

                    if (text.trim().length() > 0 && text.length() < 500) {
                        Comment comment = new Comment(happening.getId(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), text, "Today", "a while ago");
                        addCommentRequestSent.set(true);
                        DbActions.getInstance().addComment(comment, getActivity(), adapter,addCommentRequestSent);
                    }
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