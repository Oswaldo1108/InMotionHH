package com.automatica.AXCPT.c_Inventarios.Granel;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Inventarios_BajaEmpaqueGranel extends AppCompatActivity {
    Bundle b;
    EditText edtx_Busqueda, edtx_Lote, edtx_Unidades, edtx_CantidadEmpaques;
    TextView txtv_Pallet;
    FrameLayout progressBarHolder;
    int renglonSeleccionado;
    int RenglonSeleccionado;
    int renglonAnterior = -1;
    AlphaAnimation inAnimation;
    String IdInventario, UbicacionIntent, Pallet, Lote, Producto;
    AlphaAnimation outAnimation;
    TableView tabla;
    Context contexto = Inventarios_BajaEmpaqueGranel.this;
    SimpleTableHeaderAdapter sth;
    SimpleTableDataAdapter st;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventarios__baja_empaque_granel);
        try {
            SacaExtrasIntent();
            InicializarVariables();
            AgregarListeners();
            edtx_CantidadEmpaques.setEnabled(false);
            txtv_Pallet.setText(Pallet);
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);

        }

    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        new SegundoPlano("llenarTabla").execute();
    }

    public void InicializarVariables() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Baja empaque");
        getSupportActionBar().setSubtitle("No etiquetado");

        tabla = findViewById(R.id.tableView_ConfirmarEmpaque);

        edtx_Busqueda = findViewById(R.id.edtx_Busqueda);
        edtx_Lote = findViewById(R.id.edtx_Lote);
        edtx_Unidades = findViewById(R.id.edtx_Unidades);
        edtx_CantidadEmpaques = findViewById(R.id.edtx_CantidadEmpaques);

        txtv_Pallet = findViewById(R.id.txtv_Pallet);

        progressBarHolder = findViewById(R.id.progressBarHolder);
    }

    public void AgregarListeners() {
        tabla.addDataClickListener(new ListenerClickTabla());
        edtx_CantidadEmpaques.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(edtx_CantidadEmpaques.getText())) {
                    new popUpGenerico(contexto, getCurrentFocus(), "Llene el campo 'Cantidad Empaques'", "false", true, true);
                }
                new SegundoPlano("Baja").execute();
                return false;
            }
        });
    }

    private class ListenerClickTabla implements TableDataClickListener<String[]> {
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData) {
            try {
                RenglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                edtx_Busqueda.setText(clickedData[2]);
                edtx_Lote.setText(clickedData[4]);
                Lote = clickedData[5];
                Producto = clickedData[2];
                edtx_Unidades.setText(clickedData[4]);
                edtx_CantidadEmpaques.setEnabled(true);
                edtx_CantidadEmpaques.requestFocus();
                if (renglonAnterior != rowIndex) {

                    renglonAnterior = rowIndex;
                    renglonSeleccionado = rowIndex;

                    tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                    tabla.getDataAdapter().notifyDataSetChanged();
                    edtx_Busqueda.setText(clickedData[2]);
                    edtx_Lote.setText(clickedData[5]);
                    edtx_Unidades.setText(clickedData[4]);
                    Lote = clickedData[5];
                    Producto = clickedData[2];
                    edtx_CantidadEmpaques.setEnabled(true);
                    edtx_CantidadEmpaques.requestFocus();
                } else if (renglonAnterior == rowIndex) {
                    renglonSeleccionado = -1;
                    renglonAnterior = -1;
                    tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                    tabla.getDataAdapter().notifyDataSetChanged();
                    edtx_Busqueda.setText("");
                    edtx_Lote.setText("");
                    edtx_Unidades.setText("");
                    edtx_CantidadEmpaques.setText("");
                    Lote = "";
                    Producto = "";
                    edtx_CantidadEmpaques.setEnabled(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
        }
    }

    private class cambiaColorTablaClear implements TableDataRowBackgroundProvider<String[]> {
        int Color;

        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData) {
            Color = R.color.Transparente;
            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
            return new ColorDrawable(getResources().getColor(Color));
        }
    }

    private class cambiaColorTablaSeleccionada implements TableDataRowBackgroundProvider<String[]> {

        int Color;

        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData) {
            if (rowIndex == renglonSeleccionado) {
                Color = R.color.RengSelStd;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));


            } else {
                Color = R.color.Transparente;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

            }


           /* if(RenglonSeleccionado== rowIndex )
            {
                Color = R.color.RengSelStd;
            }
            else
            {
                Color = R.color.blancoLetraStd;
            }*/
            return new ColorDrawable(getResources().getColor(Color));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.main_toolbar, menu);
            return true;
        } catch (Exception ex) {
            Toast.makeText(this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, getCurrentFocus());
            }
            if ((id == R.id.borrar_datos)) {

                reiniciaVariables();
            }
            if ((id == R.id.recargar)) {
                new SegundoPlano("llenarTabla").execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }


        return super.onOptionsItemSelected(item);
    }

    private void SacaExtrasIntent() {
        try {
            b = getIntent().getExtras();
            UbicacionIntent = b.getString("UbicacionIntent");
            IdInventario = b.getString("IdInventario");
            Pallet = b.getString("CodigoPallet");
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    public void reiniciaVariables() {
        edtx_CantidadEmpaques.setText("");
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        String tarea;
        DataAccessObject dao;
        adInventarios ca = new adInventarios(contexto);

        public SegundoPlano(String tarea) {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute() {
            try {
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                switch (tarea) {

                    /**
                     * Revisar sp
                     */
                    case "llenarTabla":
                        dao = ca.c_ConsultaEmpaquesPorPalletInventario_NE(IdInventario, txtv_Pallet.getText().toString());
                        break;
                    case "Baja":
                        dao = ca.c_BajaEmpaquesInventario_NE(IdInventario, txtv_Pallet.getText().toString(), Producto, Lote, edtx_CantidadEmpaques.getText().toString());
                        break;
                    default:
                        dao = new DataAccessObject();
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (dao.iscEstado()) {
                    switch (tarea) {
                        case "llenarTabla":
                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(contexto, dao.getcTablaUnica()));
                            tabla.setHeaderAdapter(sth = new SimpleTableHeaderAdapter(contexto, dao.getcEncabezado()));
                            sth.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            tabla.getDataAdapter().notifyDataSetChanged();
                            break;
                        case "Baja":
                            new popUpGenerico(contexto, getCurrentFocus(), "Empaque dado de baja con éxito", true, true, true);
                            new SegundoPlano("llenarTabla").execute();
                            break;

                    }
                } else {
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), "false", true, true);
                }
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }

        }
    }
}