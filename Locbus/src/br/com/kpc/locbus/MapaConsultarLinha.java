package br.com.kpc.locbus;

import java.util.ArrayList;

import br.com.kpc.locbus.core.Linha;
import br.com.kpc.locbus.core.Onibus;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MapaConsultarLinha extends Activity {

	EditText numeroLinha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_consultar_linha);

		numeroLinha = (EditText) findViewById(R.id.edtNumeroLinha);

	}

	public void onClick_lotacoes(View v) {
		// criar a intent
		Intent it = new Intent();
		it.putExtra("msg", numeroLinha.getText().toString());
		setResult(10, it);

		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa_consultar_linha, menu);
		return true;
	}

}
