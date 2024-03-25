package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class dashboard extends navDrawerActivity {
    CardView salescard, productcard,purchasecard,supplierCardView,customerCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setupNavDrawer(findViewById(R.id.drawerLayout));

        productcard = findViewById(R.id.productcard);
        purchasecard = findViewById(R.id.purchasecard);
        salescard = findViewById(R.id.salescard);
        supplierCardView = findViewById(R.id.supplierCardView);
        customerCardView = findViewById(R.id.customerCardView);
        productcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, product_list.class);
                startActivity(i);
                finish();
            }
        });
        purchasecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, purchase_list.class);
                startActivity(i);
                finish();
            }
        });
        salescard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, sales_list.class);
                startActivity(i);
                finish();
            }
        });
        supplierCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,Supplierlist.class);
                startActivity(i);
                finish();
            }
        });
        customerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, customer_list.class);
                startActivity(i);
                finish();
            }
        });

    }


}