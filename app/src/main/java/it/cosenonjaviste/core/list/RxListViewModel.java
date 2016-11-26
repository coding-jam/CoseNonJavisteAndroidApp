package it.cosenonjaviste.core.list;

import android.databinding.ObservableBoolean;

import it.cosenonjaviste.mv2m.rx.RxViewModel;

public abstract class RxListViewModel<A, M extends ListModel<?>> extends RxViewModel<A, M> implements GenericRxListViewModel {
    protected ObservableBoolean loading = new ObservableBoolean();

    protected ObservableBoolean loadingNextPage = new ObservableBoolean();

    protected ObservableBoolean loadingPullToRefresh = new ObservableBoolean();

    @Override public ObservableBoolean isLoading() {
        return loading;
    }

    @Override public ObservableBoolean isLoadingPullToRefresh() {
        return loadingPullToRefresh;
    }

    @Override public ObservableBoolean isLoadingNextPage() {
        return loadingNextPage;
    }

    @Override public ObservableBoolean isError() {
        return model.error;
    }

    @Override public void resume() {
        super.resume();
        if (!model.isLoaded() && !loading.get()) {
            reloadData();
        }
    }

    public void reloadData() {
        reloadData(loading);
    }

    public final void loadDataPullToRefresh() {
        reloadData(loadingPullToRefresh);
    }

    protected abstract void reloadData(ObservableBoolean loadingAction);
}
