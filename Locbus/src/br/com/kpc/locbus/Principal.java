package br.com.kpc.locbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

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
		Intent i = new Intent(getApplicationContext(), OnibusActivity.class);
		startActivity(i);
	}
	public void btnMapa(View v) {
		Intent i = new Intent(getApplicationContext(), MapaActivity.class);
		startActivity(i);
	}

}
