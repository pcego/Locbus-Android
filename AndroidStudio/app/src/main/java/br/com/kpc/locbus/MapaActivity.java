package br.com.kpc.locbus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import br.com.kpc.locbus.core.Linha;
import br.com.kpc.locbus.core.Parada;
import br.com.kpc.locbus.core.Posicao;
import br.com.kpc.locbus.core.Veiculo;
import br.com.kpc.locbus.util.ConexaoServidor;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends Activity implements LocationListener {

	private static final int RETORNO_MENU = 0;

	private ListView listView;
	// Array que vai armazenar os dados da consulta e coloca no List
	private ArrayList<Parada> arrayDadosParada = new ArrayList<Parada>();

	// private ArrayList<Veiculo> arrayDadosVeiculos = new ArrayList();
	private List<Veiculo> arrayDadosVeiculos = new ArrayList<Veiculo>();

	// Classe
	private Parada parada;

	// Classe
	private Veiculo veiculo;

	// Iniciando a variavel que vai grava lat long
	LatLng latLng = new LatLng(-16.722954, -43.865749);
	private GoogleMap map;

	@Override
	protected void onResume() {
		super.onResume();
		// Ligar GPS
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// Desliga GPS //Testado - Funcionando
		// LocationManager locationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// locationManager.removeUpdates(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		// Chamando o metodo de verificar se o GPS esta ativo.
		verificarGPSAtivo();

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa))
				.getMap();

		// Habilitando a opção meu local no mapa
		map.setMyLocationEnabled(true);

		// Iniciando o mapa no centro de montes claros
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 14);
		map.animateCamera(update);

		// Recebendo valor do activity Parada para ver no mapa
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			Bundle bundle = intent.getExtras();

			if (bundle.getString("idParada") != null) {
				Toast.makeText(getApplicationContext(),
						"Parada " + bundle.getString("DescricaoParada"),
						Toast.LENGTH_SHORT).show();
				adicionarMarcador(
						new LatLng(Double.parseDouble(bundle
								.getString("LatitudeParada")),
								Double.parseDouble(bundle
										.getString("longitudeParada"))), "(P) "
								+ bundle.getString("DescricaoParada"),
						R.drawable.mapa_localizacao_parada, true, false);

			} // Ao clicar na tela principal em "Onibus" digite o numero da
				// linha o sistema vai chamar essa opção para buscar todos os
				// ônibus disponiveis para a determinada linha que vai vim na
				// variavel (numeroLinha)
			else if (bundle.getString("numeroLinha") != null) {

				// Verificar se tem conexão com internet antes de chamar o WS
				if (ConexaoServidor.verificaConexao(getApplicationContext())) {
					// Chama o WS para buscar os veiculos com sua ultima
					// localização
					new VeiculosPorLinhaWS().execute(bundle
							.getString("numeroLinha"));
				}

			}

		}

		// Ao clicar no marcador
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			public void onInfoWindowClick(Marker marker) {

				// Instanciando Intent
				Intent i = new Intent(getApplicationContext(),
						MapaActivityInformacaoParada.class);

				if (marker.getTitle().substring(1, 2).equals("P")) {
					i.putExtra("titulo", marker.getTitle().substring(4));
					i.putExtra("tipo", "P");
					// Chama activity
					startActivity(i);

				}
			}
		});

	}

	// Verificando se esta ativo o GPS
	public boolean verificarGPSAtivo() {

		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);

		boolean statusGPS = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (statusGPS) {
			return true;
		}
		// Caso n�o esteja ativo abre um novo di�logo com as configura��es para
		// realizar se ativamento
		if (!statusGPS) {
			mensagemConfirmacaoAtivarGPS();

		}
		// Fim da verifica��o se o GPS esta ativo

		return false;
	}

	private void mensagemConfirmacaoAtivarGPS() {
		// Cria o gerador do AlertDialog
		AlertDialog alerta;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// define o titulo
		builder.setTitle(R.string.alerta_gps_desativado_titulo);
		// define a mensagem
		builder.setMessage(R.string.alerta_gps_desativado_pergunta);
		// define um bot�o como positivo

		builder.setPositiveButton(R.string.sim,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						// Chamando a tela de configura��o do GPS
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);

					}
				});

		builder.setNegativeButton(R.string.nao,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						Toast.makeText(
								getApplicationContext(),
								R.string.alerta_gps_desativado_mensagem_nao_ativou_gps,
								Toast.LENGTH_LONG).show();

					}
				});
		// cria o AlertDialog
		alerta = builder.create();
		// Exibe
		alerta.show();
	}

	public void onClick_Satelite(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		com.google.android.gms.maps.CameraUpdate update = CameraUpdateFactory
				.newLatLngZoom(latLng, 15);
		map.animateCamera(update);
	}

	public void onClick_MeuLocal(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 14);
		map.animateCamera(update);

	}

	@Override
	public void onLocationChanged(Location location) {
		// Centraliza o mapa na coordenada atual
		latLng = new LatLng(location.getLatitude(), location.getLongitude());
		// adicionarMarcador(latLng, "Meu Local", R.drawable.meu_local, false,
		// false);

	}

	@Override
	public void onProviderDisabled(String provider) {

		Toast.makeText(getApplicationContext(),
				R.string.alerta_usuario_desativou_gps, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/*
	 * Chamando a tela MapaConsultaLinha que retorna para o metodo
	 * onActivityResult o (resultado == 1) para garantir que o usuario
	 * selecionou alguma linha, junto ao mesmo vem o numero da linha que é feita
	 * a consulta no web service.
	 */
	public void onClick_MapaConsultarLinha(View v) {
		startActivityForResult(new Intent(this, MapaBuscarActivity.class),
				RETORNO_MENU);
	}

	@Override
	protected void onActivityResult(int codigo, int resultado, Intent itRetorno) {
		super.onActivityResult(codigo, resultado, itRetorno);
		// Retornos pode ser:
		// 1 - Onibus de determinada linha;
		// 0 - RETORNO_MENU opção cancelada.

		// Retorno da opção do menu = 0
		if (codigo == RETORNO_MENU) {
			// 1 - Onibus de determinada linha;
			// Verificando se tem conexão com a internet
			if (resultado == 1
					&& ConexaoServidor.verificaConexao(getApplicationContext())) {
				String msg = "";
				msg = itRetorno.getStringExtra("numeroLinha");
				new VeiculosPorLinhaWS().execute(msg);

			}

		}
	}

	public void exibirTodasAsParadas(View v) {

		// Exibir todas as paradas ativas
		// Chama o WebService em um AsyncTask
		// Verificar se tem conexão com internet
		if (ConexaoServidor.verificaConexao(getApplicationContext())) {
			new TodasAsParadasWS().execute();

		}

	}

	// Informe a Latitude e Longitude, Titulo, Icone do ponto.
	public void adicionarMarcador(LatLng latLng, String titulo, int icone,
			boolean zoomNoLocal, boolean limparMapa) {

		// // // Cria um marcador LOCAL ONDE ESTA
		// Marker frameworkSystem = map.addMarker(new MarkerOptions()
		// .position(latLng).title(titulo)
		// .icon(BitmapDescriptorFactory.fromResource(icone)));

		// // Cria um marcador LOCAL ONDE ESTA
		Marker frameworkSystem = map.addMarker(new MarkerOptions()
				.position(latLng).title(titulo)
				.icon(BitmapDescriptorFactory.fromResource(icone)));

		// Move a câmera para Framework System com zoom 15.
		if (zoomNoLocal)
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

		if (limparMapa)
			map.clear();
	}

	// xxxxxxxxxxxxxxxxx INICIANDO CONSULTA ( PARADAS ) WEB SERVICE

	// Declaração de Variaveis Global da Class
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
	class TodasAsParadasWS extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Limpamdo o array lsita de dados
			arrayDadosParada.clear();

			// Animação enquando executa o web service
			progressDialog = ProgressDialog.show(MapaActivity.this, "Aguarde",
					"Todas as Paradas Processando...");
		}

		@Override
		protected String doInBackground(Void... params) {
			// Passando link como parametro. getLink da class ConexãoServidor
			executarWebServiceParada(ConexaoServidor.getConexaoServidor()
					.getLinkParadasTodas());
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (arrayDadosParada.isEmpty()) {
				Toast.makeText(MapaActivity.this,
						R.string.nenhum_registro_encontrado, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(MapaActivity.this,
						R.string.informacoes_carregada_com_sucesso,
						Toast.LENGTH_SHORT).show();

				Iterator<Parada> it = arrayDadosParada.iterator();
				while (it.hasNext()) {
					Parada p = it.next();

					adicionarMarcador(
							new LatLng(Double.parseDouble(p.getLatitude()),
									Double.parseDouble(p.getLongitude())),
							"(P) " + p.getDescricao(),
							R.drawable.mapa_localizacao_parada, true, false);

				}
			}
			progressDialog.dismiss();

		}

		// Executando o webservice para buscar informações no banco de dados.
		private void executarWebServiceParada(String linkOnibus) {
			String result = null;

			result = getRESTFileContent(linkOnibus);

			if (result.equals("null")) {
				// Nenhum resultado encontrado
			} else {

				try {
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

						arrayDadosParada.add(parada);
					}

				} catch (JSONException e) {
					Log.e("Erro", "Erro no parsing do JSON", e);
				}
			}
		}
	}

	// xxxxxxxxxxxxxxxxx FINALIZANDO CONSULTA ( PARADAS )WEB SERVICE

	// xxxxxxxxxxxxxxxxx INICIANDO CONSULTA ( VEICULOS POR LINHA ) WEB SERVICE

	// Tarefa assincrona para realizar requisições e tratar retorno
	class VeiculosPorLinhaWS extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Limpamdo o array lsita de dados
			arrayDadosVeiculos.clear();

			// Animação enquando executa o web service
			progressDialog = ProgressDialog.show(MapaActivity.this, "Aguarde",
					"Veiculo Por Linha Processando...");
		}

		@Override
		protected String doInBackground(String... params) {
			String imei = params[0];
			// Passando link como parametro. getLink da class ConexãoServidor
			try {

				executarWebServiceVeiculoPorLinha(ConexaoServidor
						.getConexaoServidor().getLinkVeiculosPorLinha() + imei);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Se arrayDadosLinha ainda estiver em BRANCO,
			// e porque não encontrou varios resultado para o array.
			// Então faz a busca para apenas um Registro.

			if (arrayDadosVeiculos.isEmpty()) {
				executarWebServiceSingleResult(ConexaoServidor
						.getConexaoServidor().getLinkVeiculosPorLinha() + imei);
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (arrayDadosVeiculos.isEmpty()) {
				Toast.makeText(MapaActivity.this,
						R.string.nenhum_registro_encontrado, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(MapaActivity.this,
						R.string.informacoes_carregada_com_sucesso,
						Toast.LENGTH_SHORT).show();

				// Criando e iniciando a thread definida
				Thread thread = new ThreadBasica1();
				thread.start();

			}
			progressDialog.dismiss();
		}

		// Executando o webservice para buscar informações no banco de dados.
		private void executarWebServiceVeiculoPorLinha(
				String LinkVeiculosPorLinha) throws JSONException {

			String result = null;

			result = getRESTFileContent(LinkVeiculosPorLinha);

			if (result.equalsIgnoreCase("null")) {
				// Nenhum resultado encontrado

			} else {

				JSONObject json = new JSONObject(result);
				JSONArray arrayJsonVeiculo = json.getJSONArray("veiculo");
				JSONObject objetoJsonVeiculo;

				for (int i = 0; i < arrayJsonVeiculo.length(); i++) {
					objetoJsonVeiculo = new JSONObject(
							arrayJsonVeiculo.getString(i));

					veiculo = new Veiculo();

					veiculo.set_id(objetoJsonVeiculo.getInt("id"));
					veiculo.setDescricao(objetoJsonVeiculo
							.getString("descricao"));
					veiculo.setImei(objetoJsonVeiculo.getString("imei"));
					arrayDadosVeiculos.add(veiculo);

				}

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

					result = result.substring(11, result.length() - 1);

					JSONObject o = new JSONObject(result);

					veiculo = new Veiculo();

					veiculo.set_id(o.getInt("id"));
					veiculo.setDescricao(o.getString("descricao"));
					veiculo.setImei(o.getString("imei"));
					arrayDadosVeiculos.add(veiculo);

				} catch (JSONException e) {
					Log.e("Erro", "Erro no parsing do JSON", e);
				}
			}
		}

	}

	// xxxxxxxxxxxxxxxxx FINALIZANDO CONSULTA ( VEICULOS POR LINHA )WEB SERVICE

	// xxxxxxxxxxxxxxxxx INICIANDO CONSULTA ( ULTIMAS POSIÇÕES ) WEB SERVICE

	// Definindo e inicializando as variaveis como NULL
	String idUltimaPosicao = null;
	String latitude = null;
	String longitude = null;

	public void fazerAlgoDemorado() {

		veiculo = new Veiculo();
		Iterator<Veiculo> it = arrayDadosVeiculos.listIterator();
		while (it.hasNext()) {

			veiculo = it.next();

			// Executando o webservice para buscar informações no banco de
			// dados.
			String result = null;
			// Passando link como parametro. getLink da class ConexãoServidor
			result = getRESTFileContent(ConexaoServidor.getConexaoServidor()
					.getLinkUltimaPosicaoImei() + veiculo.getImei());

			if (result.equalsIgnoreCase("null")) {
				// Result sem resultado
			} else {

				try {

					String json = result;
					JSONObject o = new JSONObject(json);

					idUltimaPosicao = (o.getString("id"));
					latitude = (o.getString("latitude"));
					longitude = (o.getString("longitude"));

				} catch (JSONException e) {
					Log.e("Erro", "Erro no parsing do JSON", e);
				}

				// Criando um marcador
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						LatLng latLngTemp;
						latLngTemp = new LatLng(Double.parseDouble(latitude),
								Double.parseDouble(longitude));

						adicionarMarcador(latLngTemp, veiculo.getDescricao(),
								R.drawable.mapa_localizacao_onibus, true, false);

					}
				});
			}

		}

	}

	// INICIANDO THREADS NOVA
	// A classe ThreadBasica1 herda de Thread
	class ThreadBasica1 extends Thread {
		// Este método(run()) é chamado quando a thread é iniciada
		public void run() {
			fazerAlgoDemorado();
		}
	}

	// FINALIZANDO THREADS NOVA
	// xxxxxxxxxxxxxxxxx FINALIZANDO CONSULTA ( ULTIMAS POSIÇÕES ) WEB SERVICE

}
