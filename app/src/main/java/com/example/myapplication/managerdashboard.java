package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.cardview.widget.CardView;

public class managerdashboard extends navDrawerActivity {
    CardView salescard, productcard,purchasecard,supplierCardView,customerCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerdashboard);

        setupNavDrawer(findViewById(R.id.drawerLayout));

        productcard = findViewById(R.id.productcard);
        purchasecard = findViewById(R.id.purchasecard);
        salescard = findViewById(R.id.salescard);
        supplierCardView = findViewById(R.id.supplierCardView);
        customerCardView = findViewById(R.id.customerCardView);
        productcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(managerdashboard.this, product_list.class);
                startActivity(i);
                finish();
            }
        });
        purchasecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(managerdashboard.this, purchase_list.class);
                startActivity(i);
                finish();
            }
        });
        salescard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(managerdashboard.this, sales_list.class);
                startActivity(i);
                finish();
            }
        });
        supplierCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(managerdashboard.this,Supplierlist.class);
                startActivity(i);
                finish();
            }
        });
        customerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(managerdashboard.this, addcustomer.class);
                startActivity(i);
                finish();
            }
        });

    }


}