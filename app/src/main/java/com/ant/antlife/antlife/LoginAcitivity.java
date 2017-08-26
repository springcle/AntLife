package com.ant.antlife.antlife;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.FacebookSdk;

/**
 * Created by NAKNAK on 2017-08-26.
 */

public class LoginAcitivity extends Activity{

    Button facebook_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        facebook_login.findViewById(R.id.facebook_login);

    }
}
