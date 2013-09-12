package br.com.kpc.locbus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class OnibusInforActivyty extends Activity {
TextView tvIdOnibus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onibus_infor);
		
		tvIdOnibus = (TextView) findViewById(R.id.tvIdOnibus);
		
		//Recebendo valor do activity anterior
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		tvIdOnibus.setText(bundle.getString("idOnibus"));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.onibus_infor_activyty, menu);
		return true;
	}

}
