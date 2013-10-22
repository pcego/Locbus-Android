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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import br.com.kpc.locbus.adapter.LinhasAdapter;
import br.com.kpc.locbus.core.Linha;
import br.com.kpc.locbus.util.ConexaoServidor;

public class MapaBuscarActivity extends Activity {

	// Declaração de Variaveis Global da Class
	private ProgressDialog progressDialog;

	// Array que vai armazenar os dados da consulta e coloca no List
	ArrayList arrayDados = new ArrayList();
	// Classe
	Linha linhas;

	ListView lvLinhas;
	ImageButton ibLinhas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_buscar);

		// Limpamdo o array lsita de dados
		arrayDados.clear();


		lvLinhas = (ListView) findViewById(R.id.lvLinhas);

		// Chama o WebService em um AsyncTask
		new TodasAsLinhasWS().execute();

		// Quando selecionar algum item da lista fazer...
		lvLinhas.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = lvLinhas.getItemAtPosition(position);
				linhas = (Linha) o;

				// Passando item selecionado para Activity
				Bundle bundle = new Bundle();
				bundle.putString("numeroLinha",
						Integer.toString(linhas.getNumeroLinha()));
				// Chamando a proxima tela
				Intent i = new Intent();
				// Passando o Budle com o valor do id.
				i.putExtras(bundle);
				setResult(1, i);
				// Finalizando a tela.
				finish();

			}

		});

	}

	// xxxxxxxxxxxxxxxx INICINADO CONSULTA WEB SERVICE

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
	class TodasAsLinhasWS extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Animação enquando executa o web service
			progressDialog = ProgressDialog.show(MapaBuscarActivity.this,
					"Aguarde", "processando...");
		}

		@Override
		protected String doInBackground(Void... params) {
			// Passando link como parametro. getLink da class ConexãoServidor
			return executarWebService(ConexaoServidor.getConexaoServidor()
					.getLinkTodaslinhas());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (arrayDados.isEmpty()) {
				Toast.makeText(MapaBuscarActivity.this,
						R.string.nenhum_registro_encontrado, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(MapaBuscarActivity.this,
						R.string.informacoes_carregada_com_sucesso,
						Toast.LENGTH_SHORT).show();

				// Enviando os dados para o ListView (Atualizando a tela)
				lvLinhas.setAdapter(new LinhasAdapter(MapaBuscarActivity.this,
						arrayDados));

			}

			progressDialog.dismiss();

		}

		// Executando o webservice para buscar informações no banco de dados.
		private String executarWebService(String linkOnibus) {
			String result = null;

			result = getRESTFileContent(linkOnibus);

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
				JSONArray dadosArray = json.getJSONArray("linha");
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

						linhas = new Linha();
						// newsData.set_id(Integer.parseInt(dadosJson.getString("id")))
						// ;
						linhas.set_id(dadosJson.getInt("id"));
						linhas.setHoraFinal(dadosJson.getString("horaFinal"));
						linhas.setHoraInicial(dadosJson
								.getString("horaInicial"));
						linhas.setNumeroLinha(dadosJson.getInt("numeroLinha"));
						linhas.setPontoFinal(dadosJson.getString("pontoFinal"));
						linhas.setPontoInicial(dadosJson
								.getString("pontoInicial"));
						linhas.setStatus(dadosJson.getBoolean("status"));
						linhas.setTipoLinha(dadosJson.getString("tipoLinha"));
						arrayDados.add(linhas);

					}
				}
				return sb.toString();

			} catch (JSONException e) {
				Log.e("Erro", "Erro no parsing do JSON", e);
			}
			return null;
		}
	}

	// xxxxxxxxxxxxxxxx FIM CONSULTA WEB SERVICE

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa_buscar, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d("LOG TEST", "ON DESTROI");

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.d("LOG TEST", "ON STOP");

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("LOG TEST", "ON PAUSE");
		// seta o status do resultado e a intenet
		// 1 - Onibus de determinada linha;
		// 2 - Parada de Onibus mais proxima;

	}
}
