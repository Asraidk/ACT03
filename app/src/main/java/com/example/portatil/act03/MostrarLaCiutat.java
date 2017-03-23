package com.example.portatil.act03;


import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MostrarLaCiutat extends AppCompatActivity {
    private TalkerOH bd;
    private long task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_la_ciutat);
        bd=new TalkerOH(this);
        //recollim la id que em anviat al fer click en una row de la list
        task = this.getIntent().getExtras().getLong("id");
        CarregarNom();
    }
    //metode que conte la logica de l'activity
    //comencem per instanciar el que necesitem ames de mostraar el nom de la ciutat que em clicat--> aixo es fa amb la BD
    //Posteriorment pasem a la part de la lectura del JSON
    public void CarregarNom (){
        Cursor datos = bd.carregaPerIdNomCiutat(task);
        datos.moveToFirst();
        final TextView veureCiutat=(TextView)findViewById(R.id.veureCiutat);
        final String nomCiutat=datos.getString(datos.getColumnIndex(MyOpenHelper.TEMPS_CIUTAT));
        veureCiutat.setText(nomCiutat);

        //porque tiene que ser asic porque si
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2,10000);
        //la nuestra url per contactar amb la api
        String Url = "http://api.openweathermap.org/data/2.5/weather?q="+ nomCiutat +"&units=metric&APPID=ef2f8b023a31118d12bb4bd0217aa5bd";
        //String Url = "http://api.openweathermap.org/data/2.5/weather?units=metric&APPID=<ef2f8b023a31118d12bb4bd0217aa5bd>&q=" + nomCiutat;
        //acto de fe asi porque si, metode que asocia url i te metodes per la lectura del json
        client.get(this,Url, new AsyncHttpResponseHandler() {

            //para toast
             Toast toast ;
            //comencem amb el onStart que ens informa que estem fen feeina y tenim que esperar
            @Override
            public void onStart() {
                toast =Toast.makeText(MostrarLaCiutat.this,"Estem calculant", Toast.LENGTH_SHORT);
                toast.show();
            }
            //Si tot anat be pasem al succes on tenim la chicha del programa
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //declaracion per tractar els json y alguns objectes que te son necesaris si o si
                JSONObject temps = null;
                JSONObject main=null;
                double tempera=0,pressio=0,humitat=0,tempMIN=0,tempMAX=0;
                String str = new String(responseBody);//este si o si
                //primer intente de posar el json lleguit per igualar el que tenim a un jsonobjcet
                try {

                    temps = new JSONObject(str);

                }catch (JSONException e){

                    //e.printStackTrace();
                    toast =Toast.makeText(MostrarLaCiutat.this,"Alguna cosa no anat com esperavem", Toast.LENGTH_SHORT);
                    toast.show();

                }
                //del json objet si no esta a null el pasem mirem propietat que volem que alhora es un objecte json i l'agafem
                try {
                    if (temps != null) {
                       main = temps.getJSONObject("main");
                    }
                } catch (JSONException e) {
                    toast =Toast.makeText(MostrarLaCiutat.this,"Alguna cosa no anat com esperavem", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //quan el tenim obtenim les propietats del json que volem
                try {
                    tempera = main.getDouble("temp");
                    pressio = main.getDouble("pressure");
                    humitat = main.getDouble("humidity");
                    tempMIN = main.getDouble("temp_min");
                    tempMAX = main.getDouble("temp_max");
                } catch (JSONException e) {
                    toast =Toast.makeText(MostrarLaCiutat.this,"Alguna cosa no anat com esperavem", Toast.LENGTH_SHORT);
                    toast.show();

                }
                //si tot anat be i tenim el valors pasem a assignarlos a el textview
                try {
                    TextView temp = (TextView)findViewById(R.id.tempsFa);
                    TextView presio = (TextView)findViewById(R.id.presio);
                    TextView hum = (TextView)findViewById(R.id.humitat);
                    //para los text views a ocultar
                    TextView tv1 = (TextView)findViewById(R.id.temperatura);
                    TextView tv2 = (TextView)findViewById(R.id.textView4);
                    TextView tv3 = (TextView)findViewById(R.id.textView8);

                    //control per saber si la ciutat existeix!!! agafem una propetat del json general
                    if(temps.getString("name").equals(nomCiutat)){

                        temp.setText(String.valueOf(tempera)+" ºC");
                        presio.setText(String.valueOf(pressio)+" hPa");
                        hum.setText(String.valueOf(humitat)+" %");

                    }else{
                        veureCiutat.setText("CITY NOT FOUND");
                        temp.setVisibility(View.INVISIBLE);
                        presio.setVisibility(View.INVISIBLE);
                        hum.setVisibility(View.INVISIBLE);
                        tv1.setVisibility(View.INVISIBLE);
                        tv2.setVisibility(View.INVISIBLE);
                        tv3.setVisibility(View.INVISIBLE);

                    }
                } catch (Exception e) {
                    toast =Toast.makeText(MostrarLaCiutat.this,"Alguna cosa no anat com esperavem", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            //si peta pues diremos que peta
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                toast =Toast.makeText(MostrarLaCiutat.this,"Alguna cosa no anat com esperavem", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
