package br.com.kpc.locbus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class MapaActivityInformacao extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_activity_informacao);

		
		if (getIntent().hasExtra("titulo")){
			
			Bundle bundle = getIntent().getExtras();
			Toast.makeText(getApplicationContext(), bundle.getString("titulo"), Toast.LENGTH_SHORT).show();
			
			Toast.makeText(getApplicationContext(),
					"ID: " + bundle.getString("id"), Toast.LENGTH_SHORT).show();
			
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa_activity_informacao, menu);
		return true;
	}

}
