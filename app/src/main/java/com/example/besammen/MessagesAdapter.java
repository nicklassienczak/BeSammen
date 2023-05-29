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
//Arne

public class MessagesAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Messages> messagesArrayList;

    //This will help identify if the user is sending a message or not
    private int ITEM_SEND = 1;
    private int ITEM_RECEIVE = 2;

    //This is a constructor, where we assign the two parameters to the correct values
    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    //To assign the messages to the correct chat layout
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
            //To attach the context of the message to the receiver chat layout
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_chat_layout, parent, false);
            //This will bind the text to the receiver view holder
            return new ReceiverViewHolder(view);

        }
    }

    //To delcare which viewHolder the message should be posted in. If position equals to 1, then it's the sender, if not it's the receiver
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //If the position equals the sender
        Messages messages = messagesArrayList.get(position);
        if (holder.getClass() == SenderViewHolder.class){
            //The sender view holder will receive this information

            //We get the data from the Messages.java
            SenderViewHolder senderViewHolder = (SenderViewHolder)holder;
            senderViewHolder.msenderMessage.setText(messages.getMessage());
            senderViewHolder.mtimeMessageSent.setText(messages.getCurrentTime());
        }
        //If its not the sender, then it's the receiver
        else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder)holder;
            receiverViewHolder.mreceiverMessage.setText(messages.getMessage());
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

    //We have two layouts so thats why we need two view holder

    //We only want the message the user have sent and the timestamp the message was sent on to display that on the message aswell.
    class SenderViewHolder extends RecyclerView.ViewHolder{

        //We have two things we need to show in our holder
        TextView msenderMessage, mtimeMessageSent;


        //This is used to bind the variables to the textviews inside the chat sender layout
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            msenderMessage = itemView.findViewById(R.id.senderMessage);
            mtimeMessageSent = itemView.findViewById(R.id.timeMessageSent);

        }
    }

    //We only want the message the receiver have sent and the timestamp the message was sent on to display that on the message aswell.
    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        //We have two things we need to show in our holder
        TextView mreceiverMessage, mtimeMessageSent;


        //This is used to bind the variables to the textviews inside the chat receiver layout
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            mreceiverMessage = itemView.findViewById(R.id.receiverMessage);
            mtimeMessageSent = itemView.findViewById(R.id.timeMessageSent);

        }
    }

}
