package it.cosenonjaviste.lib.mvp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

import it.cosenonjaviste.mvp.base.RxMvpPresenter;

public class PresenterSaverFragment extends Fragment {

    private static final String TAG = PresenterSaverFragment.class.getName();

    private Map<Long, RxMvpPresenter<?>> presenters = new HashMap<>();

    public PresenterSaverFragment() {
        setRetainInstance(true);
    }

    public static void save(FragmentManager fragmentManager, RxMvpPresenter<?> presenter) {
        PresenterSaverFragment fragment = getPresenterSaverFragment(fragmentManager);
        fragment.presenters.put(presenter.getId(), presenter);
    }

    private static PresenterSaverFragment getPresenterSaverFragment(FragmentManager fragmentManager) {
        PresenterSaverFragment fragment = (PresenterSaverFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new PresenterSaverFragment();
            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }

    public static <P extends RxMvpPresenter<?>> P load(FragmentManager fragmentManager, long id) {
        PresenterSaverFragment fragment = getPresenterSaverFragment(fragmentManager);
        return (P) fragment.presenters.get(id);
    }

    protected static <P extends RxMvpPresenter<?>> P initPresenter(Bundle savedInstanceState, FragmentManager fragmentManager, PresenterFactory<P> presenterCreator) {
        P presenter = null;
        if (savedInstanceState != null) {
            long presenterId = savedInstanceState.getLong(RxMvpFragment.PRESENTER_ID, 0);
            if (presenterId != 0) {
                presenter = load(fragmentManager, presenterId);
            }
        }
        if (presenter == null) {
            presenter = presenterCreator.create();
            save(fragmentManager, presenter);
        }
        return presenter;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        for (RxMvpPresenter<?> presenter : presenters.values()) {
            presenter.destroy();
        }
    }

    public interface PresenterFactory<P> {
        P create();
    }
}
