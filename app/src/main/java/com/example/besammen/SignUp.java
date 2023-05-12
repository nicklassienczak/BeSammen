package com.example.besammen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUp extends AppCompatActivity {

    private EditText mgetUserName, mgetAge, mgetDiagnose;
    private ProgressBar mprogressBarSignUp;
    private RelativeLayout msaveProfile;
    private CardView mgetUserImage;
    private ImageView mgetUserImageInImageView;
    private static int USER_IMAGE = 123;
    private Uri imagePath;
    private RadioButton mradio_man, mradio_woman, mradio_other;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    private String name, diagnose;
    private String ImageUriAcessToken;
    private int age;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        //This will help store our images on our database
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mgetAge = findViewById(R.id.getAge);
        mgetUserImage = findViewById(R.id.getUserImage);
        mgetUserName = findViewById(R.id.getUserName);
        mgetDiagnose = findViewById(R.id.getDiagnose);
        mgetUserImageInImageView = findViewById(R.id.getUserImageInImageView);

        msaveProfile = findViewById(R.id.saveProfile);
        mprogressBarSignUp = findViewById(R.id.progressBarSignUp);

        mradio_man = findViewById(R.id.radio_man);
        mradio_woman = findViewById(R.id.radio_woman);
        mradio_other = findViewById(R.id.radio_other);

        //If user click getUserImage, they will be forwarded to the image selector
        mgetUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This will take the user to there photo library
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, USER_IMAGE);
            }
        });


        mgetUserImageInImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, ChatOverview.class));
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


    //This is used to take the selected image and put it as the users profile image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == USER_IMAGE && resultCode == RESULT_OK){

            //Set the image path
            imagePath = data.getData();
            mgetUserImageInImageView.setImageURI(imagePath);
        }




        super.onActivityResult(requestCode, resultCode, data);
    }
}