package com.example.besammen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GroupFragment extends Fragment {

    private EditText mgetMessageGroup;
    private TextView msendMessageGroup;

    //RecyclerView to show the messages
    private RecyclerView mrecyclerViewGroupChat;

    //This is used so store the text the user puts in the edittext before it gets sent to the sender chat layout
    private String inputMessageGroup, mnameOfReceivingUser, mnameOfSenderUser;

    //We need a intent because we are getting the data from our chat fragment
    Intent intent;

    //We need to be able to store the name and Uid of the receiver and the sender
    private String mreceiverName, mreceiverUid, msenderUid;

    //We need a string to hold the current time
    private String curretTime;
    private Calendar calendar;
    //To format the date
    private SimpleDateFormat simpleDateFormat;

    //We need to create a room for sender and receiver, because when working with database we need to store them seperately like this
    private String senderRoom, receiverRoom;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;

    private MessagesAdapterGroup messagesAdapterGroup;
    private ArrayList<MessagesGroup> messagesArrayList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //So we can find the elements inside chatFragment.java
        View viewGROUP = inflater.inflate(R.layout.groupfragment, container, false);

        mgetMessageGroup = viewGROUP.findViewById(R.id.getMessageGroup);
        msendMessageGroup = viewGROUP.findViewById(R.id.sendMessageGroup);
        mrecyclerViewGroupChat = viewGROUP.findViewById(R.id.recyclerViewGroupChat);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();
        //This is to change the format so that it is in hours and minuts, and the "a" is for 24 hour format
        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        //Who is the sender, sender Uid
        //This gets the Uid of the sender and stores it
        msenderUid = firebaseAuth.getUid();
        mreceiverUid = firebaseFirestore.collection("Users").getId();



        //Who is the receiver, receiving Uid
        //We are getting that Uid from the chat fragment
        //mreceiverUid = getIntent().getStringExtra("receiverUid");
        //Getting the receiver name from chat fragment
        //mreceiverName = getIntent().getStringExtra("name");

        //To take the data from the intent
        //intent = getIntent();


        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        firebaseFirestore.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<String> allUsersUid = new ArrayList<>();
                            List<String> allUsersNames = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()){
                                String userUid = document.getId();
                                String userName = document.getString("name");
                                if (!userUid.equals(currentUserUid)){
                                    allUsersUid.add(userUid);
                                    allUsersNames.add(userName);
                                }
                                if (userUid.equals(msenderUid)){
                                    mnameOfSenderUser = userName;
                                }
                                if (userUid.equals(mnameOfReceivingUser)){
                                    mnameOfReceivingUser = userName;
                                }
                            }
                            senderRoom = currentUserUid + allUsersUid.get(0);
                            receiverRoom = allUsersUid.get(0) + currentUserUid;





                            //The array list of all the messages
                            messagesArrayList = new ArrayList<>();

                            //To store all the messages inside the realtime database
                            DatabaseReference databaseReference = firebaseDatabase.getReference().child("groupChats").child(senderRoom).child("groupMessages");
                            messagesAdapterGroup = new MessagesAdapterGroup(getActivity(), messagesArrayList);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //Now we have to clear the previous arraylist
                                    messagesArrayList.clear();
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                        MessagesGroup messages = snapshot1.getValue(MessagesGroup.class);
                                        messagesArrayList.add(messages);
                                    }
                                    messagesAdapterGroup.notifyDataSetChanged();



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            //This is because we want to start recyclerview from reverse
                            linearLayoutManager.setStackFromEnd(true);
                            mrecyclerViewGroupChat.setLayoutManager(linearLayoutManager);
                            messagesAdapterGroup = new MessagesAdapterGroup(getActivity(), messagesArrayList);
                            mrecyclerViewGroupChat.setAdapter(messagesAdapterGroup);

                        }
                        else {
                            Toast.makeText(getContext(), "Kunne ikke finde brugeren", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





        //To change the name in the toolbar of the specific receiving user, we have to parse the name that is stored in the mreceivername


        //If the user clicks on the send text, the message will be sent
        msendMessageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Then we will take the message that we have stored in the input message
                inputMessageGroup = mgetMessageGroup.getText().toString();
                if (inputMessageGroup.isEmpty()){
                    //We will check if the user even have entered a message by checking if it's empty.
                    //If it is, this message will show
                    //Toast.makeText(getApplicationContext(), "Skriv venligst en besked først", Toast.LENGTH_SHORT).show();
                }
                else{
                    //This is date for timestamp
                    Date date = new Date();
                    //Currenttime will now store the current time in the specific time format that i have declared in the simple date format
                    curretTime = simpleDateFormat.format(calendar.getTime());

                    //Now we have to send this message
                    //This will create a new message through the message class, and all the necessary info is being parsed into that
                    MessagesGroup messages = new MessagesGroup(inputMessageGroup, firebaseAuth.getUid(), curretTime, mnameOfSenderUser, mnameOfReceivingUser, date.getTime());

                    //Now we have to store that on our database
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("groupChats")
                            //Here we have to create a room for the sender, which contain the sender name and sender Uid
                            .child(senderRoom)
                            .child("groupMessages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //If this happens successfully then we have to send this to the receiver though the receiverroom
                                    firebaseDatabase.getReference()
                                            .child("groupChats")
                                            .child(receiverRoom)
                                            .child("groupMessages")
                                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //Here i can display a toast to make sure the message has been sent
                                                    System.out.println("Beskeden er blevet sendt");


                                                    messagesAdapterGroup.notifyDataSetChanged();
                                                }
                                            });
                                }
                            });

                    //After the message have been sent, then we will clear the edittext
                    mgetMessageGroup.setText(null);


                }


            }
        });


        return viewGROUP;



    }

}
