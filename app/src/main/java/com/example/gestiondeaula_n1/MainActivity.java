package com.example.gestiondeaula_n1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gestiondeaula_n1.WebServices.Asynchtask;
import com.example.gestiondeaula_n1.WebServices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Asynchtask{

    boolean btnEstado= false;
    Spinner spListaBancos;
    TextView txtEstadoTrans;
    List<String> lsBancos;
    ArrayAdapter<String> comboAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
    }
    public void SolicitarListaBancos(View view){
        load();
    }
    private  void load(){
        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService("https://api-uat.kushkipagos.com/transfer-subscriptions/v1/bankList",
                datos, MainActivity.this, MainActivity.this);
        ws.execute("GET","Public-Merchant-Id","517007e3328d45c68e36c455332006b8");
    }
    public  void SolicitarEstadoTransacciones(View view){
        btnEstado = true;
        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService("https://api-uat.kushkipagos.com/payouts/transfer/v1/transaction/{ticketNumber}",
                datos, MainActivity.this, MainActivity.this);
        ws.execute("GET","Private-Merchant-Id","b82ff453f49f4614bb92533893346df3");
    }
    @Override
    public void processFinish(String result) throws JSONException {
        if(btnEstado){
            ObtenerEstado(result);
            btnEstado = false;
        }else{
            ObtenerBancos(result);
        }
    }
    public void ObtenerBancos(String result) throws JSONException{
        spListaBancos = (Spinner) findViewById(R.id.spBancos);
        lsBancos = new ArrayList<>();
        JSONArray JSONlista =  new JSONArray(result);
        for(int i=0; i< JSONlista.length();i++){
            JSONObject banco=  JSONlista.getJSONObject(i);
            lsBancos.add((i+1) +". "+banco.getString("name").toString());
        }
        comboAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lsBancos);
        spListaBancos.setAdapter(comboAdapter);
    }
    public void ObtenerEstado(String result) throws JSONException{
        JSONObject nodo = new JSONObject(result);
        txtEstadoTrans = (TextView) findViewById(R.id.txtEstado);
        txtEstadoTrans.setText(nodo.getString("message").toString());
    }
}