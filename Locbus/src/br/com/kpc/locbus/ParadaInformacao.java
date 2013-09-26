package br.com.kpc.locbus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ParadaInformacao extends Activity {

	private TextView tvDescricao;
	private TextView tvRua;
	private TextView tvBairro;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parada_informacao);
		
		
		tvDescricao = (TextView) findViewById(R.id.paradaTvDescricao);
		tvRua = (TextView) findViewById(R.id.paradaTvRua);
		tvBairro = (TextView) findViewById(R.id.paradaTvBairro);

		//Recebendo valor do activity anterior
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		tvDescricao.setText(bundle.getString("paradaDescricao"));
		tvRua.setText(bundle.getString("paradaBairro"));
		tvBairro.setText(bundle.getString("paradaRua"));

		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parada_informacao, menu);
		return true;
	}

}
