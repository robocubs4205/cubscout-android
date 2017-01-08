package com.robocubs4205.cubscout.admin.gamemanager;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.R;
import com.robocubs4205.cubscout.net.DaggerNetComponent;
import com.robocubs4205.cubscout.net.NetModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by trevor on 12/27/16.
 */

public class GameManagerFragment extends Fragment implements GameListView {
    private final List<Game> games = new ArrayList<>();
    private RecyclerView gameListView;
    private GameListAdapter adapter;
    private LinearLayoutManager layoutManager;
    private GameListPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = DaggerGameListComponent.builder().gameListModule(
                new GameListModule(this, games)).netComponent(
                DaggerNetComponent.builder().netModule(new NetModule(getContext())).build()).build()
                                           .presenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_manager, parent, false);
        gameListView = (RecyclerView) view.findViewById(R.id.game_list);
        adapter = new GameListAdapter(games);
        gameListView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        gameListView.setLayoutManager(layoutManager);
        gameListView.addItemDecoration(
                new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        return view;
    }

    @Override
    public void notifyListChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyListItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void notifyListItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyListItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.game_name_text)
        TextView gameNameView;
        @BindView(R.id.game_detail_text)
        TextView gameTypeView;
        @BindView(R.id.edit_button)
        Button editButton;
        @BindView(R.id.expand_divider)
        View expandDivider;

        GameListView mView;

        private boolean isShowingDetail = false;

        public ViewHolder(View itemView, GameListView view) {
            super(itemView);
            mView = view;
            ButterKnife.bind(this, itemView);
        }

        public void bindGame(Game game) {
            gameNameView.setText(game.name);
            gameTypeView.setText(
                    String.format(Locale.getDefault(), "%s (%s)", game.type, game.year));
        }

        public void showDetail() {
            editButton.setVisibility(View.VISIBLE);
            expandDivider.setVisibility(View.VISIBLE);
        }

        @OnClick(R.id.edit_button)
        public void editButtonClicked() {

        }

        @OnClick
        public void expandButtonClicked() {
            if (isShowingDetail) hideDetail();
            else showDetail();
            isShowingDetail = !isShowingDetail;
        }

        private void hideDetail() {
            editButton.setVisibility(View.GONE);
            expandDivider.setVisibility(View.GONE);
        }
    }

    private class GameListAdapter extends RecyclerView.Adapter<ViewHolder> {

        final List<Game> mGames;

        public GameListAdapter(List<Game> games) {
            super();
            mGames = games;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                                                .inflate(R.layout.item_game_manager_game,
                                                         parent, false), GameManagerFragment.this);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            holder.bindGame(mGames.get(position));
        }

        @Override
        public int getItemCount() {
            return mGames.size();
        }
    }

}
