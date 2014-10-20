package it.cosenonjaviste.mvp.base;


import it.cosenonjaviste.mvp.base.events.EndLoadingModelEvent;
import it.cosenonjaviste.mvp.base.events.ModelEvent;
import it.cosenonjaviste.mvp.base.pausable.CompositePausableSubscription;
import it.cosenonjaviste.mvp.base.pausable.PausableSubscriptions;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observers.Observers;
import rx.subjects.PublishSubject;

public abstract class RxMvpPresenter<M> {
    protected M model;

    protected CompositePausableSubscription pausableSubscriptions = new CompositePausableSubscription();

    protected ContextBinder contextBinder;

    private boolean newModelCreated;

    protected Navigator navigator;

    private long id;

    private PublishSubject<ModelEvent<M>> modelUpdates = PublishSubject.create();

    public Observable<ModelEvent<M>> getModelUpdates() {
        return modelUpdates.asObservable();
    }

    public void saveInBundle(ObjectSaver<M> objectSaver) {
        objectSaver.saveInBundle(model);
    }

    public M init(ContextBinder contextBinder, ObjectSaver<M> objectSaver, PresenterArgs args, Navigator navigator) {
        this.contextBinder = contextBinder;
        this.navigator = navigator;
        model = objectSaver.loadFromBundle();
        if (model == null) {
            newModelCreated = true;
            model = createModel(args);
        }
        return model;
    }

    protected void loadOnFirstStart() {
    }

    protected abstract M createModel(PresenterArgs args);

    protected void publish(ModelEvent<M> event) {
        modelUpdates.onNext(event);
    }

    public void subscribe() {
        if (pausableSubscriptions != null) {
            pausableSubscriptions.resume();
        }
        if (newModelCreated) {
            loadOnFirstStart();
            newModelCreated = false;
        }
        publish(new EndLoadingModelEvent<>(model));
    }

    public void pause() {
        if (pausableSubscriptions != null) {
            pausableSubscriptions.pause();
        }
    }

    public void destroy() {
        if (pausableSubscriptions != null) {
            pausableSubscriptions.destroy();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    protected <T> void subscribePausable(Observable<T> observable, Observer<T> observer) {
        pausableSubscriptions.add(PausableSubscriptions.subscribe(contextBinder.bindObservable(observable), observer));
    }

    protected <T> void subscribePausable(Observable<T> observable, Action1<? super T> onNext, Action1<Throwable> onError) {
        pausableSubscriptions.add(PausableSubscriptions.subscribe(contextBinder.bindObservable(observable), Observers.create(onNext, onError)));
    }

    protected <T> void subscribePausable(Observable<T> observable, Action0 onAttach, Action1<? super T> onNext, Action1<Throwable> onError) {
        pausableSubscriptions.add(PausableSubscriptions.subscribe(contextBinder.bindObservable(observable), onAttach, Observers.create(onNext, onError)));
    }

    protected <T> void subscribePausable(Observable<T> observable, Action0 onAttach, Action1<? super T> onNext, Action1<Throwable> onError, Action0 onCompleted) {
        pausableSubscriptions.add(PausableSubscriptions.subscribe(contextBinder.bindObservable(observable), onAttach, Observers.create(onNext, onError, onCompleted)));
    }

    protected <T> void subscribePausable(Observable<T> observable, Action1<? super T> onNext, Action1<Throwable> onError, Scheduler scheduler) {
        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler);
        }
        observable = contextBinder.bindObservable(observable);
        pausableSubscriptions.add(PausableSubscriptions.subscribe(observable, Observers.create(onNext, onError)));
    }

    protected <T> void subscribePausable(Observable<T> observable, Action1<? super T> onNext, Action1<Throwable> onError, Action0 onCompleted) {
        pausableSubscriptions.add(PausableSubscriptions.subscribe(contextBinder.bindObservable(observable), Observers.create(onNext, onError, onCompleted)));
    }
}
