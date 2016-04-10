package com.anhlt.maddiscover.services;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import com.anhlt.maddiscover.fragments.event.EditEvent;
import com.anhlt.maddiscover.fragments.event.EventDetails;
import com.anhlt.maddiscover.fragments.organizer.CreateOrganizer;
import com.anhlt.maddiscover.entities.Organizer;
import com.anhlt.maddiscover.data.repositories.OrganizerRepository;
import com.anhlt.maddiscover.fragments.organizer.EditOrginazer;
import com.anhlt.maddiscover.fragments.organizer.OrganizerDetails;

import java.util.List;

/**
 * Created by anhlt on 3/5/16.F
 */
public class OrganizerService {

    Context context;
    OrganizerRepository organizerRepository;
    BaseService baseService;

    public OrganizerService(Context context) {
        this.context = context;
        organizerRepository = new OrganizerRepository(context);
    }

    public void viewOrg(FragmentManager fm, Context context,long orgId){

        OrganizerDetails details = new OrganizerDetails();
        Bundle args = new Bundle();
        args.putLong("orgId", orgId);
        details.setArguments(args);

        baseService = new BaseService(fm,context);
        baseService.replaceFragment(details);
    }

    public Organizer findById(long id){
        return organizerRepository.findById(id);
    }

    public Organizer findByName(String name){
        return organizerRepository.findByName(name);
    }

    public List<Organizer> getOrganizerList(){
        return organizerRepository.getAllOrganizer();
    }

    public void createOrganizerForm(FragmentManager fm, Context context){
        baseService = new BaseService(fm,context);
        baseService.replaceFragment(CreateOrganizer.getInstance());
    }

    public void saveNewOrganizer(Organizer organizer){
        organizerRepository.create(organizer);
    }

    public void deleteOrganizer(){

    }

    public void searchOrganizer(){
    }

    public List<String> listOrganizerName(){
        return organizerRepository.getListOrganizerName();
    }

    public boolean canDelete(Long orgId){
        return organizerRepository.canDeleteOrganizer(orgId);
    }

    public void editOrganizer(FragmentManager fm, Context context,long orgId){
        EditOrginazer editEvent = new EditOrginazer();
        Bundle args = new Bundle();
        args.putLong("orgId", orgId);
        editEvent.setArguments(args);

        baseService = new BaseService(fm,context);
        baseService.replaceFragment(editEvent);
    }

    public boolean validEdit(String name) {
        return organizerRepository.findByName(name)!=null;
    }

    public void updateOrg(Organizer organizer){
        organizerRepository.update(organizer);
    }
}
