package com.anhlt.maddiscover.fragments.venue;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.adapter.VenueListAdapter;
import com.anhlt.maddiscover.services.VenueService;

/**
 * Created by anhlt on 2/18/16.
 */
public class ListVenue extends ListFragment{

    VenueService venueService;
    VenueListAdapter adapter;
    Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        venueService = new VenueService(getActivity().getApplicationContext());
        adapter = new VenueListAdapter(getActivity(),venueService.getVenueList());
        setListAdapter(adapter);
        return inflater.inflate(R.layout.list_venue,container,false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_venue, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        long orgId = adapter.venues.get((Integer) v.findViewById(R.id.venue_item).getTag()).getId();
        venueService.viewDetails(getFragmentManager(), context, orgId);
    }

    @Override
    public void onStart() {
        super.onStart();
        listFilter();
    }

    private void listFilter(){

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
