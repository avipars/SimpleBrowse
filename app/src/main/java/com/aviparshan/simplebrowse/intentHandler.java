package com.aviparshan.simplebrowse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;

import static com.aviparshan.simplebrowse.R.id.toolbar;
import static com.aviparshan.simplebrowse.R.id.webview;

public class intentHandler extends AppCompatActivity {

    WebView myWebView;
    ProgressBar progress;
    private Toolbar mToolbar;
    String currentUrl = "https://www.google.com";
    String combine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_main);
        initToolbar();

        myWebView = (WebView) findViewById(webview);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        mToolbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.getSettings().setSupportMultipleWindows(true);

        Intent intent = getIntent();
        String action = intent.getAction();
                if (!action.equals(Intent.ACTION_VIEW)) {
                throw new RuntimeException("Should not happen");
           }

            Uri data = getIntent().getData();//set a variable for the Intent
            String scheme = data.getScheme();//get the scheme (http,https)
            String fullPath = data.getEncodedSchemeSpecificPart();//get the full path -scheme - fragments

            String combine = scheme+"://"+fullPath; //combine to get a full URI

                 String url = null;//declare variable to hold final URL
                  url = combine;


        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                currentUrl = url;
                progress.setVisibility(ProgressBar.VISIBLE);
                myWebView.setVisibility(View.INVISIBLE);
                setTitle(currentUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.android.onUrlChange(window.location.href);");
            }
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                currentUrl = url;
                progress.setVisibility(ProgressBar.GONE);
                myWebView.setVisibility(View.VISIBLE);
                setTitle(currentUrl);
            }
        });
        openURL(url);

    }
    /**
     * Opens the URL in a browser
     */
    private void openURL(String URL) {
//        Intent in = getIntent();
//        String tv1 = in.getExtras().getString("url");
        myWebView.loadUrl(URL);
        setTitle(currentUrl);
        myWebView.requestFocus();
    }


    private String ErrorCheckComplete(EditText etText) {
        String text = etText.getText().toString();
        etText.setText(ErrorCheck(text));
        return ErrorCheck(text);
    }
    private String ErrorCheck(String etText){

        String tv2;
        if (!etText.startsWith("http://") && !etText.startsWith("https://")) {
            tv2 = "http://" + etText;
        }
        else {
            tv2 = etText;
        }
        //txt.setText(tv2);
        return tv2;

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
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


    public void showInputDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.change)
                .content(R.string.enterURl)
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText(R.string.submit)
                .input(R.string.url_a, R.string.url_a, false, (dialog, input) -> {
                    String url = input.toString();
                    openURL(ErrorCheck(url));

                })                            .show();

    }
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
                clearCookies(intentHandler.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
       // getActionBar().setDisplayHomeAsUpEnabled(true);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
