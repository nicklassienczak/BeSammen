package com.example.besammen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    private EditText msignUpEmail, msignUpPassword, msignUpUserName, msignUpAge;
    private TextView mgoToMainActivity;
    private RelativeLayout msignUp;
    private ImageView mbeSammenLogo;
    private RadioButton mradio_man, mradio_woman, mradio_other;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        msignUpEmail = findViewById(R.id.signUpEmail);
        msignUpPassword = findViewById(R.id.signUpPassword);
        msignUpUserName = findViewById(R.id.signUpUserName);
        msignUpAge = findViewById(R.id.signUpAge);
        mgoToMainActivity = findViewById(R.id. goToMainActivity);
        msignUp = findViewById(R.id.signUp);
        mbeSammenLogo = findViewById(R.id.beSammenLogo);
        mradio_man = findViewById(R.id.radio_man);
        mradio_woman = findViewById(R.id.radio_woman);
        mradio_other = findViewById(R.id.radio_other);

        mbeSammenLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, MainActivity.class));
            }
        });

        mgoToMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, MainActivity.class));
            }
        });






    }


    //Gender choosing radio buttons
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_man:
                if (checked)
                    // Gender = Man
                    break;
            case R.id.radio_woman:
                if (checked)
                    // Gender = Woman
                    break;
            case R.id.radio_other:
                if (checked)
                    // Gender = Other
                    break;
        }
    }
}