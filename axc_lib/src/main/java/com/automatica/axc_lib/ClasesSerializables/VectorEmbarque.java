package com.automatica.axc_lib.ClasesSerializables;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;
import java.util.Vector;

public class VectorEmbarque extends Vector<Embarque> implements KvmSerializable
{
    private static final long serialVersionUID = -1166006770093411055L;

    @Override
    public Object getProperty(int arg0) {
        return this.get(arg0);
    }

    @Override
    public int getPropertyCount() {
        return this.elementCount;
    }

    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
        arg2.name = "Embarque";
        arg2.type = PropertyInfo.OBJECT_TYPE;
    }

    @Override
    public void setProperty(int arg0, Object arg1) {
        this.add((Embarque) arg1);
    }


}
