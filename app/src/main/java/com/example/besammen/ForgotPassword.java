package com.example.besammen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ForgotPassword extends AppCompatActivity {

    private EditText mforgotPasswordEmail;
    private RelativeLayout mrecoverPassword;
    private TextView mgotoMainActivity;
    private ImageView mbeSammenLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        mforgotPasswordEmail = findViewById(R.id.forgotPasswordEmail);
        mrecoverPassword = findViewById(R.id.recoverPassword);
        mgotoMainActivity = findViewById(R.id.goToMainActivity);
        mbeSammenLogo = findViewById(R.id.beSammenLogo);


        mbeSammenLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
            }
        });

        mgotoMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
            }
        });



    }
}