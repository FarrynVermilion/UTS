package com.example.uts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class belajar extends AppCompatActivity {
    private SQLiteDatabase db = null;
    private Cursor kamusCursor = null;
    private translate dbTranslate = null;
    Button btn;
    int mode;
    int counter = 1;
    TextView soal,indonesia,kanji,onyomi,kunyomi;
    musicAndSFX m = new musicAndSFX();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.belajar);

        dbTranslate = new translate(this);
        db = dbTranslate.getWritableDatabase();
        dbTranslate.createTable(db);
        dbTranslate.generateData(db);

        mode=getIntent().getIntExtra("mode",0);

        btn=findViewById(R.id.play);
        soal=findViewById(R.id.Soal);
        indonesia=findViewById(R.id.Indonesia);
        kanji=findViewById(R.id.Kanji);
        onyomi=findViewById(R.id.Onyomi);
        kunyomi=findViewById(R.id.Kunyomi);

        run();
    }
    public void run(){
        setMode(mode);
        setText(counter);
        m.randomMusic(belajar.this);
    }

    public void setText(int x) {
        kamusCursor = db.rawQuery("Select ID, INDONESIA, KANJI, ONYOMI, KUNYOMI FROM kamus where ID='" + x + "'", null);
        kamusCursor.moveToFirst();

        String str1 = kamusCursor.getString(1);
        String str2 = kamusCursor.getString(2);
        String str3 = kamusCursor.getString(3);
        String str4 = kamusCursor.getString(4);

        soal.setText(String.valueOf(x));
        indonesia.setText(str1);
        kanji.setText(str2);
        onyomi.setText(str3);
        kunyomi.setText(str4);
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
        String str1="PLAY "+str;
        btn.setText(str1);
    }

    public void prev(View view){
        counter=counter-1;
        if(counter<1){counter=103;}
        setText(counter);
    }
    public void next(View vies){
        counter=counter+1;
        if(counter>103){counter=1;}
        setText(counter);
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
    public void onDestroy() {
        super.onDestroy();
        kamusCursor.close();
        db.close();
    }
}
