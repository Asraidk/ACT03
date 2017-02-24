package com.example.portatil.act03;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ControlProductes extends AppCompatActivity  implements View.OnClickListener{

    private long task,id;
    private TalkerOH bd;
    //en el on create instanciem tots el botons que necesitarem encara que estiguin o no visibles depenend
    //de si venen a l'activytat perque volen fer updates a les row o afegir una nova
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_productes);

        bd=new TalkerOH(this);
        TextView tv;//esencial per fer controls de tipus en la creacio per defecte el 0 y en modificar que el codi no es modificqui
        task = this.getIntent().getExtras().getLong("id");


        Button ok=(Button) findViewById(R.id.btnOk);
        ok.setOnClickListener(this);
        Button cancelar=(Button) findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(this);
        Button deletear=(Button) findViewById(R.id.btnDelete);
        deletear.setOnClickListener(this);
        //si generem una nova row arriabra un -1 y mostrarem un el layout amb unes modificacions
        //en canvi sino es -1 mostrarem altres modificacions del layout a mes tambe carregem dades
        //si el que es vol es modificar camps
        if(task==-1){
            deletear.setVisibility(View.GONE);
            tv = (TextView) findViewById(R.id.editboxStock);
            tv.setText("0");
            tv.setKeyListener(null);

        }
        if(task!=-1){
            deletear.setVisibility(View.VISIBLE);
            tv = (TextView) findViewById(R.id.edtCodi);
            tv.setKeyListener(null);
            cargarDatos();
        }

    }
    //botons amb les seves accions em metodes
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOk:
                afegir();
                break;
            case R.id.btnCancelar:
                cancelar();
                break;
            case R.id.btnDelete:
                borrar(task);
                break;
        }


    }
    //metode per carregar tota la info de la base de dades en les row y view que te la listview
    private void cargarDatos() {

        // Demanem un cursor que retorna un sol registre amb les dades de la tasca
        // Això es podria fer amb un classe pero...
        Cursor datos = bd.carregaPerId(task);
        datos.moveToFirst();

        // Carreguem les dades en la interfície
        TextView tv;

        tv = (TextView) findViewById(R.id.edtCodi);
        tv.setText(datos.getString(datos.getColumnIndex(MyOpenHelper.COLUMN_CODI)));

        tv = (TextView) findViewById(R.id.edtDescripcion);
        tv.setText(datos.getString(datos.getColumnIndex(MyOpenHelper.COLUMN_DESCRIPCIO)));

        tv = (TextView) findViewById(R.id.editboxPVP);
        tv.setText(datos.getString(datos.getColumnIndex(MyOpenHelper.COLUMN_PVP)));

        tv = (TextView) findViewById(R.id.editboxStock);
        tv.setText(datos.getString(datos.getColumnIndex(MyOpenHelper.COLUMN_STOCK)));
    }

    //metode que afegira o fara ipdates a la bse de dades
    private void afegir() {
        //declaracions per utilitzar
        TextView tv;
        Toast toast ;
        //camps del camp codi amb les seves restricions
        tv = (TextView) findViewById(R.id.edtCodi);
        String codi = tv.getText().toString();
        if (codi.trim().equals("")) {

            toast =Toast.makeText(ControlProductes.this,"El camp codi te que ser obligatori", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }
        //camp de la descripcio
        tv = (TextView) findViewById(R.id.edtDescripcion);
        String descripcion = tv.getText().toString();
        tv = (TextView) findViewById(R.id.editboxPVP);
        //camp de el pvp
        double PVP;
        try {
            PVP = Double.valueOf(tv.getText().toString());
        }
        catch (Exception e) {
            toast =Toast.makeText(ControlProductes.this,"El camp PVP te que ser un real format (nn.d)", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        //camp del stock
        tv = (TextView) findViewById(R.id.editboxStock);
        Integer stock;
        try {
            stock = Integer.valueOf(tv.getText().toString());
        }
        catch (Exception e) {
            toast =Toast.makeText(ControlProductes.this,"El camp stock te que ser obligatori", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // Mirem si estem creant o estem guardant per fer cridar metode amb el insert o amb el upadte
        if (task == -1) {
            //mirem si aquest codi de objecte ja esta introduit a la base de dades
            if(bd.codiRepetit(codi).getCount()!=0){
                toast =Toast.makeText(ControlProductes.this,"Aquest codi ja esta a la base de dades", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }else{
                task = bd.AfegirProducte(codi, descripcion,PVP,stock);}
        }
        else {
            bd.ModificarProducte(task,descripcion,PVP,stock);
        }

        Intent mIntent = new Intent();
        mIntent.putExtra("id", task);
        setResult(RESULT_OK, mIntent);

        finish();
    }
    //si toques el boton de cancelar es cridara aquest metode que retornara al activyti de la llista de productes y refrescara
    private void cancelar() {
        Intent mIntent = new Intent();
        mIntent.putExtra("id", task);
        setResult(RESULT_CANCELED, mIntent);

        finish();
    }
    //metode cridat per eliminar les row que em selecionat previament al fer click en
    //la listview mostrar la seva info y decidin si estas segur de fero(atraves de la id=pk)
    private  void borrar(final long clauprimaria){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Desitja eliminar la tasca?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.ElminarProducte(clauprimaria);

                Intent mIntent = new Intent();
                mIntent.putExtra("id", -1);  // Devolvemos -1 indicant que s'ha eliminat
                setResult(RESULT_OK, mIntent);

                finish();
            }
        });

        builder.setNegativeButton("No", null);
        builder.show();

    }
}
