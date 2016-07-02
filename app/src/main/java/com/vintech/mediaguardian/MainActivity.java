package com.vintech.mediaguardian;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vintech.mediaguardian.framework.FrameEvent;
import com.vintech.mediaguardian.framework.WorkspaceLayout;
import com.vintech.util.tool.PermissionTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        EventBus.getDefault().register(this);

        MainApplication.getDBManager().startModel();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            WorkspaceLayout workspace = (WorkspaceLayout) findViewById(R.id.workspace);
            if (workspace.handleBackKey()) {
                return;
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_gallery) {
            EventBus.getDefault().post(new FrameEvent.EventSetLayout(R.id.photo_gallery_layout));
        } else if (id == R.id.nav_sys_gallery) {
            EventBus.getDefault().post(new FrameEvent.EventSetLayout(R.id.photo_pick_layout));
        } else if (id == R.id.nav_video) {
            EventBus.getDefault().post(new FrameEvent.EventSetLayout(R.id.video_gallery_layout));
        } else if (id == R.id.nav_sys_video) {
            EventBus.getDefault().post(new FrameEvent.EventSetLayout(R.id.video_pick_layout));
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRequestPermission(FrameEvent.EventRequestPermission event) {
        if (!PermissionTool.hasPermission(this, event.mPermission) && ActivityCompat.shouldShowRequestPermissionRationale(this, event.mPermission)) {
            ActivityCompat.requestPermissions(this, new String[]{event.mPermission}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                EventBus.getDefault().post(new FrameEvent.EventPermissionResult(permissions[i], grantResults[i]));
            }
        }
    }
}
