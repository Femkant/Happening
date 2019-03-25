package com.example.happening;


import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowHappening extends Fragment {

    private Button attendButton;
    private ImageButton attendersButton;
    private boolean commentsShowing = false;



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
        Happening happening = (Happening) bundle.getSerializable("object");

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


        attendButton = (Button) view.findViewById(R.id.attend);
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

        final Button commentsTitle = (Button) view.findViewById(R.id.commentsTitle);
        commentsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentsShowing){
                    commentsTitle.setText("Show comments");
                    commentsShowing = false;
                    hideComments();
                }else {
                    commentsTitle.setText("Hide comments");
                    commentsShowing = true;
                    showComments();
                }
            }
        });



        return view;
    }

    private void attendButtonClicked() {
        attendButton.setText("Attending!");
        attendButton.setTextColor(Color.parseColor("#008000"));
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
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }

    private void showComments(){
         ListView mListView = (ListView) getView().findViewById(R.id.commentsListView);
         ArrayList<Comment> commentsList = new ArrayList<>();

        commentsList.add(new Comment("hestveda@gmail.se", "Jag ser fram emot detta enormt mycket.", "2019/03/25"));
        commentsList.add(new Comment("KarlSvensson@gmail.se", "Jag heter Karl. Vissa kallar mig kalle. Ingen aning varför^^.", "2019/03/25"));
        commentsList.add(new Comment("BeritBerg@Telia.se", "Säljer ni korv?.", "2019/03/25"));
        commentsList.add(new Comment("MaritBultsax@hotmail.com", "Min bultsax är tyvärr sönder. Den gick i två bitar när jag" +
                "bet i det i förrgår. Jag funderar starkt på att köpa en ny, har ni sånna till salu på denna happening? I så fall kan ni räkna med" +
                "att jag kommer dit och köper en eller två!", "2019/03/24"));
         final CommentListAdapter adapter = new CommentListAdapter(getContext(), R.layout.adapter_view_comment, commentsList);
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
}