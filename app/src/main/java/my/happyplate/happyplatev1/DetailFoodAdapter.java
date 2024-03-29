package my.happyplate.happyplatev1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailFoodAdapter extends RecyclerView.Adapter<DetailFoodAdapter.MyViewHolder>{
    private Context context;
    private ArrayList label, weight;

    public DetailFoodAdapter(Context context, ArrayList label, ArrayList weight) {
        this.context = context;
        this.label = label;
        this.weight = weight;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layoutdetail,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.label.setText(String.valueOf(label.get(position)));
        holder.weight.setText(String.valueOf(weight.get(position)));
    }

    @Override
    public int getItemCount() {
        return label.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView label, weight;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);
            weight = itemView.findViewById(R.id.weight);
        }
    }
}
