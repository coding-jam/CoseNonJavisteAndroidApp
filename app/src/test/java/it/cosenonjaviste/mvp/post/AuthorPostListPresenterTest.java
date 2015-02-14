package it.cosenonjaviste.mvp.post;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;

import dagger.Module;
import dagger.ObjectGraph;
import it.cosenonjaviste.model.WordPressService;
import it.cosenonjaviste.mvp.MvpJUnitTestModule;
import it.cosenonjaviste.mvp.TestData;
import it.cosenonjaviste.post.PostListFragment;
import it.cosenonjaviste.post.PostListModel;
import it.cosenonjaviste.post.PostListPresenter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorPostListPresenterTest {

    @Mock PostListFragment view;

    @Inject PostListPresenter presenter;

    @Inject WordPressService wordPressService;

    @Before
    public void setup() {
        ObjectGraph.create(getTestModule()).inject(this);
    }

    protected Object getTestModule() {
        return new TestModule();
    }

    @Test
    public void testLoad() throws InterruptedException {
        when(wordPressService.listAuthorPosts(anyLong(), anyInt()))
                .thenReturn(TestData.postResponse(1));

        PostListModel model = new PostListModel(TestData.createAuthor(145));

        presenter.initAndSubscribe(model, view);

        assertThat(model.getItems().size()).isEqualTo(1);
        verify(wordPressService).listAuthorPosts(eq(145L), eq(1));
    }

    @Module(injects = {AuthorPostListPresenterTest.class}, includes = MvpJUnitTestModule.class)
    public static class TestModule {
    }
}