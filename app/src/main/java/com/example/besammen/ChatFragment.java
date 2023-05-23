package com.example.besammen;

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
    private FirestoreRecyclerAdapter<BeSammenModel, NoteViewHolder> chatAdapter;
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
       Query query = firebaseFirestore.collection("Users");
       FirestoreRecyclerOptions<BeSammenModel> allUserName = new FirestoreRecyclerOptions.Builder<BeSammenModel>().setQuery(query, BeSammenModel.class).build();

       chatAdapter = new FirestoreRecyclerAdapter<BeSammenModel, NoteViewHolder>(allUserName) {
           @Override
           protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull BeSammenModel beSammenModel) {

               //Get the name and show it on the chat layout
               noteViewHolder.specificUserName.setText(beSammenModel.getName());
               String uri = beSammenModel.getImage();

               //Set profile picture from the user for others to see on there profile
               Picasso.get().load(uri).into(mimageViewUser);

               //Checking if the user is online
               if (beSammenModel.getStatus().equals("Online")){
                   noteViewHolder.mstatusOfUser.setText(beSammenModel.getStatus());
                   //Changing the color when online
                   noteViewHolder.mstatusOfUser.setTextColor(Color.GREEN);
               }
               else {
                   noteViewHolder.mstatusOfUser.setText(beSammenModel.getStatus());
               }

               noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Toast.makeText(getActivity(), "Brugeren er blevet trykket på", Toast.LENGTH_SHORT).show();
                   }
               });



           }

           @NonNull
           @Override
           public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

               View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view_layout, parent, false);
               return new NoteViewHolder(view1);


           }
       };


       mrecyclerView.setHasFixedSize(true);
       linearLayoutManager = new LinearLayoutManager(getContext());
       linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
       mrecyclerView.setLayoutManager(linearLayoutManager);
       mrecyclerView.setAdapter(chatAdapter);

       return view;



    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView specificUserName;
        private TextView mstatusOfUser;


        public NoteViewHolder(@NonNull View itemView) {
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
