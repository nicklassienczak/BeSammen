package com.example.besammen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class ChatOverview extends AppCompatActivity {

    private TabLayout tabLayout;
    private TabItem mchats, mgroups, mevents;
    private ViewPager viewPager;
    private androidx.viewpager.widget.PagerAdapter pagerAdapter;
    private androidx.appcompat.widget.Toolbar mtoolBar;

    private androidx.appcompat.widget.SearchView msearchViewChat;

    //Used to change the status of the user, if the user leaves the app on this activity
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_overview);
        viewPager = findViewById(R.id.fragmentContainer);

        tabLayout = findViewById(R.id.include);

        mchats = findViewById(R.id.chats);
        mgroups = findViewById(R.id.groups);
        mevents = findViewById(R.id.events);

        mtoolBar = findViewById(R.id.toolBar);
        setSupportActionBar(mtoolBar);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        //Set this adapter on our viewpager
        viewPager.setAdapter(pagerAdapter);
        msearchViewChat = findViewById(R.id.searchViewChat);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        //Set custom menu icon to access the toolbar
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.menu_white);
        mtoolBar.setOverflowIcon(drawable);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2){
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    }

    //This will redirect the user to either the profile or settings page, when clicked on the menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //If user click on either profile or settings
        switch (item.getItemId()){
            //Go to profile page
            case R.id.profile:
                Intent intent = new Intent(ChatOverview.this, ProfileActivity.class);
                startActivity(intent);
                break;

            //Go to settings page
            case R.id.settings:
                Toast.makeText(getApplicationContext(), "Du har trykket på indstillinger", Toast.LENGTH_SHORT).show();
                //Intent intent1 = new Intent(ChatOverview.this, SettingsActivity.class);
                //startActivity(intent1);
                break;
        }


        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);




        return true;
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