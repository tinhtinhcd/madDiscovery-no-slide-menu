package com.anhlt.maddiscover.fragments.organizer;

import android.content.Context;
import android.content.DialogInterface;
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
import com.anhlt.maddiscover.entities.Organizer;
import com.anhlt.maddiscover.fragments.BaseFragment;
import com.anhlt.maddiscover.services.BaseService;
import com.anhlt.maddiscover.services.OrganizerService;
import android.app.AlertDialog;

/**
 * Created by anhlt on 3/13/16.
 */
public class OrganizerDetails extends BaseFragment {

    Long orgId;
    TextView name,tele,email,address,about;
    OrganizerService organizerService;
    Context context;
    BaseService baseService;

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
        return inflater.inflate(R.layout.org_details, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_org_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit_org_detail){
            organizerService.editOrganizer(getFragmentManager(),context, orgId);
        }else if (item.getItemId()  == R.id.delete_org_detail){
            deleteOrg();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteOrg(){
        if(canDelete())
            delete();
        else{
            showErrorDialog("Cannot Delete","This organizer is already in event, cannot delete.");
        }
    }

    private boolean canDelete(){
        return organizerService.canDelete(orgId);
    }

    private void delete(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle("Delete Organizer");
        builder.setMessage("Do you want to delete organizer: " + name.getText());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                organizerService.deleteOrganizer();
                baseService = new BaseService(getActivity().getFragmentManager(), context);
                baseService.replaceFragment(new ListOrganizer());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void init(){
        getUI();
        loadData();
    }

    private void getUI(){
        name = (TextView) getView().findViewById(R.id.org_name);
        tele = (TextView) getView().findViewById(R.id.tele);
        email = (TextView) getView().findViewById(R.id.org_email);
        address = (TextView) getView().findViewById(R.id.org_address);
        about = (TextView) getView().findViewById(R.id.org_about);
    }

    private void loadData(){
        orgId = getArguments().getLong("orgId");
        Organizer org = organizerService.findById(orgId);
        name.setText(org.getName());
        tele.setText(org.getMobile());
        email.setText(org.getEmail());
        address.setText(org.getAddress());
        about.setText(org.getAbout());
    }

    private void initService(){
        context = getActivity().getApplicationContext();
        organizerService = new OrganizerService(context);
    }

}
