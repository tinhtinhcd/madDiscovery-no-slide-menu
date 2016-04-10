package com.anhlt.maddiscover.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import com.anhlt.maddiscover.services.ApplicationService;
import android.app.FragmentManager.OnBackStackChangedListener;

/**
 * Created by anhlt on 2/26/16.
 */


public class BaseFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() == 0)
                    getActivity().finish();
                else
                    getFragmentManager().popBackStack();
            }
        });
    }

    public void showErrorDialog(String title, String message){
        ApplicationService.showErrorDialog(title, message, getActivity());
    }



}
