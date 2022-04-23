package hu.mobil.pizzaapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import hu.mobil.pizzaapp.MainActivity;
import hu.mobil.pizzaapp.R;
import hu.mobil.pizzaapp.adapters.FoodAdapter;
import hu.mobil.pizzaapp.models.Food;
import hu.mobil.pizzaapp.models.User;


public class FoodsFragment extends Fragment {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Food> mItemsData;
    private ArrayList<Food> mItemsDataAll;
    private FoodAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private MainActivity mMainContext;

    private ImageView mCopyImg; //Image fly to cart
    private SearchView searchbar;
    private HorizontalScrollView categoriesBar;

    public FoodsFragment(MainActivity context) {
        mMainContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foods, container, false);

        categoriesBar = view.findViewById(R.id.categoriesBar);
        searchbar = view.findViewById(R.id.searchbar);
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length() > 0) {
                    categoriesBar.setVisibility(View.GONE);
                }
                else {
                    categoriesBar.setVisibility(View.VISIBLE);
                }
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        //Image fly to cart
        mCopyImg = view.findViewById(R.id.copy_img);

        // recycle view
        mRecyclerView = view.findViewById(R.id.recyclerView);

        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();
        mItemsDataAll = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new FoodAdapter(view.getContext(), mItemsData, mCopyImg, mMainContext);
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
                mItemsDataAll.add(item);
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

        mItems.add(new Food("burger", "Sima", "hamburgerhús, saláta, paradicsom, szósz", 1650, "burger_basic"));
        mItems.add(new Food("burger", "Sajtos", "hamburgerhús, saláta, paradicsom, sajt, szósz", 1790, "burger_cheese"));
        mItems.add(new Food("burger", "Baconos", "hamburgerhús, saláta, paradicsom,sajt, bacon szósz", 2190, "burger_bacon"));

        mItems.add(new Food("salad", "Paradicsomos", "saláta, paradicsom, dresszing", 1390, "salad_tomato"));
        mItems.add(new Food("salad", "Cézár", "saláta, csirkemell, sajt, kenyér", 1590, "salad_caesar"));
        mItems.add(new Food("salad", "Csirkés", "saláta, csirkemell, paradicsom, uborka, lilahagyma, sajt", 1880, "salad_chicken"));

        mItems.add(new Food("drink", "Cola", "", 490, "drink_cola"));
        mItems.add(new Food("drink", "Fanta", "", 490, "drink_fanta"));
        mItems.add(new Food("drink", "Sprite", "", 490, "drink_sprite"));
        mItems.add(new Food("drink", "Víz", "", 350, "drink_water"));
    }

    public void switchCategory(String catName) {

    }

    public FoodAdapter getAdapter() {
        return mAdapter;
    }
}