package it.cosenonjaviste.core.twitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import it.cosenonjaviste.core.ParcelableTester;
import it.cosenonjaviste.core.TestData;
import it.cosenonjaviste.model.TwitterService;
import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TweetListViewModelTest {

    @Mock TweetListView view;

    @Mock TwitterService twitterService;

    @InjectMocks TweetListViewModel viewModel;

    @Test
    public void testParcelable() {
        TweetListModel model = new TweetListModel();
        ParcelableTester.check(model, TweetListModel.CREATOR);

        model.done(Arrays.asList(TestData.createTweet(1), TestData.createTweet(2)));
        ParcelableTester.check(model, TweetListModel.CREATOR);
    }

    @Test public void testLoadTweets() {
        Mockito.when(twitterService.loadTweets(Matchers.eq(1)))
                .thenReturn(TestData.tweets(10));

        TweetListModel model = viewModel.initAndResume(view);

        assertThat(model.getItems()).hasSize(10);
    }

    @Test public void testRetryAfterError() {
        Mockito.when(twitterService.loadTweets(Matchers.eq(1)))
                .thenReturn(Observable.error(new RuntimeException()));

        TweetListModel model = viewModel.initAndResume(view);

        assertThat(viewModel.isError().get()).isTrue();

        Mockito.when(twitterService.loadTweets(Matchers.eq(1)))
                .thenReturn(TestData.tweets(10));

        viewModel.reloadData();

        assertThat(viewModel.isError().get()).isFalse();
        assertThat(model.getItems()).hasSize(10);
    }

    @Test public void testLoadMoreTweets() {
        Mockito.when(twitterService.loadTweets(Matchers.eq(1)))
                .thenReturn(TestData.tweets(20));
        Mockito.when(twitterService.loadTweets(Matchers.eq(2)))
                .thenReturn(TestData.tweets(8));

        TweetListModel tweetListModel = viewModel.initAndResume(view);

        assertThat(tweetListModel.getItems()).hasSize(20);

        viewModel.loadNextPage();

        assertThat(tweetListModel.getItems()).hasSize(28);
    }
}
