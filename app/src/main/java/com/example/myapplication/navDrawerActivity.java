package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class navDrawerActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navView;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("shelfiq", MODE_PRIVATE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Clear the shared preference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Navigate to login activity
        Intent intent = new Intent(navDrawerActivity.this, login.class);
        startActivity(intent);
        finish();  // Finish the current activity
    }

    private boolean navMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuProfile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menuLogout:
                logout();
                return true;

            case R.id.menuPurchaseInsert:
//                Toast.makeText(this, "Insert Purchase", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(navDrawerActivity.this, addpurchase.class);
                startActivity(i);
                return true;

            case R.id.menuPurchaseView:
                // Toast.makeText(this, "View Purchase", Toast.LENGTH_SHORT).show();
                Intent a = new Intent(navDrawerActivity.this, addpurchase.class);
                startActivity(a);

                return true;

            case R.id.menuSalesInsert:
                Intent f = new Intent(navDrawerActivity.this, addsales.class);
                startActivity(f);
//
//                Toast.makeText(this, "Insert Sales", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menuSalesView:
                Intent h = new Intent(navDrawerActivity.this, addsales.class);
                startActivity(h);

                // Toast.makeText(this, "View Sales", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menuCustomersInsert:
                Intent b = new Intent(navDrawerActivity.this, addcustomer.class);
                startActivity(b);

                //Toast.makeText(this, "Insert Customers", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menuCustomersView:
                Intent s = new Intent(navDrawerActivity.this, addcustomer.class);
                startActivity(s);
//
                return true;

            case R.id.menuSupplierInsert:
                Intent k = new Intent(navDrawerActivity.this, addsupplier.class);
                startActivity(k);

                //Toast.makeText(this, "Insert Supplier", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menuSupplierView:
                Intent c = new Intent(navDrawerActivity.this, addsupplier.class);
                startActivity(c);

                return true;

            default:
                return false;
        }
    }

    protected void setupNavDrawer(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navMenuItemSelected(item);
            }
        });
    }
}