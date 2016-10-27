package com.robocubs4205.cubscout.admin;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.robocubs4205.cubscout.JSONSerializableInputFragment;

/**
 * Created by trevor on 10/26/16.
 */

abstract class GameDesignerFragment extends Fragment  implements JSONSerializableInputFragment {
    GameDesignerFragmentListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (GameDesignerFragmentListener) context;
        } catch (ClassCastException e) {
            Log.e("GameDesignerFragment"," must implement OnHeadlineSelectedListener");
        }

    }

    protected class RemoveButtonOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            listener.GameDesignerFragmentRemoveButtonClicked(GameDesignerFragment.this);
        }
    }
    protected class UpButtonOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            listener.GameDesignerFragmentUpButtonClicked(GameDesignerFragment.this);
        }
    }
    protected class DownButtonOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            listener.GameDesignerFragmentDownButtonClicked(GameDesignerFragment.this);
        }
    }
}
