package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {
    private List<PurchaseModel> purchaseList;

    public PurchaseAdapter(List<PurchaseModel> purchaseList) {
        this.purchaseList = purchaseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_item, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseModel model = purchaseList.get(position);
        holder.Invoice_No.setText("Invoice No: " + model.getInvoice_No());
        holder.Supplier_ID.setText("Supplier Name: " + model.getSupplier_ID());
        holder.Product_ID.setText("Product Name: " + model.getProduct_ID());
        holder.Quantity.setText("Quantity: " + model.getQuantity());
        holder.Cost_Price.setText("Cost_Price: " + model.getCost_Price());
        holder.Amount.setText("Amount: " + model.getAmount());


        // Set up a click listener for the update button


    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    // Add the setProducts method to update the data in the adapter
    public void setPurchase(List<PurchaseModel> purchaseList) {
        this.purchaseList = purchaseList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    // Other methods as before

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  Invoice_No, Product_ID, Supplier_ID, Quantity,Cost_Price,Amount;

        public ViewHolder(@NonNull View itemView, PurchaseAdapter adapter) {
            super(itemView);
            adapter = adapter;
            Invoice_No = itemView.findViewById(R.id.Invoice_No);
            Supplier_ID = itemView.findViewById(R.id.Supplier_ID);
            Product_ID = itemView.findViewById(R.id.Product_ID);
            Quantity = itemView.findViewById(R.id.Quantity);
            Cost_Price = itemView.findViewById(R.id.Cost_Price);
            Amount = itemView.findViewById(R.id.Amount);




        }
    }
}
