package com.example.amine.learngesture;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.example.amine.learngesture.LoginActivity.INTENTEMAIL;
import static com.example.amine.learngesture.LoginActivity.INTENTID;
import static com.example.amine.learngesture.LoginActivity.INTENTSERVERADDRESS;

public class UploadVideoPage extends AppCompatActivity {

    @BindView(R.id.rv_videos)
    RecyclerView rvVideos;
    @BindView(R.id.tv_filename)
    TextView tv_filename;
    @BindView(R.id.pb_progress)
    ProgressBar progressBar;
    UploadListAdapter upload_List_Adapter;
    UploadVideoPage upload_Video_Page;
    SharedPreferences shared_Preferences;
    static int uploadNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        upload_Video_Page = this;
        rvVideos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        shared_Preferences =  this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        File m = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign");
        if(m.exists()) {
            if(m.isDirectory()) {
                File[] videos =  m.listFiles();
                for(int i=0;i<videos.length;i++) {
                    Log.d("msg",videos[i].getPath());

                    if(shared_Preferences.getString("mode","learn").equalsIgnoreCase("practice")) {
                        String tempName = videos[i].getPath();
                        int ii = tempName.lastIndexOf("_");
                        String tmpName = tempName.substring(0,ii)+"_"+ shared_Preferences.getInt("rating",10)+".mp4";
                        File file2 = new File(tmpName);
                        if(videos[i].renameTo(file2))
                            System.out.println("Rename successful");

                    }
                }
            }
        }
        upload_List_Adapter = new UploadListAdapter(m.listFiles(), this.getApplicationContext());
        rvVideos.setAdapter(upload_List_Adapter);



    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_upload, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        String id = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getString(INTENTID,"00000000");

        HashSet<String> menuClickSet = (HashSet<String>) shared_Preferences.getStringSet("MENU_CLICK", new HashSet<String>());
        menuClickSet.add("MENU_CLICK_" + item.getItemId() + "_" + shared_Preferences.getString(INTENTID, "") + "_" + shared_Preferences.getString(INTENTEMAIL, "") + "_" + String.valueOf(System.currentTimeMillis()));
        shared_Preferences.edit().putStringSet("MENU_CLICK", menuClickSet).apply();

        switch (item.getItemId()) {
            case R.id.menu_upload:
                final File[] toUpload = upload_List_Adapter.getVideoFiles();
                final boolean[] checked = upload_List_Adapter.getChecked();
                String server_ip = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE).getString(INTENTSERVERADDRESS,"192.168.0.223");
                Log.d("msg",server_ip);
                for(int i=0;i<checked.length;i++) {
                    RequestParams params = new RequestParams();
                    if(checked[i]) {
                        params.put("checked",1);
                    } else {
                        params.put("checked",0);
                    }
                        try {
                            params.put("filename", toUpload[i]);
                            params.put("id",id);

                        } catch(FileNotFoundException e) {}


                        AsyncHttpClient client = new AsyncHttpClient();
                    final int finalI = i;
                    String urlString="";
                    if(shared_Preferences.getString("mode","learn").equalsIgnoreCase("learn")) {
                        urlString = "http://"+server_ip +"/";
                        Log.e("url",urlString+" filename "+params);
                    }
                    else {
                        urlString = "http://"+server_ip +"/";
                        Log.e("url2",urlString);

                    }
                    client.post(urlString, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                                Log.e("msg success",statusCode+"");
                                if(statusCode==200) {
                                    Toast.makeText(UploadVideoPage.this, "Success", Toast.LENGTH_SHORT).show();
                                    toUpload[finalI].delete();
                                    upload_List_Adapter.getVideoFiles()[finalI] = null;
                                    upload_List_Adapter.notifyDataSetChanged();

                                    if(checked[finalI]) //video accepted
                                        shared_Preferences.edit().putInt("Number_Accepted",1+ shared_Preferences.getInt("Number_Accepted",0)).apply();

                                }
                                else {
                                    Toast.makeText(UploadVideoPage.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                                Log.e("msg fail",statusCode+"");

                                Toast.makeText(UploadVideoPage.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            }
                            @Override
                            public void onProgress(long bytesWritten, long totalSize) {
                                tv_filename.setText(bytesWritten + " out of " + totalSize);

                                super.onProgress(bytesWritten, totalSize);
                            }


                            @Override
                            public void onStart() {
                                tv_filename.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                super.onStart();
                            }

                            @Override
                            public void onFinish() {
                                Log.e("msg on finish", uploadNumber +"");
                                uploadNumber = uploadNumber + 1;
                                if(uploadNumber == checked.length) {
                                    upload_log_file();
                                }
                                tv_filename.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                super.onFinish();
                            }
                        });



                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void upload_log_file() {
        uploadNumber = 0;
        Toast.makeText(this,"Upload to Server", Toast.LENGTH_LONG).show();
        String id = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getString(INTENTID,"00000000");

        String server_ip = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE).getString(INTENTSERVERADDRESS,"10.211.17.171");

        File n = new File(getFilesDir().getPath());
        File f = new File(n.getParent()+"/shared_prefs/" + getPackageName() +".xml");
        AsyncHttpClient client_logs = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("uploaded_file",f);
            params.put("id",id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client_logs.post("http://"+server_ip+"/upload_log_file.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(UploadVideoPage.this, "Done", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
}

