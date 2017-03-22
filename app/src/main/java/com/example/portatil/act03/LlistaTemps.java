package com.example.portatil.act03;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
        omplitCiutats();
    }

    @Override
    public void onClick(View v) {
        Toast toast ;
        EditText nomtext=(EditText) findViewById(R.id.edtCiutat);
        String nomCiutat = nomtext.getText().toString();
        switch (v.getId()){
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
        omplitCiutats();
    }
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