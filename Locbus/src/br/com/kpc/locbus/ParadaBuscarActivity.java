package br.com.kpc.locbus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import br.com.kpc.locbus.adapter.ParadaAdapter;
import br.com.kpc.locbus.core.Veiculo;
import br.com.kpc.locbus.core.Parada;
import br.com.kpc.locbus.util.ConexaoServidor;

public class ParadaBuscarActivity extends Activity {

	// Declara��o de Variaveis Global da Class
	ListView listView;

	String opBuscaSelecionada;
	EditText DescricaoPesquisa;
	Spinner spOpcaoBusca;
	// Array que vai armazenar os dados da consulta e coloca no List
	ArrayList arrayDados = new ArrayList();
	// Classe
	Parada parada;

	private String[] arrayOpcoesBusca;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parada_buscar);

		listView = (ListView) findViewById(R.id.paradaLvLinhas);
		DescricaoPesquisa = (EditText) findViewById(R.id.paradaEdtDescricaoPesquisa);
		arrayOpcoesBusca = getResources().getStringArray(
				R.array.opcao_de_busca_paradas);
		spOpcaoBusca = (Spinner) findViewById(R.id.paradaSpOpcaoBusca);

		// Cria um ArrayAdapter usando um padr�o de layout da classe R do
		// android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, arrayOpcoesBusca);
		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spOpcaoBusca.setAdapter(spinnerArrayAdapter);

		// M�todo do Spinner para capturar o item selecionado
		spOpcaoBusca
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int posicao, long id) {

						// pega nome pela posi��o
						opBuscaSelecionada = parent.getItemAtPosition(posicao)
								.toString();
						if (opBuscaSelecionada.equalsIgnoreCase("Todas")) {
							DescricaoPesquisa.setEnabled(false);
							DescricaoPesquisa.setHint("");
							DescricaoPesquisa.setText("");
						} else {
							DescricaoPesquisa.setEnabled(true);
							DescricaoPesquisa
									.setHint(R.string.hint_informe_o_endereco);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		// Implementa��o do Menu da ListView
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

	public Boolean validarCampoPesquisa() {
		// Verificando se tem no minimo 3 caracteres no campo de busca.
		if (DescricaoPesquisa.getText().length() >= 3) {
			return true;
		} else {

			Toast.makeText(getApplicationContext(),
					R.string.limite_minimo_de_caracteres, Toast.LENGTH_LONG)
					.show();
			return false;
		}

	}

	public void btnBuscar(View v) {

		// Limpar ListView
		listView.clearTextFilter();

		if (opBuscaSelecionada.equalsIgnoreCase("Todas")) {
			// Chama o WebService em um AsyncTask para Todas as Paradas
			// Passando link do WebService
			new BuscarParadasWS().execute(ConexaoServidor.getConexaoServidor()
					.getLinkParadasTodas());

		} else if (opBuscaSelecionada.equalsIgnoreCase("Bairro")
				&& validarCampoPesquisa()) {
			// Chama o WebService em um AsyncTask para Paradas de acordo com
			// o
			// Bairro digitado
			// Passando link do WebService
			new BuscarParadasWS().execute(ConexaoServidor.getConexaoServidor()
					.getLinkParadasPorBairro() + DescricaoPesquisa.getText());
		} else if (opBuscaSelecionada.equalsIgnoreCase("Rua")
				&& validarCampoPesquisa()) {
			// Chama o WebService em um AsyncTask para Paradas de acordo com
			// a
			// Rua digitado
			// Passando link do WebService

			new BuscarParadasWS().execute(ConexaoServidor.getConexaoServidor()
					.getLinkParadasPorRua() + DescricaoPesquisa.getText());

			Log.d("LOG RUA ",
					""
							+ ConexaoServidor.getConexaoServidor()
									.getLinkParadasPorRua()
							+ DescricaoPesquisa.getText());
		}
	}

	private void mensagemConfirmacao() {
		// Cria o gerador do AlertDialog
		AlertDialog alerta;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// define o titulo
		builder.setTitle("O que deseja fazer?");
		// define a mensagem sub Titulo
		// builder.setMessage("sub titulo");

		// define um bot�o como positivo

		builder.setPositiveButton("Informa��o",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						// Instanciando Intent
						Intent i = new Intent(getApplicationContext(),
								MapaActivityInformacaoParada.class);
						i.putExtra("titulo", parada.getDescricao());

						i.putExtra("tipo", "P");

						// Chama activity
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
						bundle.putString("longitudeParada",
								parada.getLongitude());
						bundle.putString("DescricaoParada",
								parada.getDescricao());

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

	// // Implementa��o do Menu da ListView
	// //Adicionando op��es de menu
	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View v,
	// ContextMenuInfo menuInfo) {
	// // TODO Auto-generated method stub
	// super.onCreateContextMenu(menu, v, menuInfo);
	// menu.setHeaderTitle("O que deseja fazer?");
	// menu.add(0, v.getId(), 0, "Informa��es");
	// menu.add(0, v.getId(), 0, "Ver no Mapa");
	// }
	// // Implementa��o do Menu da ListView
	// // Tratando a op��o selecionada.
	// @Override
	// public boolean onContextItemSelected(MenuItem item) {
	// // TODO Auto-generated method stub
	// super.onContextItemSelected(item);
	// if (item.getTitle() == "Informa��es") {
	//
	// Toast.makeText(this, "informa��o..", Toast.LENGTH_SHORT)
	// .show();
	//
	// }
	// else if(item.getTitle() == "Ver no Mapa"){
	// Toast.makeText(this, "Op��o ver no mapa", Toast.LENGTH_SHORT)
	// .show();
	// }
	// return true;
	// }

	// xxxxxxxxxxxxxxxxx INICIANDO CONSULTA ( PARADAS ) WEB SERVICE

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

	// Tarefa assincrona para realizar requisi��o e tratar retorno
	class BuscarParadasWS extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Limpamdo o array lsita de dados
			arrayDados.clear();

			// Anima��o enquando executa o web service
			progressDialog = ProgressDialog.show(ParadaBuscarActivity.this,
					"Aguarde", "processando...");
		}

		@Override
		protected String doInBackground(String... params) {

			// Passando link como parametro. getLink da class Conex�oServidor
			// params[0] recebe uma string co o link e passa para ao metodo.
			return executarWebService(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();

			if (arrayDados.isEmpty()) {
				Toast.makeText(ParadaBuscarActivity.this,
						"Nenhum registro encontrado!", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(ParadaBuscarActivity.this,
						"Informa��es carregada com sucesso!",
						Toast.LENGTH_SHORT).show();

			}

			// Enviando os dados para o ListView (Atualizando a tela)
			listView.setAdapter(new ParadaAdapter(ParadaBuscarActivity.this,
					arrayDados));

		}

		// Executando o webservice para buscar informa��es no banco de dados.
		private String executarWebService(String link) {
			String result = null;

			result = getRESTFileContent(link);

			if (result == null) {
				Log.e("Paradas", "Falha ao acessar WS");
				Toast.makeText(getApplicationContext(),
						"Paradas Falha ao acessar WS", Toast.LENGTH_SHORT)
						.show();
				return null;
			}

			try {

				JSONObject json = new JSONObject(result);

				JSONArray dadosArray = json.getJSONArray("parada");
				JSONObject dadosJson;

				// Se tem apenas um registro no banco
				if (dadosArray.length() == 1) {

					Log.d("LOD TAMANHO JSON 2", "ENTROU OP��O IF == 1");

					parada = new Parada();

					parada.set_id(json.getInt("id"));
					parada.setDescricao(json.getString("descricao"));
					parada.setLatitude(json.getString("latitude"));
					parada.setLongitude(json.getString("longitude"));
					parada.setBairro(json.getString("bairro"));
					parada.setRua(json.getString("rua"));

					arrayDados.add(parada);
				}
				// se tem mais de um registro no banco cria um array
				else if (dadosArray.length() > 1) {
					Log.d("LOD TAMANHO JSON 2", "ENTROU OP��O IF > 1");

					for (int i = 0; i < dadosArray.length(); i++) {
						dadosJson = new JSONObject(dadosArray.getString(i));

						parada = new Parada();

						parada.set_id(dadosJson.getInt("id"));
						parada.setDescricao(dadosJson.getString("descricao"));
						parada.setLatitude(dadosJson.getString("latitude"));
						parada.setLongitude(dadosJson.getString("longitude"));
						parada.setBairro(dadosJson.getString("bairro"));
						parada.setRua(dadosJson.getString("rua"));

						arrayDados.add(parada);

					}
				}
				return "";

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
