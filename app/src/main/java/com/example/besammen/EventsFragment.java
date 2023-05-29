package com.example.besammen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EventsFragment extends Fragment {

    private ImageView mjoinEvent;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewEVENTS = inflater.inflate(R.layout.eventsfragment, container, false);

        mjoinEvent = viewEVENTS.findViewById(R.id.joinEvent);


        mjoinEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Du deltager nu i denne begivenhed", Toast.LENGTH_SHORT).show();
            }
        });


        return viewEVENTS;

    }
}
