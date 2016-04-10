package com.anhlt.maddiscover.fragments.venue;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.fragments.BaseFragment;
import com.anhlt.maddiscover.entities.Venue;
import com.anhlt.maddiscover.services.BaseService;
import com.anhlt.maddiscover.services.VenueService;

public class CreateVenue extends BaseFragment {

    Context context;
    BaseService baseService;
    VenueService venueService;

    EditText name ;
    EditText address;

    public static CreateVenue getInstance(){
        return new CreateVenue();
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();
        venueService = new VenueService(context);
        return inflater.inflate(R.layout.venue_form, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_create_venue, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()  == R.id.save){
            if(createVenue()){
                baseService = new BaseService(getActivity().getFragmentManager(),context);
                baseService.replaceFragment(new ListVenue());
            }
        }
        if (item.getItemId()  == R.id.save_more){
            if(createVenue()){
                resetForm();
                Toast.makeText(context, "Create success", Toast.LENGTH_LONG);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean createVenue(){

        Venue venue = new Venue();
        getFormUI();

        if (name.getText().toString().isEmpty()){
            showErrorDialog("Error", "Please enter name of venue");
            return false;
        }else{
            mapData(venue);
            if(validVenue(venue)) {
                venueService.saveNewVenue(venue);
            }else {
                showErrorDialog("Error", "Venue is already exists!");
                return false;
            }
            return true;
        }
    }

    private void mapData(Venue venue){
        venue.setName(name.getText().toString());
        if(!address.getText().toString().isEmpty())
            venue.setAddress(address.getText().toString());
    }

    private void getFormUI(){
        if(name == null)
            name =  (EditText)getView().findViewById(R.id.create_venue_name);
        if(address == null)
            address =  (EditText)getView().findViewById(R.id.address);
    }

    private boolean validVenue(Venue venue){
        Venue venue1 = venueService.findByName(venue.getName());
        if (venue1.getName() == null)
            return true;
        return false;
    }

    private void resetForm(){
        getFormUI();
        name.setText("");
        address.setText("");
    }
}
