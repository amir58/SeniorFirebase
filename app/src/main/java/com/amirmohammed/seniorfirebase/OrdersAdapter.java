package com.amirmohammed.seniorfirebase;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderHolder> {

    List<OrderData> orderDataList;
    OrdersI ordersI;

    public OrdersAdapter(List<OrderData> orderDataList, OrdersI ordersI) {
        this.orderDataList = orderDataList;
        this.ordersI = ordersI;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        OrderData orderData = orderDataList.get(position);

        holder.textViewOrderDescription.setText(orderData.getDescription());
        holder.textViewOrderDate.setText(orderData.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersI.onOrderClick(orderData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDataList.size();
    }


    class OrderHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderDescription, textViewOrderDate;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderDate = itemView.findViewById(R.id.item_order_tv_date);
            textViewOrderDescription = itemView.findViewById(R.id.item_order_tv_description);
        }
    }

    interface OrdersI{
        void onOrderClick(OrderData orderData);
    }
}
