package com.automatica.AXCPT.Servicios.TableHelpers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnPxWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;

public class TableViewDataConfigurator
{
   private StandarDataAdapter st;
   private SortableTableView Tabla;
   private DataAccessObject  dao;
   private Activity Actividad;
   private int renglonSeleccionado=-1;
   private int ColumnaStatus =-2;
   private boolean renglonEstaSeleccionado;
   private HashMap<Integer,Integer> sizes;
   private HashMap<String,Integer> ColoresStatus;
   private String ValorVerde;
   private String ValorAmarillo;
   private String ValorRojo;
   private String IdConfigurador = "";
   private TableClickInterface Listener;
   private TableColumnPxWidthModel columnModel;




    /**
     * Esta clase realiza la configuración de las tablas.
     * */

    public TableViewDataConfigurator(SortableTableView Tabla, DataAccessObject dao, Activity Actividad)
    {
        if(Tabla == null)
        {
            throw new NullPointerException("La TableView no puede ser nula.");
        }
        if(dao == null)
        {
            throw new NullPointerException("El DAO no puede ser nulo.");
        }
        if(Actividad == null)
        {
            throw new NullPointerException("La Actividad no puede ser nula.");
        }

        this.Tabla = Tabla;
        this.dao = dao;
        this.Actividad = Actividad;
        Inicializacion();
    }
    public TableViewDataConfigurator(SortableTableView Tabla, DataAccessObject dao, Activity Actividad, boolean bol)
    {
        if(Tabla == null)
        {
            throw new NullPointerException("La TableView no puede ser nula.");
        }
//        if(dao == null)
//        {
//            throw new NullPointerException("El DAO no puede ser nulo.");
//        }
        if(Actividad == null)
        {
            throw new NullPointerException("La Actividad no puede ser nula.");
        }

        this.Tabla = Tabla;
        this.dao = dao;
        this.Actividad = Actividad;
        InicializacionSinInterfaz();
    }
    public TableViewDataConfigurator() {
    }

    public static TableViewDataConfigurator newInstance(SortableTableView Tabla, DataAccessObject dao, Activity Actividad,TableViewDataConfigurator.TableClickInterface listener){
        TableViewDataConfigurator configurator = new TableViewDataConfigurator(Tabla,dao,Actividad,true);
        configurator.Listener= listener;
        return configurator;
    }

    public TableViewDataConfigurator(String IdConfigurador, SortableTableView Tabla, DataAccessObject dao, Activity Actividad)
    {
        if(IdConfigurador == null)
        {
            throw new NullPointerException("Debe ingresar un identificador no nulo.");
        }
        if(Tabla == null)
        {
            throw new NullPointerException("La TableView no puede ser nula.");
        }
        if(dao == null)
        {
            throw new NullPointerException("El DAO no puede ser nulo.");
        }
        if(Actividad == null)
        {
            throw new NullPointerException("La Actividad no puede ser nula.");
        }

        this.Tabla = Tabla;
        this.dao = dao;
        this.Actividad = Actividad;

        Inicializacion();
    }


    public TableViewDataConfigurator( int ColumnaStatus,
                                      String ValorVerde,
                                      String ValorAmarillo,
                                      String ValorRojo,
                                      SortableTableView Tabla,
                                      DataAccessObject dao,
                                      Activity Actividad)
    {
        if(Tabla == null)
            {
                throw new NullPointerException("La TableView no puede ser nula.");
            }
        if(dao == null)
            {
                throw new NullPointerException("El DAO no puede ser nulo.");
            }
        if(Actividad == null)
            {
                throw new NullPointerException("La Actividad no puede ser nula.");
            }
        if(ColumnaStatus<0)
            {
                throw new IllegalArgumentException("La columna de la que se sacara el estatus debe existir: " + String.valueOf(ColumnaStatus));
            }
        this.Tabla = Tabla;
        this.dao = dao;
        this.Actividad = Actividad;
        this.ColumnaStatus = ColumnaStatus;
        this.ValorVerde = ValorVerde;
        this.ValorAmarillo = ValorAmarillo;
        this.ValorRojo = ValorRojo;

        Inicializacion();

    }

