package com.example.uts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class play extends AppCompatActivity {
    private SQLiteDatabase db = null;
    private Cursor[] kamusCursor = new Cursor[4];
    private translate dbTranslate = null;

    TextView PertanyaanTV,SoalTV,SkorTV,TimerTV;
    RadioGroup RG;
    Button BTN;
    RadioButton jawaban;
    RadioButton[] RB = new RadioButton[4];

    ArrayList<Integer >soalNo = new ArrayList<>();
    ArrayList<Integer >soal = new ArrayList<>();
    ArrayList<Integer >jwb = new ArrayList<>();
    ArrayList<Integer >opsi1 = new ArrayList<>();
    ArrayList<Integer >opsi2 = new ArrayList<>();
    ArrayList<Integer >opsi3 = new ArrayList<>();

    ArrayList<String>SOAL = new ArrayList<>();
    ArrayList<String>JAWABAN = new ArrayList<>();
    ArrayList<String>BS = new ArrayList<>();
    ArrayList<String>PERTANYAAN = new ArrayList<>();

    int skorCounter = 0;

    SharedPreferences simpan;
    SharedPreferences.Editor editor;

    int createCounter=0;
    int saveCounter=1;


    CountDownTimer myCountDownTimer;
    long  elapsedCountDown;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        //initialize kamus cursor dan db
        for(int x=0;x<kamusCursor.length;x++){
            kamusCursor[x]=null;
        }

        dbTranslate = new translate(this);
        db = dbTranslate.getWritableDatabase();
        dbTranslate.createTable(db);
        dbTranslate.generateData(db);

        //create random buat soal,jwabana, dan opsi
        initRandom();

        //simpan ke shared preference
        simpan = getSharedPreferences("test", Context.MODE_PRIVATE);
        editor = simpan.edit();

        PertanyaanTV = findViewById(R.id.pertanyaan);
        SoalTV = findViewById(R.id.soal);
        TimerTV = findViewById(R.id.timer);
        RG = findViewById(R.id.answer);
        RB[0]= findViewById(R.id.A);
        RB[1]= findViewById(R.id.B);
        RB[2]= findViewById(R.id.C);
        RB[3]= findViewById(R.id.D);
        BTN= findViewById(R.id.jawab);
        SkorTV=findViewById(R.id.score);

        clearScore();

        createSoal(soalNo.get(createCounter),soal.get(createCounter),jwb.get(createCounter),opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
        timer(60000,0);
        addListenerButton();
    }

    public void initRandom(){
        Random rand = new Random();
        int maxSoal = 103;
        int maxOpsi = 4;
        int min = 1;
        for (int x = min;x<=maxSoal;x++){
            soalNo.add(x);

            int y = rand.nextInt(maxOpsi - min + 1) + min;
            int z = rand.nextInt(maxOpsi - min + 1) + min;
            while(y==z){
                y=rand.nextInt(maxOpsi - min + 1) + min;
            }
            soal.add(y);
            jwb.add(z);
        }
        Collections.shuffle(soalNo);

        for (int x=0; x<maxSoal; x++){
            int num = soalNo.get(x);
            int num1 = rand.nextInt(maxSoal - min + 1) + min;
            int num2 = rand.nextInt(maxSoal - min + 1) + min;
            int num3 = rand.nextInt(maxSoal - min + 1) + min;

            while(num==num1||num==num2||num==num3||num1==num2||num1==num3||num2==num3){
                num1 = rand.nextInt(maxSoal - min + 1) + min;
                num2 = rand.nextInt(maxSoal - min + 1) + min;
                num3 = rand.nextInt(maxSoal - min + 1) + min;
            }
            opsi1.add(num1);
            opsi2.add(num2);
            opsi3.add(num3);
        }
    }
    public void createSoal(int SoalNo,int Soal,int Jawab, int Opsi1, int Opsi2, int Opsi3){
        kamusCursor[0] = db.rawQuery("Select ID, INDONESIA, KANJI, ONYOMI, KUNYOMI FROM kamus where ID='" + SoalNo + "'", null);
        kamusCursor[1] = db.rawQuery("Select ID, INDONESIA, KANJI, ONYOMI, KUNYOMI FROM kamus where ID='" + Opsi1 + "'", null);
        kamusCursor[2] = db.rawQuery("Select ID, INDONESIA, KANJI, ONYOMI, KUNYOMI FROM kamus where ID='" + Opsi2 + "'", null);
        kamusCursor[3] = db.rawQuery("Select ID, INDONESIA, KANJI, ONYOMI, KUNYOMI FROM kamus where ID='" + Opsi3 + "'", null);
        for(int x=0;x<kamusCursor.length;x++){
            kamusCursor[x].moveToFirst();
        }

        //acak opsi
        ArrayList<String> arr = new ArrayList<>();
        for(int x = 0; x<RB.length;x++){
            arr.add(String.valueOf(kamusCursor[x].getString(Jawab)));
        }
        Collections.shuffle(arr);

        //display opsi
        SoalTV.setText(kamusCursor[0].getString(Soal));
        PertanyaanTV.setText(pertanyaan(Jawab));
        for(int x = 0; x<RB.length;x++){
            RB[x].setText(arr.get(x));
        }

        SOAL.add(String.valueOf(kamusCursor[0].getString(Soal)));
        JAWABAN.add(String.valueOf(kamusCursor[0].getString(Jawab)));
        PERTANYAAN.add(String.valueOf(pertanyaan(Jawab)));
    }


    public String pertanyaan(int x){
        String result = "";
        switch (x){
            case 1:
                result = "Indonesia dari";
                break;
            case 2:
                result = "Kanji dari";
                break;
            case 3:
                result = "Onyomi dari";
                break;
            case 4:
                result = "Kunyomi dari";
                break;
        }
        return result;
    }
    public void addListenerButton() {
        BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = RG.getCheckedRadioButtonId();
                if(selectedId==-1){
                    Toast.makeText(getBaseContext(),"Belum menjawab",Toast.LENGTH_SHORT).show();
                }
                else{
                    // find the radiobutton by returned id
                    jawaban = findViewById(selectedId);
                    String X = JAWABAN.get(createCounter);
                    String Y = String.valueOf(jawaban.getText());
                    createCounter=createCounter+1;

                    myCountDownTimer.cancel();
                    if(X.equals(Y)){
                        skorCounter=skorCounter+10;
                        SkorTV.setText(String.valueOf(skorCounter));
                        BS.add("Benar");
                        timer(elapsedCountDown,5000);
                    }
                    else{
                        BS.add("Salah");
                        timer(elapsedCountDown,-5000);
                    }
                    save();
                    checkLoop();
                }
            }
        });
    }
    public void save(){
        int HighSkor = simpan.getInt("HiScore",0);
        for(int x =0; x<JAWABAN.size();x++){
            String str1 = SOAL.get(x);
            String str2 = JAWABAN.get(x);
            String str3 = BS.get(x);
            String str4 = PERTANYAAN.get(x);
            editor.putString("Soal "+x,str1);
            editor.putString("Jawaban "+x,str2);
            editor.putString("BS "+x,str3);
            editor.putString("Pertanyaan "+x,str4);
            editor.putInt("lastAttemptScore",skorCounter);
            editor.putInt("lastAttemptSoal",saveCounter);
            if(skorCounter>=HighSkor){
                editor.putString("Soal Hi Score "+x,str1);
                editor.putString("Jawaban High Score "+x,str2);
                editor.putString("BS HiScore "+x,str3);
                editor.putString("Pertanyaan Hi Score "+x,str4);
                editor.putInt("HiScore",skorCounter);
                editor.putInt("bestAttempt",saveCounter);
            }

            editor.commit();
        }
        saveCounter=saveCounter+1;
    }
    public void timer(long x,long y){
        myCountDownTimer=new CountDownTimer(x+y, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                elapsedCountDown=millisUntilFinished;
                TimerTV.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                TimerTV.setText("00:00:00");
                nextPage();
            }
        }.start();
    }
    public void goToResult(View view){
        nextPage();
    }
    public void nextPage(){
        myCountDownTimer.cancel();
        Intent next = new Intent(this,result.class);
        next.putExtra("skor",skorCounter);
        startActivity(next);

    }
    public void onDestroy() {
        super.onDestroy();
        for(int x=0;x<kamusCursor.length;x++){
            kamusCursor[x].close();
        }
        db.close();
    }
    public void clearScore(){
        int jumSelesai = simpan.getInt("lastAttemptSoal",0);
        if(jumSelesai > 0){
            editor.remove("lastAttemptScore");
            editor.remove("lastAttemptSoal");
            editor.commit();
            for(int x = 0;x<jumSelesai;x++){
                editor.remove("Soal "+ x);
                editor.remove("Jawaban "+x);
                editor.remove("BS "+x);
                editor.remove("Pertanyaan "+x);
                editor.commit();
            }
        }
    }
    public void checkLoop(){
        if(createCounter>=soalNo.size()){
            String str = "semua soal sudah dijawab semua";
            Toast.makeText(getBaseContext(),str, Toast.LENGTH_SHORT).show();
            nextPage();
        }
        else{
            Toast.makeText(getBaseContext(),String.valueOf(createCounter), Toast.LENGTH_SHORT).show();
            createSoal(soalNo.get(createCounter),soal.get(createCounter),jwb.get(createCounter),opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
        }
    }
}
