package br.com.kpc.locbus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Principal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}

	public void btnOnibus(View v) {
		if (verificaConexao()) {
			Intent i = new Intent(getApplicationContext(), OnibusActivity.class);
			startActivity(i);
		} else {
			Toast.makeText(getApplicationContext(),
					"Atenção!!! não existência conexão com a internet.",
					Toast.LENGTH_LONG).show();
		}

	}

	public void btnParada(View v) {
		if (verificaConexao()) {
			Intent i = new Intent(getApplicationContext(),
					ParadaBuscarActivity.class);
			startActivity(i);
		} else {
			Toast.makeText(getApplicationContext(),
					"Atenção!!! não existência conexão com a internet.",
					Toast.LENGTH_LONG).show();
		}
	}

	public void btnMapa(View v) {
		if (verificaConexao()) {
			Intent i = new Intent(getApplicationContext(), MapaActivity.class);
			startActivity(i);
		} else {
			Toast.makeText(getApplicationContext(),
					"Atenção!!! não existência conexão com a internet.",
					Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Função para verificar existência de conexão com a internet
	 */
	public boolean verificaConexao() {
		boolean conectado;
		ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conectivtyManager.getActiveNetworkInfo() != null
				&& conectivtyManager.getActiveNetworkInfo().isAvailable()
				&& conectivtyManager.getActiveNetworkInfo().isConnected()) {
			conectado = true;
		} else {
			conectado = false;
		}
		return conectado;
	}

}
