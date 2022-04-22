package hu.mobil.pizzaapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import hu.mobil.pizzaapp.LoginActivity;
import hu.mobil.pizzaapp.MainActivity;
import hu.mobil.pizzaapp.R;
import hu.mobil.pizzaapp.models.User;


public class AccountFragment extends Fragment {
    private User mUser;
    private ImageView profileImageView;
    private ImageButton addImageBtn;
    private EditText lastnameEditText;
    private EditText firstnameEditText;
    private EditText emailEditText;
    private EditText addressEditText;

    public AccountFragment() {}



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        profileImageView = view.findViewById(R.id.profileImageView);
        addImageBtn = view.findViewById(R.id.add_image_button);
        lastnameEditText = view.findViewById(R.id.acc_lastname);
        firstnameEditText = view.findViewById(R.id.acc_firstname);
        emailEditText = view.findViewById(R.id.acc_email);
        addressEditText = view.findViewById(R.id.acc_address);

        updateUser();
        if(mUser != null)
            initForm();

        return view;
    }



    private void initForm() {
        lastnameEditText.setText(mUser.getLastName());
        firstnameEditText.setText(mUser.getFirstName());
        emailEditText.setText(mUser.getEmail());
        addressEditText.setText(mUser.getAddress());
    }

    public void updateUser() {
        mUser = MainActivity.getAcitivity().getUser();
    }

    private void updateUserData() {
        if(mUser == null)
            return;
        mUser.setFirstName(firstnameEditText.getText().toString());
        mUser.setLastName(lastnameEditText.getText().toString());
        mUser.setEmail(emailEditText.getText().toString());
        mUser.setAddress(addressEditText.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateUserData();
        Toast.makeText(MainActivity.getAcitivity(), "Adatok sikeresen mentve! ", Toast.LENGTH_SHORT).show();
    }
}