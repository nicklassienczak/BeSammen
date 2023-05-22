package com.example.besammen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    //This java class is only to display the users current profile data, all changes happens in updateProfile

    private EditText mviewUserName;
    private TextView mgoToUpdateProfile;
    private ImageView mviewUserImageInImageView;
    private ImageButton mbackButtonViewProfile;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    private StorageReference storageReference;

    //To fetch the users profile image with the image uri access token
    private String ImageAccessToken;

    private androidx.appcompat.widget.Toolbar mtoolBarViewProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mviewUserName = findViewById(R.id.viewUserName);
        mgoToUpdateProfile = findViewById(R.id.goToUpdateProfile);
        mviewUserImageInImageView = findViewById(R.id.viewUserImageInImageView);
        mbackButtonViewProfile = findViewById(R.id.backButtonViewProfile);
        mtoolBarViewProfile = findViewById(R.id.toolBarViewProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //This will store the image
        storageReference = firebaseStorage.getReference();
        //This is the image path
        //The getDownloadUrl() let us get the access token
        storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Store the image uri access token in this variable, turn it into a string
                ImageAccessToken = uri.toString();

                //We will here use picasso (image library) to use the image token
                //Picasso will get the uri, load it into the image view on the profile activity page
                Picasso.get().load(uri).into(mviewUserImageInImageView);
            }
        });

        //We store the user using Uid
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        //Then we will add the value
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //It will send our value inside our snapshot
                //To send the new value to the profile
                userProfileData muserProfileData = snapshot.getValue(userProfileData.class);
                mviewUserName.setText(muserProfileData.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //If somehting happened, this message will show
                Toast.makeText(getApplicationContext(), "Forbindelse til serveren utilgængelig", Toast.LENGTH_SHORT).show();

            }
        });

        //Go to update profile page
        mgoToUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UpdateProfile.class);
                startActivity(intent);
            }
        });

        //To set the toolbar as the actionbar for a more professional looking app
        setSupportActionBar(mtoolBarViewProfile);

        mbackButtonViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //This will automatically sent the user back to the chat page
                finish();
            }
        });



    }
}