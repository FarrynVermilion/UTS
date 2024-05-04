package com.example.uts;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Array;

public class translate extends SQLiteOpenHelper {
    private static final String Database_Name= "kamus";
    private static final String INDONESIA = "indonesia";
    private static final String KANJI = "kanji";
    private static final String ONYOMI = "onyomi";
    private static final String KUNYOMI = "kunyomi";
    String[] Indonesia = {"murah","satu","minum","kanan"
            ,"hujan","stasiun","yen","api","bunga","bawah","apa","bertemu","diluar","belajar","interval","mood","sembilan","istirahat","ikan","uang","langit","bulan","melihat","berkata","lama/kuno","lima","dibelakang, setelah","siang","bahasa","sekolahan","mulut","pergi, melaksanakan","mahal, tinggi","negara","sekarang","kiri","tiga","gunung","empat","anak","telinga","waktu","tujuh","mobil","perusahaan","tangan","minggu","sepuluh","keluar","menulis","perempuan","kecil","sedikit","atas","makan","baru","orang","air","hidup, lahir","barat","sungai","seribu","tadi, ujung","sebelum","kaki","banyak","besar","laki-laki","tengah","panjang","surga","toko","listrik","tanah","timur","jalan","membaca","selatan","dua","hari","masuk","tahun","membeli","putih","delapan","separuh","seratus","ayah","menit","mendengar","ibu","utara","pohon","asal, buku","setiap","sepuluh ribu","nama","mata","teman","datang","berdiri","enam","berbicara"
            };
    String[] Kanji = {"安","一", "飲","右"
            ,"雨","駅","円","火","花","下","何","会","外","学","間","気","九","休","魚","金","空","月","見","言","古","五","後","午","語","校","口","行","高","国","今","左","三","山","四","子","耳","時","七","車","社","手","週","十","出","書","女","小","少","上","食","新","人","水","生","西","川","千","先","前","足","多","大","男","中","長","天","店","電","土","東","道","読","南","二","日","入","年","買","白","八","半","百","父","分","聞","母","北","木","本","毎","万","名","目","友","来","立","六","話"
            };
    String[] Onyomi = {"an","ichi","in","u, yuu"
            ,"u","eki","en","ka","ka","ka, ge","ka","kai","gai, ge","gaku","kan","ki, ke","kyuu","kyuu","gyo","kin","kuu","getsu, gatsu","ken","gen, gon","ko","go","go, kou","go","go","kou","kou","gyou, kou","kou","koku","kon","sa","san","san","shi","shi, su","ji","ji","shichi","sha","sha","shu","shuu","juu","shutsu","sho","jo","shou","shou","jou","shoku","shin","jin, nin","sui","sei","sei, sai","sen","sen","sen","zen","soku","ta","dai, tai","dan, nan","chuu","chou","ten","ten","den","do, to","tou","dou","doku","nan","ni","nichi, jitsu","nyuu","nen","bai","haku","hachi","han","hyaku","fu","fun, bun","bun, mon","bo","hoku","moku, boku","hon","mai","man, ban","mei, myou","moku","yuu","rai","ritsu","roku","wa"
    };
    String[] Kunyomi = {"yasu-i","hito-tsu","no-mu","migi",
            "ame","eki","maru","hi","hana","shita, kuda-saru","nani, nan","a-u","soto","mana-bu","ma, aida","ki, ke","kokono-tsu","yasu-mu","sakana, uo","kane","sora","tsuki","mi-ru","koto, i-u","furu-i","itsu-tsu","ato, ushi-ro","go","kata-ru","kou","kuchi","i-ku, okona-u","taka-i","kuni","ima","hidari","mi-tsu","yama","yo-tsu","ko","mimi","toki","nana-tsu","kuruma","yashiro","te","shuu","tou","de-ru, da-su","ka-ku","onna","chii-sai","suku-nai","ue, a-garu","ta-beru","atara-shii","hito","mizu","i-kiru, u-mu","nishi","kawa","chi","saki","mae","ashi","oo-i","oo-kii","otoko","naka","naga-i","ama","mise","den","tsuchi","higashi","michi","yo-mu","minami","futa-tsu","hi, -ka","hai-ru","toshi","ka-u","shiro-i","ya-tsu","naka-ba","hyaku","chichi","wa-keru, wa-karu","ki-ku","haha","kita","ki","moto","mai","man, ban","na","me","tomo","ku-ru","ta-tsu","mu-tsu","hana-su, hanashi"
    };

    public translate (Context context) {
        super(context, Database_Name, null, 1);
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS kamus (id integer primary key autoincrement, indonesia TEXT, kanji TEXT, onyomi TEXT, kunyomi TEXT);");
    }
    public void generateData (SQLiteDatabase db) {
        for(int X=0;X<Indonesia.length;X++){
            ContentValues cv = new ContentValues();
            cv.put(INDONESIA, Indonesia[X]);
            cv.put(KANJI, Kanji[X]);
            cv.put(ONYOMI,Onyomi[X]);
            cv.put(KUNYOMI,Kunyomi[X]);
            db.insert("kamus", INDONESIA, cv);
        }
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onCreate (SQLiteDatabase db) {

    }
}
