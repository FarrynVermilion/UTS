package com.example.uts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class belajar extends AppCompatActivity {
    private SQLiteDatabase db = null;
    private Cursor kamusCursor = null;
    private translate dbTranslate = null;
    int mode;
    TableLayout TL;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.belajar);

        dbTranslate = new translate(this);
        db = dbTranslate.getWritableDatabase();
        dbTranslate.createTable(db);
        dbTranslate.generateData(db);

        mode =getIntent().getIntExtra("mode",0);

        TL =findViewById(R.id.belajarTL);

        run();
    }
    public void run(){
        String[] header = {"NO","Indonesia","Kanji","Onyomi","Kunyomi"};
        for(int A=0;A<=103;A++){
            TableRow TR = new TableRow(this);
            TR.setBackground(getResources().getDrawable(R.drawable.line));

            TextView[] TV = new TextView[5];
            for(int B = 0;B<5;B++) {
                TV[B] = new TextView(this);

                if (A == 0) {
                    TV[B].setText(header[B]);
                } else {
                    kamusCursor = db.rawQuery("Select ID, INDONESIA, KANJI, ONYOMI, KUNYOMI FROM kamus where ID='" + A + "'", null);
                    kamusCursor.moveToFirst();
                    String str = kamusCursor.getString(B);
                    TV[B].setText(str);
                }
                TV[B].setGravity(Gravity.CENTER_HORIZONTAL);
                TR.addView(TV[B]);
            }
            TL.addView(TR);
        }
    }

    public void goToMainpage(View view){
        Intent next = new Intent(this,MainActivity.class);
        next.putExtra("mode",mode);
        startActivity(next);
    }
    public void playAgain(View view){
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
