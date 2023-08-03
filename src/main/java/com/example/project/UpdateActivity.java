package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateActivity extends AppCompatActivity {

    private ImageView updateImage;
    private Button updateButton;
    private EditText updateLang;

    private String key, imageUrl, oldImageURL;
    private Uri uri;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.updateButton);
        updateImage = findViewById(R.id.updateImage);
        updateLang = findViewById(R.id.updateLang);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(UpdateActivity.this).load(bundle.getString("Image")).into(updateImage);
            updateLang.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
        }

        if (key != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials").child(key);
        } else {
            // Handle the case when the key is null
            Toast.makeText(UpdateActivity.this, "Invalid key", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity or take appropriate action
        }

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to select and update the image if needed
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceString = updateLang.getText().toString().trim();
                if (!priceString.isEmpty()) {
                    int newPrice = Integer.parseInt(priceString);

                    // Retrieve the current price value from the database
                    databaseReference.child("dataPrice").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                DataSnapshot dataSnapshot = task.getResult();
                                if (dataSnapshot.exists()) {
                                    int currentPrice = dataSnapshot.getValue(Integer.class);

                                    // Compare the new price with the current price
                                    if (newPrice > currentPrice) {
                                        // Update only if the new price is greater than the current price
                                        databaseReference.child("dataPrice").setValue(newPrice)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // Get the current user's ID
                                                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                                            if (currentUser != null) {
                                                                String userId = currentUser.getUid();

                                                                // Fetch the user details from the Realtime Database using the user ID
                                                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if (snapshot.exists()) {
                                                                            User user = snapshot.getValue(User.class);
                                                                            if (user != null) {
                                                                                String username = user.getUsername();

                                                                                // Add the username as the highest bidder under the current node
                                                                                databaseReference.child("highestBidder").setValue(username)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    Toast.makeText(UpdateActivity.this, "Price Updated. Highest bidder: " + username, Toast.LENGTH_SHORT).show();
                                                                                                    finish();
                                                                                                } else {
                                                                                                    Toast.makeText(UpdateActivity.this, "Failed to update price", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            } else {
                                                                                Toast.makeText(UpdateActivity.this, "User data is null", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        } else {
                                                                            Toast.makeText(UpdateActivity.this, "User data does not exist", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                        Toast.makeText(UpdateActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            } else {
                                                                Toast.makeText(UpdateActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(UpdateActivity.this, "Failed to update price", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(UpdateActivity.this, "New price must be greater than the current price", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(UpdateActivity.this, "Price data does not exist", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UpdateActivity.this, "Failed to retrieve price data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(UpdateActivity.this, "Please enter a price amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
