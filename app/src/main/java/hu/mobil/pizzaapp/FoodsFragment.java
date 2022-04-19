package hu.mobil.pizzaapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import hu.mobil.pizzaapp.adapters.FoodAdapter;
import hu.mobil.pizzaapp.models.Food;


public class FoodsFragment extends Fragment {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Food> mItemsData;
    private FoodAdapter mAdapter;

    private ImageView mCopyImg; //Image fly to cart

    public FoodsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foods, container, false);

        //Image fly to cart
        mCopyImg = view.findViewById(R.id.copy_img);

        // recycle view
        mRecyclerView = view.findViewById(R.id.recyclerView);

        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new FoodAdapter(view.getContext(), mItemsData, mCopyImg);
        mRecyclerView.setAdapter(mAdapter);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        // Get the data.
        initializeData();



        return view;
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