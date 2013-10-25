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
import br.com.kpc.locbus.core.Linha;
import br.com.kpc.locbus.core.Veiculo;
import br.com.kpc.locbus.core.Parada;
import br.com.kpc.locbus.util.ConexaoServidor;

public class ParadaBuscarActivity extends Activity {

	// Declaração de Variaveis Global da Class
	ListView listView;

	String opBuscaSelecionada;
	EditText DescricaoPesquisa;
	Spinner spOpcaoBusca;
	// Array que vai armazenar os dados da consulta e coloca no List
	ArrayList arrayDados = new ArrayList();
	// Classe
	private Parada parada;

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

		// Cria um ArrayAdapter usando um padrão de layout da classe R do
		// android, passando o ArrayList nomes
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, arrayOpcoesBusca);

		ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;

		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spOpcaoBusca.setAdapter(spinnerArrayAdapter);

		// Método do Spinner para capturar o item selecionado
		spOpcaoBusca
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int posicao, long id) {

						// pega nome pela posição
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

		if (opBuscaSelecionada.equalsIgnoreCase("Todas")
				&& ConexaoServidor.verificaConexao(getApplicationContext())) {
			// Chama o WebService em um AsyncTask para Todas as Paradas
			// Passando link do WebService
			// Valida se tem conexão com a internet
			new BuscarParadasWS().execute(ConexaoServidor.getConexaoServidor()
					.getLinkParadasTodas());

		} else if (opBuscaSelecionada.equalsIgnoreCase("Bairro")
				&& validarCampoPesquisa()
				&& ConexaoServidor.verificaConexao(getApplicationContext())) {
			// Chama o WebService em um AsyncTask para Paradas de acordo com
			// o Bairro digitado
			// Passando link do WebService
			// Valida se tem conexão com a internet
			// Valida se tem mais de 3 caracteres
			new BuscarParadasWS().execute(ConexaoServidor.getConexaoServidor()
					.getLinkParadasPorBairro() + DescricaoPesquisa.getText());
		} else if (opBuscaSelecionada.equalsIgnoreCase("Rua")
				&& validarCampoPesquisa()
				&& ConexaoServidor.verificaConexao(getApplicationContext())) {
			// Chama o WebService em um AsyncTask para Paradas de acordo com
			// a Rua digitado
			// Passando link do WebService
			// Valida se tem conexão com a internet
			// Valida se tem mais de 3 caracteres
			new BuscarParadasWS().execute(ConexaoServidor.getConexaoServidor()
					.getLinkParadasPorRua() + DescricaoPesquisa.getText());

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

		// Ação para o opção Informação
		builder.setPositiveButton("Informação",
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
		// Ação para a opção ver no mapa
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

	// Tarefa assincrona para realizar requisição e tratar retorno
	class BuscarParadasWS extends AsyncTask<String, Void, String> {

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
			if (arrayDados.isEmpty()) {
				try {
					executarWebServiceSingleResult(params[0]);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return null;

		}

		// Executando o webservice para buscar informações no banco de dados.
		private void executarWebService(String link) throws JSONException {
			String result = null;

			result = getRESTFileContent(link);

			if (result.equalsIgnoreCase("null")) {
				//Nenhum resultado encontrado.
			} else {

				JSONObject json = new JSONObject(result);

				JSONArray dadosArray = json.getJSONArray("parada");
				JSONObject dadosJson;

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
		}

		// Executando o webservice para buscar informações no banco de dados.
		// Metodo SINGLE RESULT
		private void executarWebServiceSingleResult(String link)
				throws JSONException {
			String result = null;

			result = getRESTFileContent(link);

			if (result.equalsIgnoreCase("null")) {
				// verificando se tem algum registro no retorno.
			} else {

				// Corta o link transformando em Json de um Objeto
				// Remove a parte {"parada":[ e o final }
				result = result.substring(10, result.length() - 1);

				JSONObject o = new JSONObject(result);

				parada = new Parada();

				parada.set_id(o.getInt("id"));
				parada.setDescricao(o.getString("descricao"));
				parada.setLatitude(o.getString("latitude"));
				parada.setLongitude(o.getString("longitude"));
				parada.setBairro(o.getString("bairro"));
				parada.setRua(o.getString("rua"));

				arrayDados.add(parada);

			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();

			if (arrayDados.isEmpty()) {
				Toast.makeText(ParadaBuscarActivity.this,
						R.string.nenhum_registro_encontrado, Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(ParadaBuscarActivity.this,
						R.string.informacoes_carregada_com_sucesso,
						Toast.LENGTH_SHORT).show();

				// Enviando os dados para o ListView (Atualizando a tela)
				listView.setAdapter(new ParadaAdapter(
						ParadaBuscarActivity.this, arrayDados));
			}
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
