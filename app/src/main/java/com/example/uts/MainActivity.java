package com.example.uts;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences simpan;
    SharedPreferences.Editor editor;
    public static String prefs;
    TextView ContextHiScore,modeTV;
    LinearLayout ll0,ll1;
    boolean ll0bool=true;
    boolean ll1bool=true;
    int HighScore;
    int mode;
    musicAndSFX m = new musicAndSFX();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m.playMusic(0,MainActivity.this);

        ContextHiScore = findViewById(R.id.contextHiScore);
        ll0 = findViewById(R.id.result0);
        ll1 = findViewById(R.id.result1);
        modeTV = findViewById(R.id.mode);
        mode=getIntent().getIntExtra("mode",0);
        setDisplay();
    }
    public void setDisplay(){
        prefs = String.valueOf(mode);
        simpan = getSharedPreferences(prefs, Context.MODE_PRIVATE);
        editor = simpan.edit();

        setMode(mode);

        HighScore = simpan.getInt("HiScore",0);
        ContextHiScore.setText(String.valueOf(HighScore));
    }
    public void setMode(int x){
        String str="";
        switch (x){
            case 0:
                str = "Random";
                break;
            case 1:
                str = "Indonesia -> Kunyomi";
                break;
            case 2:
                str = "Indonesia -> Onyomi";
                break;
            case 3:
                str = "Indonesia -> Kanji";
                break;
            case 4:
                str = "Kunyomi -> Indonesia";
                break;
            case 5:
                str = "Kunyomi -> Onyomi";
                break;
            case 6:
                str = "Kunyomi -> Kanji";
                break;
            case 7:
                str = "Onyomi -> Indonesia";
                break;
            case 8:
                str = "Onyomi -> Kunyomi";
                break;
            case 9:
                str = "Onyomi -> Kanji";
                break;
            case 10:
                str = "Kanji -> Indonesia";
                break;
            case 11:
                str = "Kanji -> Kunyomi";
                break;
            case 12:
                str = "Kanji -> Onyomi";
                break;
        }
        modeTV.setText(str);
    }

    public void clearHiScore(View view){
        int jumSelesai = simpan.getInt("bestAttempt",0);
        if(jumSelesai > 0){
            removeAllTV();
            editor.remove("lastAttemptScore");
            editor.remove("lastAttemptSoal");
            editor.remove("HiScore");
            editor.remove("bestAttempt");
            editor.commit();
            for(int x = 0;x<jumSelesai;x++){
                editor.remove("Soal "+ x);
                editor.remove("Jawaban "+x);
                editor.remove("BS "+x);
                editor.remove("Pertanyaan "+x);
                editor.remove("Soal Hi Score "+x);
                editor.remove("Jawaban High Score "+x);
                editor.remove("BS HiScore "+x);
                editor.remove("Pertanyaan Hi Score "+x);
                editor.commit();
                HighScore=0;
                ContextHiScore.setText(String.valueOf(HighScore));
            }
            Toast.makeText(getBaseContext(),"High Scoree Dihapus",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getBaseContext(),"Belum ada High score",Toast.LENGTH_SHORT).show();
        }
    }
    public void ShowHiScore(View view){
        int z =simpan.getInt("bestAttempt",0);
        if(z==0){
            Toast.makeText(getBaseContext(),"Tidak ada hi score",Toast.LENGTH_SHORT).show();
        }
        else {
            if(ll0bool){
                setTVloop("bestAttempt","HiScore",
                        "Pertanyaan Hi Score ","Soal Hi Score ","Jawaban High Score ","BS HiScore ",
                        ll0,z);
                ll0bool=false;
            }
            else{
                ll0.removeAllViews();
                ll0bool=true;
            }
        }

    }
    public void showLastPlay(View view){
        int z =simpan.getInt("lastAttemptSoal",0);
        if(z<=0){
            Toast.makeText(getBaseContext(),"Tidak ada last score",Toast.LENGTH_SHORT).show();
        }
        else{
            if(ll1bool){
                setTVloop("lastAttemptSoal","lastAttemptScore",
                        "Pertanyaan ","Soal ","Jawaban ", "BS ",
                        ll1,z);
                ll1bool=false;
            }
            else{
                ll1.removeAllViews();
                ll1bool=true;
            }
        }
    }

    public void setTVloop(String str0,String str1,String Pertanyaan,String Soal,String Jawab,String BS,LinearLayout layout,int z){
        TextView textJwb=new TextView(this);
        String A = String.valueOf(simpan.getInt(str0,0));
        String B = String.valueOf(simpan.getInt(str1,0));
        textJwb.setText("Jumlah soal yang dijawab "+A+" dengan skor "+B);
        layout.addView(textJwb);
        for(int x=0;x<z;x++){
            String pertamyaam = simpan.getString(Pertanyaan+ x,null);
            String soal = simpan.getString(Soal+ x,null);
            String jawab = simpan.getString(Jawab + x,null);
            String bs = simpan.getString(BS+ x,null);
            String str = pertamyaam+" "+soal+" adalah "+jawab+" "+bs;
            TextView textIsi = new TextView(this);
            textIsi.setText(str);
            textIsi.setId(x);
            layout.addView(textIsi);
        }

    }
    public void left(View view){
        mode=mode-1;
        if(mode<0){mode=12;}
        setDisplay();
        removeAllTV();
    }
    public void right(View view){
        mode=mode+1;
        if(mode>12){mode=0;}
        setDisplay();
        removeAllTV();
    }
    public void removeAllTV(){
        ll0.removeAllViews();
        ll0bool=true;
        ll1.removeAllViews();
        ll1bool=true;
    }
    public void play(View view){
        m.stopMusic();
        Intent next = new Intent(this,play.class);
        next.putExtra("mode",mode);
        startActivity(next);
    }
    public void belajar(View view){
        m.stopMusic();
        Intent intent = new Intent(this,belajar.class);
        intent.putExtra("mode",mode);
        startActivity(intent);
    }
}