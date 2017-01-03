package com.robocubs4205.cubscout.admin.gamemanager;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.robocubs4205.cubscout.Game;
import com.robocubs4205.cubscout.R;
import com.robocubs4205.cubscout.net.DaggerNetComponent;
import com.robocubs4205.cubscout.net.NetModule;

import java.util.ArrayList;
import java.util.List;

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
    private MyLayoutManager layoutManager;
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
        layoutManager = new MyLayoutManager(getContext());
        gameListView.setLayoutManager(layoutManager);
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
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDetail(int position) {
        adapter.showDetail(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewSwitcher switcher;
        @BindView(R.id.game_name_text)
        TextView gameNameView;
        @BindView(R.id.game_type_text)
        TextView gameTypeView;

        GameListView mView;

        public ViewHolder(View itemView, GameListView view) {
            super(itemView);
            mView = view;
            ButterKnife.bind(this, itemView);
            switcher = (ViewSwitcher) itemView.getRootView();
        }

        public void bindGame(Game game) {
            gameNameView.setText(game.name);
            gameTypeView.setText(game.type);
        }

        public void showDetail() {
            switcher.showNext();
        }

        @OnClick(View.NO_ID)
        public void onClicked() {
            mView.showDetail(getAdapterPosition());
        }
    }

    private class GameListAdapter extends RecyclerView.Adapter<ViewHolder> {

        List<Game> mGames;

        public GameListAdapter(List<Game> games) {
            super();
            mGames = games;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                                                .inflate(R.layout.item_game_manager_game,
                                                         parent, false),GameManagerFragment.this);
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

        public void showDetail(int position) {
            gameListView.scrollToPosition(position);
            layoutManager.setScrollEnabled(false);
            ((ViewHolder) gameListView.findViewHolderForAdapterPosition(position)).showDetail();
        }
    }

    private class MyLayoutManager extends LinearLayoutManager {

        private boolean isScrollEnabled = true;

        public MyLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            return isScrollEnabled && super.canScrollVertically();
        }
    }
}
