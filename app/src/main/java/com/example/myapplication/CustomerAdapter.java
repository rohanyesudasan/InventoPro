package com.example.myapplication;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private List<CustomerModel> customerList;

    public CustomerAdapter(List<CustomerModel> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        CustomerModel model = customerList.get(position);
        holder.Customer_Name.setText("Customer Name: " + model.getCustomer_Name());
        holder.Phone_Number.setText("Phone Number: " + model.getPhone_Number());
        holder.Email.setText("Email: " + model.getEmail());
        holder.Address.setText("Address: " + model.getAddress());

        // Set up a click listener for the update button
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the product at the selected position
                    CustomerModel customer = customerList.get(position);

                    // Pass the product's ID to the UpdateProductActivity using the getter method
                    Intent intent = new Intent(view.getContext(), updatecustomer.class);
                    intent.putExtra("customer_id", customer.getID());

                    intent.putExtra("custmoer_name", customer.getCustomer_Name());
                    intent.putExtra("phone_number", customer.getPhone_Number());
                    intent.putExtra("email", customer.getEmail());
                    intent.putExtra("address", customer.getAddress());
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    // Add the setCustomer method to update the data in the adapter
    public void setCustomer(List<CustomerModel> customerList) {
        this.customerList = customerList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    // Other methods as before

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Customer_Name, Phone_Number, Email, Address;
        MaterialButton update;

        public ViewHolder(@NonNull View itemView, CustomerAdapter adapter) {
            super(itemView);
            adapter = adapter;
            Customer_Name = itemView.findViewById(R.id.Customer_Name);
            Phone_Number = itemView.findViewById(R.id.Customer_Phone_Number);
            Email = itemView.findViewById(R.id.Email);
            Address = itemView.findViewById(R.id.Address);
            update = itemView.findViewById(R.id.update); // Make sure you have an "update" button in your item layout
        }
    }
}
