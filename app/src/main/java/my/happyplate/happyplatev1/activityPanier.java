package my.happyplate.happyplatev1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.view.View;
import android.widget.Button;

public class activityPanier extends AppCompatActivity {


    MyDatabaseHelper myDatabaseHelper;
    private RecyclerView foodRv;
    private ArrayList<String> id;
    private ArrayList<String> title;
    private ArrayList<String> category;
    private ArrayList<String> image;
    private panierAdapter favoriteFoodAdapter;
    private androidx.appcompat.app.ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        Toolbar toolbar = findViewById(R.id.barreoutil);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextAppearance(this, R.style.CustomToolbarTitleStyle);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Panier");
        actionBar.setDisplayHomeAsUpEnabled(true);

        myDatabaseHelper = new MyDatabaseHelper(activityPanier.this);

        id = new ArrayList<>();
        title = new ArrayList<>();
        category = new ArrayList<>();
        image = new ArrayList<>();
        foodRv = findViewById(R.id.article);

        showData();

        favoriteFoodAdapter = new panierAdapter(activityPanier.this,this, id,title, category, image);
        foodRv.setAdapter(favoriteFoodAdapter);
        Button validateOrderButton = findViewById(R.id.validerButton);
        validateOrderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(activityPanier.this, "Commande validée", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void showData(){
        Cursor cursor = myDatabaseHelper.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(activityPanier.this, "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                category.add(cursor.getString(2));
                image.add(cursor.getString(3));
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}