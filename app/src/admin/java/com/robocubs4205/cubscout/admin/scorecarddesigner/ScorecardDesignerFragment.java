package com.robocubs4205.cubscout.admin.scorecarddesigner;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.robocubs4205.cubscout.JSONSerializableInputFragment;

abstract class ScorecardDesignerFragment extends Fragment  implements JSONSerializableInputFragment {
    private ScorecardDesignerFragmentListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ScorecardDesignerFragmentListener) context;
        } catch (ClassCastException e) {
            Log.e("ScorecardDesigner","Activity for ScorecardDesignerFragments must implement OnHeadlineSelectedListener");
        }

    }

    protected class RemoveButtonOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            listener.GameDesignerFragmentRemoveButtonClicked(ScorecardDesignerFragment.this);
        }
    }
    protected class UpButtonOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            listener.GameDesignerFragmentUpButtonClicked(ScorecardDesignerFragment.this);
        }
    }
    protected class DownButtonOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            listener.GameDesignerFragmentDownButtonClicked(ScorecardDesignerFragment.this);
        }
    }
}
