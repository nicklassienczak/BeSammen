package com.example.besammen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {


    //We will not let the user update there age or diagnose, it is only there name and profile picture


    private EditText mnewUserName;
    private ImageView mnewUserImageInImageView;
    private ImageButton mbackButtonUpdateProfile;
    private RelativeLayout mupdateProfileButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    private StorageReference storageReference;

    //To fetch the users profile image with the image uri access token
    private String ImageAccessToken;
    //String to store the new name
    private String newName, newDiagnose;
    private int newAge;

    private androidx.appcompat.widget.Toolbar mtoolBarUpdateProfile;
    private ProgressBar mprogressBarUpdateProfile;
    //Image path to select a new profile picture
    private Uri imagePath;
    private Intent intent;

    //To pick image
    private static int USER_IMAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mnewUserName = findViewById(R.id.newUserName);
        mnewUserImageInImageView = findViewById(R.id.newUserImageInImageView);
        mbackButtonUpdateProfile = findViewById(R.id.backButtonUpdateProfile);
        mupdateProfileButton = findViewById(R.id.updateProfileButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mprogressBarUpdateProfile = findViewById(R.id.progressBarUpdateProfile);
        mtoolBarUpdateProfile = findViewById(R.id.toolBarUpdateProfile);

        //Have to get the data of the intent
        intent = getIntent();

        setSupportActionBar(mtoolBarUpdateProfile);

        //If a user clicks the back button, then the activity will finish and the user will go back to the profile activity
        mbackButtonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //To show the previous name of the user
        //Fetching this from the intent
        mnewUserName.setText(intent.getStringExtra("nameOfUser"));



        //If the user wants to only change the image and not the name
        //We have to make that work aswell
        //This will store the data according to the user id
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        //What happens when user clicks update profile button
        mupdateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //This will take the new username the user have put in the edittext and store it in the newName
                newName = mnewUserName.getText().toString();
                //Making sure that the newName isn't empty. If it is, this message will show
                if (newName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Navn'et må ikke været tomt", Toast.LENGTH_SHORT).show();
                }
                //If the user have updated the profile picture we have to update name and image
                else if (imagePath != null) {
                    //Here we have to fetch so we will show the progress bar to let the user know that the action has started
                    //But can take time depending on the connection
                    mprogressBarUpdateProfile.setVisibility(View.VISIBLE);

                    //Then we have to set the new username and image to the user profile data
                    userProfileData muserProfile = new userProfileData(newName, firebaseAuth.getUid(), newDiagnose, newAge);
                    databaseReference.setValue(muserProfile);

                    updateUserImageToStorage();

                    //To let the user know that there profile have been updated
                    Toast.makeText(getApplicationContext(), "Profil opdateret", Toast.LENGTH_SHORT).show();

                    //When the action is done, the progressbar will disappear
                    mprogressBarUpdateProfile.setVisibility(View.INVISIBLE);

                    //When the user have updated there profile they will get back to the chat overview
                    Intent intent1 = new Intent(UpdateProfile.this, ChatOverview.class);
                    startActivity(intent1);
                    finish();

                }
                //If user only wants to update the name but not the image
                else {

                    //To let the user know that the action has started the progressbar will show
                    mprogressBarUpdateProfile.setVisibility(View.VISIBLE);

                    //Then we have to set the new/old username and image to the user profile data
                    userProfileData muserProfile = new userProfileData(newName, firebaseAuth.getUid(), newDiagnose, newAge);
                    databaseReference.setValue(muserProfile);

                    //Then we have to update the username in the cloud firestore
                    updateNameCloudFirestore();

                    //Then we will display a message to let the user know the user have been updated
                    Toast.makeText(getApplicationContext(), "Profil opdateret", Toast.LENGTH_SHORT).show();

                    //When the action is done, the progressbar will disappear
                    mprogressBarUpdateProfile.setVisibility(View.INVISIBLE);

                    //When the user have updated there profile they will get back to the chat overview
                    Intent intent1 = new Intent(UpdateProfile.this, ChatOverview.class);
                    startActivity(intent1);
                    finish();

                }


            }
        });

        mnewUserImageInImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This will forward the user inside there image folder inside there phone
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                //Then we parse the image
                startActivityForResult(intent1, USER_IMAGE);
            }
        });

        //Before updating the image we have to show the user the previous image of the user
        storageReference = firebaseStorage.getReference();
        //The image path inside the storage in firebase storage
        //But we first have to get the download url to show the image and get the image token
        storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //We take the image token and turn it into a string
                ImageAccessToken = uri.toString();

                //Then parse it to picasso library
                Picasso.get().load(uri).into(mnewUserImageInImageView);
            }
        });




    }

    //To update the username in the could firestore
    private void updateNameCloudFirestore() {

        //To update all existing data that we want to update
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String , Object> userData = new HashMap<>();
        userData.put("name", newName);
        userData.put("image", ImageAccessToken);
        userData.put("uid", firebaseAuth.getUid());
        userData.put("status", "Online");

        //To let the user know that the name have been updated
        Toast.makeText(getApplicationContext(), "Navn er blevet opdateret", Toast.LENGTH_SHORT).show();


        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Profil opdateret", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //To update the user image in the storage
    private void updateUserImageToStorage() {

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
                        ImageAccessToken = uri.toString();
                        Toast.makeText(getApplicationContext(), "Billede gemt", Toast.LENGTH_SHORT).show();
                        //Push this to the cloud firestore
                        updateNameCloudFirestore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Billede blev ikke gemt", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(), "Billede opdateret", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Billede blev ikke sendt til skyen", Toast.LENGTH_SHORT).show();
            }
        });


    }


    //This is used to take the selected image and put it as the users profile image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == USER_IMAGE && resultCode == RESULT_OK){

            //Set the image path
            imagePath = data.getData();
            mnewUserImageInImageView.setImageURI(imagePath);
        }




        super.onActivityResult(requestCode, resultCode, data);
    }

    //Changes the status from offline to online when you open the app
    @Override
    protected void onStart() {
        super.onStart();

        //Get the path for the collection for Users using Uid to change the status
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());

        //Here we tell firebase that we want to update the status to online
        documentReference.update("status", "Aktiv").
                //We have added a success listener to check if everything is alright. If it is, it will show this message to let the user know
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Nu er du nu aktiv", Toast.LENGTH_SHORT).show();
                    }
                });



    }


    //Changes the status from online to offline when you close the app
    @Override
    protected void onStop() {
        super.onStop();

        //Get the path for the collection for Users using Uid to change the status
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());

        //Here we tell firebase that we want to update the status to offline
        documentReference.update("status", "Ikke aktiv").
                //We have added a success listener to check if everything is alright. If it is, it will show this message to let the user know
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Nu er du ikke længere aktiv", Toast.LENGTH_SHORT).show();
                    }
                });

    }



}