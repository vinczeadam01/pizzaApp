package hu.mobil.pizzaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import hu.mobil.pizzaapp.fragments.AccountFragment;
import hu.mobil.pizzaapp.fragments.CartFragment;
import hu.mobil.pizzaapp.fragments.FoodsFragment;
import hu.mobil.pizzaapp.models.Food;
import hu.mobil.pizzaapp.models.User;


/*

    TODO: onDestroy save cart list to database

 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = MainActivity.class.getName();

    //Firebase
    private FirebaseUser mAuthUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mUserCollection;
    private User mUser;

    public static BadgeDrawable cartBadge;
    public static View cartIconView = null;

    BottomNavigationView bottomNavigationView;
    private static ArrayList<Food> cartArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check user if logged in
        mAuthUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mAuthUser != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.foods);

        cartIconView = findViewById(R.id.cart);

        mFirestore = FirebaseFirestore.getInstance();
        mUserCollection = mFirestore.collection("Users");
        userInit();

        cartBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);


    }

    private void userInit() {
        mUserCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                if(document.get("id").equals(mAuthUser.getUid())) {
                    mUser = document.toObject(User.class);
                }
            }

            if(mUser == null) {
                mUser = new User(mAuthUser.getUid(), "", "", mAuthUser.getEmail(), "photo", new HashMap<String, Integer>());
                mUserCollection.add(mUser);
            }

            cartBadge.setVisible(mUser.getCart().size() != 0);
            cartBadge.setNumber(mUser.getCart().size());
        });


    }

    FoodsFragment productsFragment = new FoodsFragment();
    CartFragment cartFragment = new CartFragment();
    AccountFragment accountFragment = new AccountFragment();



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.foods:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, productsFragment).commit();
                return true;
            case R.id.cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, cartFragment).commit();
                return true;
            case R.id.account:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();
                return true;
        }
        return false;
    }

    //TODO: set the inactive button back to white
    public void switchCategory(View view) {
        String catName = "";

        switch(view.getId()) {
            case R.id.pizza_btn:
                catName = "pizza";
                findViewById(R.id.pizza_btn).setBackground(getDrawable(R.drawable.bg_rounded_plusbtn));
                break;
            case R.id.burger_btn:
                catName = "burger";
                findViewById(R.id.burger_btn).setBackground(getDrawable(R.drawable.bg_rounded_plusbtn));
                break;
            case R.id.salad_btn:
                catName = "salad";
                findViewById(R.id.salad_btn).setBackground(getDrawable(R.drawable.bg_rounded_plusbtn));
                break;
            case R.id.drink_btn:
                catName = "drink";
                findViewById(R.id.drink_btn).setBackground(getDrawable(R.drawable.bg_rounded_plusbtn));
                break;
        }
        productsFragment.getAdapter().switchCategory(catName);
    }

    public static void addToCart(Food food) {
        cartArrayList.add(new Food(food));
        int cartNum = cartBadge.getNumber();
        if (cartNum == 0)
            cartBadge.setVisible(true);
        cartBadge.setNumber(cartNum + 1);
    }

    public static ArrayList<Food> listCart() {
        return (ArrayList<Food>) cartArrayList.clone();
    }

    public static void removeFromCart(Food delFood) {
        Iterator<Food> i = cartArrayList.iterator();
        while (i.hasNext()) {
            Food food = i.next(); // must be called before you can call i.remove()
            if(food.getName().equals(delFood.getName())) {
                i.remove();
                int cartNum = cartBadge.getNumber();
                if (cartNum == 1)
                    cartBadge.setVisible(true);
                cartBadge.setNumber(cartNum - 1);
            }
        }
    }
}