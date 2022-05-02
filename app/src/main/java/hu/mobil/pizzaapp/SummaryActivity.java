package hu.mobil.pizzaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import hu.mobil.pizzaapp.models.Food;
import hu.mobil.pizzaapp.models.Order;
import hu.mobil.pizzaapp.models.User;

public class SummaryActivity extends AppCompatActivity {

    private TableLayout table;
    private ArrayList<Food> cartList;
    private int priceSum = 0;

    private EditText lastnameEditText;
    private EditText firstnameEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private CheckBox checkbox;

    private NotificationHandler mNotificatinHandeler;

    private User mUser;

    //Firebase
    private FirebaseFirestore mFirestore;
    private CollectionReference mOrderCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        mUser = MainActivity.getAcitivity().getUser();

        cartList = MainActivity.listCart();
        table = findViewById(R.id.list_table);

        mNotificatinHandeler = new NotificationHandler(this);

        tableInit();

        lastnameEditText = findViewById(R.id.summ_lastname);
        firstnameEditText = findViewById(R.id.summ_firstname);
        emailEditText = findViewById(R.id.summ_email);
        addressEditText = findViewById(R.id.summ_address);
        checkbox = findViewById(R.id.save_form_check_box);

        formInit();

        mFirestore = FirebaseFirestore.getInstance();
        mOrderCollection = mFirestore.collection("Orders");
    }

    private void tableInit() {
        for (Food food : cartList) {
            priceSum += food.getPrice();

            TableRow tr = new TableRow(this);

            TextView label = new TextView(this);
            label.setText(food.getName());
            tr.addView(label);

            TextView price = new TextView(this);
            price.setGravity(Gravity.RIGHT);
            price.setText(Integer.toString(food.getPrice()) + " Ft");
            tr.addView(price);

            table.addView(tr);
        }

        View hr = findViewById(R.id.sumHR);
        table.removeView(hr);
        table.addView(hr);

        TableRow tr = new TableRow(this);

        TextView label = new TextView(this);
        label.setText("Összesen:");
        tr.addView(label);

        TextView price = new TextView(this);
        price.setGravity(Gravity.RIGHT);
        price.setText(Integer.toString(priceSum) + " Ft");
        tr.addView(price);

        table.addView(tr);

    }

    private void formInit() {
        lastnameEditText.setText(mUser.getLastName());
        firstnameEditText.setText(mUser.getFirstName());
        emailEditText.setText(mUser.getEmail());
        addressEditText.setText(mUser.getAddress());
    }

    private void saveForm() {
        mUser.setFirstName(firstnameEditText.getText().toString());
        mUser.setLastName(lastnameEditText.getText().toString());
        mUser.setAddress(addressEditText.getText().toString());
    }

    public void order(View view) {
        mNotificatinHandeler.send("Sikeres rendelés! A futár nemsokára kopogtat!!");

        mOrderCollection.add(new Order(mUser.getId(), Order.list2string(cartList), priceSum));

        MainActivity.getAcitivity().cleanCart();
        if (checkbox.isChecked())
            saveForm();
        finish();
    }
}