package my.happyplate.happyplatev1;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class activityDetailPanier extends AppCompatActivity {

    private TextView titleTv;
    private TextView category;
    private TextView ingredient1;
    private TextView ingredient2;
    private TextView ingredient3;
    private TextView ingredient4;
    private TextView ingredient5;
    private String foodId;
    private ImageView imageView;
    private Button deleteFavorite;

    private RecyclerView detailMeasures;
    private DetailFoodAdapter detailFoodAdapter;
    private ArrayList<String> label2;
    private ArrayList<String> weight2;
    private String image1;
    private static  final String TAG = "DETAIL_TAG1";
    private androidx.appcompat.app.ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_panier);

        Toolbar toolbar = findViewById(R.id.barreoutil);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Détail du plat");
        toolbar.setTitleTextAppearance(this, R.style.CustomToolbarTitleStyle);
        actionBar.setDisplayHomeAsUpEnabled(true);

        titleTv = findViewById(R.id.titleTv);
        category = findViewById(R.id.categorie);
        ingredient1 = findViewById(R.id.ingredient1);
        ingredient2 = findViewById(R.id.ingredient2);
        ingredient3 = findViewById(R.id.ingredient3);
        ingredient4 = findViewById(R.id.ingredient4);
        ingredient5 = findViewById(R.id.ingredient5);
        imageView = findViewById(R.id.imageView);
        deleteFavorite = findViewById(R.id.deleteFavorite);
        detailMeasures = findViewById(R.id.detailMesures);
        label2 = new ArrayList<>();
        weight2 = new ArrayList<>();

        foodId = getIntent().getStringExtra("foodId");
        Log.d(TAG, "onCreate:"+foodId);

        loadDetail();

        deleteFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }
    private void loadDetail() {
        String url = "https://api.edamam.com/api/food-database/v2/parser?app_id=d3560df2&app_key=e4510b3ee676a353abcf1c290f3bef64&ingr="
                + foodId;
        Log.d(TAG, "loadDetail: URL" + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onReponse:" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray hintsArray = jsonObject.getJSONArray("hints");

                    if (hintsArray.length() > 0) {
                        JSONObject foodObject = hintsArray.getJSONObject(0).getJSONObject("food");

                        String loadedFoodId = foodObject.getString("foodId");
                        String category1 = foodObject.getString("category");
                        String label = foodObject.getString("label");
                        String ENERC_KCAL = foodObject.getJSONObject("nutrients").getString("ENERC_KCAL");
                        String PROCNT = foodObject.getJSONObject("nutrients").getString("PROCNT");
                        String FAT = foodObject.getJSONObject("nutrients").getString("FAT");
                        String CHOCDF = foodObject.getJSONObject("nutrients").getString("CHOCDF");
                        String FIBTG = foodObject.getJSONObject("nutrients").getString("FIBTG");

                        JSONArray measuresOject = hintsArray.getJSONObject(0).getJSONArray("measures");
                        for (int i = 0; i < measuresOject.length(); i++) {
                            try {
                                JSONObject jsonObject1 = measuresOject.getJSONObject(i);
                                String label1 = jsonObject1.getString("label");
                                String weight = jsonObject1.getString("weight");

                                label2.add(label1);
                                weight2.add(weight);
                            }catch (Exception e){
                                Log.d(TAG,"onResponse: 1:"+e.getMessage());
                                Toast.makeText(activityDetailPanier.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }

                        detailFoodAdapter = new DetailFoodAdapter(activityDetailPanier.this,label2,weight2);
                        detailMeasures.setAdapter(detailFoodAdapter);

                        image1 = foodObject.getString("image");
                        titleTv.setText(label);
                        category.setText(category1);
                        ingredient1.setText(ENERC_KCAL);
                        ingredient2.setText(PROCNT);
                        ingredient3.setText(FAT);
                        ingredient4.setText(CHOCDF);
                        ingredient5.setText(FIBTG);
                        Picasso.get().load(image1).placeholder(R.drawable.photo6).into(imageView);
                    }

                } catch (Exception e) {
                    Toast.makeText(activityDetailPanier.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activityDetailPanier.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    void confirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Supprimer");
        builder.setMessage("Confirmer la suppression");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(activityDetailPanier.this);
                myDatabaseHelper.delete(foodId);
                finish();
            }
        });
        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or perform any desired action
            }
        });
        builder.create().show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        setTitleColor(Color.WHITE);
        return true;
    }
}