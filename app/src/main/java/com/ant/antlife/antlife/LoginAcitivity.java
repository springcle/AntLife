package com.ant.antlife.antlife;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NAKNAK on 2017-08-26.
 */

public class LoginAcitivity extends Activity {

    private SharedPreferences settings;
    private CallbackManager fbCallbackManager;
    private ProfileTracker fbProfileTracker;
    Button facebook_login;
    String password, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        settings = getSharedPreferences("KEY", 0);
        setContentView(R.layout.activity_login);
        facebook_login.findViewById(R.id.facebook_login);
        initFacebookButton();
    }
/**
    FacebookCallback<LoginResult> loginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse graphResponse) {
                    try {
                        String email = object.getString("email");
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("ID", email);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        editor.apply();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginAcitivity.this, "Error", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException exception) {
            Toast.makeText(LoginAcitivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    };
**/

    private void initFacebookButton() {
        fbCallbackManager = CallbackManager.Factory.create();
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        try {
                                            password = object.getString("id");
                                            email = "facebook/" + object.getString("name");

                                    /*로그인 이력 갱신*/
                                            SharedPreferences settings = getSharedPreferences("KEY", 0); //여러 액티비티와 공유하기위한 KEY값 2번쨰 전달인자는 mode
                                            SharedPreferences.Editor editor = settings.edit();
                                            editor.putString("ID", email); //로그인한 사용자의 ID 저장
                                            editor.apply();

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
            }
        });
    }
}