    public TableViewDataConfigurator(String IdConfigurador, int ColumnaStatus,
                                      String ValorVerde,
                                      String ValorAmarillo,
                                      String ValorRojo,
                                      SortableTableView Tabla,
                                      DataAccessObject dao,
                                      Activity Actividad)
    {
        if(IdConfigurador == null)
        {
            throw new NullPointerException("Debe ingresar un identificador no nulo.");
        }
        if(Tabla == null)
        {
            throw new NullPointerException("La TableView no puede ser nula.");
        }
        if(dao == null)
        {
            throw new NullPointerException("El DAO no puede ser nulo.");
        }
        if(Actividad == null)
        {
            throw new NullPointerException("La Actividad no puede ser nula.");
        }
        if(ColumnaStatus<0)
        {
            throw new IllegalArgumentException("La columna de la que se sacara el estatus debe existir: " + String.valueOf(ColumnaStatus));
        }
        this.Tabla = Tabla;
        this.dao = dao;
        this.Actividad = Actividad;
        this.ColumnaStatus = ColumnaStatus;
        this.ValorVerde = ValorVerde;
        this.ValorAmarillo = ValorAmarillo;
        this.ValorRojo = ValorRojo;
        this.IdConfigurador = IdConfigurador;

        Inicializacion();
    }


    public TableViewDataConfigurator(int ColumnaStatus,
                                     HashMap<String,Integer> ColoresStatus,
                                     SortableTableView Tabla,
                                     DataAccessObject dao,
                                     Activity Actividad)
    {

        if(Tabla == null)
        {
            throw new NullPointerException("La TableView no puede ser nula.");
        }
        if(dao == null)
        {
            throw new NullPointerException("El DAO no puede ser nulo.");
        }
        if(Actividad == null)
        {
            throw new NullPointerException("La Actividad no puede ser nula.");
        }
        if(ColumnaStatus<0)
        {
            throw new IllegalArgumentException("La columna de la que se sacara el estatus debe existir: " + String.valueOf(ColumnaStatus));
        }
        if(ColoresStatus == null)
        {
            throw new NullPointerException("Los colores de estatus no puede ser nulos.");
        }
        this.Tabla = Tabla;
        this.dao = dao;
        this.Actividad = Actividad;
        this.ColumnaStatus = ColumnaStatus;
        this.ColoresStatus = ColoresStatus;

        Inicializacion();
    }
    public TableViewDataConfigurator(String IdConfigurador, int ColumnaStatus,
                                     HashMap<String,Integer> ColoresStatus,
                                     SortableTableView Tabla,
                                     DataAccessObject dao,
                                     Activity Actividad)
    {
        if(IdConfigurador == null)
        {
            throw new NullPointerException("Debe ingresar un identificador no nulo.");
        }
        if(Tabla == null)
        {
            throw new NullPointerException("La TableView no puede ser nula.");
        }
        if(dao == null)
        {
            throw new NullPointerException("El DAO no puede ser nulo.");
        }
        if(Actividad == null)
        {
            throw new NullPointerException("La Actividad no puede ser nula.");
        }
        if(ColumnaStatus<0)
        {
            throw new IllegalArgumentException("La columna de la que se sacara el estatus debe existir: " + String.valueOf(ColumnaStatus));
        }
        if(ColoresStatus == null)
        {
            throw new NullPointerException("Los colores de estatus no puede ser nulos.");
        }
        this.Tabla = Tabla;
        this.dao = dao;
        this.Actividad = Actividad;
        this.ColumnaStatus = ColumnaStatus;
        this.ColoresStatus = ColoresStatus;
        this.IdConfigurador = IdConfigurador;

        Inicializacion();
    }

    private void Inicializacion()
    {
        if (Actividad instanceof TableClickInterface)
            {
                Listener = (TableClickInterface) Actividad;
            } else
            {
                throw new RuntimeException(Actividad.toString() + "se debe implementar TableClickInterface.");
            }
        CargarDatosTabla(dao);
        ajustarTamañosTabla();
        ListenersTabla();
    }
    private void InicializacionSinInterfaz()
    {
        if(dao!=null)
        {
            CargarDatosTabla(dao);
            ajustarTamañosTabla();

        } ListenersTabla();
    }
    public ArrayList<Constructor_Dato> getRenglonSeleccionado()
    {
        if(renglonSeleccionado == -1)
        {
            return null;
        }
        return dao.getcTablas().get(renglonSeleccionado);
    }

