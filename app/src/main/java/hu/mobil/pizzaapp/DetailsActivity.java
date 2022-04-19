package hu.mobil.pizzaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
    private ImageView imgCppy;

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
        imgCppy = findViewById(R.id.imgcopy);

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

        itemImg.setImageResource(getResources().getIdentifier(data.getImageSrc(), "drawable", getPackageName()));
    }

    public void addToCart(View view) {
        for(int i = 0; i < counter; i++) {
            MainActivity.addToCart(new Food(data));
            cartAnimate();
        }
    }

    private void cartAnimate() {
        imgCppy.setImageDrawable(itemImg.getDrawable());
        imgCppy.setVisibility(View.VISIBLE);
        int[] dest = new int[2];
        findViewById(R.id.orderbtn).getLocationInWindow(dest);
        int[] start = new int[2];
        itemImg.getLocationInWindow(start);
        imgCppy.setX(start[0]);
        imgCppy.setY(start[1]);
        AnimatorSet animSetXY = new AnimatorSet();
        ObjectAnimator y = ObjectAnimator.ofFloat(imgCppy, "translationY", start[1]-300, dest[1]);
        //ObjectAnimator x = ObjectAnimator.ofFloat(imgCppy, "translationX", start[0], dest[0]);
        ObjectAnimator sy = ObjectAnimator.ofFloat(imgCppy, "scaleY", 0.6f, 0.01f);
        ObjectAnimator sx = ObjectAnimator.ofFloat(imgCppy, "scaleX", 0.6f, 0.01f);
        animSetXY.playTogether( y, sx, sy);
        animSetXY.setDuration(650);
        animSetXY.start();
        //mCopyImg.setVisibility(View.GONE);

    }
}