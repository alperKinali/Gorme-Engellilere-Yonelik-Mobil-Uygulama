package com.nexis.seslihavadurumu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationVoiceActivity extends AppCompatActivity implements LocationListener {
    private TextToSpeech mTTS;
    TextView textView,textView_i,textView_ü;
    LocationManager locationManager;
    Button btn_Loc,btn_Db;
    String adress;
    SQLiteDatabase sqLiteDatabase;
    //ArrayList<Place> placeList= new ArrayList<>();
    LocationListener locationListener;
    //ArrayList<Place> placeList_= new ArrayList<>();
    EditText editText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_voice);

        textView=findViewById(R.id.txtknmGstr);
        btn_Loc=findViewById(R.id.btn_Get_Loc);
        //btn_Db=findViewById(R.id.btn_Get_Loc);
        voiceauto_();

        if (ContextCompat.checkSelfPermission(LocationVoiceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LocationVoiceActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        mTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS){
                    Locale locale = new Locale("tr","TR");
                    int result =mTTS.setLanguage(locale);

                    if (result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","Language not supported");
                    }else{

                    }
                }else{
                    Log.e("TTS","Initialization Failed");
                }
            }
        });

        btn_Loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLocation();
                //Getvoice();
            }
        });

    }
    private void voiceauto_(){
        Intent ses_ = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        ses_.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        Locale locale2 = new Locale("tr","TR");
        //ses.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.setDefault(locale2));
        ses_.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"tr");
        ses_.putExtra(RecognizerIntent.EXTRA_PROMPT,"Neredeyim Diyerek Anlık lokasyonu alabilirsiniz");
        startActivityForResult(ses_,1);

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            ArrayList<String> arrayList =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (arrayList.get(0).toString().equals("neredeyim")){

                GetLocation();





            }

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentToMain = new Intent(this,LoginActivity.class);
        startActivity(intentToMain);
        finish();
        if (mTTS!=null){
            mTTS.stop();
            mTTS.shutdown();
        }
    }
    @SuppressLint("MissingPermission")
    private void GetLocation() {
        try {
            locationManager=(LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,5,LocationVoiceActivity.this);
            //textView.setText(adress);
            //String LocC=textView.getText().toString();

            //mTTS.speak("Şu anda Bulunduğunuz Konum:"+LocC,TextToSpeech.QUEUE_FLUSH,null);


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this,""+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_LONG).show();
        try {
            Geocoder geocoder = new Geocoder(LocationVoiceActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            adress= addresses.get(0).getAddressLine(0);
            textView.setText(adress);
            //editText.setText(adress);

            Getvoice();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {

    }
    public void Getvoice() {
        String LocC=textView.getText().toString();
        //textView.setText(adress);
        mTTS.speak("Şu anda Bulunduğunuz Konum:"+LocC,TextToSpeech.QUEUE_FLUSH,null);
    }
    @Override
    public void onDestroy() {
        if (mTTS!=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

}