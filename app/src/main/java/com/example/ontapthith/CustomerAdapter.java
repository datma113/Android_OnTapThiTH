package com.example.ontapthith;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<Customer> list = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public CustomerAdapter(List<Customer> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_list, parent, false);
        return new CustomerViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.CustomerViewHolder holder, int position) {
        Customer c = list.get(position);
        holder.tvName.setText(c.getName());
        holder.tvAddress.setText(c.getAddress());

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendingData sendingData = (SendingData) context;
                sendingData.sendData(c);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress;
        CustomerAdapter customerAdapter;
        ImageButton imageButton;
        public CustomerViewHolder(@NonNull View view, CustomerAdapter customerAdapter) {
            super(view);
            tvName = view.findViewById(R.id.rcv_tvName);
            tvAddress = view.findViewById(R.id.rcv_tvAddress);
            imageButton = view.findViewById(R.id.rcv_ibtnUpdate);
            this.customerAdapter = customerAdapter;

        }
    }
}
