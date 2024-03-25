package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.cardview.widget.CardView;

public class staffdashboard extends navDrawerActivity {
    CardView salescard, productcard,purchasecard,supplierCardView,customerCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staffdashboard);

        setupNavDrawer(findViewById(R.id.drawerLayout));

        productcard = findViewById(R.id.productcard);

        salescard = findViewById(R.id.salescard);

        customerCardView = findViewById(R.id.customerCardView);
        productcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(staffdashboard.this, addproduct.class);
                startActivity(i);
                finish();
            }
        });

        salescard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(staffdashboard.this, addsales.class);
                startActivity(i);
                finish();
            }
        });
        customerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(staffdashboard.this, addcustomer.class);
                startActivity(i);
                finish();
            }
        });
    }


}