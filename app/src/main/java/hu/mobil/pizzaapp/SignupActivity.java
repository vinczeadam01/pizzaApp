package hu.mobil.pizzaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignupActivity.class.getName();
    private FirebaseAuth mAuth;

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordConfirmEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        this.firstNameEditText = findViewById(R.id.firstnameEt);
        this.lastNameEditText = findViewById(R.id.lastnameEt);
        this.emailEditText = findViewById(R.id.emailEt);
        this.passwordEditText = findViewById(R.id.passwordEt);
        this.passwordConfirmEditText = findViewById(R.id.passwordConfirmEt);
    }

    public void onSignup(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConfirm = passwordConfirmEditText.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a megerősítése.");
            Toast.makeText(SignupActivity.this, "Jelszavak nem egyeznek!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "User created successfully");
                    startMain();
                } else {
                    Log.d(LOG_TAG, "User wasn't created successfully");
                    Toast.makeText(SignupActivity.this, "User was't created successfully: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBackLogin(View view) {
        super.onBackPressed();
    }
}
