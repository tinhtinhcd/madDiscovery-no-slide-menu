package com.anhlt.maddiscover.form;

import android.content.Context;
import android.content.Intent;

import com.anhlt.maddiscover.fragments.organizer.CreateOrganizer;
import com.anhlt.maddiscover.data.repositories.OrganizerRepository;

/**
 * Created by anhlt on 2/26/16.
 */
public class OrganizerForm {

    Context context;
    OrganizerRepository repository;

    public OrganizerForm(Context context) {
        this.context = context;
    }

    public void createOrginazer(){
        Intent intent = new Intent(context, CreateOrganizer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void editOrginazer(){
    }

    public void deleteOrginazer(){
    }

    public void searchOrginazer(){
    }

}
