package it.cosenonjaviste.category;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quentindommerc.superlistview.SuperGridview;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import it.cosenonjaviste.CoseNonJavisteApp;
import it.cosenonjaviste.R;
import it.cosenonjaviste.lib.mvp.RxMvpFragment;
import it.cosenonjaviste.post.PostListFragment;
import it.cosenonjaviste.post.PostListModel;
import it.cosenonjaviste.utils.SingleFragmentActivity;
import rx.functions.Actions;

public class CategoryListFragment extends RxMvpFragment implements CategoryListView {

    @InjectView(R.id.grid) SuperGridview grid;

    private CategoryAdapter adapter;

    @Inject CategoryListPresenter presenter;

    @Override public void init(Bundle state) {
        createComponent(
                () -> DaggerCategoryListComponent.builder().applicationComponent(CoseNonJavisteApp.getComponent(getActivity())).build()
        ).inject(this);
    }

    @SuppressLint("ResourceAsColor") @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.super_grid, container, false);
        ButterKnife.inject(this, view);
        adapter = new CategoryAdapter(getActivity());
        grid.setAdapter(adapter);
        grid.getList().setNumColumns(2);
        grid.setRefreshingColor(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        grid.setOnItemClickListener((parent, v, position, id) -> presenter.goToPosts(position));
        return view;
    }

    @OnClick(R.id.error_retry) void retry() {
        presenter.loadData();
    }


    @Override public void update(CategoryListModel model) {
        model.call(
                categories -> {
                    grid.showList();
                    adapter.reloadData(categories);
                }
        ).whenError(
                t -> grid.showError()
        ).whenEmpty(
                Actions.empty()
        );
    }

    @Override public void startLoading() {
        grid.showProgress();
    }

    @Override public void openPostList(PostListModel model) {
        SingleFragmentActivity.open(getActivity(), PostListFragment.class, model);
    }
}
