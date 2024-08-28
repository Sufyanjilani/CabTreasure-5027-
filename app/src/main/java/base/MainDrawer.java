package base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eurosoft.customerapp.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import base.adapters.MenuAdapter;
import base.models.MenuModel;

public class MainDrawer extends AppCompatActivity {

    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private RecyclerView menuRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drawer);
        init();
        initMenuList();
    }

    private void init() {
        drawerLayout = findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        actionBarDrawerToggle.syncState();

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.app_black));

    }

    private void initMenuList() {
        menuRv = findViewById(R.id.menuRv);
        menuRv.setLayoutManager(new LinearLayoutManager(this));
        menuRv.setHasFixedSize(true);

        ArrayList<MenuModel> menuModels = new ArrayList<>();
        menuModels.add(new MenuModel("Your Trip", R.drawable.ic_note));
        menuModels.add(new MenuModel("Payment", R.drawable.ic_cash));
        menuModels.add(new MenuModel("Setting", R.drawable.ic_setting));
        menuModels.add(new MenuModel("Share App", R.drawable.shareapp));

        MenuAdapter menuAdapter = new MenuAdapter(this, menuModels);
        menuRv.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();
    }
}
