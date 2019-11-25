package com.example.assignmentapi.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignmentapi.Models.DataModel;
import com.example.assignmentapi.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<DataModel> models=null;

    public DataAdapter(List<DataModel> models){
        this.models = models;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.api_item, parent, false);
        return new ViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String image=models.get(position).getUrl();
        String name=models.get(position).getName();
        String age=models.get(position).getAge();
        String location=models.get(position).getLocation();

        holder.setData(image,name,age,location);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name,tv_age,tv_location;
        private CircleImageView imageView;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.name);
            tv_age = itemView.findViewById(R.id.age);
            tv_location = itemView.findViewById(R.id.location);
        }

        private void setData(String image,String name,String age, String location ){


            //Setting image from url
            Glide.with(itemView.getContext()).load(image).placeholder(R.drawable.placeholder_icon).into(imageView);

            //setting name
            tv_name.setText(name);

            //setting age
            tv_age.setText(age);

            //setting location
            tv_location.setText(location);
        }


    }
}
