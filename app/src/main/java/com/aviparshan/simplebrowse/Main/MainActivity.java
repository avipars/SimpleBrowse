package com.aviparshan.simplebrowse.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aviparshan.simplebrowse.Browse.BrowserActivity;
import com.aviparshan.simplebrowse.R;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import static com.aviparshan.simplebrowse.R.id.editText;

public class MainActivity extends AppCompatActivity {
    private EditText etLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // 205097787
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "205097787", false);
        setContentView(R.layout.activity_main);

        StartAppAd.disableSplash();


        Button btn = (Button) findViewById(R.id.button);
        etLocation = (EditText) findViewById(editText);

        etLocation.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) && !isEmpty(etLocation)) {sendURL();
            }
            return false;
        });

        btn.setOnClickListener(v -> {
            if (isEmpty(etLocation)) {
                Toast.makeText(getApplicationContext(), R.string.no, Toast.LENGTH_LONG).show();
            }
            else
            {
//                    Intent i = new Intent(MainActivity.this, BrowserActivity.class);
//                    i.putExtra("url", ErrorCheckComplete(etLocation)); //etLocation.getText().toString()
//                    txt.setText(etLocation.getText().toString());
//                    startActivity(i);
                sendURL();

            }
        });
    }



//    private void sendURL(Uri url){
//        String test = String.valueOf(url);
//
//        String packageName = "com.android.chrome";
//
//        prefetchContent(test);
//
//        CustomTabsClient.bindCustomTabsService(this, packageName, mConnection);
//
//        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//        builder.addDefaultShareMenuItem();
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.shared));
//        intent.putExtra(Intent.EXTRA_TEXT, url);
//        int requestCode = 100;
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
//                requestCode,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_white_24dp);
//        builder.setActionButton(bitmap, getString(R.string.link), pendingIntent, true);
//
//        builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
//        CustomTabsIntent customTabsIntent = builder.build();
//        customTabsIntent.launchUrl(MainActivity.this, Uri.parse(test));
//    }




    private void sendURL()
    {
        Intent i = new Intent(MainActivity.this, BrowserActivity.class);
                    i.putExtra("link", etLocation.getText().toString()); //etLocation.getText().toString()
                    //txt.setText(etLocation.getText().toString());

        startActivity(i);

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
        return tv2;

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}
