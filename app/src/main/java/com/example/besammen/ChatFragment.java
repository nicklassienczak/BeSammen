package com.example.besammen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class ChatFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    private ImageView mimageViewUser;
    private FirestoreRecyclerAdapter<BeSammenModel, UserViewHolder> chatAdapter;
    private RecyclerView mrecyclerView;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //So we can find the elements inside chatFragment.java
        View view = inflater.inflate(R.layout.chatfragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mrecyclerView = view.findViewById(R.id.recyclerView);

        //Fetch all the users
        //Query query = firebaseFirestore.collection("Users");

        //To show all users except yourself
        Query query = firebaseFirestore.collection("Users").whereNotEqualTo("uid", firebaseAuth.getUid());
        FirestoreRecyclerOptions<BeSammenModel> allUserName = new FirestoreRecyclerOptions.Builder<BeSammenModel>().setQuery(query, BeSammenModel.class).build();

        chatAdapter = new FirestoreRecyclerAdapter<BeSammenModel, UserViewHolder>(allUserName) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i, @NonNull BeSammenModel beSammenModel) {

                //Get the name and show it on the chat layout
                userViewHolder.specificUserName.setText(beSammenModel.getName());
                String uri = beSammenModel.getImage();

                //Set profile picture from the user for others to see on there profile
                Picasso.get().load(uri).into(mimageViewUser);

                //Checking if the user is online
                if (beSammenModel.getStatus().equals("Aktiv")){
                    userViewHolder.mstatusOfUser.setText(beSammenModel.getStatus());
                    //Changing the color when online
                    userViewHolder.mstatusOfUser.setTextColor(Color.GREEN);
                }
                else {
                    userViewHolder.mstatusOfUser.setText(beSammenModel.getStatus());
                    userViewHolder.mstatusOfUser.setTextColor(Color.RED);
                }

                //What will happen when you click on a user
                userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //When click on specific user you go to the specific chat you have with that person
                        Intent intent = new Intent(getActivity(), SpecificChat.class);
                        //Parse data from beSammenModel to get the name the uid from the receiver and image
                        intent.putExtra("name", beSammenModel.getName());
                        intent.putExtra("receiverUid", beSammenModel.getUid());
                        intent.putExtra("imageUri", beSammenModel.getImage());
                        startActivity(intent);
                    }
                });



            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view_layout, parent, false);
                return new UserViewHolder(view1);


            }
        };


        mrecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerView.setLayoutManager(linearLayoutManager);
        mrecyclerView.setAdapter(chatAdapter);

        return view;



    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private TextView specificUserName;
        private TextView mstatusOfUser;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            specificUserName = itemView.findViewById(R.id.nameOfUser);
            mstatusOfUser = itemView.findViewById(R.id.statusOfUser);
            mimageViewUser = itemView.findViewById(R.id.imageViewUser);





        }
    }

    //All the users will show on start
    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    //If there are no users then don't listen
    @Override
    public void onStop() {
        super.onStop();
        if (chatAdapter != null){
            chatAdapter.stopListening();
        }
    }
}
