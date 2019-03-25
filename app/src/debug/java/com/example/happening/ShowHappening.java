package com.example.happening;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowHappening extends Fragment {

    private Button attendButton;
    private Button attendersButton;

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
                attendbuttonClicked();
            }
        });

        attendersButton = (Button) view.findViewById(R.id.attendersButton);
        attendersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendersButtonClicked();
            }
        });

        return view;
    }

    private void attendbuttonClicked() {
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
}