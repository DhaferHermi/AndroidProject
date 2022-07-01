package com.example.projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddArticle extends AppCompatActivity {
    EditText et1, et2;
    TextView tv1;
    Button btnAdd;
    private String uTitle, uDesc, uId;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("add data");
        et1 = findViewById(R.id.editText);
        et2 = findViewById(R.id.editText2);
        tv1 = findViewById(R.id.textView7);
        btnAdd = findViewById(R.id.BtnAdd);
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uTitle = bundle.getString("title");
            uId = bundle.getString("id");
            uDesc = bundle.getString("description");
            et1.setText(uTitle);
            et2.setText(uDesc);
            actionBar.setTitle("Update Article");
            btnAdd.setText("Update");
            tv1.setText("Update Article");
        } else {
            btnAdd.setText("Save");
        }

        // event listener to send data from ui to controller
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et1.getText().toString();
                String Description = et2.getText().toString();
                Bundle bundle1 = getIntent().getExtras();
                // if bundle is present; we updating an existing article
                if (bundle1 != null) {
                    String id = uId;
                    saveToFireStore(id, title, Description);
                }
                // no bundle: means we are adding new article
                else {
                    String id = UUID.randomUUID().toString();
                    uploadData(title, Description);
                }
            }
        });
    }

    // updates the data in the firebase
    private void saveToFireStore(String id, String title, String desc) {
        if (!title.isEmpty() && !desc.isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("title", title);
            map.put("description", desc);
            db.collection("Articles").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), ListeArticle.class));
                                Toast.makeText(AddArticle.this, "Data Saved !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddArticle.this, "Failed !!", Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(this, "Empty Fields not Allowed", Toast.LENGTH_SHORT).show();
    }

    // add the new article to firebase
    private void uploadData(String title, String description) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("description", description);
        db.collection("Articles").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(AddArticle.this, "Complete", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddArticle.this, "Failure", Toast.LENGTH_LONG).show();
                    }
                });
    }
}