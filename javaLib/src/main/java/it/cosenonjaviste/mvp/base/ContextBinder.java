package it.cosenonjaviste.mvp.base;

import rx.Observable;
import rx.functions.Action1;

public interface ContextBinder {
    <T> Observable<T> bindObservable(Observable<T> observable);

    void showInActivity(String fragmentClassName, Action1<PresenterArgs> argsAction);

    void startNewActivity(Class<? extends MvpConfig<?, ?, ?>> config, Action1<PresenterArgs> argsAction);

    <T> T createFragment(MvpConfig<?, ?, ?> config, Action1<PresenterArgs> argsAction);

    <T> T getObject(Class<T> type);
}
