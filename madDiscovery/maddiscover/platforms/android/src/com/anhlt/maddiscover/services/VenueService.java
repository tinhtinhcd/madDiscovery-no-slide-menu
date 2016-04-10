package com.anhlt.maddiscover.services;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import com.anhlt.maddiscover.fragments.organizer.OrganizerDetails;
import com.anhlt.maddiscover.fragments.venue.CreateVenue;
import com.anhlt.maddiscover.entities.Venue;
import com.anhlt.maddiscover.data.repositories.VenueRepository;
import com.anhlt.maddiscover.fragments.venue.VenueDetails;

import java.util.List;

/**
 * Created by anhlt on 2/19/16.
 */
public class VenueService {

    Context context;
    VenueRepository venueRepository;
    BaseService baseService;

    public VenueService(Context context) {
        this.context = context;
        venueRepository = new VenueRepository(context);
    }

    public void viewDetails(FragmentManager fm, Context context,long venueId){

        VenueDetails details = new VenueDetails();
        Bundle args = new Bundle();
        args.putLong("venueId", venueId);
        details.setArguments(args);

        baseService = new BaseService(fm,context);
        baseService.replaceFragment(details);
    }

    public Venue findById(long id){
        return venueRepository.findById(id);
    }

    public Venue findByName(String name){
        return venueRepository.findByName(name);
    }

    public List<Venue> getVenueList(){
        return venueRepository.getAllVenue();
    }

    public void createVenueForm(FragmentManager fm, Context context){
        baseService = new BaseService(fm,context);
        baseService.replaceFragment(CreateVenue.getInstance());
    }

    public void saveNewVenue(Venue venue){
        venueRepository.create(venue);
    }

    public void editVenue(){
    }

    public void deleteVenue(){

    }

    public void searchVenue(){
    }

    public List<String> listVenueName(){
        return venueRepository.getListVenueName();
    }
}
