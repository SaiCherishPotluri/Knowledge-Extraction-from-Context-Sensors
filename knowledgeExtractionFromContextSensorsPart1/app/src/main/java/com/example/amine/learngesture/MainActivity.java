package com.example.amine.learngesture;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

import static com.example.amine.learngesture.LoginActivity.INTENTEMAIL;
import static com.example.amine.learngesture.LoginActivity.INTENTID;
import static com.example.amine.learngesture.LoginActivity.INTENTSERVERADDRESS;
import static com.example.amine.learngesture.LoginActivity.INTENT_TIMEWATCHED;
import static com.example.amine.learngesture.LoginActivity.INTENT_TIMEWATCHEDVIDEO;
import static com.example.amine.learngesture.LoginActivity.INTENTURI;
import static com.example.amine.learngesture.LoginActivity.INTENTWORD;

public class MainActivity extends AppCompatActivity {


    static final int REQUEST_VIDEO_CAPTURE = 1;

    @BindView(R.id.rg_practice_learn)
    RadioGroup rgPracticeLearn;

    @BindView(R.id.rb_learn)
    RadioButton rbLearn;

    @BindView(R.id.rb_practice)
    RadioButton rbPractice;


    @BindView(R.id.sp_words)
    Spinner spWords;

    @BindView(R.id.sp_ip_address)
    Spinner spIpAddress;

    @BindView(R.id.vv_video_learn)
    VideoView vvVideoLearn;

    @BindView(R.id.vv_record)
    VideoView vvRecord;

    @BindView(R.id.bt_record)
    Button btRecord;


    @BindView(R.id.scores_heading)
    TextView scoresHeading;

    @BindView(R.id.scores_values)
    TextView scoresValues;


    @BindView(R.id.bt_send)
    Button btSend;

    @BindView(R.id.bt_cancel)
    Button btCancel;

    @BindView(R.id.ll_after_record)
    LinearLayout llAfterRecord;

    @BindView(R.id.ratingText)
    TextView ratingText;

