/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {
  EditText usernameEditText;
  EditText passwordEditText;

  Boolean loginModeActive = false;

  public void redirectUser(){
    if(ParseUser.getCurrentUser() != null){
      Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
      startActivity(intent);
    }
  }

  public void toggleLoginMode(View view){
    Button signupLoginButton = (Button) findViewById(R.id.signupLoginButton);
    TextView toggleLoginModeTextView = (TextView) findViewById(R.id.toggleLoginModeTextView);

    if(loginModeActive){

      loginModeActive = false;
      signupLoginButton.setText("SignUp");
      toggleLoginModeTextView.setText("or,login");


    }else{

      loginModeActive = true;
      signupLoginButton.setText("Login");
      toggleLoginModeTextView.setText("or,sign up");

    }
  }

  public void signupLogin(View view){
    final String username = usernameEditText.getText().toString();
    final String password = passwordEditText.getText().toString();

    ParseUser user = new ParseUser();
    user.setUsername(username);
    user.setPassword(password);
    if(loginModeActive) {

      user.logInInBackground(username, password, new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if(e == null){
            Log.i("Info", "user logged in");
            redirectUser();
          }else{
            String message = e.getMessage();
            if(message.toLowerCase().contains("java")){
              message = e.getMessage().substring(e.getMessage().indexOf(" "));
            }

            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
          }

        }
      });

    }else{

      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if (e == null) {
            Log.i("Info", "signed up successfully");
            redirectUser();
          } else {
            String message = e.getMessage();
            if(message.toLowerCase().contains("java")){
              message = e.getMessage().substring(e.getMessage().indexOf(" "));
            }

            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
          }
        }
      });

    }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Whatsapp Login");
    redirectUser();


    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}