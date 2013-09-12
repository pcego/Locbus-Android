package br.com.kpc.locbus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MapaBuscarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_buscar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa_buscar, menu);
		return true;
	}
	
	public void onClick_lotacoes(View v) {
		//criar a intent
		Intent it = new Intent();
		//seta o valor de retorno
		it.putExtra("msg", "onibus");
		//seta o status do resultado e a intenet
		// 1 - Onibus de determinada linha;
		// 2 - Parada de Onibus mais proxima;
		setResult(1,it);
		finish();
	}
	
	public void onClick_parada(View v) {
		//criar a intent
		Intent it = new Intent();
		//seta o valor de retorno
		it.putExtra("msg", "parada");
		//seta o status do resultado e a intenet
		// 1 - Onibus de determinada linha;
		// 2 - Parada de Onibus mais proxima;
		setResult(2,it);
		finish();
	}

}
