package com.anhlt.maddiscover.fragments.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.app.ListFragment;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.adapter.EventListAdapter;
import com.anhlt.maddiscover.services.EventService;

/**
 * Created by anhlt on 2/19/16.
 */
public class ListEvent extends ListFragment{

    Context context;
    EventService eventService;
    EventListAdapter adapter;
    static ListEvent listevent;

    public static ListEvent getInstance(){
        if(listevent==null)
            listevent = new ListEvent();
        return listevent;
    }

    @Override
    public void onAttach(Context context) {
        this.context =context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        eventService = new EventService(getActivity().getApplicationContext());
        adapter = new EventListAdapter(getActivity(),eventService.getEventList());
        setListAdapter(adapter);
        return inflater.inflate(R.layout.list_event, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        listFilter();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        long eventId = adapter.events.get((Integer)v.findViewById(R.id.checkbox).getTag()).getId();
        eventService.viewEvent(getFragmentManager(),context, eventId);
    }

    private void listFilter() {

        EditText filter = (EditText) getView().findViewById(R.id.filter);
        filter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
        });
    }

}
