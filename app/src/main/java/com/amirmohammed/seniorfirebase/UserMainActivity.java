package com.amirmohammed.seniorfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends AppCompatActivity {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FloatingActionButton floatingActionButtonAddOrder;
    List<OrderData> orderDataList = new ArrayList<>();

    RecyclerView recyclerView;
    OrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButtonAddOrder = findViewById(R.id.main_btn_add_order);
        recyclerView = findViewById(R.id.main_rv_orders);

        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(UserMainActivity.this, LoginActivity.class));
            finish();
            return;
        }


        floatingActionButtonAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, UserAddOrderActivity.class));
            }
        });

        getOrders();
    }

    private void getOrders() {
        firestore.collection("seniorOrders")
                .whereEqualTo("userId", firebaseAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value
                            , @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            String errorMessage = error.getLocalizedMessage();
                            Toast.makeText(UserMainActivity.this, errorMessage
                                    , Toast.LENGTH_SHORT).show();
                            return;
                        }

                        orderDataList.clear();
                        for (int i = 0; i < value.getDocuments().size(); i++) {
                            OrderData orderData
                                    = value.getDocuments().get(i).toObject(OrderData.class);
                            orderDataList.add(orderData);

                        }

                        adapter = new OrdersAdapter(orderDataList, ordersI);
                        recyclerView.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
                    }
                });
    }

    OrdersAdapter.OrdersI ordersI = new OrdersAdapter.OrdersI() {
        @Override
        public void onOrderClick(OrderData orderData) {
            Intent intent = new Intent(UserMainActivity.this, UserOrderDetailsActivity.class);
            intent.putExtra("order", orderData);
            startActivity(intent);
        }
    };

    public void signOut() {
        firebaseAuth.signOut();
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(UserMainActivity.this, LoginActivity.class));
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