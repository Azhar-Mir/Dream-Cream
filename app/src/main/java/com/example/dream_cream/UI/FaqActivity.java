package com.example.dream_cream.UI;

import android.os.Bundle;

import com.example.dream_cream.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FaqActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        recyclerView = findViewById(R.id.recyclerView);

        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        MovieAdapter movieAdapter = new MovieAdapter(movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);
    }

    private void initData() {
        movieList = new ArrayList<>();
        movieList.add(new Movie("What is Dreame Creme?","Dreame Creme is an online e-commerce platform to deliver bakery product to your doorstep with the help of personalized shoppers. Dreame Creme facilitates to place bakery orders through the mobile app that is on Android for now we expand to other mobile system."));
        movieList.add(new Movie("How do I order bakery products?",  "It’s so easy!\n" +
                "\n" +
                "Using Dreame Creme app, add your favorite items to the cart, choose a delivery location, and place your order.\n" +
                "\n" +
                "What are you waiting for? Start shopping now!"));
        movieList.add(new Movie("How to I add things to my cart?",  "It’s too simple!\n" +
                "\n" +
                "Select the cart sign next to the item you want to be delivered to add the item to your cart. You can also click the item to see a more detailed item description, and then select the cart sign to add it to the cart. When an item is in your cart, you will see a tag appear next to the item that says 1 in the cart."));
        movieList.add(new Movie("Is there a minimum order amount?",  "No! There is no minimum order amount. However, we cheer you to shop more to save more!"));
        movieList.add(new Movie("Are there a maximum number of deliveries allowed in a day?",  "No! You can order as much as you want!"));
        movieList.add(new Movie("What if the store is out of something that I ordered?",  "Your shopper will contact you to see if you would like a substitute item!"));
        movieList.add(new Movie("How do I clear my cart?","If you have not yet selected ‘Checkout’:\n" +
                "\n" +
                "Select ‘Clear’ to empty your cart."));
        movieList.add(new Movie("Where is my receipt?","You can view the receipt of your order in the order history."));
        movieList.add(new Movie("I am having problems with the app, How do I fix it?","For Android phones, check the following:\n" +
                "1. Verify that your phone has been updated to the latest version of its operating system.\n" +
                "2. Verify that your Dreame Creme app has been updated to the latest version.\n" +
                "3. If both your phone’s operating system and your Dreame Creme app has the latest version, then try closing the app.\n" +
                "On your phone, select the ‘Recent Apps’ icon or press and hold the ‘Home’ button until the Recent Apps list appears. Swipe the open app towards the right of the phone. To exit this feature, select the ‘Recent Apps’ icon again."));
    }
}