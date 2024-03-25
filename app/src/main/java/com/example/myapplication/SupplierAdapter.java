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

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {
    private List<SupplierModel> SupplierList;

    public SupplierAdapter(List<SupplierModel> SupplierList) {
        this.SupplierList = SupplierList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_item, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupplierModel model = SupplierList.get(position);

        holder.Supplier_Name.setText("Supplier Name: " + model.getSupplier_Name());
        holder.Supplier_Phone_Number.setText("Phone Number: " + model.getSupplier_Phone_Number());
        holder.Supplier_Email.setText("Email: " + model.getSupplier_Email());
        holder.Supplier_Address.setText("Address: " + model.getSupplier_Address());
        holder.Supplier_gstin.setText("GST Number: " + model.getSupplier_gstin());

        // Set up a click listener for the update button
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the product at the selected position
                    SupplierModel supplier = SupplierList.get(position);

                    // Pass the product's ID to the UpdateProductActivity using the getter method
                    Intent intent = new Intent(view.getContext(), update_supplier.class);
                    intent.putExtra("supplier_id", supplier.getID());
                    intent.putExtra("supplier_name",supplier.getSupplier_Name());
                    intent.putExtra("phone_number",supplier.getSupplier_Phone_Number());
                    intent.putExtra("email",supplier.getSupplier_Email());
                    intent.putExtra("address",supplier.getSupplier_Address());
                    intent.putExtra("gstin",supplier.getSupplier_gstin());
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return SupplierList.size();
    }

    // Add the setSupplier method to update the data in the adapter
    public void setSupplier(List<SupplierModel> SupplierList) {
        this.SupplierList = SupplierList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    // Other methods as before

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Supplier_Name, Supplier_Phone_Number, Supplier_Email, Supplier_Address, Supplier_gstin;
        MaterialButton update;

        public ViewHolder(@NonNull View itemView, SupplierAdapter adapter) {
            super(itemView);
            adapter = adapter;
            Supplier_Name = itemView.findViewById(R.id.Supplier_Name);
            Supplier_Phone_Number = itemView.findViewById(R.id.Supplier_Phone_Number);
            Supplier_Email = itemView.findViewById(R.id.Supplier_Email);
            Supplier_Address = itemView.findViewById(R.id.Supplier_Address);
            Supplier_gstin = itemView.findViewById(R.id.Supplier_gstin);
            update = itemView.findViewById(R.id.update); // Make sure you have an "update" button in your item layout
        }
    }
}
