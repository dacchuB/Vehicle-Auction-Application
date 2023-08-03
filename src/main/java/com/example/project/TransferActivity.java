package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransferActivity extends AppCompatActivity {
    private RecyclerView transferRecyclerView;
    private List<DataModel> transferDataList;
    private TransferAdapter transferAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        transferRecyclerView = findViewById(R.id.transferRecyclerView);
        transferDataList = new ArrayList<>();
        transferAdapter = new TransferAdapter(transferDataList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        transferRecyclerView.setLayoutManager(layoutManager);
        transferRecyclerView.setAdapter(transferAdapter);

        // Call the method to retrieve and populate the transferred data
        retrieveTransferredData();
    }

    // Method to retrieve and populate the transferred data in the RecyclerView
    private void retrieveTransferredData() {
        DatabaseReference transferReference = FirebaseDatabase.getInstance().getReference().child("desiredNode");
        transferReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                transferDataList.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    DataModel data = childSnapshot.getValue(DataModel.class);
                    transferDataList.add(data);
                }
                transferAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the cancellation
                Toast.makeText(TransferActivity.this, "Data retrieval cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
