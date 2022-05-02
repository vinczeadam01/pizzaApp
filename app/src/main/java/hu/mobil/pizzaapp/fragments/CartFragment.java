package hu.mobil.pizzaapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import hu.mobil.pizzaapp.DetailsActivity;
import hu.mobil.pizzaapp.MainActivity;
import hu.mobil.pizzaapp.R;
import hu.mobil.pizzaapp.SummaryActivity;
import hu.mobil.pizzaapp.adapters.CartAdapter;
import hu.mobil.pizzaapp.adapters.FoodAdapter;
import hu.mobil.pizzaapp.models.Food;


public class CartFragment extends Fragment {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Food> mItemsData;
    private CartAdapter mAdapter;
    private TextView emptyCartTextView;
    private Button orderButton;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        emptyCartTextView = view.findViewById(R.id.emptycartTV);
        orderButton = view.findViewById((R.id.orderbutton));

        // recycle view
        mRecyclerView = view.findViewById(R.id.recyclerView);

        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new CartAdapter(view.getContext(), mItemsData, this);
        mRecyclerView.setAdapter(mAdapter);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Get the data.
        initializeData();


        checkEmptyCart(mItemsData.size());


        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.getAcitivity(), SummaryActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    public void checkEmptyCart(int size){
        if(size > 0) {
            emptyCartTextView.setVisibility(View.GONE);
            orderButton.setVisibility(View.VISIBLE);
        }

        else {
            emptyCartTextView.setVisibility(View.VISIBLE);
            orderButton.setVisibility(View.GONE);
        }
    }

    private void initializeData() {
        mItemsData.clear();
        mItemsData.addAll(MainActivity.listCart());
    }

    public void rebuildAdapter() {
        mItemsData.clear();
        mItemsData.addAll(MainActivity.listCart());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.checkEmptyCart(mItemsData.size());
    }
}