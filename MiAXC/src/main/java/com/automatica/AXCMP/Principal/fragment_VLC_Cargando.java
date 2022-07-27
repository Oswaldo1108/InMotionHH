package com.automatica.AXCMP.Principal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphamovie.lib.AlphaMovieView;
import com.automatica.AXCMP.R;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.io.IOException;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class fragment_VLC_Cargando extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private static final String frgtag_InicioFrag= "FRGIF";
    private  AlphaMovieView amv;


    private String mParam1;
    private String mParam2;





    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;
    private static final String ASSET_FILENAME = "a_sin_suavizado.webm";

    private VLCVideoLayout mVideoLayout = null;

    private LibVLC mLibVLC = null;
    private MediaPlayer mMediaPlayer = null;

    private OnFragmentInteractionListener mListener;

    public fragment_VLC_Cargando()
    {
        // Required empty public constructor
    }
    public static fragment_VLC_Cargando newInstance(String param1, String param2)
    {
        fragment_VLC_Cargando fragment = new fragment_VLC_Cargando();
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
        View view = inflater.inflate(R.layout.fragment_intro_fragment_vlc, container, false);
        final ArrayList<String> args = new ArrayList<>();
        args.add("-vvv");
        mLibVLC = new LibVLC(getContext(), args);
        mMediaPlayer = new MediaPlayer(mLibVLC);

        mVideoLayout =  view.findViewById(R.id.video_layout);



        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);


        final Media media;
        try
            {
                media = new Media(mLibVLC, getContext().getAssets().openFd(ASSET_FILENAME));
                mMediaPlayer.setMedia(media);
                media.release();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        mMediaPlayer.play();
        return view;
    }


    private void cierraIntro()
    {
        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment_VLC_Cargando.this).commit();

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
//        amv.stop();
//        amv.release();
//        mListener = null;

        super.onDetach();
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);

    }
}
