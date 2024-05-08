package com.example.uts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class result extends AppCompatActivity {
    TextView skorTV,nameTV,TV,TV0;
    SharedPreferences simpan;
    int mode;
    LinearLayout ll;
    boolean bool = true;
    musicAndSFX m = new musicAndSFX();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        mode = getIntent().getIntExtra("mode",0);
        simpan = getSharedPreferences(String.valueOf(mode), Context.MODE_PRIVATE);

        skorTV=findViewById(R.id.skor);
        ll = findViewById(R.id.result);

        int skr = getIntent().getIntExtra("skor",0);
        skorTV.setText(String.valueOf(skr));
        if(skr >= simpan.getInt("HiScore",0)){
            m.playMusic(2,result.this);
            nameTV=findViewById(R.id.nameSK);
            nameTV.setText("New High score");
        }
        else{
            m.playMusic(1,result.this);
        }
    }
    public void showResultPlay(View view){
        int z = simpan.getInt("lastAttemptSoal",0);
        if (bool){
            TV0=new TextView(this);
            String A = String.valueOf(simpan.getInt("lastAttemptSoal",0));
            String B = String.valueOf(simpan.getInt("lastAttemptScore",0));
            TV0.setText("Jumlah soal yang dijawab "+A+" dengan skor "+B);
            ll.addView(TV0);
            for(int x=0;x<z;x++){
                String pertamyaam = simpan.getString("Pertanyaan "+ x,null);
                String soal = simpan.getString("Soal "+ x,null);
                String jawab = simpan.getString("Jawaban "+ x,null);
                String bs = simpan.getString("BS "+ x,null);
                String str = pertamyaam+" "+soal+" adalah "+jawab+" "+bs;
                TV = new TextView(this);
                TV.setText(str);
                TV.setId(x);
                ll.addView(TV);
                ll.setVisibility(View.VISIBLE);
                bool = false;
            }
        }
        else {
            ll.removeAllViews();
            bool=true;
        }
    }
    public void goToMainpage(View view){
        m.stopMusic();
        Intent next = new Intent(this,MainActivity.class);
        next.putExtra("mode",mode);
        startActivity(next);
    }
    public void playAgain(View view){
        m.stopMusic();
        Intent next = new Intent(this,play.class);
        next.putExtra("mode",mode);
        startActivity(next);
    }
}
