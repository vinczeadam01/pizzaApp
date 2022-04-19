package hu.mobil.pizzaapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import hu.mobil.pizzaapp.adapters.FoodAdapter;
import hu.mobil.pizzaapp.models.Food;


public class FoodsFragment extends Fragment {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Food> mItemsData;
    private FoodAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

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
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Foods");
        queryData();

        return view;
    }

    private void queryData() {
        mItemsData.clear();
        mItems.orderBy("name").limit(20).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Food item = document.toObject(Food.class);
                mItemsData.add(item);
            }

            if (mItemsData.size() == 0) {
                initializeData();
                queryData();
            }

            // Notify the adapter of the change.
            mAdapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        mItems.add(new Food("pizza", "Margarita", "pizzaszósz, sajt", 1590, "pizza_6"));
        mItems.add(new Food("pizza", "Sonkás", "pizzaszósz, sonka, sajt", 1790, "pizza_8"));
        mItems.add(new Food("pizza", "Sonka-gomba", "pizzaszósz, sonka, gomba, sajt", 1990, "pizza_7"));
        mItems.add(new Food("pizza", "Sonka-kukorica", "pizzaszósz, sonka, kukorica, sajt", 1990, "pizza_7"));
        mItems.add(new Food("pizza", "Kolbászos", "pizzaszósz, kolbász, sajt", 1890, "pizza_3"));
        mItems.add(new Food("pizza", "Hawaii", "pizzaszósz, sonka, ananász, sajt", 1990, "pizza_1"));
        mItems.add(new Food("pizza", "Bolognai", "bolognai ragu, darálthús, sajt", 1990, "pizza_6"));
        mItems.add(new Food("pizza", "Csípős", "csípős pizzaszósz, szalámi, sajt", 1990, "pizza_3"));
    }
}