package com.aviparshan.simplebrowse.Browse;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aviparshan.simplebrowse.Main.AboutDialog;
import com.aviparshan.simplebrowse.Main.MainActivity;
import com.aviparshan.simplebrowse.R;

import static android.webkit.WebView.HitTestResult.SRC_ANCHOR_TYPE;
import static com.aviparshan.simplebrowse.R.id.toolbar;
import static com.aviparshan.simplebrowse.R.id.webview;

/**
 * Created by avi on 4/12/2017 on com.aviparshan.simplebrowse
 * Takes in intent from Regular activity and also intent. Runs error check and then proceeeds to render the page
 */

public class BrowserActivity extends AppCompatActivity {

    WebView myWebView;
    ProgressBar progress;
    private Toolbar mToolbar;
    CoordinatorLayout contain;
    String currentUrl = "https://www.google.com";
    static String mURL;

    String search = "https://www.google.co.il/search?q=";
    String requestURL = String.format("https://www.google.com/?a=%s&b=%s", Uri.encode("foo bar"), Uri.encode("100% fubar'd"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_main);
        initToolbar();

        contain = (CoordinatorLayout) findViewById(R.id.main_content);
        myWebView = (WebView) findViewById(webview);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        myWebView.getSettings().setUseWideViewPort(false);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSupportMultipleWindows(false);
        myWebView.getSettings().setSupportZoom(true);
        // Enabling built in zooming shows the controls by default
        myWebView.getSettings().setBuiltInZoomControls(true);
        // So we hide the controls after enabling zooming
        myWebView.getSettings().setDisplayZoomControls(false);
        // To respect the html viewport:
        myWebView.getSettings().setLoadWithOverviewMode(true);
        // Also increase text size to fill the viewport (this mirrors the behaviour of Firefox,
        // Chrome does this in the current Chrome Dev, but not Chrome release).
        //myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        // Disable access to arbitrary local files by webpages - assets can still be loaded
        // via file:///android_asset/res, so at least error page images won't be blocked.
        myWebView.getSettings().setAllowFileAccess(false);
        myWebView.getSettings().setAllowFileAccessFromFileURLs(false);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        // Right now I do not know why we should allow loading content from a content provider
        myWebView.getSettings().setAllowContentAccess(false);
        myWebView.setVerticalScrollBarEnabled(true);
        myWebView.setHorizontalScrollBarEnabled(true);

        // The default for those myWebView.getSettings() should be "false" - But we want to be explicit.
        myWebView.getSettings().setAppCacheEnabled(false);
        myWebView.getSettings().setDatabaseEnabled(false);
        myWebView.getSettings().setDomStorageEnabled(false);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        // We do not implement the callbacks - So let's disable it.
        myWebView.getSettings().setGeolocationEnabled(false);
        // We do not want to save any data...
        myWebView.getSettings().setSaveFormData(false);
        //noinspection deprecation - This method is deprecated but let's call it in case WebView implementations still obey it.
        myWebView.getSettings().setSavePassword(false);
        myWebView.setLongClickable(true);
        myWebView.setFocusable(true);
        myWebView.setFocusableInTouchMode(true);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        myWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture,
                                          Message resultMsg) {
                WebView.HitTestResult result = view.getHitTestResult();
                String data = result.getExtra();
               // Log.d("DATA", "" + data);
                 view.loadUrl(data);

                WebView newWebView = new WebView(view.getContext());
                WebView.WebViewTransport transport =
                        (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);
                resultMsg.sendToTarget();
                return true;
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                result.cancel();
                return true;
            }

        });

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
                                           view.loadUrl("javascript:window.android.onUrlChange(window.location.href);");  //if url changes update the current url
                                       }

                                       @Override
                                       public void onPageCommitVisible(WebView view, String url) {
                                           super.onPageCommitVisible(view, url);
                                           currentUrl = url;
                                           progress.setVisibility(ProgressBar.GONE);
                                           myWebView.setVisibility(View.VISIBLE);
                                           setTitle(currentUrl);
                                       }

                                    @SuppressWarnings("deprecation")
                                    @Override
                                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                        // Handle the error
                                        //Toast.makeText(BrowserActivity.this, "Error: " + errorCode +  description + failingUrl, Toast.LENGTH_SHORT).show();
                                        errorDialog(description,failingUrl);                                    }

                                    @TargetApi(android.os.Build.VERSION_CODES.M)
                                    @Override
                                    public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                                        // Redirect to deprecated method, so you can use it in all SDK versions
                                        onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                                    }

                                        @SuppressWarnings("deprecation")
                                       @Override
                                       public boolean shouldOverrideUrlLoading(WebView wv, String url) {
                                           if (url.startsWith("tel:")) {
                                               Intent intent = new Intent(Intent.ACTION_DIAL);
                                               intent.setData(Uri.parse(url));
                                               startActivity(intent);
                                               return true;
                                           }
                                           if (url.startsWith("mailto:")) {
                                               sendEmail(url.substring(7));
                                               return true;
                                           }
                                           if (url.startsWith("market://")) {
                                               Intent intent = new Intent(Intent.ACTION_VIEW);
                                               intent.setData(Uri.parse(url));
                                               startActivity(intent);
                                               return true;
                                           }
                                           return false;
                                       }

                                       @TargetApi(Build.VERSION_CODES.N)
                                       @Override
                                       public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                           final Uri uri = request.getUrl();
                                           if (uri.toString().startsWith("mailto:")) {
                                               //Handle mail Urls
                                               sendEmail(uri.toString().substring(7));
                                               return true;
                                           }
                                           if (uri.toString().startsWith("tel:")) {
                                               //Handle telephony Urls
                                               startActivity(new Intent(Intent.ACTION_DIAL, uri));
                                               return true;
                                           }
                                           if (uri.toString().startsWith("market://")) {
                                               Intent intent = new Intent(Intent.ACTION_VIEW);
                                               intent.setData((uri));
                                               startActivity(intent);
                                               return true;
                                           }
                                           if (uri.toString().endsWith(".mp4")) {
                                               Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uri.toString()));
                                               view.getContext().startActivity(intent);
                                               return true;
                                           }
                                           return false;

                                       }
                                   });

        mToolbar.setOnClickListener(view -> showInputDialog(currentUrl));

        myWebView.setOnLongClickListener(v -> {
            unregisterForContextMenu(myWebView);
            WebView.HitTestResult result = myWebView.getHitTestResult();
            if(result.getType() == SRC_ANCHOR_TYPE) {
                Message msg = new Message();
                msg.setTarget(new MyHandler());
                myWebView.requestFocusNodeHref(msg);
                registerForContextMenu(myWebView);
            }
            return false;
        });

        Bundle bundle = getIntent().getExtras();
       //Extract the data…
        String passedURL = bundle.getString("link");
        openURL(passedURL);

    }

    /*
 * Used to get the result of requestFocusNodeHref(msg)
 */
        private static class MyHandler extends Handler{
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // Do something with it.
               // mURL = (String) msg.getData().get("url");
                String url = (String) msg.getData().get("url");
                // Do something with it.
                if (url != null)
                {
                    mURL = url;
                }
            }
        }

    @Override //Maintains state on orientation change
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.click, menu);
        //menu.setHeaderTitle(mURL);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.copy:
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("URI", mURL);
                    clipboard.setPrimaryClip(clip);
                return true;
            case R.id.share: {
                shareText(getString(R.string.sharedby), mURL);
            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


        /**
         * Opens the URL in a browser
         */
    private void openURL(String URL) {
        //String checked = ErrorCheck(URL);
        myWebView.loadUrl(URL);
        setTitle(currentUrl);
        myWebView.requestFocus();
    }


    private String ErrorCheck(String etText){
        String tv2;
        if (!etText.startsWith("http://") && !etText.startsWith("https://")) {
            tv2 = "http://" + etText;
        }
        else {
            tv2 = etText;
        }
        return tv2;
    }


    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
            Toast.makeText(context, R.string.cookies, Toast.LENGTH_SHORT).show();

        }



    public void destroyWebView() {
        // Make sure you remove the WebView from its parent view before doing anything.
        contain.removeAllViews();
        myWebView.clearHistory();
        // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
        // Probably not a great idea to pass true if you have other WebViews still alive.
        myWebView.clearCache(true);
        // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
        myWebView.loadUrl("about:blank");

        myWebView.onPause();
        myWebView.removeAllViews();
        myWebView.destroyDrawingCache();

        // NOTE: This pauses JavaScript execution for ALL WebViews,
        // do not use if you have other WebViews still alive.
        // If you create another WebView after calling this,
        // make sure to call myWebView.resumeTimers().
        // myWebView.pauseTimers();

        // NOTE: This can occasionally cause a segfault below API 17 (4.2)
        myWebView.destroy();

        // Null out the reference so that you don't end up re-using it.
        myWebView = null;
        Toast.makeText(this, R.string.history, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    public void showInputDialog(String cURL) {
        new MaterialDialog.Builder(this)
                .title(R.string.change)
                .content(R.string.enterURl)
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_URI
                                )
                .positiveText(R.string.submit)
                .input("http://", cURL, false, (dialog, input) -> {
                    String url = input.toString();
                    openURL(ErrorCheck(url));

                }).show();

    }

    public void errorDialog(String error, String cURL) {
        new MaterialDialog.Builder(this)
                .title(error)
                .content(R.string.diffUrl)
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_URI
                )
                .positiveText(R.string.submit)
                .input("http://", cURL, false, (dialog, input) -> {
                    String url = input.toString();
                    openURL(ErrorCheck(url));

                }).show();

    }

    private void sendEmail(String add) {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, new String[] { add });
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.sent_via));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(BrowserActivity.this,
                    R.string.no_email, Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private boolean isChecked = false;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.desktop);
        checkable.setChecked(isChecked);
        return true;
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
            case R.id.about:
                AboutDialog.show(this);
                return true;
            case R.id.cookies:
                clearCookies(BrowserActivity.this);
                return true;

            case R.id.share:
                shareText(getString(R.string.shared_by), currentUrl);
                return true;
            case R.id.desktop:
                isChecked = !item.isChecked();
                item.setChecked(isChecked);
               // myWebView.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
                setDesktopMode(isChecked);
            case R.id.reload:
                if (myWebView != null) {
                    myWebView.reload();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareText(String subject, String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent ,"Share Link"));
    }

    public void setDesktopMode(final boolean enabled) {

        final String newUserAgent;
        if (enabled) {
            newUserAgent = myWebView.getSettings().getUserAgentString().replace("Mobile", "eliboM").replace("Android", "diordnA");
        }
        else {
            newUserAgent = myWebView.getSettings().getUserAgentString().replace("eliboM", "Mobile").replace("diordnA", "Android");
        }

        myWebView.getSettings().setUserAgentString(newUserAgent);
        myWebView.getSettings().setUseWideViewPort(enabled);
        myWebView.getSettings().setLoadWithOverviewMode(enabled);
        myWebView.getSettings().setSupportZoom(enabled);
        myWebView.getSettings().setBuiltInZoomControls(enabled);
    }


    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
            clearCookies(this);
            destroyWebView();
        }
    }
}



