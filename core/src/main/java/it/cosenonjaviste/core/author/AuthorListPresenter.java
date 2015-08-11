package it.cosenonjaviste.core.author;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import it.cosenonjaviste.bind.BindableBoolean;
import it.cosenonjaviste.core.model.Author;
import it.cosenonjaviste.core.model.AuthorResponse;
import it.cosenonjaviste.core.model.WordPressService;
import it.cosenonjaviste.core.post.PostListModel;
import it.cosenonjaviste.lib.mvp.RxMvpListPresenterAdapter;
import rx.Observable;

public class AuthorListPresenter extends RxMvpListPresenterAdapter<Author, AuthorListModel, AuthorListView> {

    @Inject WordPressService wordPressService;

    @Inject public AuthorListPresenter() {
    }

    @Override public AuthorListModel createDefaultModel() {
        return new AuthorListModel();
    }

    public void loadDataPullToRefresh() {
        reloadData(loadingPullToRefresh);
    }

    public void reloadData() {
        reloadData(loading);
    }

    private void reloadData(BindableBoolean loadingAction) {
        loadingAction.set(true);

        Observable<List<Author>> observable = wordPressService
                .listAuthors()
                .map(AuthorResponse::getAuthors)
                .doOnNext(Collections::sort)
                .finallyDo(() -> loadingAction.set(false));

        subscribe(observable,
                this::done,
                throwable -> error());
    }

    @Override public void resume() {
        super.resume();
        if (!getModel().isLoaded() && !loading.get()) {
            reloadData();
        }
    }

    public void goToAuthorDetail(int position) {
        Author author = getModel().get(position);
        getView().openPostList(new PostListModel(author));
    }
}