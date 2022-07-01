package com.example.projet;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    TextView fullName, email, phoneNumber;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private FirebaseFirestore db;
    Button mLoginBtn, btnAddArticle, btnLstArt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.textView4);
        fullName = findViewById(R.id.profileName);
        phoneNumber = findViewById(R.id.textView5);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        email.setText(fAuth.getCurrentUser().getEmail().toString());
        mLoginBtn = findViewById(R.id.logout);
        btnAddArticle = findViewById(R.id.addArticle);
        btnLstArt = findViewById(R.id.BtnLstArticle);
        db = FirebaseFirestore.getInstance();
        String id = FirebaseAuth.getInstance().getUid().toString();

        // retrieve connected user
        db.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fullName.setText(documentSnapshot.getString("fullName"));
                phoneNumber.setText(documentSnapshot.getString("phone"));
                // if connected as admin: you can add articles
                if (documentSnapshot.getString("role").equals("Admin"))
                    btnAddArticle.setVisibility(View.VISIBLE);
                else
                    btnAddArticle.setVisibility(View.INVISIBLE);
            }
        });

        // logout
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });

        // redirect to add article screen
        btnAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddArticle.class));

            }
        });

        // redirect to articles list
        btnLstArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListeArticle.class));

            }
        });
    }
}