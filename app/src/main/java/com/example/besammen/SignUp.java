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

    private EditText mgetUserName, mgetAge, mgetDiagnose;
    private RelativeLayout msaveProfile;
    private ImageView mgetUserImageInImageView;
    private RadioButton mradio_man, mradio_woman, mradio_other;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mgetAge = findViewById(R.id.getAge);
        mgetUserName = findViewById(R.id.getUserName);
        mgetDiagnose = findViewById(R.id.getDiagnose);
        mgetUserImageInImageView = findViewById(R.id.getUserImageInImageView);
        msaveProfile = findViewById(R.id.saveProfile);
        mradio_man = findViewById(R.id.radio_man);
        mradio_woman = findViewById(R.id.radio_woman);
        mradio_other = findViewById(R.id.radio_other);









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