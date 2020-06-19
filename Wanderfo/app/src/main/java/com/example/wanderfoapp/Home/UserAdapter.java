package com.example.wanderfoapp.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfoapp.Shop.ExampleItem;
import com.example.wanderfoapp.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<ExampleItem> mExampleList;
    private UserAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(UserAdapter.OnItemClickListener listener){
        mListener=listener;
    }

    public UserAdapter(ArrayList<ExampleItem> mExampleList) {
        this.mExampleList = mExampleList;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView itemVal;
        TextView itemValType;

        public UserViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemVal = itemView.findViewById(R.id.itemVal);
            itemValType = itemView.findViewById(R.id.itemValType);
        }
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view_item,parent, false);
        UserAdapter.UserViewHolder evh = new UserAdapter.UserViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.itemName.setText(currentItem.getName());
        holder.itemValType.setText(currentItem.getType());
        holder.itemVal.setText(currentItem.getType_value());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}







