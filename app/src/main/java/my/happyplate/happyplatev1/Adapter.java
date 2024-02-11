package my.happyplate.happyplatev1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.HolderPost> {
    private Context context;
    private ArrayList<Model> models;

    public Adapter(Context context, ArrayList<Model> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public HolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layoutproduit,parent,false);
        return new HolderPost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPost holder, int position) {
        Model model = models.get(position);

        String foodId = model.getFoodId();
        String label = model.getLabel();
        String category = model.getCategory();
        String categoryLabel = model.getCategoryLabel();


        try{
            String image = model.getImage();
            Picasso.get().load(image).placeholder(R.drawable.photo6).into(holder.image);
        }catch (Exception e){
            holder.image.setImageResource(R.drawable.photo6);
        }
        holder.title.setText(label);
        holder.content.setText(category);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activityArticleDetail.class);
                intent.putExtra("foodId",foodId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class HolderPost extends RecyclerView.ViewHolder{

        ImageButton moreBtn;
        TextView title, content;
        ImageView image;
        public HolderPost(@NonNull View itemView) {
            super(itemView);

            moreBtn = itemView.findViewById(R.id.button3);
            title = itemView.findViewById(R.id.titre);
            content = itemView.findViewById(R.id.contenu);
            image = itemView.findViewById(R.id.image);
        }
    }
}
