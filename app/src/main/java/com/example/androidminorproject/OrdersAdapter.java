package com.example.androidminorproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> ordersList;

    public OrdersAdapter(List<Order> ordersList) {
        this.ordersList = ordersList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = ordersList.get(position);

        holder.orderId.setText("Order ID: " + order.getOrderId());
        holder.utrNumber.setText("UTR: " + order.getUtrNumber());
        holder.totalAmount.setText("Total: ₹" + order.getTotal());
        holder.date.setText("Date: " + order.getDate());
        holder.status.setText("Status: " + order.getStatus());

        // Display product details inside the order
        StringBuilder productDetails = new StringBuilder();
        for (int i = 0; i < order.getProductNames().size(); i++) {
            productDetails.append(order.getProductNames().get(i))
                    .append(" (Qty: ").append(order.getProductQuantities().get(i))
                    .append(") - ₹").append(order.getProductTotals().get(i))
                    .append("\n");
        }

        holder.productsList.setText(productDetails.toString());
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, utrNumber, totalAmount, date, status, productsList;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderIdTextView);
            utrNumber = itemView.findViewById(R.id.utrTextView);
            totalAmount = itemView.findViewById(R.id.totalTextView);
            date = itemView.findViewById(R.id.dateTextView);
            status = itemView.findViewById(R.id.statusTextView);
            productsList = itemView.findViewById(R.id.productsTextView); // Make sure this exists in order_item.xml
        }
    }
}
