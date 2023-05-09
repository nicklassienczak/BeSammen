package com.example.besammen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mlogInPassword, mlogInEmail;
    private TextView mforgotPassword;
    private RelativeLayout mlogIn, msignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mlogInPassword = findViewById(R.id.logInPassword);
        mlogInEmail = findViewById(R.id.logInEmail);
        mforgotPassword = findViewById(R.id.forgotPassword);
        mlogIn = findViewById(R.id.login);
        msignUp = findViewById(R.id.signUp);




    }
}