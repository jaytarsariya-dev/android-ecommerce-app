package com.example.androidminorproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<ContactData> contactList;
    private Context context;

    public ContactAdapter(Context context, List<ContactData> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactData contact = contactList.get(position);

        // Display email and message
        holder.emailTextView.setText("Email: " + contact.getEmail());
        holder.messageTextView.setText("Message: " + contact.getMessage());

        holder.removeButton.setOnClickListener(v -> {
            String email = contact.getEmail();

            // Remove the item from Firestore
            FirebaseFirestore.getInstance().collection("ContactUs")
                    .document(email)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Remove the item from the list and notify the adapter
                        contactList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, contactList.size());
                        Toast.makeText(context, "Contact removed successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to remove contact: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        holder.replyButton.setOnClickListener(v -> {
            // Handle reply button click
            showReplyDialog(contact);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private void showReplyDialog(ContactData contact) {
        // Inflate the custom dialog layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_reply, null);

        // Get references to the dialog's views
        TextView messageTextView = dialogView.findViewById(R.id.messageText);
        EditText replyEditText = dialogView.findViewById(R.id.replyEditText);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button sendButton = dialogView.findViewById(R.id.sendButton);

        // Set the message in the dialog
        messageTextView.setText("Message: " + contact.getMessage());

        // Create the dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(dialogView);

        // Create the AlertDialog
        android.app.AlertDialog dialog = builder.create();

        // Set the Cancel button click listener
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Set the Send button click listener
        sendButton.setOnClickListener(v -> {
            String replyMessage = replyEditText.getText().toString().trim();

            if (!replyMessage.isEmpty()) {
                // Add the reply to Firestore in the same document using email as the document ID
                FirebaseFirestore.getInstance().collection("ContactUs")
                        .document(contact.getEmail())
                        .update("reply", replyMessage)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Reply sent", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(context, "Error sending reply: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(context, "Please write a reply", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        dialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView, messageTextView;
        Button removeButton, replyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
            replyButton = itemView.findViewById(R.id.replyButton);
        }
    }
}
