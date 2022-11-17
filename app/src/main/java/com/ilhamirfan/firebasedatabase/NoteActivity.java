package com.ilhamirfan.firebasedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoteActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView emailView,  uidView;
    private EditText titleInput, descriptionInput;
    private Button logoutButton, submitButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance("https://papb-modul-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference();

        emailView = findViewById(R.id.tv_email);
        uidView = findViewById(R.id.tv_uid);
        logoutButton = findViewById(R.id.btn_keluar);

        titleInput = findViewById(R.id.et_title);
        descriptionInput = findViewById(R.id.et_description);
        submitButton = findViewById(R.id.btn_submit);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnRequestLogout();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnAddNotes();
            }
        });
    }

    private void OnRequestLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent mainActivity = new Intent(NoteActivity.this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivity);
    }

    private void OnAddNotes() {
        String title = titleInput.getText().toString();
        String desc = descriptionInput.getText().toString();

        Note note = new Note(title, desc);

        Log.v("Test", note.toString());

        databaseReference.child("Notes").child(mAuth.getUid()).push().setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(NoteActivity.this, "Add data", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NoteActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            emailView.setText(currentUser.getEmail());
            uidView.setText(currentUser.getUid());
        }
    }
}