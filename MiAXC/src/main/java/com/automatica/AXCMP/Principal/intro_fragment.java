package com.automatica.AXCMP.Principal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphamovie.lib.AlphaMovieView;
import com.automatica.AXCMP.R;

public class intro_fragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private static final String frgtag_InicioFrag= "FRGIF";
    private  AlphaMovieView amv;


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public intro_fragment()
    {
        // Required empty public constructor
    }
    public static intro_fragment newInstance(String param1, String param2)
    {
        intro_fragment fragment = new intro_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_intro_fragment, container, false);
//        VideoView videoView = view.findViewById(R.id.videoView);
      //  videoView.resolveAdjustedSize(,)
        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.a_sin_suavizado);

           amv = view.findViewById(R.id.videoView);

            amv.setVideoFromUri(getContext(), video);
            //amv.setVideoByUrl("http://simpl.info/videoalpha/video/soccer1.webm");
            amv.start();

            amv.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //amv.stop();
                }
            });
//        videoView.setVideoURI(video);
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
//        {
//            @Override
//            public void onCompletion(MediaPlayer mp)
//            {
//                cierraIntro();
//            }
//        });
//
//        videoView.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                cierraIntro();
//                return false;
//            }
//        });
//        videoView.start();
        //Toast.makeText(getContext(), "JORGE SE LA COME", Toast.LENGTH_LONG).show();

        return view;
    }


    private void cierraIntro()
    {
        getActivity().getSupportFragmentManager().beginTransaction().remove(intro_fragment.this).commit();

    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
            {
                mListener.onFragmentInteraction(uri);
            }
    }



    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            {
                mListener = (OnFragmentInteractionListener) context;
            } else
            {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
    }

    @Override
    public void onDetach()
    {
        amv.stop();
        amv.release();

        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);

    }
}
