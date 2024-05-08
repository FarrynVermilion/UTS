package com.example.uts;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Random;

public class musicAndSFX {
    static MediaPlayer mp;
    MediaPlayer sfx;

    public static void randomMusic(Context context){
        int maxOpsi = 7;
        int min = 3;
        Random rand = new Random();
        int x = rand.nextInt(maxOpsi - min + 1) + min;
        playMusic(x,context);
    }

    // static supaya gk kena GC android
    public static void playMusic(int x, Context context){
        switch (x){
            case 0:
                mp = MediaPlayer.create(context,R.raw.the_promise);
                break;
            case 1:
                mp = MediaPlayer.create(context,R.raw.hollow_seclusion);
                break;
            case 2:
                mp = MediaPlayer.create(context,R.raw.glory_fanfare);
                break;
            case 3:
                mp = MediaPlayer.create(context,R.raw.missing_link);
                break;
            case 4:
                mp = MediaPlayer.create(context,R.raw.labyrinth_of_chaos);
                break;
            case 5:
                mp = MediaPlayer.create(context,R.raw.historia_crux);
                break;
            case 6:
                mp = MediaPlayer.create(context,R.raw.the_gapra_whitewood);
                break;
            case 7:
                mp = MediaPlayer.create(context,R.raw.the_ark);
                break;

        }
        mp.setLooping(true);
        mp.start();
    }
    // karena gk terus terusan kayak bg static gk perlu
    public void playSFX(int x, Context context){
        switch (x){
            case 0:
                sfx = MediaPlayer.create(context,R.raw.beep_short);
                break;
            case 1:
                sfx = MediaPlayer.create(context,R.raw.clang_and_wobble);
                break;
        }
        sfx.setLooping(false);
        sfx.start();
    }

    // gk perlu static juga buat stop
    public void stopMusic(){
        mp.stop();
        mp.release();
    }
    public void stopSFX(){
        sfx.stop();
        sfx.release();
    }
}
