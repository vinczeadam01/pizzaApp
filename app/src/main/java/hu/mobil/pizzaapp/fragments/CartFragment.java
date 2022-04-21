package hu.mobil.pizzaapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hu.mobil.pizzaapp.MainActivity;
import hu.mobil.pizzaapp.R;
import hu.mobil.pizzaapp.adapters.CartAdapter;
import hu.mobil.pizzaapp.adapters.FoodAdapter;
import hu.mobil.pizzaapp.models.Food;


public class CartFragment extends Fragment {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Food> mItemsData;
    private CartAdapter mAdapter;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // recycle view
        mRecyclerView = view.findViewById(R.id.recyclerView);

        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new CartAdapter(view.getContext(), mItemsData);
        mRecyclerView.setAdapter(mAdapter);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Get the data.
        initializeData();
        return view;
    }

    private void initializeData() {
        mItemsData.clear();
        mItemsData.addAll(MainActivity.listCart());
    }
}