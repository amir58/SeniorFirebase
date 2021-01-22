package com.amirmohammed.seniorfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    ProgressBar progressBar;
    TextInputEditText editTextEmail, editTextName, editTextPhone;
    MaterialButton buttonUpdate;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        circleImageView = findViewById(R.id.profile_user_image);
        progressBar = findViewById(R.id.profile_progress_bar);
        editTextEmail = findViewById(R.id.profile_et_user_email);
        editTextName = findViewById(R.id.profile_et_username);
        editTextPhone = findViewById(R.id.profile_et_phone);
        buttonUpdate = findViewById(R.id.profile_btn_update);

        getUserData();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void updateUserData() {
        String newName = editTextName.getText().toString().trim();
        String newPhone = editTextPhone.getText().toString().trim();

        if (newName.isEmpty()) {
            editTextName.setError("Name required");
            return;
        }

        if (newPhone.isEmpty()) {
            editTextPhone.setError("Phone required");
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", newName);
        map.put("phone", newPhone);

        firestore.collection("seniorUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Data updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private static final String TAG = "ProfileActivity";

    private void getUserData() {
        firestore.collection("seniorUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            UserData userData = task.getResult().toObject(UserData.class);
                            Log.i(TAG, "onComplete: " + userData.toString());

                            updateUi(userData);

                        } else {

                        }
                    }
                });

    }

    private void updateUi(UserData userData) {
        editTextEmail.setText(userData.getEmail());
        editTextName.setText(userData.getName());
        editTextPhone.setText(userData.getPhone());

        Picasso.get()
                .load(userData.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(circleImageView);
    }


    // update image

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            progressBar.setVisibility(View.VISIBLE);
            uploadImage();
        }
    }

    private void uploadImage() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        Log.i(TAG, "uploadImage: " + uid);

        storageReference.child("seniorImages").child(uid).putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                            getImageUrl();

                        } else {
                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getImageUrl() {
        String uid = firebaseAuth.getCurrentUser().getUid();

        storageReference.child("seniorImages").child(uid).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String imageUrl = task.getResult().toString();
                            Log.i(TAG, "onComplete: " + imageUrl);
                            updateUserImageUrl(imageUrl);

                        } else {
                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserImageUrl(String imageUrl) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("imageUrl", imageUrl);

        firestore.collection("seniorUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Image updated", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            circleImageView.setImageURI(imageUri);

                        }
                    }
                });
    }

}