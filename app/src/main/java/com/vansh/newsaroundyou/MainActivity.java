package com.vansh.newsaroundyou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar materialToolbar;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.main_bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_host);

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);

        materialToolbar = findViewById(R.id.main_appbar);
        setSupportActionBar(materialToolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.main_home_fragment, R.id.main_saved_fragment, R.id.main_explore_fragment, R.id.main_settings_fragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        materialToolbar.setTitle("NewsAroundYou");

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.main_host);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}