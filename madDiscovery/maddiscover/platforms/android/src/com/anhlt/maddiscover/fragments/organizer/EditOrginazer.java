package com.anhlt.maddiscover.fragments.organizer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.entities.Organizer;
import com.anhlt.maddiscover.services.OrganizerService;

/**
 * Created by anhlt on 3/13/16.
 */
public class EditOrginazer extends CreateOrganizer {

    Context context;
    Long orgId;
    OrganizerService organizerService;
    Organizer organizer;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        orgId = getArguments().getLong("orgId");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();
        organizerService = new OrganizerService(context);
        return inflater.inflate(R.layout.organizer_form, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_edit_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_edit){
            saveEdit();
        }if(item.getItemId() == R.id.cancel_edit){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        getFormUI();
        initData();
    }

    private void saveEdit(){

        if (name.getText().toString().isEmpty()){
            showErrorDialog("Error", "Please enter event name");
        }else{
            if(validData()){
                save(newData(organizer));
            }else{
                showErrorDialog("Error", "This event has been create by the same organizer on this location!");
            }
        }
    }

    private void initData(){
        organizer = organizerService.findById(orgId);
        name.setText(organizer.getName());
        mobile.setText(organizer.getMobile());
        email.setText(organizer.getEmail());
        address.setText(organizer.getAddress());
        about.setText(organizer.getAbout());
    }

    private Organizer newData(Organizer organizer){

        organizer.setName(name.getText().toString());
        organizer.setMobile(mobile.getText().toString());
        organizer.setEmail(email.getText().toString());
        organizer.setAddress(address.getText().toString());
        organizer.setAbout(about.getText().toString());

        return organizer;
    }

    public Context getContext(){
        if(context == null)
            context = getActivity().getApplicationContext();
        return context;
    }

    private boolean validData(){
        return organizerService.validEdit(name.getText().toString());
    }

    private void save(Organizer organizer){
        organizerService.updateOrg(organizer);
        organizerService.viewOrg(getFragmentManager(),context,orgId);
    }
}
