package com.jmrp.checkyourtime.dagger;

import com.jmrp.checkyourtime.interactors.Interactor;
import com.jmrp.checkyourtime.presenters.ResultActivityPresenter;
import com.jmrp.checkyourtime.views.ResultActivity;

import dagger.Module;
import dagger.Provides;


@Module(injects = {ResultActivity.class})
public class ResultModule {

    @Provides
    public Interactor.PresenterResult provideResultActivityPresenter(){
        return new ResultActivityPresenter();
    }
}
