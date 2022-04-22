package hu.mobil.pizzaapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

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

    // PhotoUpdate
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    private FirebaseStorage storage;
    private StorageReference storageReference;

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

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // on pressing btnSelect SelectImage() is called
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }

        });

        return view;
    }



    private void initForm() {
        lastnameEditText.setText(mUser.getLastName());
        firstnameEditText.setText(mUser.getFirstName());
        emailEditText.setText(mUser.getEmail());
        addressEditText.setText(mUser.getAddress());

        if(!mUser.getPhoto().equals("default")) {
            try {
                StorageReference imgStorageRef = storage.getReference(mUser.getPhoto());
                imgStorageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(this.getContext()).load(uri).into(profileImageView));
            } catch (Exception e) {
                Toast.makeText(this.getContext(), "Nem sikerült a kép betöltése!", Toast.LENGTH_SHORT);
            }
        }
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



    // PROFILE IMAGE UPLOAD
    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data){
        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == -1
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                this.getContext().getContentResolver(),
                                filePath);
                profileImageView.setImageBitmap(bitmap);
                uploadImage();
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle("Feltöltés...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + mUser.getId());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(AccountFragment.this.getContext(),
                                                    "Kép sikeresen feltöltve!!",
                                                    Toast.LENGTH_SHORT).show();
                                    mUser.setPhoto(taskSnapshot.getMetadata().getPath());

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(AccountFragment.this.getContext(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Feltöltés "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
}