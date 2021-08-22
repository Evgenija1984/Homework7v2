package keyone.keytwo.homework7v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        readSettings();
        Toolbar toolbar = getToolbar();
        initDrawer(toolbar);
    }

    private Toolbar getToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, (R.string.open), (R.string.close));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.buttonMain:
                        showFragment(MainFragment.newInstance());
                        break;
                    case R.id.buttonFavorite:
                        showFragment(FavoriteFragment.newInstance());
                        break;
                    case R.id.buttonSettings:
                        showFragment(SettingsFragment.newInstance());
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showFragment(SettingsFragment.newInstance());
                return true;
            case R.id.action_main:
                showFragment(MainFragment.newInstance());
                return true;
            case R.id.action_favorite:
                showFragment(FavoriteFragment.newInstance());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void readSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Settings.isBackStack = sharedPreferences.getBoolean(Settings.IS_BACK_STACK_USED, false);
        Settings.isAddFragment = sharedPreferences.getBoolean(Settings.IS_ADD_FRAGMENT_USED, false);
        Settings.isReplaceFragment = sharedPreferences.getBoolean(Settings.IS_REPLACE_FRAGMENT_USED, false);
        Settings.isBackAsRemove = sharedPreferences.getBoolean(Settings.IS_BACK_AS_REMOVE_FRAGMENT, false);
        Settings.isDeleteBeforeAdd = sharedPreferences.getBoolean(Settings.IS_DELETE_FRAGMENT_BEFORE_ADD, false);
    }

    private void initView() {
        Button buttonBack = findViewById(R.id.buttonBack);
        Button buttonMain = findViewById(R.id.buttonMain);
        Button buttonFavorite = findViewById(R.id.buttonFavorite);
        Button buttonSettings = findViewById(R.id.buttonSettings);

        buttonBack.setOnClickListener(this);
        buttonMain.setOnClickListener(this);
        buttonFavorite.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (Settings.isBackAsRemove) {
                    Fragment fragmentForDelete = getVisibleFragment(fragmentManager);
                    if (fragmentForDelete != null) {
                        fragmentManager.beginTransaction().remove(fragmentForDelete).commit();
                    }
                } else {
                    fragmentManager.popBackStack();
                }
                break;
            case R.id.buttonMain:
                showFragment(MainFragment.newInstance());
                break;
            case R.id.buttonFavorite:
                showFragment(FavoriteFragment.newInstance());
                break;
            case R.id.buttonSettings:
                showFragment(SettingsFragment.newInstance());
                break;
        }
    }

    Fragment getVisibleFragment(FragmentManager fragmentManager) {
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            if (fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (Settings.isDeleteBeforeAdd) {
            Fragment fragmentForDelete = getVisibleFragment(fragmentManager);
            if (fragmentForDelete != null) {
                fragmentTransaction.remove(fragmentForDelete);
            }
        }
        if (Settings.isAddFragment) {
            fragmentTransaction
                    .add(R.id.fragment_container, fragment);
        } else {
            fragmentTransaction
                    .replace(R.id.fragment_container, fragment);
        }


        if (Settings.isBackStack) {
            fragmentTransaction
                    .addToBackStack("");
        }
        fragmentTransaction.commit();
    }
}