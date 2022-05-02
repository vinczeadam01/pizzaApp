package hu.mobil.pizzaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int RC_SIGN_IN = 1234;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.emailEditText = findViewById(R.id.emailEt);
        this.passwordEditText = findViewById(R.id.passwordEt);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(LOG_TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LOG_TAG, "signInWithCredential:success");
                    startMain();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    public void onLogin(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(email.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "Email vagy jelszó mező üres!", Toast.LENGTH_LONG).show();
            return;
        }

        // Log.i(LOG_TAG, "Bejelentkezett: " + userName + ", jelszó: " + password);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "User loged in successfully");
                    startMain();
                } else {
                    Log.d(LOG_TAG, "User log in fail");
                    Toast.makeText(LoginActivity.this, "User log in fail: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("firstname", "");
        intent.putExtra("lastname", "");
        startActivity(intent);
    }

    public void onLoginWFacebook(View view) {

    }

    public void onLoginWGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public void onGoSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}