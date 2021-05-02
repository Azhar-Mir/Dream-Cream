package com.example.dream_cream.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dream_cream.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private CircleImageView image;
    private TextView mperson_name;
    private FirebaseAuth mAuth;
    private String Uid, name, photo;
    private FirebaseUser CurrentUser;
    private NavigationView navigationView;
    private ViewPager pager;
    private My_Adapter adapter;
    private List<model> models;
    private DatabaseReference m;
    private View mnavigationview;
    private static List<favouritesClass> favourites;
    //Custom Xml Views (cart Icon)
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    private TextView CustomCartNumber;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        Uid = CurrentUser.getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();
        navigationView = findViewById(R.id.navegation_view);
        navigationView.setNavigationItemSelectedListener(this);
        mnavigationview = navigationView.getHeaderView(0);
        mperson_name = mnavigationview.findViewById(R.id.persname);
        image = mnavigationview.findViewById(R.id.circimage);
        drawerLayout = findViewById(R.id.drawer);

        mToolBar = findViewById(R.id.main_TooBar);
        setSupportActionBar(mToolBar);
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onStart() {
        super.onStart();

        //Retrieve Header View User Data
        Navigation_view_header_data();

        //Retrieve Favourites
        Retrieve_fav();

        // FirstView
        Retrieve_Electroncis();

        // SecondView
        Retrieve_Fruits();

        //Third View
        Retrieve_Meats();

        // Fourth View
        Retrieve_Vegatables();

        // OFFERS
        Retrieve_offers();

        //Refresh CartIcon
        showCartIcon();

        //to check if the total price is zero or not
        HandleTotalPriceToZeroIfNotExist();
    }

    public void Retrieve_Electroncis() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Cakes");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<com.example.dream_cream.UI.HorizontalProductModel> lastmodels = new ArrayList<>();
        final com.example.dream_cream.UI.GridproductAdapter my_adapter;
        my_adapter = new com.example.dream_cream.UI.GridproductAdapter(lastmodels, favourites, com.example.dream_cream.UI.MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Cakes");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    com.example.dream_cream.UI.user my_user = new com.example.dream_cream.UI.user();
                    my_user = ds.getValue(com.example.dream_cream.UI.user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new com.example.dream_cream.UI.HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false, my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CategoryActivity.class);
                intent.putExtra("Category Name", "Cakes");
                startActivity(intent);
            }
        });

    }

    public void Retrieve_fav() {
        if (user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favourites")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            favourites = new ArrayList<>();
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        favouritesClass fav = new favouritesClass();
                        fav = ds.getValue(favouritesClass.class);
                        favourites.add(fav);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            ref.addListenerForSingleValueEvent(eventListener);
        } else {
            Toast.makeText(this, "please login", Toast.LENGTH_SHORT).show();
        }
    }

    public void Retrieve_Fruits() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView2);
        LayoutInflater inflater = getLayoutInflater();

        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Cookies");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<com.example.dream_cream.UI.HorizontalProductModel> lastmodels = new ArrayList<>();
        final com.example.dream_cream.UI.GridproductAdapter my_adapter;
        my_adapter = new com.example.dream_cream.UI.GridproductAdapter(lastmodels, favourites, com.example.dream_cream.UI.MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Cookies");
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    com.example.dream_cream.UI.user my_user = new com.example.dream_cream.UI.user();
                    my_user = ds.getValue(com.example.dream_cream.UI.user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new com.example.dream_cream.UI.HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false, my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CategoryActivity.class);
                intent.putExtra("Category Name", "Cookies");
                startActivity(intent);
            }
        });
    }

    public void Retrieve_Meats() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView3);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Breads");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<com.example.dream_cream.UI.HorizontalProductModel> lastmodels = new ArrayList<>();
        final com.example.dream_cream.UI.GridproductAdapter my_adapter;
        my_adapter = new com.example.dream_cream.UI.GridproductAdapter(lastmodels, favourites, com.example.dream_cream.UI.MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Breads");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    com.example.dream_cream.UI.user my_user = new com.example.dream_cream.UI.user();
                    my_user = ds.getValue(com.example.dream_cream.UI.user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new com.example.dream_cream.UI.HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false, my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CategoryActivity.class);
                intent.putExtra("Category Name", "Breads");
                startActivity(intent);
            }
        });

    }

    public void Retrieve_Vegatables() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView4);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Muffins");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<com.example.dream_cream.UI.HorizontalProductModel> lastmodels = new ArrayList<>();
        final com.example.dream_cream.UI.GridproductAdapter my_adapter;
        my_adapter = new com.example.dream_cream.UI.GridproductAdapter(lastmodels, favourites, com.example.dream_cream.UI.MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Muffins");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    com.example.dream_cream.UI.user my_user = new com.example.dream_cream.UI.user();
                    my_user = ds.getValue(com.example.dream_cream.UI.user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new com.example.dream_cream.UI.HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false, my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CategoryActivity.class);
                intent.putExtra("Category Name", "Muffins");
                startActivity(intent);
            }
        });
    }

    public void Retrieve_offers() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("offers");
        models = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    com.example.dream_cream.UI.Offers offer = new com.example.dream_cream.UI.Offers();
                    offer = ds.getValue(com.example.dream_cream.UI.Offers.class);
                    offer.setTitle(ds.getKey().toString());
                    models.add(new model(offer.getImg(), offer.getTitle(), offer.getDescribtion()));
                    adapter = new My_Adapter(models, com.example.dream_cream.UI.MainActivity.this);
                    pager = findViewById(R.id.cardview);
                    pager.setAdapter((PagerAdapter) adapter);
                    pager.setPadding(130, 0, 130, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Profile) {


            startActivity(new Intent(com.example.dream_cream.UI.MainActivity.this, UserProfileActivity.class));


        } else if (id == R.id.favourites) {

            startActivity(new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.favourites_activity.class));

        } else if (id == R.id.Cart) {
            startActivity(new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CartActivity.class));

        } else if (id == R.id.MyOrders) {
            startActivity(new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.OrderActivity.class));

        } else if (id == R.id.Support) {
            startActivity(new Intent(com.example.dream_cream.UI.MainActivity.this, SupportActivity.class));
        } else if (id == R.id.fruits) {
            Intent intent = new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CategoryActivity.class);
            intent.putExtra("Category Name", "Cookies");
            startActivity(intent);
        } else if (id == R.id.vegetables) {
            Intent intent = new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CategoryActivity.class);
            intent.putExtra("Category Name", "Muffins");
            startActivity(intent);
        } else if (id == R.id.meats) {
            Intent intent = new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CategoryActivity.class);
            intent.putExtra("Category Name", "Breads");
            startActivity(intent);
        } else if (id == R.id.electronics) {
            Intent intent = new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CategoryActivity.class);
            intent.putExtra("Category Name", "Cakes");
            startActivity(intent);
        } else if (id == R.id.Logout) {
            if (user != null) {
                CheckLogout();
            } else {
                Toast.makeText(this, "please login first", Toast.LENGTH_SHORT).show();
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void CheckLogout() {
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(com.example.dream_cream.UI.MainActivity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.loginActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = checkAlert.create();
        alert.setTitle("LogOut");
        alert.show();

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public void Navigation_view_header_data() {

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("Name").getValue().toString();
                    photo = dataSnapshot.child("Image").getValue().toString();
                    if (photo.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(image);
                    } else
                        Picasso.get().load(photo).placeholder(R.drawable.profile).into(image);
                    mperson_name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

    }


    private void showCartIcon() {
        //toolbar & cartIcon

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main2_toolbar, null);
        actionBar.setCustomView(view);

        //************custom action items xml**********************
        CustomCartContainer = (RelativeLayout) findViewById(R.id.CustomCartIconContainer);
        PageTitle = (TextView) findViewById(R.id.PageTitle);
        CustomCartNumber = (TextView) findViewById(R.id.CustomCartNumber);

        PageTitle.setText("Dream_Cream");
        setNumberOfItemsInCartIcon();

        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.dream_cream.UI.MainActivity.this, com.example.dream_cream.UI.CartActivity.class));
            }
        });


    }


    private void setNumberOfItemsInCartIcon() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("cart").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getChildrenCount() == 1) {
                        CustomCartNumber.setVisibility(View.GONE);
                    } else {
                        CustomCartNumber.setVisibility(View.VISIBLE);
                        CustomCartNumber.setText(String.valueOf(dataSnapshot.getChildrenCount() - 1));
                    }
                } else {
                    CustomCartNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }


    private void HandleTotalPriceToZeroIfNotExist() {
        if (user != null) {
            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            DatabaseReference m = root.child("cart").child(Uid);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        FirebaseDatabase.getInstance().getReference().child("cart").child(Uid).child("totalPrice").setValue("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
            m.addListenerForSingleValueEvent(eventListener);

        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
        }
    }

}
