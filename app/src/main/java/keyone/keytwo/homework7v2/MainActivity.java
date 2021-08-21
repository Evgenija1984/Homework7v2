package keyone.keytwo.homework7v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        readSettings();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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

    private void showFragment(Fragment fragment) {
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
        } else if (Settings.isReplaceFragment)
            fragmentTransaction
                    .replace(R.id.fragment_container, fragment);
        if (Settings.isBackStack) {
            fragmentTransaction
                    .addToBackStack("");
        }
        fragmentTransaction.commit();
    }
}