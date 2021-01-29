package com.amirmohammed.seniorfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProviderOrderDetailsActivity extends AppCompatActivity {
    MaterialButton materialButtonAccept, materialButtonUpdate;

    TextInputEditText textInputEditTextRequestDescription,
            textInputEditTextPhoneNumber,
            textInputEditTextFirstLocation,
            textInputEditTextLastLocation,
            textInputEditTextDate,
            textInputEditTextTime,
            textInputEditTextState;
    CheckBox checkBoxIsFinish;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    OrderData orderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_order_details);


        orderData = (OrderData) getIntent().getSerializableExtra("order");

        materialButtonAccept = findViewById(R.id.provider_order_details_material_button_accept);
        materialButtonUpdate = findViewById(R.id.provider_order_details_material_button_update);
        textInputEditTextRequestDescription = findViewById(R.id.order_details_request_description);
        textInputEditTextPhoneNumber = findViewById(R.id.order_details_phone_number);
        textInputEditTextFirstLocation = findViewById(R.id.order_details_first_location);
        textInputEditTextLastLocation = findViewById(R.id.order_details_last_location);
        textInputEditTextDate = findViewById(R.id.order_details_date);
        textInputEditTextTime = findViewById(R.id.order_details_time);
        textInputEditTextState = findViewById(R.id.order_details_state);
        checkBoxIsFinish = findViewById(R.id.provider_order_details_cb_is_finish);

        textInputEditTextRequestDescription.setText(orderData.getDescription());
        textInputEditTextPhoneNumber.setText(orderData.getPhone());
        textInputEditTextFirstLocation.setText(orderData.getFirstLocation());
        textInputEditTextLastLocation.setText(orderData.getLastLocation());
        textInputEditTextDate.setText(orderData.getDate());
        textInputEditTextTime.setText(orderData.getTime());
        textInputEditTextState.setText(orderData.getState());

        textInputEditTextState.setEnabled(orderData.isAccept());
        checkAccept();

        materialButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptOrder();
            }
        });
        materialButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrder();
            }
        });
    }

    private void updateOrder() {
        String state = textInputEditTextState.getText().toString().trim();
        if (state.isEmpty()) {
            Toast.makeText(this, "Write order state", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> map = new HashMap<>();

        map.put("state", state);
        map.put("finish", checkBoxIsFinish.isChecked());

        firestore.collection("seniorOrders")
                .document(orderData.getOrderId())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProviderOrderDetailsActivity.this,
                                "Order updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void acceptOrder() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("state", "Accepted");
        map.put("providerId", firebaseAuth.getCurrentUser().getUid());
        map.put("accept", true);

        firestore.collection("seniorOrders")
                .document(orderData.getOrderId())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProviderOrderDetailsActivity.this,
                                "Order accept", Toast.LENGTH_SHORT).show();
                        textInputEditTextState.setEnabled(true);
                        textInputEditTextState.setText("Accepted");
                        materialButtonAccept.setVisibility(View.GONE);

                    }
                });
    }

    private void checkAccept() {
        if (orderData.isAccept()) {
            materialButtonAccept.setVisibility(View.GONE);

        } else {
            materialButtonAccept.setVisibility(View.VISIBLE);
        }
    }
}