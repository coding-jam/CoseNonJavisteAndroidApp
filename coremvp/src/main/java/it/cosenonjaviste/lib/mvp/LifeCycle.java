package it.cosenonjaviste.lib.mvp;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

@PresenterScope
public class LifeCycle {

    private Action1<Action2<String, Object>> saveObjectFunc;
    private Action1<Func1<String, Object>> loadAction;

    public class Event {
        private final Object source;

        private final EventType type;

        public Event(Object source, EventType type) {
            this.source = source;
            this.type = type;
        }

        public Object getSource() {
            return source;
        }

        public EventType getType() {
            return type;
        }
    }

    public enum EventType {
        RESUME, PAUSE, DESTROY_VIEW, DESTROY_ALL;
    }

    private PublishSubject<Event> subject = PublishSubject.create();

    @Inject public LifeCycle() {
    }

    public void emit(Object source, EventType eventType) {
        subject.onNext(new Event(source, eventType));
    }

    public Observable<Event> asObservable() {
        return subject.asObservable();
    }

    public Subscription subscribe(EventType eventType, Action0 callback) {
        return asObservable().filter(e -> e.getType() == eventType).subscribe(t -> callback.call());
    }

    public void subscribeState(Action1<Action2<String, Object>> saveObjectFunc, Action1<Func1<String, Object>> loadAction) {
        this.saveObjectFunc = saveObjectFunc;
        this.loadAction = loadAction;
    }

    public void saveState(Action2<String, Object> saver) {
        if (saveObjectFunc != null) {
            saveObjectFunc.call(saver);
        }
    }

    public void loadState(Func1<String, Object> loader) {
        loadAction.call(loader);
    }
}