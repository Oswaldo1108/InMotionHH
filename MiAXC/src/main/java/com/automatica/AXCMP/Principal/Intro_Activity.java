package com.automatica.AXCMP.Principal;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import com.automatica.AXCMP.R;

public class Intro_Activity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_);




    }

    private void cierraIntro()
    {
        startActivity(new Intent(Intro_Activity.this,Login.class));
    }


    @Override
    protected void onResume()
    {
        VideoView videoView = findViewById(R.id.videoView);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_miaxc_final);
        videoView.setVideoURI(video);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                cierraIntro();
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
               // cierraIntro();
                return false;
            }
        });
        videoView.start();
      //  selectCodec("mamarre");
        super.onResume();
    }

//
//    private static MediaCodecInfo selectCodec(String mimeType) {
//        int numCodecs = 0;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
//            {
//                numCodecs = MediaCodecList.getCodecCount();
//
//                for (int i = 0; i < numCodecs; i++)
//                    {
//                        MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
//
//                        if (!codecInfo.isEncoder())
//                            {
//                                continue;
//                            }
//
//                        String[] types = codecInfo.getSupportedTypes();
//                        for (int j = 0; j < types.length; j++)
//                            {
////                                if (types[j].equalsIgnoreCase(mimeType))
////                                    {
////                                        return codecInfo;
////                                    }
//                                Log.i("CODECS", "selectCodec: " + types[j]);
//                            }
//                    }
//            }
//        return null;
//    }

}
