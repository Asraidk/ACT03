package com.example.portatil.act03;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ControlMagatzem extends AppCompatActivity implements View.OnClickListener {
    private long task;
    private String dataAGuardar,id,dataAMostrar;
    private TalkerOH bd;
    private TextView tv;
    private Toast toast;
    private int opcioMagatzem;
    private KeyListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_magatzem);

        bd=new TalkerOH(this);
        id = this.getIntent().getExtras().getString("codi");
        opcioMagatzem = this.getIntent().getExtras().getInt("afegirStock");

        Button ok=(Button) findViewById(R.id.btnOk);
        ok.setOnClickListener(this);
        Button cancelar=(Button) findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(this);

        TextView codiProducte=(TextView) findViewById(R.id.edtCodi);
        codiProducte.setText(id);
        codiProducte.setKeyListener(null);


        final TextView data=(TextView) findViewById(R.id.edtData);
        listener = data.getKeyListener();
        data.setKeyListener(null);

        Time today=new Time(Time.getCurrentTimezone());
        today.setToNow();
        int dia=today.monthDay;
        int mes=today.month;
        int ano=today.year;
        mes=mes+1;
        data.setText(dia+"/"+mes+"/"+ano);

        final CheckBox dataActiva = (CheckBox) findViewById(R.id.ckData);


        dataActiva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dataActiva.isChecked()){
                    data.setKeyListener(listener);

                }else{
                    data.setKeyListener(null);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnOk:
                posarHistorial();
                break;
            case R.id.btnCancelar:
                cancelar();
                break;
        }
    }
    private void posarHistorial(){

        tv = (TextView) findViewById(R.id.edtData);
        Integer stock;
        Integer stockActual;

        try {
            dataAMostrar = tv.getText().toString();
            dataAGuardar = tv.getText().toString();
            String[] separated = dataAGuardar.split("/");

            if (Integer.valueOf(separated[1]) < 10 && Integer.valueOf(separated[0]) < 10) {
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
            }
        }
        catch (Exception e) {
            toast = Toast.makeText(ControlMagatzem.this, "la data no esta be introduida format dd/mm/aaaa", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        tv = (TextView) findViewById(R.id.edtStock);

        if(opcioMagatzem==1){

            try {

                stock = Integer.valueOf(tv.getText().toString());
                if(stock>0){
                    task=bd.AfegirHistorial(id,dataAGuardar,stock,"Entrada",dataAMostrar);
                    stockActual=bd.carregarCodi(id);
                    stockActual=stockActual+stock;
                    bd.CambisEnElStock(id,stockActual);

                }else{
                    toast = Toast.makeText(ControlMagatzem.this, "El camp moviment d'estoc te que ser enter, mes gran que 0", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
            }
            catch (Exception e) {
                toast = Toast.makeText(ControlMagatzem.this, "El camp moviment d'estoc te que ser enter", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }else{
            try {
                stock = Integer.valueOf(tv.getText().toString());
                if(stock>0){
                    task=bd.AfegirHistorial(id,dataAGuardar,stock,"Sortida",dataAMostrar);
                    stockActual=bd.carregarCodi(id);
                    stockActual=stockActual-stock;
                    bd.CambisEnElStock(id,stockActual);
                }else{
                    toast = Toast.makeText(ControlMagatzem.this, "El camp moviment d'estoc te que ser enter, mes gran que 0", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
            }
            catch (Exception e) {
                toast = Toast.makeText(ControlMagatzem.this, "El camp moviment d'estoc te que ser enter", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }
        Intent mIntent = new Intent(ControlMagatzem.this,LlistaProductes.class);
        //mIntent.putExtra("id", task);
        //setResult(RESULT_CANCELED, mIntent);
        startActivity(mIntent);
        //finish();

    }
    private void cancelar() {
        Intent mIntent = new Intent(ControlMagatzem.this,LlistaProductes.class);
        //mIntent.putExtra("id", task);
        //setResult(RESULT_CANCELED, mIntent);
        startActivity(mIntent);
        //finish();
    }
}
