package com.example.uts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class play extends AppCompatActivity {
    //global var db
    private SQLiteDatabase db = null;
    private Cursor[] kamusCursor = new Cursor[4];
    private translate dbTranslate = null;

    // global var buat display
    TextView PertanyaanTV,SoalTV,SkorTV,TimerTV;
    RadioGroup RG;
    Button BTN;
    RadioButton jawaban;
    RadioButton[] RB = new RadioButton[4];

    //arraylist buat soal, jawaban, dan opsi namanya harusnya jelas(mungkin?)
    ArrayList<Integer>soalNo = new ArrayList<>();
    ArrayList<Integer>soal = new ArrayList<>();
    ArrayList<Integer>jwb = new ArrayList<>();
    ArrayList<Integer>opsi1 = new ArrayList<>();
    ArrayList<Integer>opsi2 = new ArrayList<>();
    ArrayList<Integer>opsi3 = new ArrayList<>();

    //array list string untuk disimpan ke shared pref
    ArrayList<String>SOAL = new ArrayList<>();//array list string soal
    ArrayList<String>JWBN = new ArrayList<>();//array list jawaban benar
    ArrayList<String>BS = new ArrayList<>();//buat simpan jawaban salah bener atau gk
    ArrayList<String>PERTANYAAN = new ArrayList<>();//ini simpan fungsi pertanyaan
    ArrayList<String>JAWABAN = new ArrayList<>();//array list jawaban user

    //skor counter
    int skorCounter = 0;

    //shared preference global var
    SharedPreferences simpan;
    SharedPreferences.Editor editor;

    //sebenarnya bisa disatuin tapi dipisah aja
    //supaya bisa ditaro sebelum counter bikin soal sekarang berapa
    int createCounter=0;
    int saveCounter=1;

    //global var timer
    CountDownTimer myCountDownTimer;
    long elapsedCountDown;

    //mode main nya apa
    int mode;

    //main musik bg bikin obyek
    musicAndSFX m = new musicAndSFX();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        //initialize kamus cursor dan db
        Arrays.fill(kamusCursor, null);
        //set db
        dbTranslate = new translate(this);
        db = dbTranslate.getWritableDatabase();
        dbTranslate.createTable(db);
        dbTranslate.generateData(db);

        //set tombol
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

        //set mode main
        mode=getIntent().getIntExtra("mode",0);

        //simpan ke shared preference
        simpan = getSharedPreferences(String.valueOf(mode), Context.MODE_PRIVATE);
        editor = simpan.edit();

        //clear shared prefe permainan sebelumnya
        clearScore();

        //create random buat soal,jawaban, dan opsi
        initRandom();

        // bacgkround music ada warning statis acuhin soalnya
        // kalo gk bakal kena GC (garbage collection) android
        m.randomMusic(play.this);
        m.playSFX(2,play.this);
        //create soal
        pilihanModeSoal(mode);

        timer(60000,0);
        addListenerButton();
    }
    // buat arraylist dengan angka acak
    public void initRandom(){
        //init object dari random util
        Random rand = new Random();

        //soal di db ada berapa sekarang 103
        int maxSoal = 103;

        //banyak opsi ada 4
        int maxOpsi = 4;

        //1 karena primary key dimulai dari 1, dan +1 untuk key selanjutnya
        int min = 1;

        for (int x = min;x<=maxSoal;x++){
            //masukin angka ke soalno buat diacak
            soalNo.add(x);

            //bikin soal dan jawaban dari 1-4
            // angka merepresenttasikan kolom di db
            int y = rand.nextInt(maxOpsi - min + 1) + min;
            int z = rand.nextInt(maxOpsi - min + 1) + min;
            //pastiin gk sama
            while(y==z){
                y=rand.nextInt(maxOpsi - min + 1) + min;
            }

            // masukin soal dan jawaban ke arraylist
            soal.add(y);
            jwb.add(z);
        }
        // acak soalNo
        Collections.shuffle(soalNo);

        // bikin opsi
        for (int x=0; x<maxSoal; x++){

            //ambil soal nomor berapa
            int num = soalNo.get(x);

            // bikin opsi salah dalam bayangin row dalem db
            // nanti ambil dari kolom jawaban
            int num1 = rand.nextInt(maxSoal - min + 1) + min;
            int num2 = rand.nextInt(maxSoal - min + 1) + min;
            int num3 = rand.nextInt(maxSoal - min + 1) + min;

            //pastiin gk ada yang sama 1-103
            while(num==num1||num==num2||num==num3||num1==num2||num1==num3||num2==num3){
                num1 = rand.nextInt(maxSoal - min + 1) + min;
                num2 = rand.nextInt(maxSoal - min + 1) + min;
                num3 = rand.nextInt(maxSoal - min + 1) + min;
            }
            //masukin ke arraylist
            opsi1.add(num1);
            opsi2.add(num2);
            opsi3.add(num3);
        }
    }
    public void pilihanModeSoal(int x){
        switch (x){
            //random
            case 0:
                createSoal(soalNo.get(createCounter),soal.get(createCounter),jwb.get(createCounter),opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Indonesia -> Kunyomi
            case 1:
                createSoal(soalNo.get(createCounter),1,4,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Indonesia -> Onyomi
            case 2:
                createSoal(soalNo.get(createCounter),1,3,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Indonesia -> Kanji
            case 3:
                createSoal(soalNo.get(createCounter),1,2,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Kunyomi -> Indonesia
            case 4:
                createSoal(soalNo.get(createCounter),4,1,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Kunyomi -> Onyomi
            case 5:
                createSoal(soalNo.get(createCounter),4,3,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Kunyomi -> Kanji
            case 6:
                createSoal(soalNo.get(createCounter),4,2,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Onyomi -> Indonesia
            case 7:
                createSoal(soalNo.get(createCounter),3,1,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Onyomi -> Kunyomi
            case 8:
                createSoal(soalNo.get(createCounter),3,4,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Onyomi -> Kanji
            case 9:
                createSoal(soalNo.get(createCounter),3,2,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Kanji -> Indonesia
            case 10:
                createSoal(soalNo.get(createCounter),2,1,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Kanji -> Kunyomi
            case 11:
                createSoal(soalNo.get(createCounter),2,3,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
            //Kanji -> Onyomi
            case 12:
                createSoal(soalNo.get(createCounter),2,4,opsi1.get(createCounter),opsi2.get(createCounter),opsi3.get(createCounter));
                break;
        }
    }

    // buat soal
    public void createSoal(int SoalNo,int Soal,int Jawab, int Opsi1, int Opsi2, int Opsi3){
        // init cursor dan db
        // kalo bikin for harus array dalem arraylist atau arraylist dalam arraylist
        // contoh arralist<array>
        // ArrayList<Integer[]> Opsi = new ArrayList<Integer[]>();
        // atau arraylist <arraylist>
        // ArrayList<ArrayList<Integer>> outer = new ArrayList<ArrayList<Integer>>();
        // ArrayList<Integer> inner = new ArrayList<Integer>();
        // bikin rumit, nambahin line of code, gk perlu dan susah jelasin
        // jdai gini aja fungsi nya

        // kamus cursor init 0 yang bener buat soal
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

        //buat pertanyaan
        String str = pertanyaan(Jawab);

        //display opsi
        SoalTV.setText(kamusCursor[0].getString(Soal));
        PertanyaanTV.setText(str);
        for(int x = 0; x<RB.length;x++){
            RB[x].setText(arr.get(x));
        }
        //simpan jawaban untuk dicocokan nanti
        JWBN.add(String.valueOf(kamusCursor[0].getString(Jawab)));

        //simpan soal jawaban dan pertanyaan ke arraylist untuk save
        // ke shared preference
        SOAL.add(String.valueOf(kamusCursor[0].getString(Soal)));
        PERTANYAAN.add(String.valueOf(str));
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
        // ketika klik play
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

                    // ambil string buat compare
                    String X = JWBN.get(createCounter);
                    String Y = String.valueOf(jawaban.getText());

                    //simpan jawaban yang diinput ke arraylist
                    JAWABAN.add(Y);

                    // counter buat soal selanjutnya
                    createCounter=createCounter+1;

                    //tutup timer
                    myCountDownTimer.cancel();

                    //perbandingan jawaban
                    // kalau benar lakukan kalo salah else
                    if(X.equals(Y)){
                        skorCounter=skorCounter+10;
                        SkorTV.setText(String.valueOf(skorCounter));
                        BS.add("Benar");
                        timer(elapsedCountDown,5000);
                        m.playSFX(0,play.this);
                    }
                    else{
                        BS.add("Salah");
                        timer(elapsedCountDown,-5000);
                        m.playSFX(1,play.this);
                    }

                    //method dan function
                    save();
                    checkLoop();
                }
            }
        });
    }
    public void save(){
        // simpan ke shared pref
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
                // lakukan selama masih berdetik
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;

                //simpan ke var global buat diambil timer selanjutnya
                elapsedCountDown=millisUntilFinished;

                //text timer
                TimerTV.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // kalo timer abis set text gk perlu tapi kayaknya gk masalah
            // toh gak hitung jumlah line of code
            public void onFinish() {
                TimerTV.setText("00:00:00");
                funcNextPage();;
            }
        }.start();
    }
    public void goToResult(View view){
        nextPage();
    }

    //fungsi apa aja yang dilakuin pas mau ke halaman selanjutnya
    public void nextPage(){
        if (JAWABAN.isEmpty()){
            Toast.makeText(getBaseContext(),"Belum dikerjakan",Toast.LENGTH_SHORT).show();
            funcNextPage();
        }
        else {
            funcNextPage();
        }
    }
    public void funcNextPage(){
        m.stopMusic();
        m.stopSFX();
        myCountDownTimer.cancel();
        Intent next = new Intent(this,result.class);

        next.putExtra("mode",mode);
        next.putExtra("skor",skorCounter);
        startActivity(next);
    }
    public void onDestroy() {
        // tutup db kalau tutup aplikasi
        super.onDestroy();
        for(int x=0;x<kamusCursor.length;x++){
            kamusCursor[x].close();
        }
        db.close();
    }
    public void clearScore(){
        //untuk hapus skor sebelumnya
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
        // kalau lebih dari soalNo size (jumlahnya sekarang 103) game selesai
        // kalau kurang game lanjut
        if(createCounter>=soalNo.size()){
            String str = "semua soal sudah dijawab semua";
            Toast.makeText(getBaseContext(),str, Toast.LENGTH_SHORT).show();
            funcNextPage();
        }
        else{
            Toast.makeText(getBaseContext(),String.valueOf(createCounter), Toast.LENGTH_SHORT).show();
            pilihanModeSoal(mode);
        }
    }
}
