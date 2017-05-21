package com.aviparshan.simplebrowse;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.aviparshan.simplebrowse.R.id.editText;
import static com.aviparshan.simplebrowse.R.id.textView;

public class MainActivity extends AppCompatActivity {

    private EditText etLocation;
    private Button btn;
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        etLocation = (EditText) findViewById(editText);
        txt = (TextView) findViewById(textView);

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

                    String url = ErrorCheckComplete(etLocation);


                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.addDefaultShareMenuItem();

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Shared through Simple Browse");
                    intent.putExtra(Intent.EXTRA_TEXT, url);
                    int requestCode = 100;

                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_white_24dp);
                    builder.setActionButton(bitmap, "Share Link", pendingIntent, true);

                    builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(MainActivity.this, Uri.parse(url));

                }

            }
        });

    }

    private String ErrorCheckComplete(EditText etText)
    {
        String text = etText.getText().toString();

        return ErrorCheck(text);
    }
    private String ErrorCheck(String etText){

        String tv2;

        String http = "http://";
        String https = "https://";

        if (etText.toLowerCase().contains(http) | etText.toLowerCase().contains(https))
        {
            tv2 = etText;
        }
        else
        {
            tv2 = http + etText;
        }
        txt.setText(tv2);

        return tv2;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    
}
