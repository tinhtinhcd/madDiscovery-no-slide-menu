package com.anhlt.maddiscover.activities;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.services.BaseService;

/**
 * Created by anhlt on 2/19/16.
 */
public class madDiscovery extends AppCompatActivity {

    FragmentManager fm;
    private CharSequence mTitle;
    private BaseService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mad_discovery);
        selectItem();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return getService().itemSelected(item);
    }

    private void selectItem() {
        fm = getFragmentManager();
        service = new BaseService(fm,getApplicationContext());
        getService().selectItem();
    }

    private BaseService getService(){
        return service!=null?service:new BaseService(fm,getApplicationContext());
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return getService().menuActions(inflater, menu);
    }

}
