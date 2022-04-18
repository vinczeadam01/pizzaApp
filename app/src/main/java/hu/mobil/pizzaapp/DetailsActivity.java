package hu.mobil.pizzaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import hu.mobil.pizzaapp.models.Food;

public class DetailsActivity extends AppCompatActivity {
    private TextView counterTextView;
    private TextView itemName;
    private TextView itemDescription;
    private TextView itemPrice;
    private ImageView itemImg;

    private Food data;

    private int counter = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        data = (Food) getIntent().getSerializableExtra("data");

        counterTextView = findViewById(R.id.counterNum);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemPrice = findViewById(R.id.itemPrice);
        itemImg = findViewById(R.id.itemImg);

        dataUpdate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void decreaseCounter(View view) {
        counter = Math.max(Integer.parseInt(counterTextView.getText().toString()) - 1, 1);
        counterTextView.setText(Integer.toString(counter));

    }

    public void increaseCounter(View view) {
        counter = Integer.parseInt(counterTextView.getText().toString()) + 1;
        counterTextView.setText(Integer.toString(counter));
    }

    public void onBack(View view) {
        onBackPressed();
    }

    private void dataUpdate() {
        itemName.setText(data.getName());
        itemDescription.setText(data.getDescription());
        itemPrice.setText(Integer.toString(data.getPrice()) + " Ft");
    }

    public void addToCart(View view) {
        for(int i = 0; i < counter; i++) {
            MainActivity.addToCart(new Food(data));
        }
    }
}