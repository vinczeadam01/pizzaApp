package hu.mobil.pizzaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private TextView counterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        counterTextView = findViewById(R.id.counterNum);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void decreaseCounter(View view) {
        counterTextView.setText(Integer.toString(Math.max(Integer.parseInt(counterTextView.getText().toString()) - 1, 1)));

    }

    public void increaseCounter(View view) {
        counterTextView.setText(Integer.toString(Integer.parseInt(counterTextView.getText().toString()) + 1));
    }

    public void onBack(View view) {
        onBackPressed();
    }
}