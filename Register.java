package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatting-c0e17-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText name = findViewById(R.id.nameBox);
        final EditText mobile = findViewById(R.id.phoneBox);
        final EditText email = findViewById(R.id.emailBox);
        final Button registerBtn = findViewById(R.id.bregistertn);

        // ProgressDialog is deprecated, consider using ProgressBar or AlertDialog with a loading indicator

        if (MemoryData.getData(this).isEmpty()) {
            Intent intent = new Intent(Register.this, ChattingActivity.class);
            intent.putExtra("mobile", MemoryData.getData(this));
            intent.putExtra("name", MemoryData.getName(this));
            intent.putExtra("email", "");
            startActivity(intent);
            finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ProgressDialog is deprecated, consider using ProgressBar or AlertDialog with a loading indicator

                final String nameTxt = name.getText().toString();
                final String mobileTxt = mobile.getText().toString();
                final String emailText = email.getText().toString();

                if (nameTxt.isEmpty() || mobileTxt.isEmpty() || emailText.isEmpty()) {
                    Toast.makeText(Register.this, "All Fields Required!!", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("users").hasChild(mobileTxt)) {
                                Toast.makeText(Register.this, "Mobile already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("users").child(mobileTxt).child("email").setValue(emailText);
                                databaseReference.child("users").child(mobileTxt).child("name").setValue(nameTxt);
                                databaseReference.child("users").child(mobileTxt).child("profile_pic").setValue("");

                                MemoryData.saveData(mobileTxt, Register.this);
                                MemoryData.saveName(nameTxt, Register.this);

                                Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Register.this, ChattingActivity.class);
                                intent.putExtra("mobile", mobileTxt);
                                intent.putExtra("name", nameTxt);
                                intent.putExtra("email", emailText);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle error
                        }
                    });
                }
            }
        });
    }
}
