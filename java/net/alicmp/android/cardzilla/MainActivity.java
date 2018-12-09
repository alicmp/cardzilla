package net.alicmp.android.cardzilla;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.alicmp.android.cardzilla.fragment.BackpackFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.alicmp.android.cardzilla.R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(net.alicmp.android.cardzilla.R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(net.alicmp.android.cardzilla.R.drawable.ic_menu_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        Fragment fragment = new BackpackFragment();
        android.support.v4.app.FragmentManager fragmentManager =
                getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(net.alicmp.android.cardzilla.R.id.content_frame, fragment).commit();

        mDrawerLayout = (DrawerLayout) findViewById(net.alicmp.android.cardzilla.R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(net.alicmp.android.cardzilla.R.id.nav_view);
        if (navigationView != null) {

            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            int id = menuItem.getItemId();
                            Fragment fragment = null;
                            android.support.v4.app.FragmentManager fragmentManager =
                                    getSupportFragmentManager();
                            switch (id) {
                                case net.alicmp.android.cardzilla.R.id.nav_backpack:
                                    fragment = new BackpackFragment();
                                    fragmentManager.beginTransaction().
                                            replace(net.alicmp.android.cardzilla.R.id.content_frame, fragment).commit();
                                    menuItem.setChecked(true);
                                    break;
                                case net.alicmp.android.cardzilla.R.id.nav_about:
                                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                                    break;
                                case net.alicmp.android.cardzilla.R.id.nav_settings:
                                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                    break;
                            }

                            mDrawerLayout.closeDrawers();
                            return true;
                        }
                    });

            navigationView.setCheckedItem(net.alicmp.android.cardzilla.R.id.nav_backpack);

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(net.alicmp.android.cardzilla.R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPacketDialog.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
