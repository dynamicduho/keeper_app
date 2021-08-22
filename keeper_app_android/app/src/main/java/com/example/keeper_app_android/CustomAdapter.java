package com.example.keeper_app_android;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<String> merchant_names;
    ArrayList<String> receiptIDs;
    ArrayList<Double> total_prices;
    Context context;


    public CustomAdapter(Context context, ArrayList<String> merchant_names, ArrayList<String> receiptIDs, ArrayList<Double> total_prices) {
        this.context = context;
        this.merchant_names = merchant_names;
        this.receiptIDs = receiptIDs;
        this.total_prices = total_prices;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate item layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // set the data in items
        holder.merchant_name.setText(merchant_names.get(holder.getBindingAdapterPosition()));
        holder.receipt_id.setText(receiptIDs.get(holder.getBindingAdapterPosition()));
        holder.total_paid.setText(String.valueOf(total_prices.get(holder.getBindingAdapterPosition())));
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, merchant_names.get(holder.getBindingAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return merchant_names.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView merchant_name, receipt_id, total_paid;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            merchant_name = (TextView) itemView.findViewById(R.id.merchant_name);
            receipt_id = (TextView) itemView.findViewById(R.id.receipt_id);
            total_paid = (TextView) itemView.findViewById(R.id.total_paid);

        }
    }

}
