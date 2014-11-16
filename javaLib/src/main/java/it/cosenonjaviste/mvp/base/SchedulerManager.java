package it.cosenonjaviste.mvp.base;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class SchedulerManager {
    public static Scheduler io = Schedulers.io();

    public static <T> Observable<T> background(Observable<T> observable) {
        return observable.subscribeOn(io).observeOn(mainThread());
    }

    public static void setIo(Scheduler io) {
        SchedulerManager.io = io;
    }

    public <T> Observable<T> bindObservable(Observable<T> observable) {
        return background(observable);
    }
}