package com.example.project;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private ImageButton sellerButton;
    private ImageButton buyerButton;
    private ImageButton transferButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sellerButton = findViewById(R.id.sellerButton);
        buyerButton = findViewById(R.id.buyerButton);
        transferButton = findViewById(R.id.transferButton);

        sellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle seller button click
                Intent intent = new Intent(MenuActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        buyerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle buyer button click
                Intent intent = new Intent(MenuActivity.this,  MainActivity.class);
                startActivity(intent);
            }
        });

        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle transfer button click
                Intent intent = new Intent(MenuActivity.this, TransferActivity.class);
                startActivity(intent);
            }
        });
    }
}

