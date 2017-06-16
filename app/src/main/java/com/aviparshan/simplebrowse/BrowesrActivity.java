package com.aviparshan.simplebrowse;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by avi on 4/12/2017 on com.aviparshan.simplebrowse
 */

public class BrowesrActivity extends AppCompatActivity {

    WebView myWebView;
    ProgressBar progress;
    private Toolbar mToolbar;
    String currentUrl = "https://www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_main);
        initToolbar();

//        myWebView = (WebView) findViewById(webview);
//        progress = (ProgressBar) findViewById(R.id.progressBar);
//
////        myWebView.getSettings().setSaveFormData(true);
////        myWebView.getSettings().setDatabaseEnabled(true);
////        myWebView.getSettings().setDomStorageEnabled(true);
//        //local offline loader
////        myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        myWebView.getSettings().setLoadWithOverviewMode(true);
//        myWebView.getSettings().setUseWideViewPort(true);
//        myWebView.getSettings().setJavaScriptEnabled(true);
//        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//
//        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        myWebView.getSettings().setSupportMultipleWindows(true);
//        myWebView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                currentUrl = url;
//                progress.setVisibility(ProgressBar.VISIBLE);
//                myWebView.setVisibility(View.INVISIBLE);
//                setTitle(currentUrl);
//
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript:window.android.onUrlChange(window.location.href);");
//            }
//            @Override
//            public void onPageCommitVisible(WebView view, String url) {
//                super.onPageCommitVisible(view, url);
//                currentUrl = url;
//                progress.setVisibility(ProgressBar.GONE);
//                myWebView.setVisibility(View.VISIBLE);
//                setTitle(currentUrl);
//
//            }
//
////            @SuppressWarnings("deprecation")
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                if (url.startsWith("tel:")) {
////                    //initiateCall(url);
////                    return true;
////                }
////                if (url.startsWith("mailto:")) {
////                    //sendEmail(url.substring(7));
////                    return true;
////                }
////                return false;
////            }
////
////            @TargetApi(Build.VERSION_CODES.N)
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////                String url=request.getUrl().toString();
////                if (url.startsWith("tel:")) {
////                    openURL(url);
////                    return true;
////                }
////                if (url.startsWith("mailto:")) {
////                    //sendEmail(url.substring(7));
////                    return true;
////                }
////                return false;
////            }
//        });
//        openURL(currentUrl);
//

    }


        @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            //Log.d("Cookies", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
//            Log.d("Cookies", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }
//
//    /**
//     * Opens the URL in a browser
//     */
//    private void openURL(String URL) {
//        Intent in = getIntent();
//        String tv1 = in.getExtras().getString("url");
//        myWebView.loadUrl(tv1);
//        setTitle(currentUrl);
//        myWebView.requestFocus();
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.cookies:
                clearCookies(BrowesrActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }



}
