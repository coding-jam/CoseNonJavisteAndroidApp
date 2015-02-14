package it.cosenonjaviste.mvp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.cosenonjaviste.model.Author;
import it.cosenonjaviste.model.AuthorResponse;
import it.cosenonjaviste.model.Category;
import it.cosenonjaviste.model.CategoryResponse;
import it.cosenonjaviste.model.Post;
import it.cosenonjaviste.model.PostResponse;
import rx.Observable;

public class TestData {

    public static Observable<PostResponse> postResponse(int size) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            posts.add(new Post(i, createAuthor(i), "title " + i, new Date(), "url " + i, "excerpt " + i));
        }
        return Observable.just(new PostResponse(posts));
    }

    public static Author createAuthor(int i) {
        return new Author(i, "name " + i, "last name " + i);
    }

    public static Observable<AuthorResponse> authorResponse(int size) {
        List<Author> authors = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            authors.add(createAuthor(i));
        }
        return Observable.just(new AuthorResponse(authors));
    }

    public static Observable<CategoryResponse> categoryResponse(int size) {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            categories.add(createCategory(i));
        }
        return Observable.just(new CategoryResponse(categories));
    }

    private static Category createCategory(int i) {
        return new Category(i, "cat " + i, 10 + i);
    }
}
