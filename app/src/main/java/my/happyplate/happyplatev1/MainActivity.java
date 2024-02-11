package my.happyplate.happyplatev1;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView postsRv;
    private Button loadMoreBtn;
    private EditText search;
    private ImageButton searchBtn;
    private String url="";
    private String href="";
    private boolean isSearch = false;
    private ArrayList<Model> postArrayList;
    private Adapter adapter;
    private ProgressDialog progressDialog;
    private static final String TAG = "MAIN_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.barreoutil);
        toolbar.setTitle("HappyPlate");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        toolbar.setTitleTextAppearance(this, R.style.CustomToolbarTitleStyle);
        setSupportActionBar(toolbar);

        loadMoreBtn = findViewById(R.id.bouttoncommand);
        postsRv = findViewById(R.id.poster);
        search = findViewById(R.id.rechercher);
        searchBtn = findViewById(R.id.buttonrecherch);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");

        postArrayList = new ArrayList<>();
        postArrayList.clear();

        loadPosts();

        loadMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = search.getText().toString().trim();
                if(TextUtils.isEmpty(query)){
                    loadPosts();
                }
                else {
                    searchPost(query);
                }
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                href="";
                url="";

                postArrayList = new ArrayList<>();
                postArrayList.clear();

                String query = search.getText().toString().trim();
                if(TextUtils.isEmpty(query)){
                    loadPosts();
                }
                else {
                    searchPost(query);
                }
            }
        });
    }

    private void searchPost(String query) {
        isSearch = true;
        Log.d(TAG,"search: isSearch: "+isSearch);

        progressDialog.show();
        if(href.equals("")){
            Log.d(TAG,"search: Next page is empty, no more page");
            url = "https://api.edamam.com/api/food-database/v2/parser?app_id=6e08ab92&app_key=aab16120125847d1e6c129277abbfa70&nutrition-type=cooking&category=packaged-foods"+query;
        }else{
            Log.d(TAG, "search: Next page:"+href);
            url=href;
        }
        Log.d(TAG,"search: URL:"+url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d(TAG, "onReponse" + response);

                try{
                    JSONObject jsonObject =new JSONObject(response);
                    try {
                        href = jsonObject.getJSONObject("_links").getJSONObject("next").getString("href");
                        Log.d(TAG,"onReponse: NextPage:"+href);

                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"Reached end of page...",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"onReponse: Reached end of page..."+e.getMessage());
                    }


                    JSONArray jsonArray = jsonObject.getJSONArray("hints");
                    for(int i=0;i<jsonArray.length();i++){
                        try {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String foodId = jsonObject1.getJSONObject("food").getString("foodId");
                            String label = jsonObject1.getJSONObject("food").getString("label");
                            String category = jsonObject1.getJSONObject("food").getString("category");
                            String categoryLabel = jsonObject1.getJSONObject("food").getString("categoryLabel");
                            String image = jsonObject1.getJSONObject("food").getString("image");

                            Model model = new Model(""+foodId,
                                    ""+label,
                                    ""+category,
                                    ""+image,
                                    ""+categoryLabel);

                            postArrayList.add(model);

                        }catch (Exception e){
                            Log.d(TAG,"onResponse: 1:"+e.getMessage());
                            Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    adapter = new Adapter(MainActivity.this,postArrayList);
                    postsRv.setAdapter(adapter);
                    progressDialog.dismiss();

                }catch (Exception e){

                    Log.d(TAG,"onReponse: 2:"+e.getMessage());
                    Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG,"onErrorReponse:"+error.getMessage());
                Toast.makeText(MainActivity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadPosts() {
        isSearch = false;
        Log.d(TAG,"load: isSearch: "+isSearch);

        progressDialog.show();
        if(href.equals("")){
            Log.d(TAG,"load: Next page is empty, no more page");
            url = "https://api.edamam.com/api/food-database/v2/parser?app_id=6e08ab92&app_key=aab16120125847d1e6c129277abbfa70&nutrition-type=cooking&category=packaged-foods";
        }else{
            Log.d(TAG, "load: Next page:"+href);
            url=href;
        }
        Log.d(TAG,"load: URL:"+url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d(TAG, "onReponse" + response);

                try{
                    JSONObject jsonObject =new JSONObject(response);
                    try {
                        href = jsonObject.getJSONObject("_links").getJSONObject("next").getString("href");
                        Log.d(TAG,"onReponse: NextPage:"+href);

                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"Reached end of page...",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"onReponse: Reached end of page..."+e.getMessage());
                        href = "end";
                    }


                    JSONArray jsonArray = jsonObject.getJSONArray("hints");
                    for(int i=0;i<jsonArray.length();i++){
                        try {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String foodId = jsonObject1.getJSONObject("food").getString("foodId");
                            String label = jsonObject1.getJSONObject("food").getString("label");
                            String category = jsonObject1.getJSONObject("food").getString("category");
                            String categoryLabel = jsonObject1.getJSONObject("food").getString("categoryLabel");
                            String image = jsonObject1.getJSONObject("food").getString("image");

                            Model model = new Model(""+foodId,
                                    ""+label,
                                    ""+category,
                                    ""+image,
                                    ""+categoryLabel);

                            postArrayList.add(model);

                        }catch (Exception e){
                            Log.d(TAG,"onResponse: 1:"+e.getMessage());
                            Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    adapter = new Adapter(MainActivity.this,postArrayList);
                    postsRv.setAdapter(adapter);
                    progressDialog.dismiss();

                }catch (Exception e){

                    Log.d(TAG,"onReponse: 2:"+e.getMessage());
                    Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG,"onErrorReponse:"+error.getMessage());
                Toast.makeText(MainActivity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);

        MenuItem item1 = menu.findItem(R.id.carte);
        MenuItem item2 = menu.findItem(R.id.sedeconnecter);
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, activityPanier.class);
                startActivity(intent);
                return true;
            }
        });

        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                confirm();
                return true;
            }
        });
        return false;
    }
    void confirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deconnexion");
        builder.setMessage("Confirmer la deconnexion?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
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
}