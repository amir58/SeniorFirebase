package com.amirmohammed.seniorfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserAddOrderActivity extends AppCompatActivity {
    EditText editTextDescription, editTextPhoneNumber
            , editTextFirstLocation,editTextLastLocation
            ,editTextDate, editTextTime;

    MaterialButton buttonAdd;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_order);

        editTextDescription = findViewById(R.id.add_order_request_description);
        editTextPhoneNumber = findViewById(R.id.add_order_phone_number);
        editTextFirstLocation = findViewById(R.id.add_order_first_location);
        editTextLastLocation = findViewById(R.id.add_order_last_location);
        editTextDate = findViewById(R.id.add_order_date);
        editTextTime = findViewById(R.id.add_order_time);

        buttonAdd = findViewById(R.id.add_order_material_button);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromUi();
            }
        });

    }

    private void getDataFromUi() {
        String description = editTextDescription.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String firstLocation = editTextFirstLocation.getText().toString().trim();
        String lastLocation = editTextLastLocation.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();

        if (description.isEmpty() || phoneNumber.isEmpty()
                || firstLocation.isEmpty() || lastLocation.isEmpty()
                || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "All data required", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = firebaseAuth.getCurrentUser().getUid();

        String orderId = userId + System.currentTimeMillis();

        OrderData orderData
                = new OrderData(orderId, userId,
                description, phoneNumber,
                firstLocation, lastLocation,
                date, time,
                "","",false, false);

        uploadOrderData(orderData);
    }

    private void uploadOrderData(OrderData orderData) {
        firestore.collection("seniorOrders")
                .document(orderData.getOrderId())
                .set(orderData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserAddOrderActivity.this,
                                    "Order added", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            String errorMessage = task.getException().getLocalizedMessage();
                            Toast.makeText(UserAddOrderActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}