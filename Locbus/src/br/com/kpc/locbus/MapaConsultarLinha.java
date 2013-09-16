package br.com.kpc.locbus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MapaConsultarLinha extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_consultar_linha);
	}

	

	
	public void onClick_lotacoes(View v) {

		//criar a intent
		Intent it = new Intent(getApplicationContext(), MapaActivity.class);
		it.putExtra("msg", "parada");
		setResult(10,it);
		
		finish();
	}


	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa_consultar_linha, menu);
		return true;
	}

}
