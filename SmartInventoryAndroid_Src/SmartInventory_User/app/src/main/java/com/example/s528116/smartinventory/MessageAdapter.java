package com.example.s528116.smartinventory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessagesViewHolder> {
    private ArrayList<MessageContainer> messageListArray;
    private Context context;

    public MessageAdapter(ArrayList<MessageContainer> messageListArray, Context context){
        this.messageListArray = messageListArray;
        this.context = context;
    }


    public static class MessagesViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, fromTV, toTV, sendDateTV;
        public LinearLayout messagesLL;


        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTV);
            fromTV = itemView.findViewById(R.id.fromTV);
            toTV = itemView.findViewById(R.id.toTV);
            sendDateTV = itemView.findViewById(R.id.sendDateTV);

            messagesLL = itemView.findViewById(R.id.messagesLL);

        }
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_container, viewGroup, false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(v);
        return messagesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder messagesViewHolder, int i) {
        final MessageContainer currentMessage = messageListArray.get(i);
        final String dateComposed = FormatDate.getDate(currentMessage.getComposeDate());

        messagesViewHolder.titleTV.setText("Title : "+ currentMessage.getTitle());
        messagesViewHolder.fromTV.setText("Sender : "+ currentMessage.getFrom());
        messagesViewHolder.toTV.setText("To : "+ currentMessage.getTo());
        messagesViewHolder.sendDateTV.setText(("Date : "+ dateComposed));

        messagesViewHolder.messagesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, Message.class);
                in.putExtra("from", currentMessage.getFrom());
                in.putExtra("to", currentMessage.getTo());
                in.putExtra("composedDate", dateComposed);
                in.putExtra("message", currentMessage.getMessage());
                in.putExtra("title", currentMessage.getTitle());
                in.putExtra("msgDocId", currentMessage.getMessageDocId());
                in.putExtra("userEmail", currentMessage.getFrom());
                context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return  messageListArray.size();
    }


}
