package com.anhlt.maddiscover.services;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.fragments.event.ListEvent;

/**
 * Created by anhlt on 2/26/16.
 */
public class BaseService {

    FragmentManager fm;
    Context context;
    private static int position =0;
    private Fragment fragment = null;

    public BaseService(FragmentManager fm, Context context) {
        this.fm = fm;
        this.context = context;
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, fragment, fragment.toString());
        ft.commit();
    }

    public void selectItem() {
        listEvent();
    }

    public boolean menuActions(MenuInflater inflater, Menu menu){
        switch (position){
            case 0:
                inflater.inflate(R.menu.menu_event, menu);
                return true;
            default:
                inflater.inflate(R.menu.menu_event, menu);
                return false;
        }
    }

    public void listEvent(){
        fragment = new ListEvent();
        replaceFragment(fragment);
        position = 0;
    }

    public boolean itemSelected(MenuItem item){
        EventService eventService = new EventService(context);

        switch(item.getItemId()) {
            case R.id.search_event:
                eventService.searchEvent();
                return true;
            case R.id.create_event:
                eventService.createEventForm(fm,context);
                return true;
            case R.id.delete_event:
                eventService.deleteEvent(fm,fragment);
                return true;
            case R.id.edit_event:
                eventService.editEventFromListEvent(fm,fragment);
                return true;
        }
        return false;
    }

}
