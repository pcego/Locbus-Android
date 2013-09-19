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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import br.com.kpc.locbus.adapter.ParadaAdapter;
import br.com.kpc.locbus.core.Onibus;
import br.com.kpc.locbus.core.Parada;
import br.com.kpc.locbus.util.ConexaoServidor;

public class ParadaBuscarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parada_buscar);

		listView = (ListView) findViewById(R.id.paradaLvLinhas);

		// Chama o WebService em um AsyncTask
		new TarefaWS().execute();

		// Implementação do Menu da ListView
		registerForContextMenu(listView);

		// Quando selecionar algum item da lista...
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = listView.getItemAtPosition(position);
				parada = (Parada) o;
				mensagemConfirmacao();
				// Toast.makeText(ParadaBuscarActivity.this,
				// "Selecionado :" + " " + parada.getDescricao(),
				// Toast.LENGTH_SHORT)
				// .show();
				//

			}

		});

	}

	private void mensagemConfirmacao() {
		// Cria o gerador do AlertDialog
		AlertDialog alerta;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// define o titulo
		builder.setTitle("Titulo");
		// define a mensagem
		builder.setMessage("Qualifique este software");
		// define um botão como positivo

		builder.setPositiveButton("Informação",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						// Passando outra Activyt o valor selecionado
						Bundle bundle = new Bundle();
						bundle.putString("idParada",
								Integer.toString(parada.get_id()));
												
						// Chamando a proxima tela
						Intent i = new Intent(getApplicationContext(),
								ParadaInformacao.class);
						// Passando o Budle com o valor do id.
						i.putExtras(bundle);
						startActivity(i);
						// Finalizando a tela.
						finish();

					}
				});

		builder.setNegativeButton("Ver no Mapa",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						// Passando outra Activyt o valor selecionado
						Bundle bundle = new Bundle();
						bundle.putString("idParada",
								Integer.toString(parada.get_id()));

						bundle.putString("LatitudeParada", parada.getLatitude());
						bundle.putString("longitudeParada",parada.getLongitude());
						bundle.putString("DescricaoParada", parada.getDescricao());
	
						
						
						// Chamando a proxima tela
						Intent i = new Intent(getApplicationContext(),
								MapaActivity.class);
						// Passando o Budle com o valor do id.
						i.putExtras(bundle);
						startActivity(i);
						// Finalizando a tela.
						finish();

					}
				});
		// cria o AlertDialog
		alerta = builder.create();
		// Exibe
		alerta.show();
	}

	// // Implementação do Menu da ListView
	// //Adicionando opções de menu
	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View v,
	// ContextMenuInfo menuInfo) {
	// // TODO Auto-generated method stub
	// super.onCreateContextMenu(menu, v, menuInfo);
	// menu.setHeaderTitle("O que deseja fazer?");
	// menu.add(0, v.getId(), 0, "Informações");
	// menu.add(0, v.getId(), 0, "Ver no Mapa");
	// }
	// // Implementação do Menu da ListView
	// // Tratando a opção selecionada.
	// @Override
	// public boolean onContextItemSelected(MenuItem item) {
	// // TODO Auto-generated method stub
	// super.onContextItemSelected(item);
	// if (item.getTitle() == "Informações") {
	//
	// Toast.makeText(this, "informação..", Toast.LENGTH_SHORT)
	// .show();
	//
	// }
	// else if(item.getTitle() == "Ver no Mapa"){
	// Toast.makeText(this, "Opção ver no mapa", Toast.LENGTH_SHORT)
	// .show();
	// }
	// return true;
	// }

	// xxxxxxxxxxxxxxxxx INICIANDO CONSULTA ( PARADAS ) WEB SERVICE
	// Declaração de Variaveis Global da Class
	ListView listView;
	// Array que vai armazenar os dados da consulta e coloca no List
	ArrayList arrayDados = new ArrayList();
	// Classe
	Parada parada;

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
	class TarefaWS extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Limpamdo o array lsita de dados
			arrayDados.clear();

			// Animação enquando executa o web service
			progressDialog = ProgressDialog.show(ParadaBuscarActivity.this,
					"Aguarde", "processando...");
		}

		@Override
		protected String doInBackground(Void... params) {
			// Passando link como parametro. getLink da class ConexãoServidor
			return executarWebService(ConexaoServidor.getConexaoServidor()
					.getLinkTodasParadas());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (arrayDados.isEmpty()) {
				Toast.makeText(ParadaBuscarActivity.this,
						"Nenhum registro encontrado!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(ParadaBuscarActivity.this,
						"Informações carregada com sucesso!",
						Toast.LENGTH_SHORT).show();
			}

			progressDialog.dismiss();

			// Enviando os dados para o ListView (Atualizando a tela)
			listView.setAdapter(new ParadaAdapter(ParadaBuscarActivity.this,
					arrayDados));

		}

		// Executando o webservice para buscar informações no banco de dados.
		private String executarWebService(String linkOnibus) {
			String result = null;

			result = getRESTFileContent(linkOnibus);

			if (result == null) {
				Log.e("Paradas", "Falha ao acessar WS");
				Toast.makeText(getApplicationContext(),
						"Paradas Falha ao acessar WS", Toast.LENGTH_SHORT)
						.show();
				return null;
			}

			try {
				JSONObject json = new JSONObject(result);
				StringBuffer sb = new StringBuffer();
				JSONArray dadosArray = json.getJSONArray("parada");
				JSONObject dadosJson;

				// Se tem apenas um registro no banco
				if (dadosArray.length() == 1) {
					// for (int i = 0; i < pessoasArray.length(); i++) {
					// pessoaJson = new JSONObject(pessoasArray.getString(i));
					// sb.append("id=" + json.getLong("id"));
					// sb.append("|descricao=" + json.getString("descricao"));
					// sb.append('\n');
					// Log.d("TesteWs", sb.toString());
				}
				// se tem mais de um registro no banco cria um array
				else if (dadosArray.length() > 1) {

					for (int i = 0; i < dadosArray.length(); i++) {
						dadosJson = new JSONObject(dadosArray.getString(i));

						parada = new Parada();
						// newsData.set_id(Integer.parseInt(dadosJson.getString("id")))
						// ;
						parada.set_id(dadosJson.getInt("id"));
						parada.setDescricao(dadosJson.getString("descricao"));
						parada.setLatitude(dadosJson.getString("latitude"));
						parada.setLongitude(dadosJson.getString("longitude"));
						// parada.setStatus(dadosJson.getBoolean("status"));
						parada.setBairro(dadosJson.getString("endereco"));

						Log.d("aaaaaaaaaaa", dadosJson.getString("endereco"));
						// parada.setRua(dadosJson.getString("rua"));
						arrayDados.add(parada);

					}
				}
				return sb.toString();

			} catch (JSONException e) {
				Log.e("Erro", "Erro no parsing do JSON", e);
			}
			return null;
		}
	}

	// xxxxxxxxxxxxxxxxx FINALIZANDO CONSULTA ( PARADAS )WEB SERVICE

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parada_buscar, menu);
		return true;
	}

}
