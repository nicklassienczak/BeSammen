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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText mgetPhoneNumber;
    private RelativeLayout msendSmsAuth;
    private CountryCodePicker mcountryCodePicker;
    private String countryCode, phoneNumber, codeSent;

    private FirebaseAuth firebaseAuth;
    private ProgressBar mprogressBarMain;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBacks;


    private ImageView mbeSammenLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mgetPhoneNumber = findViewById(R.id.getPhoneNumber);
        msendSmsAuth = findViewById(R.id.sendSmsAuth);
        mcountryCodePicker = findViewById(R.id.countryCodePicker);
        mprogressBarMain = findViewById(R.id.progressBarMain);

        firebaseAuth = FirebaseAuth.getInstance();

        //Store the country code in this variable
        countryCode = mcountryCodePicker.getSelectedCountryCodeWithPlus();

        //If someone wants to change the country code this will change that
        mcountryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = mcountryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });


        msendSmsAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number;

                number = mgetPhoneNumber.getText().toString();
                //If the phonenumber is empty show this message
                if (number.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Angiv venligst dit nummer for at fortsætte", Toast.LENGTH_SHORT).show();
                }
                //If the phonenumber isn't 8 digits long, show this message
                else if (number.length()!=8) {
                    Toast.makeText(getApplicationContext(),"Der er sket en fejl, dit angivede nummer skal være på 8 cifre", Toast.LENGTH_SHORT).show();
                }

                //Otherwise send sms authentication
                else {

                    //Show progress bar
                    mprogressBarMain.setVisibility(View.VISIBLE);

                    //Insert the country code + the number inside the phonenumber variable
                    phoneNumber = countryCode + number;

                    //Send the sms authentication using firebase
                    PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)

                            //To which number
                            .setPhoneNumber(phoneNumber)

                            //Set a timeout if they need to get a new sms authentication
                            .setTimeout(60L, TimeUnit.SECONDS)

                            //Set the activity
                            .setActivity(MainActivity.this)

                            //Set callbacks
                            .setCallbacks(mcallBacks)

                            //Then build
                            .build();


                    //This will verify if the phonenumber is correct or not
                    PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

                }
            }
        });


        //Checking if the number is correct using the callbacks
        mcallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //If you want to automatically fetch the sms authentication (OTP)
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }


            //This will send the code
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"SMS godkendelsen er sendt", Toast.LENGTH_SHORT).show();
                mprogressBarMain.setVisibility(View.INVISIBLE);

                //To be able to parse the code we need to store the code in the codeSent
                codeSent=s;

                //Creating intent
                Intent intent = new Intent(MainActivity.this, SmsVerification.class);

                //Parsing the data
                intent.putExtra("SmsCode", codeSent);

                //Starting the activity
                startActivity(intent);
            }
        };


        mbeSammenLogo = findViewById(R.id.beSammenLogo);


        mbeSammenLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SmsVerification.class));
            }
        });






    }


    //To make sure the user don't have to verify everytime they open the app, i will have to make this to control
    //if the user is already in the firebase
    @Override
    protected void onStart() {
        super.onStart();

        //If the user is already in the firebase then they will just get redirected to the chat site
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this, ChatOverview.class);

            //It will start new task and clear any other task
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            //Start this activity
            startActivity(intent);
        }
    }
}