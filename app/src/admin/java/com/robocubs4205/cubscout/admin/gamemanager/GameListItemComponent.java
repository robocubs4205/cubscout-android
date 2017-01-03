package com.robocubs4205.cubscout.admin.gamemanager;

import com.robocubs4205.cubscout.ActivityScope;
import com.robocubs4205.cubscout.net.NetComponent;

import dagger.Component;

/**
 * Created by trevor on 1/2/17.
 */

@ActivityScope
@Component(dependencies = NetComponent.class, modules = GameListItemModule.class)
public interface GameListItemComponent {
    public GameListItemPresenter presenter();
}
