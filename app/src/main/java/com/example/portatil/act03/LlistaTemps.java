package com.example.portatil.act03;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LlistaTemps extends AppCompatActivity implements View.OnClickListener{
    //variables utilitzades
    //private MyOpenHelper bd;
    private TalkerOH comunicador;
    private long idActual;
    private int positionActual;
    /*private SimpleCursorAdapter scTasks;*///sino tuviesemos cambios de color usamos esta en plan solo mostrar info
    private adapterTodoListFilterTemps scTasks;
    //camps que mostrem del nostre array en el list view(creo)
    private static String[] from = new String[]{MyOpenHelper.TEMPS_CIUTAT};
    private static int[] to = new int[]{R.id.ciutat};
    /*on create posem les intancies dels botons que tenim per fer les funciones que busquem*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_temps);
        Button btnAfegir= (Button) findViewById(R.id.btnAfegirCiutat);
        btnAfegir.setOnClickListener(this);
        //partpoder comunicarnos amb les part de la base de dades
        comunicador = new TalkerOH(this);
        //cridem metode per omplir la llista
        omplitCiutats();
        //fem que la llista pugui escoltar click per la row que te
        ListView llista=(ListView)findViewById(R.id.lvCiutats);
        llista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View vista, int posicion,long id) {
                //pasarem d'activity per mostrar la info de la ciutat escollida
                Bundle bundle = new Bundle();
                bundle.putLong("id",id);
                idActual = id;
                Intent i = new Intent(LlistaTemps.this, MostrarLaCiutat.class );
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    //metode dels clicks dels botons que tinguem en aquesta activitat
    @Override
    public void onClick(View v) {
        //declaracions per instanciar el text d'on agafem el nom
        Toast toast ;
        EditText nomtext=(EditText) findViewById(R.id.edtCiutat);
        String nomCiutat = nomtext.getText().toString();
       //mirem les id del botons
        switch (v.getId()){
            //fem comprovacion del edit text no estigui buid i posteriorment si tot ha funcionat pasem a afer el metode per afegir
            //ciutat a la base de dades
            case R.id.btnAfegirCiutat:
                try {

                    if(TextUtils.isEmpty( nomCiutat)){
                        nomtext.setError("Buid");
                        return;
                    }else{
                        comunicador.AfegirCiutat(nomCiutat);
                    }
                }
                catch (Exception e) {
                    toast = Toast.makeText(LlistaTemps.this,"Problemes per afegir la ciutat", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
            break;

        }
        //per fer un refersh de la llista
        omplitCiutats();
    }
    //metode que cridara a la base de dades per carregar i mostrar la info dels camps de la taula ciutat
    public void omplitCiutats(){
        // Demanem els row que ens interesan de la taula
        Cursor cursorTasks = comunicador.carregarCiutats();
        scTasks = new adapterTodoListFilterTemps(this, R.layout.simple_cursor_ciutat, cursorTasks, from, to, 1);
        //setListAdapter(scTasks);
        ListView llista=(ListView)findViewById(R.id.lvCiutats);
        llista.setAdapter(scTasks);
    }
}
//extendero el cursor per tal que les row es pintin o mostrin mes coses de les que nosaltres volem
class adapterTodoListFilterTemps extends android.widget.SimpleCursorAdapter {

    public adapterTodoListFilterTemps(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        return view;
    }
}