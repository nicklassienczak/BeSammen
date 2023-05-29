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

// Nicklas

public class MessagesAdapterGroup extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<com.example.besammen.MessagesGroup> messagesArrayList;

    //This will help identify if the user is sending a message or not
    private int ITEM_SEND_GROUP = 1;
    private int ITEM_RECEIVE = 2;

    public MessagesAdapterGroup(Context context, ArrayList<com.example.besammen.MessagesGroup> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //If view type is equal to ITEM_SEND_GROUP this means that i am the sender
        if (viewType == ITEM_SEND_GROUP){

            //Then we have to attach the context of the message to the sender chat layout
            //And inflate it
            View view = LayoutInflater.from(context).inflate(R.layout.sender_group_chat_layout, parent, false);
            //This will bind the text to the sender view holder
            return new SenderViewHolderGROUP(view);
        }
        else {

            View view = LayoutInflater.from(context).inflate(R.layout.receiver_group_chat_layout, parent, false);

            return new ReceiverViewHolderGROUP(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //If the position equals the sender
        com.example.besammen.MessagesGroup messages = messagesArrayList.get(position);
        if (holder.getClass() == SenderViewHolderGROUP.class){
            //The sender view holder will receive this information

            SenderViewHolderGROUP senderViewHolder = (SenderViewHolderGROUP)holder;
            senderViewHolder.msenderMessage.setText(messages.getMessage());
            senderViewHolder.mtimeMessageSent.setText(messages.getCurrentTime());
            senderViewHolder.msenderNameOnMessage.setText(messages.getSenderName());
        }
        //If its not the sender, then it's the receiver
        else {
            ReceiverViewHolderGROUP receiverViewHolder = (ReceiverViewHolderGROUP)holder;
            receiverViewHolder.mreceiverMessage.setText(messages.getMessage());
            receiverViewHolder.mtimeMessageSent.setText(messages.getCurrentTime());
            receiverViewHolder.mreceiverNameOnMessage.setText(messages.getReceiverName());
        }
    }

    //This will identify who the sender is
    @Override
    public int getItemViewType(int position) {
        com.example.besammen.MessagesGroup messages = messagesArrayList.get(position);
        //If the current users id is euqual to the sender id then is the sender
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return ITEM_SEND_GROUP;
        }

        //If not it is the receiver
        else {
            return ITEM_RECEIVE;
        }


    }

    //This will allow there to be an unlimited amount of messages
    @Override
    public int getItemCount() {
        if (messagesArrayList != null) {
            return messagesArrayList.size();
        } else {
            return 0;
        }
    }


    //We have two layouts so thats why we need two view holder

    class SenderViewHolderGROUP extends RecyclerView.ViewHolder{

        //We have two things we need to show in our holder
        TextView msenderMessage, mtimeMessageSent, msenderNameOnMessage;


        public SenderViewHolderGROUP(@NonNull View itemView) {
            super(itemView);
            msenderMessage = itemView.findViewById(R.id.senderMessage);
            mtimeMessageSent = itemView.findViewById(R.id.timeMessageSent);
            msenderNameOnMessage = itemView.findViewById(R.id.senderNameOnMessage);

        }
    }

    class ReceiverViewHolderGROUP extends RecyclerView.ViewHolder{

        //We have two things we need to show in our holder
        TextView mreceiverMessage, mtimeMessageSent, mreceiverNameOnMessage;


        public ReceiverViewHolderGROUP(@NonNull View itemView) {
            super(itemView);
            mreceiverMessage = itemView.findViewById(R.id.receiverMessage);
            mtimeMessageSent = itemView.findViewById(R.id.timeMessageSent);
            mreceiverNameOnMessage = itemView.findViewById(R.id.receiverNameOnMessage);

        }
    }

}
