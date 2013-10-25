package br.com.kpc.locbus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import br.com.kpc.locbus.adapter.OnibusAdapter;
import br.com.kpc.locbus.core.Veiculo;
import br.com.kpc.locbus.util.ConexaoServidor;

public class OnibusActivity extends Activity {

	// Declaração de Variaveis Global da Class
	private EditText edtNumeroLinha;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onibus);

		edtNumeroLinha = (EditText) findViewById(R.id.edtNumeroLinha);

	}

	// Botão de carregar os dados
	public void btnOnibusWSClick(View v) {

		Bundle bundle = new Bundle();
		bundle.putString("numeroLinha", edtNumeroLinha.getText().toString());

		Intent intent = new Intent(getApplicationContext(), MapaActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.onibus, menu);
		return true;
	}

}
