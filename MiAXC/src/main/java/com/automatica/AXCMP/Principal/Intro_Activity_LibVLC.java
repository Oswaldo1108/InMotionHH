/*****************************************************************************
 * JavaActivity.java
 *****************************************************************************
 * Copyright (C) 2016-2019 VideoLAN
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD license. See the LICENSE file for details.
 *****************************************************************************/

package com.automatica.AXCMP.Principal;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


import com.automatica.AXCMP.R;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.IOException;
import java.util.ArrayList;

public class Intro_Activity_LibVLC extends AppCompatActivity
{
    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;

    private VLCVideoLayout mVideoLayout = null;

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
            {

                setContentView(R.layout.intro_act_vlc);

                final ArrayList<String> args = new ArrayList<>();
                args.add("-vvv");
                mLibVLC = new LibVLC(this, args);
                mMediaPlayer = new MediaPlayer(mLibVLC);

                mVideoLayout = findViewById(R.id.video_layout);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mLibVLC.release();
    }

    @Override
    protected void onResume() {
        super.onStart();
        try {


        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);


            final Media media = new Media(mLibVLC, getResources().openRawResourceFd(R.raw.intro_miaxc_final));
            mMediaPlayer.setMedia(media);
            media.release();





        mMediaPlayer.play();

        mMediaPlayer.setEventListener(new MediaPlayer.EventListener()
        {
            @Override
            public void onEvent(MediaPlayer.Event event)
            {
                Log.i("EVENT", String.valueOf(event.type));


                if(event.type== MediaPlayer.Event.Stopped)
                    {
                        Log.i("EVENT","FINAL");
                        startActivity(new Intent(Intro_Activity_LibVLC.this,Login.class));
                    }
            }
        });
//            while (true)
//            {
//                if(!mMediaPlayer.isPlaying())
//                    {
//                        Log.i("VLC PLAYER UwU", "NOT PLAYING");
//                     //   startActivity(new Intent(this,Login.class));
////                        break;
//                    }
//
//                if(mMediaPlayer.isPlaying())
//                    {
//                        Log.i("VLC PLAYER UwU", "PLAYING");
//                        //   startActivity(new Intent(this,Login.class));
////                        break;
//                    }
//
//
//
//            }


        } catch (Exception e)
            {
//            throw new RuntimeException("Invalid asset folder");

                e.printStackTrace();
        }

        super.onResume();


    }

    @Override
    protected void onStop() {
        super.onStop();

        mMediaPlayer.stop();
        mMediaPlayer.detachViews();
    }

}
