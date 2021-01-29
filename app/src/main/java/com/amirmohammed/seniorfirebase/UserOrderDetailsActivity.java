package com.amirmohammed.seniorfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class UserOrderDetailsActivity extends AppCompatActivity {
    MaterialButton materialButton;
    AutoCompleteTextView textInputEditTextRequestDescription,
            textInputEditTextPhoneNumber,
            textInputEditTextFirstLocation,
            textInputEditTextLastLocation,
            textInputEditTextDate,
            textInputEditTextTime,
            textInputEditTextIsAccept,
            textInputEditTextState,
            textInputEditTextIsFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_details);

        OrderData orderData = (OrderData) getIntent().getSerializableExtra("order");

        materialButton = findViewById(R.id.order_details_material_button);
        textInputEditTextRequestDescription = findViewById(R.id.order_details_request_description);
        textInputEditTextPhoneNumber = findViewById(R.id.order_details_phone_number);
        textInputEditTextFirstLocation = findViewById(R.id.order_details_first_location);
        textInputEditTextLastLocation = findViewById(R.id.order_details_last_location);
        textInputEditTextDate = findViewById(R.id.order_details_date);
        textInputEditTextTime = findViewById(R.id.order_details_time);
        textInputEditTextIsAccept = findViewById(R.id.order_details_is_accept);
        textInputEditTextState = findViewById(R.id.order_details_state);
        textInputEditTextIsFinish = findViewById(R.id.order_details_is_finish);

        textInputEditTextRequestDescription.setText(orderData.getDescription());
        textInputEditTextPhoneNumber.setText(orderData.getPhone());
        textInputEditTextFirstLocation.setText(orderData.getFirstLocation());
        textInputEditTextLastLocation.setText(orderData.getLastLocation());
        textInputEditTextDate.setText(orderData.getDate());
        textInputEditTextTime.setText(orderData.getTime());
        textInputEditTextState.setText(orderData.getState());
        textInputEditTextIsAccept.setText(orderData.isAccept() ? "Accept" : "Not Accept");
        textInputEditTextIsFinish.setText(orderData.isFinish() ? "Finished" : "Not finish");

        String accept = orderData.isAccept() ? "Accept" : "Not Accept";

        String finish;
        if (orderData.isFinish()) {
            finish = "finish";
        } else {
            finish = "not finish";
        }

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}