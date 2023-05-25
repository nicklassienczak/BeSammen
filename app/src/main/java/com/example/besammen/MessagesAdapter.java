package com.example.besammen;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Messages> messagesArrayList;

    //This will help identifi if the user is sending a message or not
    private int ITEM_SEND = 1;
    private int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //If view type is equal to ITEM_SEND this means that i am the sender
        if (viewType == ITEM_SEND){

            //Then we have to attach the context of the message to the sender chat layout
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chat_layout, parent, false);
            //This will bind the text to the sender view holder
            return new SenderViewHolder(view);
        }
        else {

            View view = LayoutInflater.from(context).inflate(R.layout.receiver_chat_layout, parent, false);

            return new ReceiverViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //If the position equals the sender
        Messages messages = messagesArrayList.get(position);
        if (holder.getClass() == SenderViewHolder.class){
            //The sender view holder will receive this information

            SenderViewHolder senderViewHolder = (SenderViewHolder)holder;
            senderViewHolder.msenderMessage.setText(messages.getMessage());
            senderViewHolder.mtimeMessageSent.setText(messages.getCurrentTime());
        }
        //If its not the sender, then it's the receiver
        else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder)holder;
            receiverViewHolder.msenderMessage.setText(messages.getMessage());
            receiverViewHolder.mtimeMessageSent.setText(messages.getCurrentTime());


        }





    }

    //This will identify who the sender is
    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        //If the current users id is euqual to the sender id then is the sender
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return ITEM_SEND;
        }

        //If not it is the receiver
        else {
            return ITEM_RECEIVE;
        }


    }

    //This will allow there to be an unlimited amount of messages
    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    //We have two layouts so thats why we need to view holder

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        //We have two things we need to show in our holder
        TextView msenderMessage, mtimeMessageSent;


        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            msenderMessage = itemView.findViewById(R.id.senderMessage);
            mtimeMessageSent = itemView.findViewById(R.id.timeMessageSent);

        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        //We have two things we need to show in our holder
        TextView msenderMessage, mtimeMessageSent;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            msenderMessage = itemView.findViewById(R.id.senderMessage);
            mtimeMessageSent = itemView.findViewById(R.id.timeMessageSent);

        }
    }



}
