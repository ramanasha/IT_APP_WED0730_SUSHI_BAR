package com.example.radoslaw.shushiapp_cheef.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radoslaw.shushiapp_cheef.R;
import com.example.radoslaw.shushiapp_cheef.model.Ingredient;
import com.example.radoslaw.shushiapp_cheef.model.Meal;
import com.example.radoslaw.shushiapp_cheef.model.Order;
import com.example.radoslaw.shushiapp_cheef.rest.RetrofitClient;
import com.example.radoslaw.shushiapp_cheef.rest.SushiAppApiService;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetails extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetails);
        final Serializable extras = getIntent().getSerializableExtra("order");
        //if(extras== null){ //TODO: Jakieś sprawdzenie się przyda/
        final Order order = (Order) extras;
        TextView mtextView = findViewById(R.id.textView);
        TextView mtextView1 = findViewById(R.id.textView2);
        TextView mtextView2 = findViewById(R.id.textView3);

        mtextView.setText(getString(R.string.order_status)+" "+order.getStatus());
        StringBuilder stringBuilder = new StringBuilder();
        for (Meal element : order.getMeals()) {
            stringBuilder.append(element.getMeal().getName() + "  x" +element.getAmount().toString() +"\n");
        }
        mtextView1.setText(stringBuilder.toString());
        mtextView2.setText(getString(R.string.order_from_table)+ " "+ order.getTable().getId());

        TextView orderTimeTextView = findViewById(R.id.textViewTime);
        orderTimeTextView.setText(getString(R.string.order_time) + String.valueOf(order.getDateStart()));

        TextView orderMealsTextView = findViewById(R.id.textViewMeals);
        orderMealsTextView.setText(getString(R.string.order_details));

        ImageButton imageButton = findViewById(R.id.imageButton);
        ImageButton imageButton2 = findViewById(R.id.imageButton2);



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SushiAppApiService client = RetrofitClient.getClient().create(SushiAppApiService.class);
                Call<Order> call = client.changeOrderToPreapering(order.getId());
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Toast.makeText(OrderDetails.this, "Change status of order to : " + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Toast.makeText(OrderDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SushiAppApiService client = RetrofitClient.getClient().create(SushiAppApiService.class);
                Call<Order> call = client.changeOrderToReady(order.getId());                                      //TODO: Sprawdzać czy jest już Ready
                call.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Toast.makeText(OrderDetails.this, "Change status of order to : " + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                       // mtextView.setText(getString(R.string.order_status)+" "+response.body().getStatus());

                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Toast.makeText(OrderDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}
