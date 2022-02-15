package com.nexis.seslihavadurumu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    Button btn1,btn2,btn3;
    private TextToSpeech mTTS;
    //burada butonlarımı tanıttım ve TextToSpeech kütüphanesinden mTTS'i oluşturdum
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn1=findViewById(R.id.goToLocApp);
        btn2=findViewById(R.id.goTooWeatherApp);
        btn3=findViewById(R.id.btnMicVoiceAgain);
        //butonları implement ettim
        mTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            //sesli komut için bu mTTS i olusturuyorum
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS){
                    Locale locale = new Locale("tr","TR");
                    // burada dilini türkçe olarak ayarladım.
                    int result =mTTS.setLanguage(locale);
                    if (result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","Dil desteklenmiyor");
                    }else{
                        //mButtonSpeak.setEnabled(true);
                    }
                }else{
                    Log.e("TTS","Initialization Failed");
                }

            }
        });
        voiceauto();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,LocationVoiceActivity.class);
                startActivity(intent);
                // 1. butona basıldığında ne olacağını yukarıda belirttim

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                // aynı şekilde ikinci butona basıldığında

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak("Engelsiz Hayat uygulamasına Hoşgeldiniz. Bu sesi Aldıkdan sonra nerede diyerek Neredeyim Uygulamasına hava diyerek ise Sesli Hava Durumu Uygulamasına " +
                        "gidebilirsiniz Tekrar ses alma ekranının açılması için en alta bulunan Hoperlör simgesine basın",TextToSpeech.QUEUE_FLUSH,null);
                voiceauto();
            }
        });
    }
    private void voiceauto() {
        Intent ses = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        ses.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        Locale locale2 = new Locale("tr","TR");
        //ses.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.setDefault(locale2));
        ses.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"tr");
        ses.putExtra(RecognizerIntent.EXTRA_PROMPT,"Seçmek istediğiniz Uygulamayı Söyleyin");
        startActivityForResult(ses,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            ArrayList<String> arrayList =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (arrayList.get(0).toString().equals("nerede")){
                Intent intentToLocationAppWithVoice=new Intent(LoginActivity.this,LocationVoiceActivity.class);
                mTTS.speak("Neredeyim Uygulamasına hoşgeldiniz. Bu uygulama ile Olası kaybolma durumlarında anlık lokasyonunuza " +
                        "erişebilirsiniz. Tam ortadaki tuşa basarak lokasyonunuzu alabilirsiniz .", TextToSpeech.QUEUE_FLUSH,null);
                startActivity(intentToLocationAppWithVoice);
            }else if (arrayList.get(0).toString().equals("hava")){
                Intent intentToWeatherAppWithVoice = new Intent(LoginActivity.this,MainActivity.class);
                mTTS.speak("Sesli Hava Durumu Uygulamasına Hoşgeldiniz. Bu uygulama ile Bulunduğunuz şehri sesli bir şekilde belirterek hava durumunu değerlerini sesli " +
                        "bir  şekilde öğrenebilirsiniz " ,TextToSpeech.QUEUE_FLUSH,null);
                startActivity(intentToWeatherAppWithVoice);
            }
        }
    }
    public void konus1(){
        mTTS.speak("Neredeyim Uygulamasına hoşgeldiniz. Bu uygulama ile Olası kaybolma durumlarında anlık lokasyonunuza " +
                "erişebilirsiniz. Tam ortadaki tuşa basarak lokasyonunuzu alabilirsiniz .", TextToSpeech.QUEUE_FLUSH,null);
    }
    public void konus2(){
        mTTS.speak("Sesli Hava Durumu Uygulamasına Hoşgeldiniz. Bu uygulama ile Bulunduğunuz şehri sesli bir şekilde belirterek hava durumunu değerlerini sesli " +
                "bir  şekilde öğrenebilirsiniz " ,TextToSpeech.QUEUE_FLUSH,null);
    }
}