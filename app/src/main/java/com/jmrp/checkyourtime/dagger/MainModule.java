package com.jmrp.checkyourtime.dagger;

import com.jmrp.checkyourtime.views.MainActivity;
import com.jmrp.checkyourtime.interactors.Interactor;
import com.jmrp.checkyourtime.presenters.MainActivityPresenter;

import dagger.Module;
import dagger.Provides;


@Module(injects = {MainActivity.class})
public class MainModule {

    @Provides
    public Interactor.PresenterMain provideMainActivityPresenter(){
        return new MainActivityPresenter();
    }
}
