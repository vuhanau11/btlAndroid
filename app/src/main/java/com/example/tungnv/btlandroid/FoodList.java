package com.example.tungnv.btlandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tungnv.btlandroid.Interface.ItemClick;
import com.example.tungnv.btlandroid.ViewHolder.FoodViewHolder;
import com.example.tungnv.btlandroid.common.Common;
import com.example.tungnv.btlandroid.model.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    String theloaiId = "";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get intent here
        if (getIntent() !=null)
            theloaiId = getIntent().getStringExtra("TheloaiId");
        if (!theloaiId.isEmpty() && theloaiId != null){
            if (Common.isConnectedToInternet(getBaseContext()))
            loadListFood(theloaiId);
            else {
                Toast.makeText(FoodList.this, "Vui lòng kiểm tra kết nối !!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void loadListFood(String theloaiId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList.orderByChild("MenuId").equalTo(theloaiId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);
                final Food local = model;
                viewHolder.setItemClick(new ItemClick() {
                    @Override
                    public void onclick(View view, int position, boolean islongclick) {
                        //băt đầu giao diện mới
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey()); //chuyển FoodId đến giao dien mới
                        startActivity(foodDetail);
                    }
                });

            }
        };
        //set adapter
        recyclerView.setAdapter(adapter);
    }
}
