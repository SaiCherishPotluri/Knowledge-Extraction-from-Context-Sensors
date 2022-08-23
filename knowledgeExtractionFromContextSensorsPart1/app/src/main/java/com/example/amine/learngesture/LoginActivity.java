package com.example.amine.learngesture;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.stetho.Stetho;

import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {



    public static String INTENTID = "INTENT_ID";
    public static String INTENTNAME = "INTENT_NAME";
    public static String INTENTEMAIL = "INTENT_EMAIL";
    public static String INTENTWORD = "INTENT_WORD";
    public static String INTENT_TIMEWATCHED = "INTENT_TIME_WATCHED";
    public static String INTENT_TIMEWATCHEDVIDEO = "INTENT_TIME_WATCHED_VIDEO";
    public static String INTENTURI = "INTENT_URI";
    public static String INTENTSERVERADDRESS = "INTENT_SERVER_ADDRESS";

    @BindView(R.id.et_email)
    EditText emailEditText;

    @BindView(R.id.et_id)
    EditText etId;

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.bt_login)
    Button btLogin;

    String email;
    String id;
    String lastName;
    SharedPreferences sharedPreferences;
    long timeToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);





        timeToLogin = System.currentTimeMillis();
        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(INTENTID) && sharedPreferences.contains(INTENTEMAIL)) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();

        }
    }

    @OnClick(R.id.bt_login)
    public void login() {

        HashSet<String> buttonClickSet = (HashSet<String>) sharedPreferences.getStringSet("LOGIN_BUTTON_CLICK", new HashSet<String>());
        buttonClickSet.add("LOGIN_BUTTON_CLICK_" + id + "_" + email + "_" + String.valueOf(System.currentTimeMillis()));
        sharedPreferences.edit().putStringSet("LOGIN_BUTTON_CLICK", buttonClickSet).apply();

        if( ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        101);

            }
        }


        if ( ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {



            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }

        }else {
            if (emailEditText.getText().toString().isEmpty() || etId.getText().toString().isEmpty()) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("ALERT");
                alertDialog.setMessage("Please Enter Login Information!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                email = emailEditText.getText().toString();
                id = etId.getText().toString();
                lastName= etName.getText().toString();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(INTENTEMAIL, email);
                intent.putExtra(INTENTID, id);
                intent.putExtra(INTENTNAME, lastName);
                Log.e("INTENT_NAME",lastName);

                if (sharedPreferences.edit().putString(INTENTEMAIL, email).commit() &&
                        sharedPreferences.edit().putString(INTENTID, id).commit()) {

                    timeToLogin = System.currentTimeMillis() - timeToLogin;

                    sharedPreferences.edit().putInt(getString(R.string.login), sharedPreferences.getInt(getString(R.string.login), 0) + 1).apply();
                    HashSet<String> hashset = (HashSet<String>) sharedPreferences.getStringSet("LOGIN_TIME", new HashSet<String>());
                    hashset.add("LOGIN_ATTEMPT_" + sharedPreferences.getInt(getString(R.string.login), 0) + "_" + id + "_" + email + "_" + timeToLogin);
                    sharedPreferences.edit().putStringSet("LOGIN_TIME", hashset).apply();
                    startActivity(intent);
                    this.finish();

                }


            }
        }
    }

}
