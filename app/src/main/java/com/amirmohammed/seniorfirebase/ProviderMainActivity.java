package com.amirmohammed.seniorfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProviderMainActivity extends AppCompatActivity {
    private static final String TAG = "ProviderMainActivity";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    List<OrderData> orderDataList = new ArrayList<>();

    RecyclerView recyclerView;
    OrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_main);
        recyclerView = findViewById(R.id.provider_orders_recycler_view);

        getOrders();
    }

    private void getOrders() {
        firestore.collection("seniorOrders")
                .whereEqualTo("accept", false)
//                .whereEqualTo("providerId", firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            String errorMessage = error.getLocalizedMessage();
                            Log.i(TAG, "onEvent: " + errorMessage);
                            Toast.makeText(ProviderMainActivity.this, errorMessage
                                    , Toast.LENGTH_SHORT).show();
                            return;
                        }

                        orderDataList.clear();
                        for (int i = 0; i < value.getDocuments().size(); i++) {
                            OrderData orderData
                                    = value.getDocuments().get(i).toObject(OrderData.class);

//                            if (orderData.isFinish()) continue;

                            orderDataList.add(orderData);
                        }
                        Log.i(TAG, "onEvent: " + orderDataList.size());

                        adapter = new OrdersAdapter(orderDataList, new OrdersAdapter.OrdersI() {
                            @Override
                            public void onOrderClick(OrderData orderData) {
                                Intent intent = new Intent(ProviderMainActivity.this, ProviderOrderDetailsActivity.class);
                                intent.putExtra("order", orderData);
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }

                });
    }

    public void signOut() {
        firebaseAuth.signOut();
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(ProviderMainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_profile) {
            openProfile();

        } else if (id == R.id.item_logout) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }
}