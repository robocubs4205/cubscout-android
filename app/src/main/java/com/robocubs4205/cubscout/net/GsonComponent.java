package com.robocubs4205.cubscout.net;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by trevor on 1/1/17.
 */

@Singleton
@Component(modules = GsonModule.class)
public interface GsonComponent {
    GsonRequest.InjectionHelper gsonRequestInjectionHelper();
}
