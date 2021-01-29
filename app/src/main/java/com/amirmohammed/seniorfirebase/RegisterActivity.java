package com.amirmohammed.seniorfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private CircleImageView circleImageView;
    private CheckBox checkBoxProvider;
    private EditText editTextEmail, editTextPassword, editTextName, editTextPhone;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private Uri imageUri = null;
    private String email = "";
    private String name = "";
    private String phone = "";
    boolean provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        circleImageView = findViewById(R.id.register_image);
        editTextEmail = findViewById(R.id.register_et_email);
        editTextPassword = findViewById(R.id.register_et_password);
        editTextName = findViewById(R.id.register_et_name);
        editTextPhone = findViewById(R.id.register_et_phone);

        checkBoxProvider = findViewById(R.id.checkbox_provider);

    }

    public void register(View view) {
        email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        name = editTextName.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();
        provider = checkBoxProvider.isChecked();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                            uploadImage();

                        } else {
                            String errorMessage = task.getException().getLocalizedMessage();
                            Log.i(TAG, "onComplete: " + errorMessage);
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void uploadImage() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        Log.i(TAG, "uploadImage: " + uid);

        storageReference.child("seniorImages").child(uid).putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                            getImageUrl();

                        } else {
                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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
                            uploadUserData(imageUrl);

                        } else {
                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadUserData(String imageUrl) {
        UserData userData = new UserData(email, name, phone, imageUrl,provider); // ctrl + p

        firestore.collection("seniorUsers")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            navigate();

                        } else {
                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void navigate() {
        if (provider) {
            startActivity(new Intent(RegisterActivity.this, ProviderMainActivity.class));
        } else {
            startActivity(new Intent(RegisterActivity.this, UserMainActivity.class));
        }
        finish();
    }

    public void openGallery(View view) {
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
            circleImageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

}