    String path;
    String returned_URI;
    String oldText = "";
    SharedPreferences shared_Preferences;
    long timeStarted = 0;
    long timeStartedReturn = 0;
    Activity main_Activity;
    SeekBar ratingSeek;
    int ratingVal = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);

        rbLearn.setChecked(true);
        btCancel.setVisibility(View.GONE);
        btSend.setVisibility(View.GONE);
        scoresHeading.setVisibility(View.GONE);
        scoresValues.setVisibility(View.GONE);
        rgPracticeLearn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                HashSet<String> radioButtonClickSet = (HashSet<String>) shared_Preferences.getStringSet("RADIO_BUTTON_CLICK", new HashSet<String>());
                radioButtonClickSet.add("RADIO_BUTTON_CLICK_" + checkedId + "_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
                shared_Preferences.edit().putStringSet("RADIO_BUTTON_CLICK", radioButtonClickSet).apply();

                if(checkedId== rbLearn.getId()) {
                    shared_Preferences.edit().putString("mode","learn").apply();
                    Toast.makeText(getApplicationContext(),"Learn",Toast.LENGTH_SHORT).show();
                    vvVideoLearn.setVisibility(View.VISIBLE);
                    vvVideoLearn.start();
                    timeStarted = System.currentTimeMillis();
                    spWords.setEnabled(true);
                    spWords.setVisibility(View.VISIBLE);
                    spIpAddress.setEnabled(true);
                    scoresHeading.setVisibility(View.GONE);
                    scoresValues.setVisibility(View.GONE);
                } else if ( checkedId== rbPractice.getId()) {
                    shared_Preferences.edit().putString("mode","practice").apply();
                    Toast.makeText(getApplicationContext(),"Practice",Toast.LENGTH_SHORT).show();
                    vvVideoLearn.setVisibility(View.GONE);
                    int randomVal = (int)(Math.random() * 25);
                    spWords.setSelection(randomVal);
                    //sp_words.setMinimumHeight(50);
                    spWords.setEnabled(false);
                    spWords.setVisibility(View.GONE);
                    spIpAddress.setEnabled(false);
                    //bt_about.setVisibility(View.GONE);
                    scoresHeading.setVisibility(View.GONE);
                    scoresValues.setVisibility(View.GONE);
                }

            }
        });

        spWords.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                HashSet<String> wordsDropdownSet = (HashSet<String>) shared_Preferences.getStringSet("WORDS_DROPDOWN_CLICK", new HashSet<String>());
                wordsDropdownSet.add("WORDS_DROPDOWN_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
                shared_Preferences.edit().putStringSet("WORDS_DROPDOWN_CLICK", wordsDropdownSet).apply();

                String text = spWords.getSelectedItem().toString();
                if(!oldText.equals(text)) {
                    path = "";
                    timeStarted = System.currentTimeMillis();
                    play_video(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spIpAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shared_Preferences.edit().putString(INTENTSERVERADDRESS, spIpAddress.getSelectedItem().toString()).apply();

                HashSet<String> ipsDropdownSet = (HashSet<String>) shared_Preferences.getStringSet("IPS_DROPDOWN_CLICK", new HashSet<String>());
                ipsDropdownSet.add("IPS_DROPDOWN_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
                shared_Preferences.edit().putStringSet("IPS_DROPDOWN_CLICK", ipsDropdownSet).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(mediaPlayer!=null)
                {
                    mediaPlayer.start();

                }

             }
        };
        vvRecord.setOnCompletionListener(onCompletionListener);
        vvVideoLearn.setOnCompletionListener(onCompletionListener);
        vvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashSet<String> ipsDropdownSet = (HashSet<String>) shared_Preferences.getStringSet("VIDEO_PREVIEW_CLICK", new HashSet<String>());
                ipsDropdownSet.add("VIDEO_PREVIEW_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
                shared_Preferences.edit().putStringSet("VIDEO_PREVIEW_CLICK", ipsDropdownSet).apply();

                vvRecord.start();
            }
        });
        vvVideoLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashSet<String> ipsDropdownSet = (HashSet<String>) shared_Preferences.getStringSet("VIDEO_PREVIEW_CLICK", new HashSet<String>());
                ipsDropdownSet.add("VIDEO_PREVIEW_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
                shared_Preferences.edit().putStringSet("VIDEO_PREVIEW_CLICK", ipsDropdownSet).apply();

                if(!vvVideoLearn.isPlaying()) {
                    vvVideoLearn.start();
                }
            }
        });
        timeStarted = System.currentTimeMillis();
        shared_Preferences =  this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        shared_Preferences.edit().putString("mode","learn").apply();
        Intent intent = getIntent();
        if(intent.hasExtra(INTENTEMAIL) && intent.hasExtra(INTENTID)) {
            Toast.makeText(this,"User : " + intent.getStringExtra(INTENTEMAIL),Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"Already Logged In",Toast.LENGTH_SHORT).show();

        }

        ratingSeek = (SeekBar) findViewById(R.id.ratingBar);
        ratingSeek.setVisibility(View.GONE);
        ratingText.setVisibility(View.GONE);
        ratingSeek.setMax(5);
        ratingSeek.setProgress(0);

        ratingSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                HashSet<String> ratingBarSet = (HashSet<String>) shared_Preferences.getStringSet("RATING_BAR_CLICK", new HashSet<String>());
                ratingBarSet.add("RATING_BAR_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
                shared_Preferences.edit().putStringSet("RATING_BAR_CLICK", ratingBarSet).apply();

                ratingText.setText("Practice Count:" + i);
                ratingVal = i;
                shared_Preferences.edit().putInt("rating", ratingVal).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        HashSet<String> backCLickSet = (HashSet<String>) shared_Preferences.getStringSet("BACK_CLICK", new HashSet<String>());
        backCLickSet.add("BACK_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
        shared_Preferences.edit().putStringSet("BACK_CLICK", backCLickSet).apply();

        moveTaskToBack(true);
        ratingSeek.setVisibility(View.GONE);
        ratingText.setVisibility(View.GONE);
        finish();
        super.onBackPressed();

    }

    @Override
    protected void onResume() {

        vvVideoLearn.start();
        timeStarted = System.currentTimeMillis();
        super.onResume();

    }

    public void play_video(String text) {
        oldText = text;
        if(text.equals("ACPower")) {
             path = "android.resource://" + getPackageName() + "/" + R.raw._acpower;
        } else if(text.equals("Algorithm")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._algorithm;
        } else if (text.equals("Antenna")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._antenna;
        }else if (text.equals("Authentication")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._authentication;
        }else if (text.equals("Authorization")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._authorization;
        }else if (text.equals("Bandwidth")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._bandwidth;
        }else if (text.equals("Bluetooth")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._bluetooth;
        }else if (text.equals("Browser")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._browser;
        }else if (text.equals("CloudComputing")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._cloudcomputing;
        }else if (text.equals("DataCompression")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._datacompression;
        }else if (text.equals("DataLinkLayer")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._datalinklayer;
        }else if (text.equals("DataMining")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._datamining;
        }else if (text.equals("Decryption")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._decryption;
        }else if (text.equals("Domain")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._domain;
        }else if (text.equals("Email")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._email;
        }else if (text.equals("Exposure")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._exposure;
        }else if (text.equals("Filter")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._filter;
        }else if (text.equals("Firewall")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._firewall;
        }else if (text.equals("Flooding")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._flooding;
        }else if (text.equals("Gateway")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._gateway;
        }else if (text.equals("Hacker")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._hacker;
        }else if (text.equals("Header")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._header;
        }else if (text.equals("Hotswap")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._hotswap;
        }else if (text.equals("HyperLink")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._hyperlink;
        }else if (text.equals("Infrastructure")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._infrastructure;
        }else if (text.equals("Integrity")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._integrity;
        }else if (text.equals("Internet")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._internet;
        }else if (text.equals("Intranet")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._intranet;
        }else if (text.equals("Latency")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._latency;
        }else if (text.equals("Loopback")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._loopback;
        }else if (text.equals("Motherboard")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._motherboard;
        }else if (text.equals("Network")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._network;
        }else if (text.equals("Networking")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._networking;
        }else if (text.equals("NetworkLayer")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._networklayer;
        }else if (text.equals("Node")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._node;
        }else if (text.equals("Packet")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._packet;
        }else if (text.equals("Partition")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._partition;
        }else if (text.equals("PasswordSniffing")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._passwordsniffing;
        }else if (text.equals("Patch")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._patch;
        }else if (text.equals("Phishing")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._phishing;
        }else if (text.equals("PhysicalLayer")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._physicallayer;
        }else if (text.equals("Ping")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._ping;
        }else if (text.equals("PortScan")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._portscan;
        }else if (text.equals("PresentationLayer")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._presentationlayer;
        }else if (text.equals("Protocol")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw._protocol;
        }
        if(!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            vvVideoLearn.setVideoURI(uri);
            vvVideoLearn.start();
        }

    }


    @OnClick(R.id.bt_record)
    public void record_video() {

        HashSet<String> buttonClickSet = (HashSet<String>) shared_Preferences.getStringSet("RECORD_BUTTON_CLICK", new HashSet<String>());
        buttonClickSet.add("RECORD_BUTTON_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
        shared_Preferences.edit().putStringSet("RECORD_BUTTON_CLICK", buttonClickSet).apply();

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

        } else {
             File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");

             if (!f.exists()) {
                 f.mkdirs();
             }

             timeStarted = System.currentTimeMillis() - timeStarted;

             Intent t = new Intent(this, VideoOperations.class);
             t.putExtra(INTENTWORD, spWords.getSelectedItem().toString());
             t.putExtra(INTENT_TIMEWATCHED, timeStarted);
             startActivityForResult(t,9999);

        }
    }

    @OnClick(R.id.bt_send)
    public void sendToServer() {
        HashSet<String> buttonClickSet = (HashSet<String>) shared_Preferences.getStringSet("SEND_BUTTON_CLICK", new HashSet<String>());
        buttonClickSet.add("SEND_BUTTON_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
        shared_Preferences.edit().putStringSet("SEND_BUTTON_CLICK", buttonClickSet).apply();

        Toast.makeText(this,"Send to Server",Toast.LENGTH_SHORT).show();
        Intent t = new Intent(this, UploadVideoPage.class);
        startActivityForResult(t,2000);
        ratingSeek.setVisibility(View.GONE);
        ratingText.setVisibility(View.GONE);

    }

    @OnClick(R.id.bt_cancel)
    public void cancel() {
        HashSet<String> buttonClickSet = (HashSet<String>) shared_Preferences.getStringSet("CANCEL_BUTTON_CLICK", new HashSet<String>());
        buttonClickSet.add("CANCEL_BUTTON_CLICK_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
        shared_Preferences.edit().putStringSet("CANCEL_BUTTON_CLICK", buttonClickSet).apply();

        vvRecord.setVisibility(View.GONE);
        if(rbLearn.isSelected()) {
            vvVideoLearn.setVisibility(View.VISIBLE);
        }
        btRecord.setVisibility(View.VISIBLE);
        btSend.setVisibility(View.GONE);
        btCancel.setVisibility(View.GONE);
        ratingSeek.setVisibility(View.GONE);
        ratingText.setVisibility(View.GONE);

        spWords.setEnabled(true);

        rbLearn.setEnabled(true);
        rbPractice.setEnabled(true);
        timeStarted = System.currentTimeMillis();


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

    Log.e("OnActivityresult",requestCode+" "+resultCode);
        if(requestCode==2000 ) {

            vvRecord.setVisibility(View.GONE);
            rbLearn.setChecked(true);
            btCancel.setVisibility(View.GONE);
            btSend.setVisibility(View.GONE);
            btRecord.setVisibility(View.VISIBLE);
            spWords.setEnabled(true);
            rbLearn.setEnabled(true);
            rbPractice.setEnabled(true);
            spIpAddress.setEnabled(true);


        }
        if(requestCode==9999 && resultCode == 8888) {
            if(intent.hasExtra(INTENTURI) && intent.hasExtra(INTENT_TIMEWATCHEDVIDEO)) {
                returned_URI = intent.getStringExtra(INTENTURI);
                timeStartedReturn = intent.getLongExtra(INTENT_TIMEWATCHEDVIDEO,0);

                vvRecord.setVisibility(View.VISIBLE);
                btRecord.setVisibility(View.GONE);
                btSend.setVisibility(View.VISIBLE);
                btCancel.setVisibility(View.VISIBLE);
                ratingSeek.setVisibility(View.VISIBLE);
                ratingText.setVisibility(View.VISIBLE);
                spWords.setEnabled(false);
                rbLearn.setEnabled(false);
                rbPractice.setEnabled(false);
                vvRecord.setVideoURI(Uri.parse(returned_URI));
                vvRecord.start();
                int try_number = shared_Preferences.getInt("record_"+ spWords.getSelectedItem().toString(),0);
                try_number++;
                String toAdd  = spWords.getSelectedItem().toString()+"_"+try_number+"_"+ timeStartedReturn + "";
                HashSet<String> set = (HashSet<String>) shared_Preferences.getStringSet("RECORDED",new HashSet<String>());
                set.add(toAdd);
                shared_Preferences.edit().putStringSet("RECORDED",set).apply();
                shared_Preferences.edit().putInt("record_"+ spWords.getSelectedItem().toString(), try_number).apply();

                if(rbPractice.isChecked()) {
                    vvVideoLearn.setVisibility(View.VISIBLE);
                    vvVideoLearn.start();
                }
                else {
                    vvVideoLearn.setVisibility(View.GONE);
                }
            }
            if(shared_Preferences.getString("mode","learn").equalsIgnoreCase("learn")) {
                ratingSeek.setVisibility(View.GONE);
                ratingText.setVisibility(View.GONE);
            }

        }

        if(requestCode==9999 && resultCode==7777)
        {
            if(intent!=null) {
                //create folder
                if(intent.hasExtra(INTENTURI) && intent.hasExtra(INTENT_TIMEWATCHEDVIDEO)) {
                    returned_URI = intent.getStringExtra(INTENTURI);
                    timeStartedReturn = intent.getLongExtra(INTENT_TIMEWATCHEDVIDEO,0);
                    File f = new File(returned_URI);
                    f.delete();
                    timeStarted = System.currentTimeMillis();
                    vvVideoLearn.start();
                }
            }

        }}
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        HashSet<String> menuClickSet = (HashSet<String>) shared_Preferences.getStringSet("MENU_CLICK", new HashSet<String>());
        menuClickSet.add("MENU_CLICK_" + item.getItemId() + "_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
        shared_Preferences.edit().putStringSet("MENU_CLICK", menuClickSet).apply();

        switch (item.getItemId()) {
            case R.id.menu_logout:
                main_Activity = this;
                    final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("ALERT");
                    alertDialog.setMessage("Logging out will delete all the data!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    shared_Preferences.edit().clear().apply();
                                    File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");
                                    if (f.isDirectory())
                                    {
                                        String[] children = f.list();
                                        for (int i = 0; i < children.length; i++)
                                        {
                                            new File(f, children[i]).delete();
                                        }
                                    }
                                    startActivity(new Intent(main_Activity,LoginActivity.class));
                                    main_Activity.finish();

                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();



                    return true;
            case R.id.menu_upload_server:
                shared_Preferences.edit().putInt(getString(R.string.gotoupload), shared_Preferences.getInt(getString(R.string.gotoupload),0)+1).apply();
                Intent t = new Intent(this, UploadVideoPage.class);
                startActivityForResult(t,2000);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class SaveFile extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            FileOutputStream fileOutputStream = null;
            FileInputStream fileInputStream = null;
            try {
                fileOutputStream = new FileOutputStream(strings[0]);
                fileInputStream = (FileInputStream) getContentResolver().openInputStream(Uri.parse(strings[1]));
                Log.d("msg", fileInputStream.available() + " ");
                byte[] buffer = new byte[1024];
                while (fileInputStream.available() > 0) {

                    fileInputStream.read(buffer);
                    fileOutputStream.write(buffer);
                    publishProgress(fileInputStream.available()+"");
                }

                fileInputStream.close();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(),"Video Saved Successfully",Toast.LENGTH_SHORT).show();
        }
    }
}