    public void CargarDatosTabla(DataAccessObject dao)
    {
        try
            {
                this.dao = dao;
                ActualizarTabla();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    public void CargarDatosTabla_Validacion(DataAccessObject dao)
    {
        try
        {
            this.dao = dao;
            ActualizarTabla_Validacion();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void ActualizarTabla()
    {
        try
        {
            if(dao==null)
            {
                Tabla.getDataAdapter().clear();
                Tabla.getDataAdapter().notifyDataSetChanged();
                return;
            }

            renglonSeleccionado=-1;
            renglonEstaSeleccionado = false;
            if(dao.getcTablaUnica()!=null)
            {
                //SE CONFIGURA EL HEADER DE LA TABLA
                Tabla.setHeaderAdapter(new StandarHeaderAdapter(Actividad,dao.getcEncabezado()));
                //SE CONFIGURAN LOS DATOS DE LA TABLA
                Tabla.setDataAdapter(st = new StandarDataAdapter(Actividad, dao.getcTablaUnica()));

                ajustarTamañosTabla();

                //SI ColumnaStatus ES DIFERENTE A MENOS DOS, ENTONCES SERA TABLA CON ESTATUS
                if(ColumnaStatus!=-2)
                {
                    //CONFIGURA EL BACKGROUND DE LA TABLA

                    if(ColoresStatus == null)
                    {
                        Tabla.setDataRowBackgroundProvider(new TableViewBackgroundStatus(
                                ColumnaStatus, -1, ValorVerde, ValorAmarillo, ValorRojo, st, Actividad));
                    }else
                    {
                        Tabla.setDataRowBackgroundProvider(new TableViewBackgroundMultiStatus(
                                ColumnaStatus, -1, ColoresStatus, st, Actividad));
                    }
                    //CONFIGURAR LA COLUMNA DE DONDE SE SACARA EL ESTATUS
                    Tabla.setColumnComparator(ColumnaStatus, new ComparadorStatus(ColumnaStatus));
                }else
                {
                    //CONFIGURA EL BACKGROUND DE LA TABLA
                    Tabla.setDataRowBackgroundProvider(new TableViewBackgroundStatus(-1,st,Actividad));
                }
            }
            else
            {
                Tabla.getDataAdapter().clear();
                Tabla.getDataAdapter().notifyDataSetChanged();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void ActualizarTabla_Validacion()
    {
        try
        {
            if(dao==null)
            {
                Tabla.getDataAdapter().clear();
                Tabla.getDataAdapter().notifyDataSetChanged();
                return;
            }

            renglonSeleccionado=-1;
            renglonEstaSeleccionado = false;
            if(dao.getcTablaUnica()!=null)
            {
                //SE CONFIGURA EL HEADER DE LA TABLA
                Tabla.setHeaderAdapter(new StandarHeaderAdapter(Actividad,dao.getcEncabezado()));
                //SE CONFIGURAN LOS DATOS DE LA TABLA
                Tabla.setDataAdapter(st = new StandarDataAdapter(Actividad, dao.getcTablaUnica()));

                //  ajustarTamañosTabla();

                //SI ColumnaStatus ES DIFERENTE A MENOS DOS, ENTONCES SERA TABLA CON ESTATUS
                if(ColumnaStatus!=-2)
                {
                    //CONFIGURA EL BACKGROUND DE LA TABLA

                    if(ColoresStatus == null)
                    {
                        Tabla.setDataRowBackgroundProvider(new TableViewBackgroundStatus(
                                ColumnaStatus, -1, ValorVerde, ValorAmarillo, ValorRojo, st, Actividad));
                    }else
                    {
                        Tabla.setDataRowBackgroundProvider(new TableViewBackgroundMultiStatus(
                                ColumnaStatus, -1, ColoresStatus, st, Actividad));
                    }
                    //CONFIGURAR LA COLUMNA DE DONDE SE SACARA EL ESTATUS
                    Tabla.setColumnComparator(ColumnaStatus, new ComparadorStatus(ColumnaStatus));
                }else
                {
                    //CONFIGURA EL BACKGROUND DE LA TABLA
                    Tabla.setDataRowBackgroundProvider(new TableViewBackgroundStatus(-1,st,Actividad));
                }
            }
            else
            {
                Tabla.getDataAdapter().clear();
                Tabla.getDataAdapter().notifyDataSetChanged();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }





    public void customRowsLength(HashMap<Integer,Integer> sizes)
    {
        this.sizes = sizes;
        ajustarTamañosTabla();
    }

    private void ajustarTamañosTabla()
    {
          Log.i("Tamaños",dao.getcTablaUnica()[0].length +  " Tamaño arreglo");
        int[] Sizes = new int[dao.getcTablaUnica()[0].length];
        int TotalSize = 0;
        int TotalSizeTemp = 0;
        for(int i = 0;i<=dao.getcTablaUnica().length-1;i++)
        {
            TotalSizeTemp = 0;
              Log.i("Tamaños","Renglon " + i);
            for(int j = 0;j<=dao.getcTablaUnica()[i].length-1;j++)
            {

                if(Sizes[j] < dao.getcTablaUnica()[i][j].length() )//* R.dimen.txtSize)
                {
                    Sizes[j] = dao.getcTablaUnica()[i][j].length();
                    if(dao.getcEncabezado()[j].length()>Sizes[j])
                    {
                        Sizes[j] =dao.getcEncabezado()[j].length();
                    }
                }

                TotalSizeTemp = TotalSizeTemp + dao.getcTablaUnica()[i][j].length();
                //  Log.i("Tamaños","Renglon: " + i + " Total Temp:" +  TotalSizeTemp + " Total: " + TotalSize);
            }
            if(TotalSizeTemp>TotalSize)
            {
                TotalSize = TotalSizeTemp;
            }
        }

        //  Log.i("Tamaños",TotalSize + " " + (int) (TotalSize * Actividad.getResources().getDimension(R.dimen.txtvr))* 1.5);
        //  Log.i("Tamaños",Sizes.length + "");
        columnModel = new TableColumnPxWidthModel(dao.getcTablaUnica()[0].length, 300);
        int CantTot = 0;
        for(int k = 0;k<=dao.getcTablaUnica()[0].length-1;k++)
        {
            int CantReng =  (int) (Sizes[k] *  Actividad.getResources().getDimension(R.dimen.txtvr)+100);


            if (sizes != null)
            {
                if(sizes.containsKey(k))
                {
                    columnModel.setColumnWidth(k, sizes.get(k));
                }
                else
                {
//                    if (CantReng < 100)
//                    {
//                        CantReng = 120;
//                    }
//                    if (CantReng > 500)
//                    {
//                        CantReng = 350;
//                    }
                      Log.i("Tamaños CantidadRenglon",CantReng+ "");
                    CantTot = CantTot + CantReng;
                    columnModel.setColumnWidth(k,CantReng);
                }
            }
            else
            {
//                if (CantReng < 100)
//                {
//                    CantReng = 120;
//                }
//                if (CantReng > 500)
//                {
//                    CantReng = 350;
//                }
                  Log.i("Tamaños CantidadRenglon",CantReng+ "");
                CantTot = CantTot + CantReng;
                columnModel.setColumnWidth(k,CantReng);
            }

        }
          Log.i("Tamaños finales",CantTot + "");
        Tabla.getLayoutParams().width =CantTot+100;
        Tabla.setColumnModel(columnModel);
        Tabla.invalidate();
    }

    private void ListenersTabla()
    {
        try
            {
                Tabla.addDataClickListener(new TableClickListener(Actividad));
                Tabla.addHeaderClickListener(new headerClickListener());
                Tabla.addDataLongClickListener(new ListenerLongClickTabla());

            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    private class TableClickListener implements TableDataClickListener<String[]>
    {

        Context context;

        public TableClickListener(Context context)
        {
            this.context = context;
        }

        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            try
                {
                    if (renglonSeleccionado != rowIndex)
                        {
                            renglonSeleccionado = rowIndex;
                            //SI ColumnaStatus ES DIFERENTE A MENOS DOS, ENTONCES SERA TABLA CON ESTATUS
                            if(ColumnaStatus!=-2)
                                {
                                    //CONFIGURA EL BACKGROUND DE LA TABLA


                                    if(ColoresStatus == null)
                                    {
                                        Tabla.setDataRowBackgroundProvider(new TableViewBackgroundStatus(
                                                ColumnaStatus, rowIndex, ValorVerde, ValorAmarillo, ValorRojo, st,Actividad));
                                    }else
                                    {
                                        Tabla.setDataRowBackgroundProvider(new TableViewBackgroundMultiStatus(
                                                ColumnaStatus, rowIndex, ColoresStatus, st, Actividad));
                                    }

                                    //CONFIGURAR LA COLUMNA DE DONDE SE SACARA EL ESTATUS
                                     Tabla.setColumnComparator(ColumnaStatus, new ComparadorStatus(ColumnaStatus));
                                }else
                                {
                                    //CONFIGURA EL BACKGROUND DE LA TABLA
                                    Tabla.setDataRowBackgroundProvider(new TableViewBackgroundStatus(rowIndex,st,Actividad));
                                }
                            Tabla.getDataAdapter().notifyDataSetChanged();

                            Listener.onTableClick(rowIndex, clickedData,true,IdConfigurador);

                            renglonEstaSeleccionado = true;
                        } else if (renglonSeleccionado == rowIndex)
                        {
                            renglonSeleccionado = -1;
                            if(ColumnaStatus!=-2)
                                {
                                    //CONFIGURA EL BACKGROUND DE LA TABLA

                                    if(ColoresStatus == null)
                                    {
                                        Tabla.setDataRowBackgroundProvider(new TableViewBackgroundStatus(
                                                ColumnaStatus, -1, ValorVerde, ValorAmarillo, ValorRojo, st,Actividad));

                                    }else
                                    {
                                        Tabla.setDataRowBackgroundProvider(new TableViewBackgroundMultiStatus(
                                                ColumnaStatus, -1, ColoresStatus, st, Actividad));
                                    }


                                    //CONFIGURAR LA COLUMNA DE DONDE SE SACARA EL ESTATUS
                                    Tabla.setColumnComparator(ColumnaStatus, new ComparadorStatus(ColumnaStatus));
                                }else
                                {
                                    //CONFIGURA EL BACKGROUND DE LA TABLA
                                    Tabla.setDataRowBackgroundProvider(new TableViewBackgroundStatus(-1,st,Actividad));
                                }
                            Tabla.getDataAdapter().notifyDataSetChanged();
                            renglonEstaSeleccionado = false;
                            Listener.onTableClick(rowIndex, clickedData,false,IdConfigurador);
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    throw e;
                }
        }
    }
    private class headerClickListener implements TableHeaderClickListener
    {
        @Override
        public void onHeaderClicked(int columnIndex)
        {

            Listener.onTableHeaderClick(columnIndex, dao.getcEncabezado()[columnIndex],IdConfigurador);
        }
    }
    private class ListenerLongClickTabla implements TableDataLongClickListener<String[]>
    {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData)
        {

            String DataToShow="";
            int i =0;
            for(String data:clickedData)
                {
                    DataToShow +=dao.getcEncabezado()[i] +" - "+ data + "\n";
                    i++;
                }
            Listener.onTableLongClick(rowIndex, clickedData, DataToShow,IdConfigurador);
            return false;
        }
    }

    public boolean renglonEstaSelec()
    {
        return renglonEstaSeleccionado;
    }



    //Region Custom Table Status configurators

    /**COLORES ESTANDAR PARA LAS TABLAS DE INVENTARIOS*/
    public static HashMap ESTATUS_INVENTARIO_STD()
    {
        HashMap<String,Integer> ColoresStatus = new HashMap<>();
        ColoresStatus.put("SIN REGISTRO",R.color.AmarilloRenglon);
        ColoresStatus.put("1.- AJUSTE(+)",R.color.VerdeRenglon);
        ColoresStatus.put("2.-AJUSTE(+)",R.color.VerdeRenglon);
        ColoresStatus.put("1.-AJUSTE(-)",R.color.VerdeRenglon);
        ColoresStatus.put("2.-AJUSTE(-)",R.color.VerdeRenglon);
        ColoresStatus.put("LECTURA NORMAL",R.color.VerdeRenglon);
        ColoresStatus.put("EDITADO",R.color.VerdeRenglon);
        ColoresStatus.put("1.-BAJA",R.color.RojoRenglon);
        ColoresStatus.put("2.-BAJA",R.color.RojoRenglon);
        ColoresStatus.put("DESCARTADO",R.color.RojoRenglon);
        return ColoresStatus;
    }
    //endregion


    public  interface TableClickInterface
    {
        void onTableHeaderClick(int columnIndex,String Header,String IdentificadorTabla);
        void onTableLongClick(int rowIndex, String[] clickedData,String MensajeCompleto,String IdentificadorTabla);
        void onTableClick(int rowIndex, String[] clickedData,boolean Seleccionado,String IdentificadorTabla);
    }

}
