package it.cosenonjaviste.post;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.parceler.ParcelClass;
import org.parceler.ParcelClasses;

import java.io.IOException;
import java.io.InputStream;

import butterknife.InjectView;
import it.cosenonjaviste.CnjFragment;
import it.cosenonjaviste.R;
import it.cosenonjaviste.mvp.base.ViewImplementation;
import it.cosenonjaviste.mvp.page.PageModel;
import it.cosenonjaviste.mvp.page.PagePresenter;
import it.cosenonjaviste.mvp.page.PageView;

@ViewImplementation
@ParcelClasses({@ParcelClass(PageModel.class)})
public class PageFragment extends CnjFragment<PagePresenter, PageModel> implements PageView {

    @InjectView(R.id.web_view) WebView webView;

    @InjectView(R.id.progress) View progressBar;

    @Override public Class<PageView> getConfig() {
        return PageView.class;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override protected void initView(View view) {
        super.initView(view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.equalsIgnoreCase("http://www.cosenonjaviste.it/wp-content/themes/flexform/style.css")) {
                    return getCssWebResourceResponseFromAsset();
                }
                if (url.startsWith("https://pbs.twimg.com/")
                        || url.startsWith("https://cdn.syndication.twimg.com/")
                        || url.startsWith("https://syndication.twitter.com")
                        || url.contains("platform.twitter.com/")
                        || url.startsWith("http://www.facebook.com/plugins/like_box.php")
                        || url.startsWith("https://fbcdn-profile-")
                        || url.contains("sharethis.com/")
                        || url.equals("http://www.cosenonjaviste.it/wp-content/uploads/2013/06/favicon.ico")
                        ) {
                    return null;
                }
                return super.shouldInterceptRequest(view, url);
            }

            private WebResourceResponse getCssWebResourceResponseFromAsset() {
                try {
                    return getUtf8EncodedCssWebResourceResponse(getActivity().getAssets().open("style.css"));
                } catch (IOException e) {
                    return null;
                }
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB) private WebResourceResponse getUtf8EncodedCssWebResourceResponse(InputStream data) {
                return new WebResourceResponse("text/css", "UTF-8", data);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override protected int getLayoutId() {
        return R.layout.post_detail;
    }

    @Override public void update(PageModel model) {
        webView.loadUrl(presenter.getPostUrl(model.getUrl()));
    }
}
