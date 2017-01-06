package com.robocubs4205.cubscout.admin.gamemanager;

/**
 * Created by trevor on 1/2/17.
 */

interface GameListView {
    void notifyListChanged();
    void notifyListItemChanged(int position);
    void showError(String error);
}
