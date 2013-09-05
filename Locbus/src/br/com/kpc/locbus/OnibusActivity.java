package br.com.kpc.locbus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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
import br.com.kpc.locbus.core.Onibus;

public class OnibusActivity extends Activity {

	// Declaração de Variaveis Global da Class
	private ProgressDialog progressDialog;
	private EditText edtNumeroLinha;

	ListView listView;
	// Array que vai armazenar os dados da consulta e coloca no List
	ArrayList arrayDados = new ArrayList();
	// Classe
	Onibus onibus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onibus);

		edtNumeroLinha = (EditText) findViewById(R.id.edtNumeroLinha);

		listView = (ListView) findViewById(R.id.listOnibus);

		// Quando selecionar algum item da lista fazer...
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = listView.getItemAtPosition(position);
				onibus = (Onibus) o;
				Toast.makeText(OnibusActivity.this,
						"Selecionado :" + " " + onibus.getDescricao(), Toast.LENGTH_SHORT)
						.show();
			}

		});
	}

	// Botão de carregar os dados
	public void btnWSClick(View v) {

		// Limpamdo o array lsita de dados
		arrayDados.clear();

		// Chama o WebService em um AsyncTask
		new TarefaWS().execute();

	}

	public byte[] getBytes(InputStream is) {
		try {
			int bytesLidos;
			ByteArrayOutputStream bigBuffer = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];

			while ((bytesLidos = is.read(buffer)) > 0) {
				bigBuffer.write(buffer, 0, bytesLidos);
			}

			return bigBuffer.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getRESTFileContent(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);

		try {
			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = new String(getBytes(instream));

				instream.close();
				return result;
			}
		} catch (Exception e) {
			Log.e("TesteWs", "Falha ao acessar Web service", e);
		}
		return null;
	}

	// Tarefa assincrona para realizar requisição e tratar retorno
	class TarefaWS extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Animação enquando executa o web service
			progressDialog = ProgressDialog.show(OnibusActivity.this, "Aguarde",
					"processando...");
		}

		@Override
		protected String doInBackground(Void... params) {
			return executarWebService();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (arrayDados.isEmpty()) {
				Toast.makeText(OnibusActivity.this,
						"Nenhum registro encontrado!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(OnibusActivity.this,
						"Informações carregada com sucesso!", Toast.LENGTH_SHORT)
						.show();
			}

			progressDialog.dismiss();

			// Enviando os dados para o ListView (Atualizando a tela)
			listView.setAdapter(new OnibusAdapter(OnibusActivity.this,
					arrayDados));

		}

		//Executando o webservice para buscar informações no banco de dados.
		private String executarWebService() {
			String result = null;

			result = getRESTFileContent("http://10.0.2.2:8080/locBus/rest/veiculos/buscaPorLinha/"
					+ edtNumeroLinha.getText());

			if (result == null) {
				Log.e("Onibus", "Falha ao acessar WS");
				Toast.makeText(getApplicationContext(),
						"Onibus Falha ao acessar WS", Toast.LENGTH_SHORT)
						.show();
				return null;
			}

			Log.d("Onibus", "acessou WS");
			Log.d("retornou ", result);

			try {
				JSONObject json = new JSONObject(result);
				StringBuffer sb = new StringBuffer();
				JSONArray dadosArray = json.getJSONArray("veiculo");
				JSONObject dadosJson;

				// Se tem apenas um registro no banco
				if (dadosArray.length() == 1) {
					// for (int i = 0; i < pessoasArray.length(); i++) {
					// pessoaJson = new JSONObject(pessoasArray.getString(i));
					sb.append("id=" + json.getLong("id"));
					sb.append("|descricao=" + json.getString("descricao"));
					sb.append('\n');
					Log.d("TesteWs", sb.toString());
				}
				// se tem mais de um registro no banco cria um array
				else if (dadosArray.length() > 1) {

					for (int i = 0; i < dadosArray.length(); i++) {
						dadosJson = new JSONObject(dadosArray.getString(i));

						sb.append("id=" + dadosJson.getInt("id"));
						sb.append("|descricao="
								+ dadosJson.getString("descricao"));
						sb.append('\n');
						Log.d("TesteWs", sb.toString());

						onibus = new Onibus();
						//newsData.set_id(Integer.parseInt(dadosJson.getString("id"))) ;
						onibus.set_id(1) ;
						onibus.setDescricao(dadosJson
								.getString("descricao"));
						onibus.setEmpresa_id(1);
						arrayDados.add(onibus);

					}
				}
				return sb.toString();

			} catch (JSONException e) {
				Log.e("Erro", "Erro no parsing do JSON", e);
			}
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.onibus, menu);
		return true;
	}

}
