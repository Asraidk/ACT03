package com.example.portatil.act03;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ResourceBundle;

//implementem el view per fer els botons escolitn els click dels botons
public class LlistaHistorial extends AppCompatActivity implements View.OnClickListener {
    //varieables per fer actes de fe per que es reinici al tornar al activiti sobre el listview y els roductes
    //variables utilitzades
    //private MyOpenHelper bd;
    private TalkerOH comunicador;
    private  TextView tv;
    private Toast toast;
    private long idActual;
    private int positionActual;
    /*private SimpleCursorAdapter scTasks;*///sino tuviesemos cambios de color usamos esta en plan solo mostrar info
    private adapterTodoListFilterHistorial scTasks;
    //camps que mostrem del nostre array en el list view(creo)
    private static String[] from = new String[]{MyOpenHelper.MOVIMENTS_CODI,MyOpenHelper.MOVIMENT_DATA,MyOpenHelper.MOVIMENTS_QUANTITAT,MyOpenHelper.MOVIMENTS_TIPUS};
    private static int[] to = new int[]{R.id.codi,R.id.dia,R.id.quantitat,R.id.tipus};
    /*on create posem les intancies dels botons que tenim per fer les funciones que busquem*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_historial);
        setTitle("ToolBar & Adapter Icon");

        //instanciem els botons
        Button dataHistoric=(Button)findViewById(R.id.btnHistoriaData);
        dataHistoric.setOnClickListener(this);
        Button codiHistoric=(Button)findViewById(R.id.btnHisotrialCodi);
        codiHistoric.setOnClickListener(this);
        //part sobre els click en la list view per llançar un metode
        comunicador = new TalkerOH(this);
        carregaCamps();
        ListView llista=(ListView)findViewById(R.id.llistaHistoric);
        llista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View vista, int posicion,
                                    long id) {

            }

        });
    }

    //determinar quin item es fa click del activyti
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnHistoriaData:
                filtrarData();
                break;
            case R.id.btnHisotrialCodi:
                filtrarPerCodi();
                break;

        }


    }

    //metode cridat on create per que carregui tota la informacio de la bd en la listview de la nostre activity
    private void carregaCamps() {

        // Demanem els row que ens interesan de la taula
        Cursor cursorTasks = comunicador.carregaTotaLaTaulaHistorial();
        scTasks = new adapterTodoListFilterHistorial(this, R.layout.simpel_cursor_historial, cursorTasks, from, to, 1);
        //setListAdapter(scTasks);
        ListView llista=(ListView)findViewById(R.id.llistaHistoric);
        llista.setAdapter(scTasks);
    }
    private void filtrarPerCodi() {
        try {
            tv = (TextView) findViewById(R.id.tvCodiBuscar);
            String descripcion = tv.getText().toString();
            Cursor cursorTasks = comunicador.historicCodi(descripcion);

            // Notifiquem al adapter que les dades han canviat i que refresqui
            scTasks.changeCursor(cursorTasks);
            scTasks.notifyDataSetChanged();

        }
        catch (Exception e) {
            toast =Toast.makeText(LlistaHistorial.this,"El camp PVP te que ser un real format (nn.d)", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
    }
    private void filtrarData() {
        tv = (TextView) findViewById(R.id.tbdataSelecio);
        String dataAGuardar;

        try {

            dataAGuardar = tv.getText().toString();
            String[] separated = dataAGuardar.split("/");

            /*if (Integer.valueOf(separated[1]) < 10 && Integer.valueOf(separated[0]) < 10) {
                dataAGuardar = separated[2] + "0" + separated[1] + "0" + separated[0];
            } else {
                if (Integer.valueOf(separated[1]) < 10) {
                    dataAGuardar = separated[2] + "0" + separated[1] + separated[0];
                } else {
                    if (Integer.valueOf(separated[0]) < 10) {
                        dataAGuardar = separated[2] + separated[1] + "0" + separated[0];
                    } else {
                        dataAGuardar = separated[2] + separated[1] + separated[0];
                    }
                }
            }*/
            dataAGuardar = separated[2] + separated[1] + separated[0];
            Cursor cursorTasks = comunicador.historicData(dataAGuardar);

            // Notifiquem al adapter que les dades han canviat i que refresqui
            scTasks.changeCursor(cursorTasks);
            scTasks.notifyDataSetChanged();
        }
        catch (Exception e) {
            toast = Toast.makeText(LlistaHistorial.this, "la data no esta be introduida format dd/mm/aaaa", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
    }




}
//extendero el cursor per tal que les row es pintin o mostrin mes coses de les que nosaltres volem
class adapterTodoListFilterHistorial extends android.widget.SimpleCursorAdapter {
    private static final String colorTaskPending = "#d78290";
    private static final String colorTaskCompleted = "#01DF01";




    public adapterTodoListFilterHistorial(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        // Agafem l'objecte de la view que es una LINEA DEL CURSOR
        final Cursor linia = (Cursor) getItem(position);
        String done = linia.getString(linia.getColumnIndexOrThrow(MyOpenHelper.MOVIMENTS_TIPUS));

        // Pintem el fons de la view segons està completada o no
        if (done.equals("Entrada")) {
            view.setBackgroundColor(Color.parseColor(colorTaskCompleted));
        }
        else {
            view.setBackgroundColor(Color.parseColor(colorTaskPending));
        }

        return view;
    }
}
