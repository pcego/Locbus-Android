package br.com.kpc.locbus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ParadaInformacao extends Activity {

	private TextView tvParada;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parada_informacao);
		
		
		tvParada = (TextView) findViewById(R.id.paradaTvInformacao);
		
		//Recebendo valor do activity anterior
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		tvParada.setText("Id da parada selecionada = " + bundle.getString("idParada"));


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parada_informacao, menu);
		return true;
	}

}
