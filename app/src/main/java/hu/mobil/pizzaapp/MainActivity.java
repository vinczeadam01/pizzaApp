package hu.mobil.pizzaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import hu.mobil.pizzaapp.models.Food;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = MainActivity.class.getName();

    BottomNavigationView bottomNavigationView;
    public static ArrayList<Food> cartArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.foods);

        cartArrayList.clear();

    }
    FoodsFragment productsFragment = new FoodsFragment();
    CartFragment cartFragment = new CartFragment();
    AccountFragment accountFragment = new AccountFragment();


    public void switchToDetails(View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
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
}