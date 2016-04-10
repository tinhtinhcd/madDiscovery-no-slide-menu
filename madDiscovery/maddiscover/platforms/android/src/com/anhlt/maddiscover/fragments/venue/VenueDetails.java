package com.anhlt.maddiscover.fragments.venue;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.entities.Venue;
import com.anhlt.maddiscover.fragments.BaseFragment;
import com.anhlt.maddiscover.services.VenueService;

/**
 * Created by anhlt on 3/13/16.
 */
public class VenueDetails extends BaseFragment {

    Long venueId;
    TextView name,address;
    VenueService venueService;
    Context context;

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        initService();
        return inflater.inflate(R.layout.venue_details, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_org_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()  == R.id.edit_org_detail){

        }else if (item.getItemId()  == R.id.delete_org_detail){
            deleteOrg();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteOrg(){
        if(canDelete())
            delete();
    }

    private boolean canDelete(){
        return false;
    }

    private void delete(){
    }

    private void init(){
        getUI();
        loadData();
    }

    private void getUI(){
        name = (TextView) getView().findViewById(R.id.venue_name);
        address = (TextView) getView().findViewById(R.id.venue_address);
    }

    private void loadData(){
        venueId = getArguments().getLong("venueId");
        Venue venue = venueService.findById(venueId);
        name.setText(venue.getName());
        address.setText(venue.getAddress());
    }

    private void initService(){
        context = getActivity().getApplicationContext();
        venueService = new VenueService(context);
    }
}
