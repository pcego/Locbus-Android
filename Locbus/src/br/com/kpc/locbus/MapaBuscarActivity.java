package br.com.kpc.locbus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MapaBuscarActivity extends Activity {

	private static final int RETORNO_MAPACONSULTALINHA = 10;

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

	public void onClick_OnibusLinha(View v) {

		 //criar a intent
		 Intent it = new Intent(getApplication(), MapaActivity.class);
		 //seta o valor de retorno
		 it.putExtra("msg", "onibus linha");
		 //seta o status do resultado e a intenet
		 // 1 - Onibus de determinada linha;
		 // 2 - Parada de Onibus mais proxima;
		 setResult(1,it);

		
		startActivityForResult(new Intent(this, MapaConsultarLinha.class),
				RETORNO_MAPACONSULTALINHA);

	}

	public void onClick_parada(View v) {
		// criar a intent
		Intent it = new Intent(getApplicationContext(), MapaActivity.class);
		// seta o valor de retorno
		it.putExtra("msg", "parada");
		// seta o status do resultado e a intenet
		// 1 - Onibus de determinada linha;
		// 2 - Parada de Onibus mais proxima;
		setResult(2, it);
		finish();
	}

	@Override
	protected void onActivityResult(int codigo, int resultado, Intent it) {

		if (codigo == RETORNO_MAPACONSULTALINHA) {
			// 1 - Onibus de determinada linha;
			if (resultado == 10) {
				Toast.makeText(this, "retornando...", Toast.LENGTH_SHORT)
						.show();

				// //criar a intent
				// Intent ite = new Intent();
				// //seta o valor de retorno
				// it.putExtra("msg", "onibus linha");
				// //seta o status do resultado e a intenet
				// // 1 - Onibus de determinada linha;
				// // 2 - Parada de Onibus mais proxima;
				// setResult(1,it);
				finish();

			}
		}

		super.onActivityResult(codigo, resultado, it);
	}


}
