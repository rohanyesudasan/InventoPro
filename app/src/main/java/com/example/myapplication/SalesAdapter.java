package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.ViewHolder> {
    private List<SalesModel> salesList;

    public SalesAdapter(List<SalesModel> salesList) {
        this.salesList = salesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_item, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalesModel model = salesList.get(position);
        holder.Product_ID.setText("Product Name: " + model.getProduct_ID());
        holder.Customer_ID.setText("Customer Name: " + model.getCustomer_ID());
        holder.Quantity.setText("Quantity: " + model.getQuantity());
        holder.Amount.setText("Amount: " + model.getAmount());
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public void setSales(List<SalesModel> salesList) {
        this.salesList = salesList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Product_ID, Customer_ID, Quantity, Amount;

        public ViewHolder(@NonNull View itemView, SalesAdapter adapter) {
            super(itemView);
            Product_ID = itemView.findViewById(R.id.Product_ID);
            Customer_ID = itemView.findViewById(R.id.Customer_ID);
            Quantity = itemView.findViewById(R.id.Quantity);
            Amount = itemView.findViewById(R.id.Amount);
        }
    }
}
