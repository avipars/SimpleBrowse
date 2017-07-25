package com.aviparshan.simplebrowse.Browse;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

//Class to handle incoming intents from browsers
public class intentHandler extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String action = intent.getAction();
        if (!action.equals(Intent.ACTION_VIEW)) {
            throw new RuntimeException("Should not happen");
        }

        Uri data = getIntent().getData();//set a variable for the Intent
        String scheme = data.getScheme();//get the scheme (http,https)
        String fullPath = data.getEncodedSchemeSpecificPart();//get the full path -scheme - fragments
        String combine = scheme + "://" + fullPath; //combine to get a full URI
        String url = null;//declare variable to hold final URL
        url = combine;
        sendURL(url);
    }

    private void sendURL(String url) {
        Intent i = new Intent(intentHandler.this, BrowserActivity.class);
        i.putExtra("link", url); //etLocation.getText().toString()
        startActivity(i);
    }

}




