package com.example.besammen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private EditText mgetUserName, mgetAge, mgetDiagnose;
    private ProgressBar mprogressBarSignUp;
    private RelativeLayout msaveProfile;
    private CardView mgetUserImage;
    private ImageView mgetUserImageInImageView;
    private static int USER_IMAGE = 123;
    private Uri imagePath;
    private RadioButton mget_radio_man, mget_radio_woman, mget_radio_other;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    private String name, diagnose, gender;
    private String ImageUriAcessToken;
    private int age;
    private String ageString;




    @SuppressLint("WrongViewCast")
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

        mget_radio_man = findViewById(R.id.get_radio_man);
        mget_radio_woman = findViewById(R.id.get_radio_woman);
        mget_radio_other = findViewById(R.id.get_radio_other);



        //If user click getUserImage, they will be forwarded to the image selector
        mgetUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This will take the user to there photo library
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, USER_IMAGE);
            }
        });



        msaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get the username
                name = mgetUserName.getText().toString();

                //Get the diagnose
                diagnose = mgetDiagnose.getText().toString();

                //Get the age
                ageString = mgetAge.getText().toString();


                //Checking if username is empty, if it is, show this message
                if (name.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Venligst indtast dit navn", Toast.LENGTH_SHORT).show();

                }
                else if (ageString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Venligst indtast din alder", Toast.LENGTH_SHORT).show();
                }
                else if (diagnose.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Venligst indsæt din diagnose", Toast.LENGTH_SHORT).show();
                }
                //Checking if the user have selected a picture
                else if (imagePath == null) {
                    Toast.makeText(getApplicationContext(), "Venligst indsæt et billede", Toast.LENGTH_SHORT).show();
                }

                else {
                    mprogressBarSignUp.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    //int age = Integer.parseInt(ageString);
                    mprogressBarSignUp.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(SignUp.this, ChatOverview.class);
                    startActivity(intent);
                    finish();


                }
            }
        });









    }


    private void sendDataForNewUser(){

        sendDataToRealTimeDataBase();


    }

    private void sendDataToRealTimeDataBase(){


        name = mgetUserName.getText().toString().trim();
        diagnose = mgetDiagnose.getText().toString().trim();
        //Getting the int
        String ageString = mgetAge.getText().toString();
        age = Integer.parseInt(ageString);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        //Declaring the databaseReference
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        //Creating user profile, and inserting the values
        userProfileData muserProfile = new userProfileData(name, firebaseAuth.getUid(), diagnose, age);

        //This will save the user profile data on the database
        databaseReference.setValue(muserProfile);

        Toast.makeText(getApplicationContext(), "Bruger oprettelse fuldført", Toast.LENGTH_SHORT).show();

        sendImageToStorage();

    }

    private void sendImageToStorage(){

        //Store the image on the specific user, using firebaseAuth.getUid
        StorageReference imageReference = storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Picture");

        //To make the image show faster, we will compress it
        Bitmap bitmap = null;
        try {
            //The imagePath
            //Inside bitmap we have the image
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        }
        //Catch error. e = error
        catch (IOException e){
            //Print the error
            e.printStackTrace();
        }

        //We want to minimize the fetch time, for that
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //Decreasing the image size
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);

        //Put it in the byte array
        byte[] data = byteArrayOutputStream.toByteArray();

        //Putting the image in the storage

        UploadTask uploadTask = imageReference.putBytes(data);

        //To make sure everything is fine we put a addOnSuccessListener on the UploadTask
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //This will tell if the URL is downloaded successfully
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //If it is stored successfully we can store the image inside our ImageUriAcessToken
                        ImageUriAcessToken = uri.toString();
                        Toast.makeText(getApplicationContext(), "Billede gemt", Toast.LENGTH_SHORT).show();
                        sendDataToCloudFirestore();
                    }
                    //If the image didnt get sent to firestore write this message
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Billede blev ikke gemt", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(), "Billede sendt til skyen", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Billede ikke sendt til skyen", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void sendDataToCloudFirestore() {

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String , Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("age", age);
        userData.put("diagnose", diagnose);
        userData.put("image", ImageUriAcessToken);
        userData.put("uid", firebaseAuth.getUid());
        userData.put("status", "Aktiv");
        userData.put("Gender", gender);

        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Data sendt til firebase", Toast.LENGTH_SHORT).show();

            }
        });

        //Could add a onFailureListener

    }


    //Gender choosing radio buttons
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.get_radio_man:
                if (checked){
                    // Gender = Man
                    gender = mget_radio_man.getText().toString();
                    break;
                }


            case R.id.get_radio_woman:
                if (checked){
                    // Gender is woman
                    gender = mget_radio_woman.getText().toString();
                    break;
                }

            case R.id.get_radio_other:
                if (checked){
                    // Gender = Other
                    gender = mget_radio_other.getText().toString();
                    break;
                }
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