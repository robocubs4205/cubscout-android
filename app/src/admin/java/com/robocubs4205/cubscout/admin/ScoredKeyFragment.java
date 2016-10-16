package com.robocubs4205.cubscout.admin;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robocubs4205.cubscout.R;

/**
 * Created by trevor on 10/14/16.
 */

public class ScoredKeyFragment extends Fragment
{
    public ScoredKeyFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_scored_key,container,false);
    }
}
