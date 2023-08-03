package com.example.project;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class DataTransferService extends Service {

    private Timer dataTransferTimer;
    private DatabaseReference sourceReference;
    private DatabaseReference destinationReference;
    private long timeIntervalInMillis = 60 * 1000; // 24 hours in milliseconds

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase database references
        sourceReference = FirebaseDatabase.getInstance().getReference().child("Android Tutorials");
        destinationReference = FirebaseDatabase.getInstance().getReference().child("desiredNode");

        // Start the data transfer timer
        startDataTransferTimer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop the data transfer timer
        stopDataTransferTimer();
    }

    private void startDataTransferTimer() {
        // Create a timer task for data transfer
        TimerTask dataTransferTask = new TimerTask() {
            @Override
            public void run() {
                // Retrieve the data from the source node
                sourceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long currentTime = new Date().getTime();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

                        boolean dataTransferred = false; // Flag to track if any data was transferred

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String creationTimeString = childSnapshot.getKey();

                            try {
                                // Parse the creation time string into a Date object
                                Date creationTime = dateFormat.parse(creationTimeString);
                                long creationTimeMillis = creationTime.getTime();

                                // Check if the time interval has passed
                                if (currentTime - creationTimeMillis >= timeIntervalInMillis) {
                                    // Transfer the data to the destination node
                                    Object data = childSnapshot.getValue();
                                    destinationReference.child(childSnapshot.getKey()).setValue(data);

                                    // Remove the data from the source node
                                    childSnapshot.getRef().removeValue();

                                    dataTransferred = true; // Data was transferred
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        // Show a toast message indicating successful data transfer if data was transferred
                        if (dataTransferred) {
                            showToast("Data transferred successfully");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Show a toast message indicating data transfer cancellation or failure
                        showToast("Data transfer cancelled or failed");
                    }
                });
            }
        };

        // Schedule the data transfer task to run periodically
        dataTransferTimer = new Timer();
        dataTransferTimer.scheduleAtFixedRate(dataTransferTask, 0, 60 * 1000); // Run every 1 minute (adjust the interval as needed)
    }

    private void stopDataTransferTimer() {
        // Cancel the data transfer timer if it's running
        if (dataTransferTimer != null) {
            dataTransferTimer.cancel();
            dataTransferTimer = null;
        }
    }

    private void showToast(final String message) {
        // Show the toast message on the main UI thread
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DataTransferService.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
