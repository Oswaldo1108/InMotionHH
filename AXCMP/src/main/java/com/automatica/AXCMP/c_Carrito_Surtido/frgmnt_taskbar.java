package com.automatica.AXCMP.c_Carrito_Surtido;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.automatica.AXCMP.R;

import androidx.fragment.app.Fragment;

public class frgmnt_taskbar extends Fragment
{
    //VARIABLES
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView txtv_Next,txtv_Last;


    //OBJECTS
    private OnFragmentInteractionListener mListener;

    private ImageButton imgb_Back, imgb_Middle, imgb_Next;

    //VIEWS

    public void setTextNavigator(String Next,String Last)
    {
        txtv_Next.setText(Next);
        txtv_Last.setText(Last);
    }


    public frgmnt_taskbar()
    {
    }

    public static Fragment newInstance(String param1, String param2)
    {
        frgmnt_taskbar fragment = new frgmnt_taskbar();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.quick_info_toolbar, container, false);
        imgb_Back = view.findViewById(R.id.imageButton3);
        imgb_Middle = view.findViewById(R.id.imageButton);
        imgb_Next = view.findViewById(R.id.imageButton2);

        txtv_Last = view.findViewById(R.id.textView2);
        txtv_Next = view.findViewById(R.id.textView3);


        setTextNavigator(mParam2,mParam1);



        imgb_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            //    Toast.makeText(getContext(), "BACK", Toast.LENGTH_SHORT).show();
                mListener.LastActivity();
             //   getActivity().finish();
            }
        });

        imgb_Middle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        imgb_Next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
             //   Toast.makeText(getContext(), "NEXT", Toast.LENGTH_SHORT).show();
              //  Intent i= null;
                mListener.NextActivity();
            }
        });

        return view;
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
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {

        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(boolean estado);
        boolean NextActivity();
        boolean LastActivity();

    }



}
