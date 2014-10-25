package it.cosenonjaviste.mvp.post;

import javax.inject.Inject;
import javax.inject.Provider;

import it.cosenonjaviste.mvp.base.MvpConfig;

public class PostListMvpConfig implements MvpConfig<PostListModel,PostListView,PostListPresenter> {

    @Inject Provider<PostListView> viewProvider;

    @Inject PostListPresenter presenter;

    @Override public PostListView createView() {
        return viewProvider.get();
    }

    @Override public PostListPresenter createPresenter() {
        return presenter;
    }
}
