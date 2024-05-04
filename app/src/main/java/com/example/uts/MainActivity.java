package com.example.uts;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
    public static final String prefs = "test";
    TextView ContextHiScore,TV,TV0;
    LinearLayout ll0,ll1;
    boolean ll0bool,ll1bool;
    int HighScore;
    int jumSelesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpan = getSharedPreferences(prefs, Context.MODE_PRIVATE);
        editor = simpan.edit();

        ContextHiScore = findViewById(R.id.contextHiScore);
        ll0 = findViewById(R.id.result0);
        ll1 = findViewById(R.id.result1);
        ll0bool = true;
        ll1bool = true;

        HighScore = simpan.getInt("HiScore",0);
        ContextHiScore.setText(String.valueOf(HighScore));

        jumSelesai = simpan.getInt("bestAttempt",0);
    }

    public void clearHiScore(View view){
        if(jumSelesai > 0){
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
                TV0 = new TextView(this);
                String A = String.valueOf(simpan.getInt("bestAttempt",0));
                String B = String.valueOf(simpan.getInt("HiScore",0));
                TV0.setText("Jumlah soal yang dijawab "+A+" dengan skor "+B);
                ll0.addView(TV0);
                for(int x=0;x<z;x++){
                    String pertamyaam = simpan.getString("Pertanyaan Hi Score "+ x,null);
                    String soal = simpan.getString("Soal Hi Score "+ x,null);
                    String jawab = simpan.getString("Jawaban High Score "+ x,null);
                    String bs = simpan.getString("BS HiScore "+ x,null);
                    String str = pertamyaam+" "+soal+" adalah "+jawab+" "+bs;
                    TV = new TextView(this);
                    TV.setText(str);
                    TV.setId(x);
                    ll0.addView(TV);
                    ll0.setVisibility(View.VISIBLE);
                }
                ll0bool=false;
            }
            else {
                ll0.removeAllViews();
                ll0bool=true;
            }
        }

    }
    public void showLastPlay(View view){
        int z =simpan.getInt("lastAttemptSoal",0);
        if(z==0){
            Toast.makeText(getBaseContext(),"Tidak ada last score",Toast.LENGTH_SHORT).show();
        }
        else{
            if(ll1bool){
                TV0=new TextView(this);
                String A = String.valueOf(simpan.getInt("lastAttemptSoal",0));
                String B = String.valueOf(simpan.getInt("lastAttemptScore",0));
                TV0.setText("Jumlah soal yang dijawab "+A+" dengan skor "+B);
                ll1.addView(TV0);
                for(int x=0;x<z;x++){
                    String pertamyaam = simpan.getString("Pertanyaan "+ x,null);
                    String soal = simpan.getString("Soal "+ x,null);
                    String jawab = simpan.getString("Jawaban "+ x,null);
                    String bs = simpan.getString("BS "+ x,null);
                    String str = pertamyaam+" "+soal+" adalah "+jawab+" "+bs;
                    TV = new TextView(this);
                    TV.setText(str);
                    TV.setId(x);
                    ll1.addView(TV);
                    ll1.setVisibility(View.VISIBLE);
                }
                ll1bool=false;
            }
            else {
                ll1.removeAllViews();
                ll1bool=true;
            }
        }
    }
    public void play(View view){
        Intent next = new Intent(this,play.class);
        startActivity(next);
    }
}