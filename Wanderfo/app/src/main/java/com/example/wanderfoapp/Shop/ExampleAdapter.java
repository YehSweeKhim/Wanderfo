package com.example.wanderfoapp.Shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderfoapp.R;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView typeTV;
        public TextView type_valueTV;
        public TextView nameTV;
        public Button deletebutton;
        public Button editbutton;

        public ExampleViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            typeTV = itemView.findViewById(R.id.type);
            type_valueTV=itemView.findViewById(R.id.type_value);
            nameTV=itemView.findViewById(R.id.name);
            deletebutton=itemView.findViewById(R.id.deletebutton);
            editbutton = itemView.findViewById(R.id.editbutton);

            deletebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            editbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> examplelist){
        mExampleList = examplelist;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_element,parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.typeTV.setText(currentItem.getType());
        holder.type_valueTV.setText(currentItem.getType_value());
        holder.nameTV.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
