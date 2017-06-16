package com.aviparshan.simplebrowse;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static com.aviparshan.simplebrowse.R.id.editText;
import static com.aviparshan.simplebrowse.R.id.textView;

public class MainActivity extends AppCompatActivity {

    private CustomTabsClient mClient;

    private EditText etLocation;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);
        etLocation = (EditText) findViewById(editText);
        txt = (TextView) findViewById(textView);

        etLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) && !isEmpty(etLocation)) {
                    sendURL();
                }
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(etLocation)) {
                    Toast.makeText(getApplicationContext(), R.string.no, Toast.LENGTH_LONG).show();
                }
                else
                {
//                    Intent i = new Intent(MainActivity.this, BrowesrActivity.class);
//                    i.putExtra("url", ErrorCheckComplete(etLocation)); //etLocation.getText().toString()
//                    txt.setText(etLocation.getText().toString());
//                    startActivity(i);
                    sendURL();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AboutDialog.show(this);
                return true;
            case R.id.feedback:
                composeEmail();
                return true;
            case R.id.rate:
                goToMyApp();
                return true;
            case R.id.other:
                Apps();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToMyApp() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }
    public void Apps() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7646777248290288031")));
        } catch (ActivityNotFoundException e1) {
            Toast.makeText(this, R.string.rate_error, Toast.LENGTH_SHORT).show();
        }
    }

    CustomTabsServiceConnection mConnection = new CustomTabsServiceConnection() {
        @Override
        public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
            mClient = customTabsClient;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mClient = null;
        }
    };

    public void prefetchContent(String link) {
         //url = "http://www.google.com";
        if (mClient != null) {
            mClient.warmup(0);
            CustomTabsSession customTabsSession = getSession();
            customTabsSession.mayLaunchUrl(Uri.parse(link), null, null);
        }
    }

    private CustomTabsSession getSession() {
        return mClient.newSession(new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
            }
        });
    }

    private void sendURL(Uri url){
        String test = String.valueOf(url);

        String packageName = "com.android.chrome";

        prefetchContent(test);

        CustomTabsClient.bindCustomTabsService(this, packageName, mConnection);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.addDefaultShareMenuItem();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared));
        intent.putExtra(Intent.EXTRA_TEXT, url);
        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_white_24dp);
        builder.setActionButton(bitmap, getString(R.string.link), pendingIntent, true);

        builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(MainActivity.this, Uri.parse(test));
    }
    private void sendURL(){
        String url = ErrorCheckComplete(etLocation);

        String packageName = "com.android.chrome";

        prefetchContent(url);

        CustomTabsClient.bindCustomTabsService(this, packageName, mConnection);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.addDefaultShareMenuItem();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared));
        intent.putExtra(Intent.EXTRA_TEXT, url);
        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_white_24dp);
        builder.setActionButton(bitmap, getString(R.string.link), pendingIntent, true);

        builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(MainActivity.this, Uri.parse(url));
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
        txt.setText(tv2);
        return tv2;

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void composeEmail() {

        //Used in CPU Info app I made: https://play.google.com/store/apps/details?id=com.aviparshan.appinfo
        String PhoneModel = android.os.Build.MODEL;
        String AndroidVersion = android.os.Build.VERSION.RELEASE;
        int API = Build.VERSION.SDK_INT;
        String Language = Locale.getDefault().getISO3Language();
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"apps@aviparshan.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, "App Information \n" +
                "Phone Model: " + PhoneModel + "\nAndroid Version: " + AndroidVersion + "\nAPI Level: " + API + "\nVersion Code: " + versionCode + "\nVersion Name: " + versionName + "\nLanguage: "+ Language);
        try {
            //startActivity(Intent.createChooser(intent, "Send mail..."));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, R.string.rate_error, Toast.LENGTH_SHORT).show();
        }
    }
}
