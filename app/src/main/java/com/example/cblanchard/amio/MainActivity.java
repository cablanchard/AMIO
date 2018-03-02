package com.example.cblanchard.amio;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.os.Bundle;



import android.support.v4.app.FragmentActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class MainActivity extends FragmentActivity implements DownloadCallback{
    Button btn1 = null;
    Button btn2 = null;
    Button getLastLum = null;
    TextView tv1 = null;
    TextView tv2 = null;
    TextView tv3 = null;
    TextView tv4 = null;
    TextView tv5 = null;
    TextView tv6 = null;
    TextView lastLum = null;
    CheckBox boot = null;


    private boolean mDownloading = false;
    private NetworkFragment mNetworkFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        if(pref.getBoolean("Check", true)!=false){
            editor.putBoolean("Check",false );
        }else {
            editor.putBoolean("Check",true );
        }
        setContentView(R.layout.activity_main);
        Log.d("MainActivity","Création de l'activité");


        btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(btn1Listener);

        boot = (CheckBox)findViewById(R.id.boot);
        boot.setOnCheckedChangeListener(bootListener);

        getLastLum = (Button)findViewById(R.id.getLastLum);
        getLastLum.setOnClickListener(getLastLumListener);

   };

    private View.OnClickListener btn1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startService(new Intent(MainActivity.this, MyService.class));
            tv2 = (TextView)findViewById(R.id.tv2);
            String text = String.valueOf(tv2.getText());
            if(text!="En cours"){
                tv2.setText("En cours");
            }else{
                tv2.setText("Arrêté");
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener bootListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Log.d("CheckBox","Changement d'état de la CheckBox");
        }
    };

    private View.OnClickListener getLastLumListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NetworkFragment mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "http://iotlab.telecomnancy.eu/rest/data/1/light1/last ");
            // startService(new Intent(MainActivity.this, MyLastLum.class));
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this, MyService.class));
    }

    @Override
    public void updateFromDownload(Object result)  {
        String s = null;
        if(result != null){
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                s = jsonObject.get("data").toString();


                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject1 = new JSONObject(jsonArray.get(0).toString());
                JSONObject jsonObject2 = new JSONObject(jsonArray.get(1).toString());
                String timestamp1 = jsonObject1.get("timestamp").toString();
                String timestamp2 = jsonObject2.get("timestamp").toString();
                if(timestamp1.compareTo(timestamp2)>0){
                    s = jsonObject1.get("value").toString();
                }else{
                    s = jsonObject2.get("value").toString();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.d(s, "RESULT");
        lastLum = (TextView)findViewById(R.id.lastLum);
        lastLum.setText(s);

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:

                break;
            case Progress.CONNECT_SUCCESS:

                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:

                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }
}
