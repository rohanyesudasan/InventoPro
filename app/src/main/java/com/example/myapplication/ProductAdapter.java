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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ProductModel> productList;

    public ProductAdapter(List<ProductModel> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel model = productList.get(position);

        // Set the product details in your TextViews as you are already doing

        holder.Product_Name.setText("Product Name: " + model.getProduct_Name());
        holder.Product_Description.setText("Product Description: " + model.getProduct_Description());
        holder.Selling_Price.setText("Selling Price: " + model.getSelling_Price());
        holder.Quantity.setText("Quantity: " + model.getQuantity());

        // Set up a click listener for the update button
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ProductModel product = productList.get(position);
                    Intent intent = new Intent(view.getContext(), update_product.class);
                    intent.putExtra("product_id", product.getID());
                    intent.putExtra("product_name", product.getProduct_Name());
                    intent.putExtra("product_description", product.getProduct_Description());
                    intent.putExtra("selling_price", product.getSelling_Price());
                    intent.putExtra("quantity", product.getQuantity());
                    view.getContext().startActivity(intent);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Add the setProducts method to update the data in the adapter
    public void setProducts(List<ProductModel> productList) {
        this.productList = productList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    // Other methods as before

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Product_Name, Product_Description, Selling_Price, Quantity;
        MaterialButton delete, update;

        public ViewHolder(@NonNull View itemView, ProductAdapter adapter) {
            super(itemView);
            adapter = adapter;
            Product_Name = itemView.findViewById(R.id.Product_Name);
            Product_Description = itemView.findViewById(R.id.Product_Description);
            Selling_Price = itemView.findViewById(R.id.Selling_Price);
            Quantity = itemView.findViewById(R.id.Quantity);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update); // Make sure you have an "update" button in your item layout

            // Set up a click listener for the delete button
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        productList.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
        }
    }
}
