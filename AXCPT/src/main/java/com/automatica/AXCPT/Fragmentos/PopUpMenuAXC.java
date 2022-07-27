package com.automatica.AXCPT.Fragmentos;

import android.content.Context;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PopUpMenuAXC {
    View v;
    Context contexto;
    ContextMenuListener listener;

    public PopUpMenuAXC() {
    }

    public PopUpMenuAXC(View v, Context contexto, int MenuResource) {
        this.v = v;
        this.contexto=contexto;
        //Inicializacion();
        Context wrapper = new ContextThemeWrapper(contexto, R.style.popupMenuStyle);
        PopupMenu mypopupmenu = new PopupMenu(wrapper, v);
        setForceShowIcon(mypopupmenu);
        MenuInflater inflater = mypopupmenu.getMenuInflater();
        inflater.inflate(MenuResource, mypopupmenu.getMenu());
        mypopupmenu.show();
//        mypopupmenu.getMenu().getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        mypopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //txtPreferredLanguage.setText(item.getTitle().toString());
                listener.listenerItem(item);
                return false;
            }
        });
    }

    public PopUpMenuAXC(View v, Context contexto, int MenuResource,boolean False) {
        this.v = v;
        this.contexto=contexto;
        //Inicializacion();
        Context wrapper = new ContextThemeWrapper(contexto, R.style.popupMenuStyle);
        PopupMenu mypopupmenu = new PopupMenu(wrapper, v);
        setForceShowIcon(mypopupmenu);
        MenuInflater inflater = mypopupmenu.getMenuInflater();
        inflater.inflate(MenuResource, mypopupmenu.getMenu());
        mypopupmenu.show();
//        mypopupmenu.getMenu().getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        mypopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //txtPreferredLanguage.setText(item.getTitle().toString());
                listener.listenerItem(item);
                return false;
            }
        });
    }


    public PopUpMenuAXC newInstance(View v, Context contexto, int MenuResource, ContextMenuListener listener){
        PopUpMenuAXC menu = new PopUpMenuAXC(v,contexto,MenuResource,false);
        menu.listener = listener;
        return menu;
    }
    private void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] mFields = popupMenu.getClass().getDeclaredFields();
            for (Field field : mFields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> popupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method mMethods = popupHelper.getMethod("setForceShowIcon", boolean.class);
                    mMethods.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }



    public interface ContextMenuListener{
        void listenerItem(MenuItem item);
    }
}
