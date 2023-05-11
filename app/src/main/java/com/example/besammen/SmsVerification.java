package com.example.besammen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SmsVerification extends AppCompatActivity {

    private ImageView mbeSammenImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        mbeSammenImg = findViewById(R.id.beSammenImg);

        mbeSammenImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SmsVerification.this, ChatOverview.class));
            }
        });

    }
}