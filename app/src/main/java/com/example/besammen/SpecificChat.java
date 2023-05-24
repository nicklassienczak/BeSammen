package com.example.besammen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SpecificChat extends AppCompatActivity {

    private EditText mgetMessage;
    private ImageButton mbackButtonSpecificChat;
    private androidx.appcompat.widget.Toolbar mtoolBarSpecificChat;
    private ImageView mreceiverUserImageInImageView;
    private TextView msendMessage, mnameOfReceivingUser;

    //RecyclerView to show the messages
    private RecyclerView mrecyclerViewSpecificChat;

    //This is used so store the text the user puts in the edittext before it gets sent to the sender chat layout
    private String inputMessage;

    //We need a intent because we are getting the data from our chat fragment
    private Intent intent;

    //We need to be able to store the name and Uid of the receiver and the sender
    private String senderName, mreceiverName, mreceiverUid, msenderUid;

    //We need a string to hold the current time
    private String curretTime;
    private Calendar calendar;
    //To format the date
    private SimpleDateFormat simpleDateFormat;

    //We need to create a room for sender and receiver, because when working with database we need to store them seperately like this
    private String senderRoom, receiverRoom;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private MessagesAdapter messagesAdapter;
    private ArrayList<Messages> messagesArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_chat);

        mgetMessage = findViewById(R.id.getMessage);
        mbackButtonSpecificChat = findViewById(R.id.backButtonSpecificChat);
        mtoolBarSpecificChat = findViewById(R.id.toolBarSpecificChat);
        mreceiverUserImageInImageView = findViewById(R.id.receiverUserImageInImageView);
        msendMessage = findViewById(R.id.sendMessage);
        mnameOfReceivingUser = findViewById(R.id.nameOfReceivingUser);
        mrecyclerViewSpecificChat = findViewById(R.id.recyclerViewSpecificChat);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        calendar = Calendar.getInstance();
        //This is to change the format so that it is in hours and minuts, and the "a" is for 24 hour format
        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        //Who is the sender, sender Uid
        //This gets the Uid of the sender and stores it
        msenderUid = firebaseAuth.getUid();

        //Who is the receiver, receiving Uid
        //We are getting that Uid from the chat fragment
        mreceiverUid = getIntent().getStringExtra("receiverUid");
        //Getting the receiver name from chat fragment
        mreceiverName = getIntent().getStringExtra("name");

        //To create the rooms for the sender, we have to get the sender and receiver Uid
        senderRoom = msenderUid + mreceiverUid;
        receiverRoom = mreceiverUid + msenderUid;

        //The array list of all the messages
        messagesArrayList = new ArrayList<>();

        //To store all the messages inside the realtime database
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("contactChats").child(senderRoom).child("messages");
        messagesAdapter = new MessagesAdapter(SpecificChat.this, messagesArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Now we have to clear the previous arraylist
                messagesArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Messages messages = snapshot1.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //This is because we want to start recyclerview from reverse
        linearLayoutManager.setStackFromEnd(true);
        mrecyclerViewSpecificChat.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(SpecificChat.this, messagesArrayList);
        mrecyclerViewSpecificChat.setAdapter(messagesAdapter);

        //To change the name in the toolbar of the specific receiving user, we have to parse the name that is stored in the mreceivername
        mnameOfReceivingUser.setText(mreceiverName);
        //To parse the image access token, we will create a local string that holds that
        String uri = intent.getStringExtra("imageUri");
        //To make sure the uri is not empty, we will set a if statement. If the uri is empty that could cause the app to crash
        if (uri.isEmpty()){
            Toast.makeText(getApplicationContext(), "Kunne ikke hente billedet", Toast.LENGTH_SHORT).show();
        }
        else {
            //This will load the profile picture of the specific receiving using onto the toolbar next to their name
            Picasso.get().load(uri).into(mreceiverUserImageInImageView);
        }







        //If the user clicks on the back button then we simple finish this activity
        mbackButtonSpecificChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //If the user clicks on the send text, the message will be sent
        msendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Then we will take the message that we have stored in the input message
                inputMessage = mgetMessage.getText().toString();
                if (inputMessage.isEmpty()){
                    //We will check if the user even have entered a message by checking if it's empty.
                    //If it is, this message will show
                    Toast.makeText(getApplicationContext(), "Skriv venligst en besked først", Toast.LENGTH_SHORT).show();
                }
                else{
                    //This is date for timestamp
                    Date date = new Date();
                    //Currenttime will now store the current time in the specific time format that i have declared in the simple date format
                    curretTime = simpleDateFormat.format(calendar.getTime());

                    //Now we have to send this message
                    //This will create a new message through the message class, and all the necessary info is being parsed into that
                    Messages messages = new Messages(inputMessage, firebaseAuth.getUid(), curretTime, date.getTime());

                    //Now we have to store that on our database
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("contactChats")
                            //Here we have to create a room for the sender, which contain the sender name and sender Uid
                            .child(senderRoom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //If this happens successfully then we have to send this to the receiver though the receiverroom
                                    firebaseDatabase.getReference()
                                            .child("contactChats")
                                            .child(receiverRoom)
                                            .child("messages")
                                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //Here i can display a toast to make sure the message has been sent
                                                }
                                            });
                                }
                            });

                    //After the message have been sent, then we will clear the edittext
                    mgetMessage.setText(null);


                }


            }
        });



        //To take the data from the intent
        intent = getIntent();

        //Set the toolbar as the action bar
        setSupportActionBar(mtoolBarSpecificChat);
        mtoolBarSpecificChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Here we can make it so that if the user clicks on the toolbar they will be able to see the user they are
                //they are chatting with
                Toast.makeText(getApplicationContext(), "Personen er blevet trykket på", Toast.LENGTH_SHORT).show();
            }
        });



    }
}