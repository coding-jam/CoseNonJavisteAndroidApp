package it.cosenonjaviste.twitter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.ParcelClass;

import javax.inject.Inject;

import it.cosenonjaviste.CoseNonJavisteApp;
import it.cosenonjaviste.databinding.TweetRowBinding;
import it.cosenonjaviste.model.Tweet;
import it.cosenonjaviste.utils.BindableViewHolder;
import it.cosenonjaviste.utils.RecyclerViewRxMvpFragment;

@ParcelClass(TweetListModel.class)
public class TweetListFragment extends RecyclerViewRxMvpFragment<Tweet> implements TweetListView {

    @Inject TweetListPresenter presenter;

    @Override public void init() {
        CoseNonJavisteApp.createComponent(this,
                c -> DaggerTweetListComponent.builder().applicationComponent(c).build()
        ).inject(this);
        addListener(presenter);
    }

    @NonNull @Override protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding.setPresenter(presenter);
        binding.swipeRefresh.setOnRefreshListener(presenter::loadDataPullToRefresh);
        presenter.setListChangeListener(adapter::reloadData);
        return view;
    }

    @Override protected void loadMoreItems() {
        presenter.loadNextPage();
    }

    @NonNull @Override protected BindableViewHolder<Tweet> createViewHolder(LayoutInflater inflater, ViewGroup v) {
        return new TweetViewHolder(TweetRowBinding.inflate(inflater, v, false));
    }

    @Override protected void retry() {
        presenter.reloadData();
    }
}