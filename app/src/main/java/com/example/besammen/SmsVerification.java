package com.example.besammen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class SmsVerification extends AppCompatActivity {

    private EditText mgetSmsAuth;
    private RelativeLayout mcheckSmsAuth;
    private TextView mchangeNumber;
    private String enteredSmsAuth;

    private FirebaseAuth firebaseAuth;
    private ProgressBar mprogressBarSmsAuth;

    private ImageView mbeSammenImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);


        mgetSmsAuth = findViewById(R.id.getSmsAuth);
        mcheckSmsAuth = findViewById(R.id.checkSmsAuth);
        mchangeNumber = findViewById(R.id.changeNumber);
        mprogressBarSmsAuth = findViewById(R.id.progressBarSmsAuth);

        firebaseAuth = FirebaseAuth.getInstance();

        //If the user have put in the wrong number, they will be taken back to start over
        mchangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmsVerification.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mcheckSmsAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enteredSmsAuth now stores the code the user have entered
                enteredSmsAuth = mgetSmsAuth.getText().toString();

                //If the user haven't put in the Sms authentication code, show this message
                if (enteredSmsAuth.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Venligst indtast godkendelses SMS'en", Toast.LENGTH_SHORT).show();
                }

                //If the user have inserted a code, then check and validate it
                else{

                    //Show progress bar
                    mprogressBarSmsAuth.setVisibility(View.VISIBLE);

                    //Variable for the code received by the firebase
                    String codeReceived = getIntent().getStringExtra("SmsCode");

                    //Check if the code the user have put in matches the code sent by firebase
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeReceived, enteredSmsAuth);

                    signInWithPhoneAuthCredential(credential);
                }
            }
        });




        mbeSammenImg = findViewById(R.id.beSammenImg);

        mbeSammenImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SmsVerification.this, SignUp.class));
            }
        });

    }


    //This method gets called when the user have clicked on the button to verify the code
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

        //We then add a on complete listener to decide what happens if it is successful or not
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //Check if task is successful
                if (task.isSuccessful()){

                    //If successful, remove progressbar
                    mprogressBarSmsAuth.setVisibility(View.INVISIBLE);

                    //And show this toast
                    Toast.makeText(getApplicationContext(), "Telefon nummer godkendt", Toast.LENGTH_SHORT).show();

                    //And get us to the sign in activity
                    Intent intent = new Intent(SmsVerification.this, SignUp.class);
                    startActivity(intent);
                    finish();

                }
                //If something went wrong
                else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){

                        //Stop progressbar
                        mprogressBarSmsAuth.setVisibility(View.INVISIBLE);

                        //Show this toast
                        Toast.makeText(getApplicationContext(), "Godkendelse fejlede", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




    }




}