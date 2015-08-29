package it.cosenonjaviste.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import it.cosenonjaviste.R;
import it.cosenonjaviste.androidtest.base.MockWebServerWrapper;
import it.cosenonjaviste.androidtest.dagger.DaggerUtils;
import it.cosenonjaviste.core.TestData;
import it.cosenonjaviste.model.TwitterService;
import it.cosenonjaviste.model.WordPressService;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Inject WordPressService wordPressService;

    @Inject MockWebServerWrapper server;

    @Inject TwitterService twitterService;

    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Before public void setUp() {
        DaggerUtils.createTestComponent().inject(this);

        when(wordPressService.listPosts(eq(1)))
                .thenReturn(TestData.postResponse(10));
        when(wordPressService.listCategories())
                .thenReturn(TestData.categoryResponse(3));
        when(wordPressService.listAuthors())
                .thenReturn(TestData.authorResponse(2));
        when(wordPressService.listAuthorPosts(anyLong(), anyInt()))
                .thenReturn(TestData.postResponse(1));

        when(twitterService.loadTweets(eq(1)))
                .thenReturn(TestData.tweets());

        server.initDispatcher("<html><body>CoseNonJaviste</body></html>");
    }

    @Test public void showMainActivity() {
        activityRule.launchActivity(null);
    }

    @Test public void showCategories() {
        activityRule.launchActivity(null);
        clickOnDrawer(R.string.categories);
        onView(withText("cat 0")).check(matches(isDisplayed()));
    }

    @Test public void showAuthors() {
        activityRule.launchActivity(null);
        clickOnDrawer(R.string.authors);
        onView(withText("name 0")).check(matches(isDisplayed()));
    }

    @Test public void showTweets() {
        activityRule.launchActivity(null);
        clickOnDrawer(R.string.twitter);
        onView(withText("tweet text 1")).check(matches(isDisplayed()));
    }

    @Test public void showContactForm() {
        activityRule.launchActivity(null);
        clickOnDrawer(R.string.contacts);
    }

    private void clickOnDrawer(int text) {
        onView(withClassName(endsWith("ImageButton"))).perform(click());

        onView(withText(text)).perform(click());
    }
}