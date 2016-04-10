package com.anhlt.maddiscover.fragments.organizer;

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
import com.anhlt.maddiscover.entities.Organizer;
import com.anhlt.maddiscover.services.BaseService;
import com.anhlt.maddiscover.services.OrganizerService;

public class CreateOrganizer extends BaseFragment {

    Context context;
    BaseService baseService;
    OrganizerService organizerService;

    EditText name ;
    EditText mobile;
    EditText email;
    EditText address;
    EditText about;

    public static CreateOrganizer getInstance(){
        return new CreateOrganizer();
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
        organizerService = new OrganizerService(context);
        return inflater.inflate(R.layout.organizer_form, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_create_organizer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()  == R.id.save){
            if(createOrganizer()){
                baseService = new BaseService(getActivity().getFragmentManager(),context);
                baseService.replaceFragment(new ListOrganizer());
            }
        }
        if (item.getItemId()  == R.id.save_more){
            if(createOrganizer()){
                resetForm();
                Toast.makeText(context,"Create success", Toast.LENGTH_LONG);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean createOrganizer(){

        getFormUI();

        if (name.getText().toString().isEmpty()){
            showErrorDialog("Error", "Please enter name of organizer");
            return false;
        }else{
            Organizer organizer = new Organizer();
            mapDataOrganizer(organizer);
            if(validOrganizer(organizer)){
                organizerService.saveNewOrganizer(organizer);
            }else {
                showErrorDialog("Error", "Organizer is already exists!");
                return false;
            }
            return true;
        }
    }

    protected void mapDataOrganizer(Organizer organizer){
        organizer.setName(name.getText().toString());
        if(!mobile.getText().toString().isEmpty())
            organizer.setMobile(mobile.getText().toString());
        if(!email.getText().toString().isEmpty())
            organizer.setEmail(email.getText().toString());
        if(!address.getText().toString().isEmpty())
            organizer.setAddress(address.getText().toString());
        if(!about.getText().toString().isEmpty())
            organizer.setAbout(about.getText().toString());
    }

    protected void getFormUI(){
        if(name == null)
            name =  (EditText)getView().findViewById(R.id.organizer_name);
        if(mobile == null)
            mobile =  (EditText)getView().findViewById(R.id.mobile);
        if(email == null)
            email =  (EditText)getView().findViewById(R.id.email);
        if(address == null)
            address =  (EditText)getView().findViewById(R.id.address);
        if(about == null)
            about =  (EditText)getView().findViewById(R.id.about);
    }

    protected boolean validOrganizer(Organizer organizer){
        Organizer organizer1 = organizerService.findByName(organizer.getName());
        if (organizer1.getName() == null)
            return true;
        return false;
    }

    private void resetForm(){
        getFormUI();
        name.setText("");
        mobile.setText("");
        email.setText("");
        about.setText("");
        address.setText("");
    }
}
