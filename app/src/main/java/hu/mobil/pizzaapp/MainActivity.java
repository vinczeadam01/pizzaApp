package hu.mobil.pizzaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
    private static MainActivity thisActivity;
    public static MainActivity getAcitivity() {
        return thisActivity;
    }

    //Firebase
    private FirebaseUser mAuthUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mUserCollection;
    private User mUser;

    private boolean isDeleting = false;

    public static BadgeDrawable cartBadge;
    public static View cartIconView = null;

    BottomNavigationView bottomNavigationView;
    private static ArrayList<Food> cartArrayList = new ArrayList<>();

    // Fragments
    FoodsFragment productsFragment = new FoodsFragment(this);
    CartFragment cartFragment = new CartFragment();
    AccountFragment accountFragment = new AccountFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        String registredFirstname = "";
        String registredLastname = "";

        registredFirstname = bundle.get("firstname").toString();
        registredLastname = bundle.get("lastname").toString();

        thisActivity = this;

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
        userInit(registredFirstname, registredLastname);

        cartBadge = bottomNavigationView.getOrCreateBadge(R.id.cart);


    }

    private void userInit(String registredFirstname, String registredLastname) {
        mUserCollection.whereEqualTo("id", mAuthUser.getUid()).limit(1).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    mUser = document.toObject(User.class);
            }

            if(mUser == null) {
                mUser = new User(mAuthUser.getUid(), registredFirstname, registredLastname, mAuthUser.getEmail(), "", "default", new HashMap<String, Integer>());
                mUserCollection.document(mAuthUser.getUid()).set(mUser);;
            }

            cartBadge.setVisible(mUser.getCart().size() != 0);
            cartBadge.setNumber(mUser.getCart().size());

            accountFragment.updateUser();
        });

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        //back to Foods fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, productsFragment).commit();

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Kilépéshez lépj vissza mégegyszer!", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

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
        Button pizzaBtn = findViewById(R.id.pizza_btn);
        Button burgerBtn = findViewById(R.id.burger_btn);
        Button saladBtn = findViewById(R.id.salad_btn);
        Button drinkBtn = findViewById(R.id.drink_btn);


        switch(view.getId()) {
            case R.id.pizza_btn:
                catName = "pizza";
                pizzaBtn.setBackground(getDrawable(R.drawable.category_button_active));
                burgerBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                saladBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                drinkBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                break;
            case R.id.burger_btn:
                catName = "burger";
                pizzaBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                burgerBtn.setBackground(getDrawable(R.drawable.category_button_active));
                saladBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                drinkBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                break;
            case R.id.salad_btn:
                catName = "salad";
                pizzaBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                burgerBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                saladBtn.setBackground(getDrawable(R.drawable.category_button_active));
                drinkBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                break;
            case R.id.drink_btn:
                catName = "drink";
                pizzaBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                burgerBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                saladBtn.setBackground(getDrawable(R.drawable.category_button_inactive));
                drinkBtn.setBackground(getDrawable(R.drawable.category_button_active));
                break;
        }
        productsFragment.getAdapter().switchCategory(catName);
    }

    public void addToCart(Food food) {
        cartArrayList.add(new Food(food));
        int cartNum = cartBadge.getNumber();
        if (cartNum == 0)
            cartBadge.setVisible(true);
        cartBadge.setNumber(cartNum + 1);
    }

    public static ArrayList<Food> listCart() {
        return (ArrayList<Food>) cartArrayList.clone();
    }

    public void removeFromCart(Food delFood) {
        Iterator<Food> i = cartArrayList.iterator();
        while (i.hasNext()) {
            Food food = i.next(); // must be called before you can call i.remove()
            if(food.getName().equals(delFood.getName())) {
                i.remove();
                int cartNum = cartBadge.getNumber();
                if (cartNum == 1)
                    cartBadge.setVisible(true);
                cartBadge.setNumber(cartNum - 1);
                break;
            }
        }
    }

    public User getUser() {
        return this.mUser;
    }

    private void updateUser() {
        mUserCollection.document(mUser.getId()).set(mUser);
    }

    public void onDeleteAccount(View view) {
        isDeleting = true;
        mUserCollection.document(mUser.getId()).delete();
        mAuthUser.delete();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isDeleting)
            updateUser();
    }

    public void onLogout(View view) {
        finish();
    }

    public void cleanCart() {
        cartArrayList.clear();
        cartBadge.setNumber(0);
        cartFragment.rebuildAdapter();
    }
}