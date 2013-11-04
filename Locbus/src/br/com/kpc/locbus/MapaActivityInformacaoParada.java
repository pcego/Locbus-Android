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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.kpc.locbus.adapter.InformacaoLinhasPorParadaAdapter;
import br.com.kpc.locbus.core.Linha;
import br.com.kpc.locbus.util.ConexaoServidor;

public class MapaActivityInformacaoParada extends Activity {

	TextView titulo;
	ListView listView;
	Linha linha;

	private ArrayList<Linha> arrayDadosLinha = new ArrayList<Linha>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_activity_informacao_parada);

		titulo = (TextView) findViewById(R.id.mapaInformacaoTitulo);
		listView = (ListView) findViewById(R.id.mapaInformacaoListView);

		if (getIntent().hasExtra("titulo")) {

			Bundle bundle = getIntent().getExtras();

			titulo.setText(bundle.getString("titulo"));

			new BuscarParadasWS().execute(ConexaoServidor.getConexaoServidor()
					.getLinkInformacaoParada()
					+ titulo.getText().toString().trim().replace(" ", "%20"));

			Log.d("MapaActivityInformacao", ConexaoServidor
					.getConexaoServidor().getLinkInformacaoParada()
					+ titulo.getText().toString().trim().replace(" ", "%20"));

		}
	}

	// xxxxxxxxxxxxxxxxx INICIANDO CONSULTA ( LINHAS POR PARADAS ) WEB SERVICE

	private ProgressDialog progressDialog;

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
	class BuscarParadasWS extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Limpamdo o array lsita de dados
			arrayDadosLinha.clear();

			// Animação enquando executa o web service
			progressDialog = ProgressDialog.show(
					MapaActivityInformacaoParada.this, "Aguarde",
					"processando...");
		}

		@Override
		protected String doInBackground(String... params) {

			// Passando link como parametro. getLink da class ConexãoServidor
			// params[0] recebe uma string co o link e passa para ao metodo.

			try {
				executarWebService(params[0]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Se arrayDadosLinha ainda estiver em BRANCO,
			// e porque não encontrou varios resultado para o array.
			// Então faz a busca para apenas um Registro.
			if (arrayDadosLinha.isEmpty()) {

				executarWebServiceSingleResult(params[0]);
			}

			return null;

		}

		// Executando o webservice para buscar informações no banco de dados.
		// Metodo para Varios resultados.
		private void executarWebService(String link) throws JSONException {
			String result = null;

			result = getRESTFileContent(link);

			if (result.equals("null")) {
				Log.e("Linha", "Falha ao acessar WS");
				Toast.makeText(getApplicationContext(),
						"Linha Falha ao acessar WS", Toast.LENGTH_SHORT).show();

			}

			JSONObject json = new JSONObject(result);

			JSONArray dadosArray = json.getJSONArray("linha");
			JSONObject dadosJson;

			for (int i = 0; i < dadosArray.length(); i++) {
				dadosJson = new JSONObject(dadosArray.getString(i));

				linha = new Linha();

				linha.set_id(dadosJson.getInt("id"));
				linha.setHoraFinal(dadosJson.getString("horaFinal"));
				linha.setHoraInicial(dadosJson.getString("horaInicial"));
				linha.setNumeroLinha(dadosJson.getInt("numeroLinha"));
				linha.setPontoFinal(dadosJson.getString("pontoFinal"));
				linha.setPontoInicial(dadosJson.getString("pontoInicial"));
				linha.setTipoLinha(dadosJson.getString("tipoLinha"));

				arrayDadosLinha.add(linha);
			}
		}

		// Executando o webservice para buscar informações no banco de dados.
		// Metodo SINGLE RESULT
		private void executarWebServiceSingleResult(String link) {
			String result = null;

			result = getRESTFileContent(link);

			if (result.equals("null")) {
				// Nenhum resultado encontrado
			} else {

				try {
					Log.d("verificando", "Result: " + result);

					result = result.substring(9, result.length() - 1);

					Log.d("verificando", "Result: " + result);

					JSONObject o = new JSONObject(result);

					linha = new Linha();

					linha.set_id(o.getInt("id"));
					linha.setHoraFinal(o.getString("horaFinal"));
					linha.setHoraInicial(o.getString("horaInicial"));
					linha.setNumeroLinha(o.getInt("numeroLinha"));
					linha.setPontoFinal(o.getString("pontoFinal"));
					linha.setPontoInicial(o.getString("pontoInicial"));
					linha.setTipoLinha(o.getString("tipoLinha"));

					arrayDadosLinha.add(linha);

				} catch (JSONException e) {
					Log.e("Erro", "Erro no parsing do JSON", e);
				}
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			progressDialog.dismiss();

			if (arrayDadosLinha.isEmpty()) {
				Toast.makeText(MapaActivityInformacaoParada.this,
						R.string.nenhum_registro_encontrado, Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(MapaActivityInformacaoParada.this,
						R.string.informacoes_carregada_com_sucesso,
						Toast.LENGTH_SHORT).show();

				// Enviando os dados para o ListView (Atualizando a tela)
				listView.setAdapter(new InformacaoLinhasPorParadaAdapter(
						MapaActivityInformacaoParada.this, arrayDadosLinha));
			}

		}

	}

	// xxxxxxxxxxxxxxxxx FINALIZANDO CONSULTA ( LINHAS POR PARADAS )WEB SERVICE

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapa_activity_informacao, menu);
		return true;
	}

}
