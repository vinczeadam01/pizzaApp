package hu.mobil.pizzaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import hu.mobil.pizzaapp.adapters.FoodAdapter;
import hu.mobil.pizzaapp.models.Food;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Food> mItemsData;
    private FoodAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recycle view
        mRecyclerView = findViewById(R.id.recyclerView);

        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new FoodAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Get the data.
        initializeData();

    }


    public void switchToDetails(View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
    }

    private void initializeData() {
        mItemsData.clear();

        mItemsData.add(new Food("pizza", "Margarita", "pizzaszósz, sajt", 1590, "pizza-7.png"));
        mItemsData.add(new Food("pizza", "Sonkás", "pizzaszósz, sonka, sajt", 1790, "pizza-7.png"));
        mItemsData.add(new Food("pizza", "Sonka-gomba", "pizzaszósz, sonka, gomba, sajt", 1990, "pizza-7.png"));
        mItemsData.add(new Food("pizza", "Sonka-kukorica", "pizzaszósz, sonka, kukorica, sajt", 1990, "pizza-7.png"));

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }
}