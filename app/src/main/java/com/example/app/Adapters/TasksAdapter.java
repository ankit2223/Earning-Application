package com.example.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.FlipCardActivity;
import com.example.app.GuessNumberActivity;
import com.example.app.LuckyActivity;
import com.example.app.Models.TasksModel;
import com.example.app.R;
import com.example.app.ReferActivity;
import com.example.app.ScratchCardActivity;
import com.example.app.WatchVideoActivity;
import com.example.app.databinding.RvTasksDesignBinding;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.viewHolder>{

    Context context;
    ArrayList<TasksModel>list;

    public TasksAdapter(Context context, ArrayList<TasksModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_tasks_design,parent,false);
        return new viewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        TasksModel model = list.get(position);

        holder.binding.tasksName.setText(model.getName());
        holder.binding.tasksImage.setImageResource(model.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==0){

                    Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                    
                } else if (position==1) {
                    Intent intent = new Intent(context, ScratchCardActivity.class);
                    context.startActivity(intent);
                }
                else if (position==2) {
                    Intent intent = new Intent(context, GuessNumberActivity.class);
                    context.startActivity(intent);
                }
                else if (position==3) {
                    Intent intent = new Intent(context, FlipCardActivity.class);
                    context.startActivity(intent);
                }
                else if (position==4) {
                    Intent intent = new Intent(context, LuckyActivity.class);
                    context.startActivity(intent);
                }
                else if (position==5) {
                    Intent intent = new Intent(context, WatchVideoActivity.class);
                    context.startActivity(intent);
                }
                else if (position==6) {
                    Intent intent = new Intent(context, ReferActivity.class);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RvTasksDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = RvTasksDesignBinding.bind(itemView);
        }
    }
}
