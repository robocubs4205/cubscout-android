package com.robocubs4205.cubscout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndividualReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndividualReportFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListIndividualReportFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public ListIndividualReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_individual_report_fragment, container, false);
        if (getArguments() != null) {
            ((TextView) view.findViewById(R.id.team_name_header)).setText(((Integer)getArguments().getInt("team")).toString());
            ((TextView) view.findViewById(R.id.average_high_goal_text)).setText(((Float) getArguments().getFloat("avgHighGoal")).toString());
            if(getArguments().getFloat("avgHighGoal") + getArguments().getFloat("avgHighMiss") != 0)
            {
                ((TextView) view.findViewById(R.id.high_goal_accuracy_text)).setText(String.format("%.2f%%", ((Float) (getArguments().getFloat("avgHighGoal") / (getArguments().getFloat("avgHighGoal") + getArguments().getFloat("avgHighMiss")) * 100))));
            }
            else
            {
                ((TextView) view.findViewById(R.id.high_goal_accuracy_text)).setText("Did not attempt");
            }
            ((TextView) view.findViewById(R.id.average_low_goal_text)).setText(((Float) getArguments().getFloat("avgLowGoal")).toString());
            if(getArguments().getFloat("avgLowGoal") + getArguments().getFloat("avgLowMiss") != 0)
            {
                ((TextView) view.findViewById(R.id.low_goal_accuracy_text)).setText(String.format("%.2f%%", ((Float) (getArguments().getFloat("avgLowGoal") / (getArguments().getFloat("avgLowGoal") + getArguments().getFloat("avgLowMiss")) * 100))));
            }
            else
            {
                ((TextView) view.findViewById(R.id.low_goal_accuracy_text)).setText("Did not attempt");
            }
            ((TextView) view.findViewById(R.id.average_total_cross_text)).setText(((Float) getArguments().getFloat("avgTotalCross")).toString());
            if (getArguments().getBoolean("didDefense")) {
                ((TextView) view.findViewById(R.id.defense_rating_text)).setText(((Float) getArguments().getFloat("avgDefense")).toString());
            } else {
                ((TextView) view.findViewById(R.id.defense_rating_text)).setText("Robot did not participate in Defense");
            }
            List<Pair<String, Float>> obstacleCrossings = new ArrayList<>();
            obstacleCrossings.add(new Pair<>("Cheval De Frise", getArguments().getFloat("avgCheval")));
            obstacleCrossings.add(new Pair<>("DrawBridge", getArguments().getFloat("avgDrawBridge")));
            obstacleCrossings.add(new Pair<>("Low Bar", getArguments().getFloat("avgLowBar")));
            obstacleCrossings.add(new Pair<>("Moat", getArguments().getFloat("avgMoat")));
            obstacleCrossings.add(new Pair<>("Portcullis", getArguments().getFloat("avgPortcullis")));
            obstacleCrossings.add(new Pair<>("Rampart", getArguments().getFloat("avgRampart")));
            obstacleCrossings.add(new Pair<>("Rock Wall", getArguments().getFloat("avgRockWall")));
            obstacleCrossings.add(new Pair<>("Rough Terrain", getArguments().getFloat("avgRoughTerrain")));
            obstacleCrossings.add(new Pair<>("Sally Port", getArguments().getFloat("avgSallyPort")));
            Collections.sort(obstacleCrossings,new ObstacleComparator());
            ((TextView) view.findViewById(R.id.first_obstacle_preference_text)).setText(obstacleCrossings.get(0).first);
            ((TextView) view.findViewById(R.id.second_obstacle_preference_text)).setText(obstacleCrossings.get(1).first);
            ((TextView) view.findViewById(R.id.third_obstacle_preference_text)).setText(obstacleCrossings.get(2).first);
        }
        return view;
    }

    private class ObstacleComparator implements Comparator<Pair<String,Float>>
    {
        @Override
        public int compare(Pair<String, Float> a, Pair<String, Float> b) {
            if(a.second>b.second)
            {
                return -1;
            }
            else if(a.second<b.second)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
