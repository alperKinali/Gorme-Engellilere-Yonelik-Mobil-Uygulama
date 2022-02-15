package com.nexis.seslihavadurumu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    protected  static final int RESULT_SPEECH=1;
    private ImageButton btnSpeak;
    String City="Ankara";
    EditText editText;
    TextView textView;
    String url="";

    String  apikey="";
    private TextToSpeech mTTS;
    private SeekBar mSeekBarPitch;
    private SeekBar msSeekBarSpeed;
    private Button mButtonSpeak;
    TextView txtCity,txtTime,txtValueFeelLike,txtValueHumidity,txtTempV,bt1,bt2,txtMinV,txtMaxV;
    RelativeLayout rlWeather,rlRoot;
    private Timer timer = new Timer();
    private final long DELAY =2800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonSpeak=findViewById(R.id.btn_speak);
        editText=findViewById(R.id.sN);
        txtCity=findViewById(R.id.txtCity);
        txtTime=findViewById(R.id.txtTime);
        txtValueFeelLike=findViewById(R.id.txtValueFeek);
        txtValueHumidity=findViewById(R.id.txtValueHumidity);
        txtMaxV=findViewById(R.id.txtMaxTempV);
        txtMinV=findViewById(R.id.txtMinTempV);
        txtTempV=findViewById(R.id.txtTemp);
        rlWeather=findViewById(R.id.rlWeather);
        rlRoot=findViewById(R.id.rlRoot);
        bt1=findViewById(R.id.textV1);
        bt2=findViewById(R.id.textV2);
        btnSpeak=findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // konusma butonuna tıklandığında
                Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,mTTS.speak("Lütfen Hava Durumunu Öğrenmek istediğiniz Şehri Söyleyiniz",TextToSpeech.QUEUE_FLUSH,null));
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"tr");
                //en-US"
                //mTTS.speak("Lütfen Hava Durumunu Öğrenmek istediğiniz Şehri Söyleyiniz",TextToSpeech.QUEUE_FLUSH,null);
                try {
                    startActivityForResult(intent,RESULT_SPEECH);
                    editText.setText("");


                }catch (ActivityNotFoundException e ){
                    Toast.makeText(getApplicationContext(),"Cihazınız Konuşmayı metine dönüştüremedi",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }







            }
        });

        if (editText.getText().toString().matches("")) {
            Toast.makeText(this, "You did not enter a username", Toast.LENGTH_SHORT).show();

        }
        if (editText.getText().toString().matches("")){
            Toast.makeText(this, "Bos Degil ", Toast.LENGTH_SHORT).show();

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
                        mButtonSpeak.setEnabled(true);
                    }
                }else{
                    Log.e("TTS","Initialization Failed");
                }

            }
        });
        mSeekBarPitch=findViewById(R.id.seek_bar_pitch);
        msSeekBarSpeed=findViewById(R.id.seek_bar_speed);
        voiceauto_i();
        
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentToMain = new Intent(this,LoginActivity.class);
        startActivity(intentToMain);
        finish();
    }
    private void voiceauto_i(){
        // konusma butonuna tıklandığında
        Intent intent_= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent_.putExtra(RecognizerIntent.EXTRA_PROMPT,mTTS.speak("Lütfen Hava Durumunu Öğrenmek istediğiniz Şehri Söyleyiniz",TextToSpeech.QUEUE_FLUSH,null));
        intent_.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"tr");
        //en-US"
        //mTTS.speak("Lütfen Hava Durumunu Öğrenmek istediğiniz Şehri Söyleyiniz",TextToSpeech.QUEUE_FLUSH,null);
        try {
            startActivityForResult(intent_,RESULT_SPEECH);
            editText.setText("");


        }catch (ActivityNotFoundException e ){
            Toast.makeText(getApplicationContext(),"Cihazınız Konuşmayı metine dönüştüremedi",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode==RESULT_OK && data!=null){
                    ArrayList<String> text=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(text.get(0));
                }
                break;
        }
    }


    public  void getWeather(View v){
        City=editText.getText().toString();
        editText.setVisibility(v.INVISIBLE);
        mButtonSpeak.setVisibility(v.INVISIBLE);
        bt1.setVisibility(v.INVISIBLE);
        bt2.setVisibility(v.INVISIBLE);
        btnSpeak.setVisibility(v.INVISIBLE);
        mSeekBarPitch.setVisibility(v.INVISIBLE);
        msSeekBarSpeed.setVisibility(v.INVISIBLE);

            rlWeather.setVisibility(v.VISIBLE);


            rlRoot.setBackgroundColor(Color.parseColor("#E6E6E6"));

        Retrofit retrofit= new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create()).build();
        weatherapi myapi=retrofit.create(weatherapi.class);
        Call<Example> exampleCall=myapi.getweather(editText.getText().toString().trim(),apikey);

        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.code()==404){
                    Toast.makeText(MainActivity.this,"Bir değer girin",Toast.LENGTH_LONG).show();
                }
                else if(!(response.isSuccessful())){
                    //Toast.makeText(MainActivity.this,response.code(),Toast.LENGTH_LONG).show();

                }
                Example mydata=response.body();
                Main main=mydata.getMain();

                Double temp =main.getTemp();
                Double feelsLike=main.getFeelsLike();
                Double mintem =main.getTempMin();
                Double maxtem = main.getTempMax();
                Integer humidity=main.getHumidity();
                Integer tempature=(int)(temp-273.15);
                Integer felsLike=(int)(feelsLike-273.15);
                Integer tempature_Min=(int)(mintem-273.15);
                Integer tempature_Max=(int)(maxtem-273.15);






                txtTempV.setText(String.valueOf(tempature)+" C");
                txtMaxV.setText(String.valueOf(tempature_Max)+" C");
                txtMinV.setText(String.valueOf(tempature_Min)+" C");

                txtValueFeelLike.setText(String.valueOf(felsLike));
                txtValueHumidity.setText(String.valueOf(humidity));
                txtCity.setText(City);



                speak();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }
    private void speak(){
        String textv=txtTempV.getText().toString();
        String textF=txtValueFeelLike.getText().toString();
        String Nem=txtValueHumidity.getText().toString();
        String max_temp_value=txtMaxV.getText().toString();
        String min_temp_value=txtMinV.getText().toString();
        float pitch=(float) mSeekBarPitch.getProgress()/50;
        if (pitch<0.1) pitch=0.1f;
        float speed=(float) msSeekBarSpeed.getProgress()/50;
        if (speed<0.1) speed=0.1f;


        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
        mTTS.speak("Bugün"+City+"Şehrindeki Anlık Sıcaklık " +textv+ "Derece  "+"Şehirdeki Hissedilen  sıcaklık"+textF+"Derece "+"Şehirdeki maksimum Sıcaklık"+max_temp_value+"  Şehirdeki minumum Sıcaklık"+min_temp_value+ "Şehirdeki  Nem oranı"+Nem, TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        if (mTTS!=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}