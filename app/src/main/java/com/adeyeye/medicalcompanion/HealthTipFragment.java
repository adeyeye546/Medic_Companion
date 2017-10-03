package com.adeyeye.medicalcompanion;


import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class HealthTipFragment extends Fragment {

    public HealthTipFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_health_tip, container, false);



       final Button myButton;
        myButton = (Button) rootView.findViewById(R.id.askButton);

        final ImageView tipDisplay;
        tipDisplay = (ImageView) rootView.findViewById(R.id.image_healthTip);

        final int[] tipArray = {
                R.drawable.tip1,
                R.drawable.tip2,
                R.drawable.tip3,
                R.drawable.tip4,
                R.drawable.tip5,
                R.drawable.tip6,
                R.drawable.tip7
        };
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("Medic Companion", "The tip has been pressed");
                Random randomNumberGenerator = new Random();
                int number = randomNumberGenerator.nextInt(7);

                tipDisplay.setImageResource(tipArray[number]);


            }
        });
        return rootView;
    }



}


