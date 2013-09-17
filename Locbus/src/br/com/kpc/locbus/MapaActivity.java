package br.com.kpc.locbus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

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
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import br.com.kpc.locbus.core.Onibus;
import br.com.kpc.locbus.core.Parada;
import br.com.kpc.locbus.core.Posicao;
import br.com.kpc.locbus.util.ConexaoServidor;
import br.com.kpc.locbus.util.InformacaoMaps;
import br.com.kpc.locbus.util.Mensagens;
import br.com.kpc.locbus.webservice.Paradas;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends Activity implements LocationListener {

	private static final int RETORNO_MENU = 0;

	// Varial que vai grava lat long
	LatLng latLng = new LatLng(49.187500, -122.849000);
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

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa))
				.getMap();

		map.setMyLocationEnabled(true);
		InformacaoMaps informacaoMaps = new InformacaoMaps();

	}

	public void onClick_City(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		com.google.android.gms.maps.CameraUpdate update = CameraUpdateFactory
				.newLatLngZoom(latLng, 9);
		map.animateCamera(update);
	}

	public void onClick_Burnaby(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 14);
		map.animateCamera(update);

	}

	public void onClick_Surrey(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
		map.animateCamera(update);

	}

	public void onClick_aki(View v) {

		// RECUPERAR INFORMAÇÃO DA TELA DE BUSCA MAPAS, QUE VAI DAR O RETORNO O
		// QUE O
		// USUARIO QUER FAZER-

		startActivityForResult(new Intent(this, MapaBuscarActivity.class),
				RETORNO_MENU);
		// Intent i = new Intent(getApplicationContext(),
		// MapaBuscarActivity.class);
		// startActivity(i);

		// / Mensagens.instancia().testando();

		// map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// CameraUpdate update = CameraUpdateFactory
		// .newLatLngZoom(localizacao, 16);
		// map.setMyLocationEnabled(true);

	}

	@Override
	public void onLocationChanged(Location location) {
		// Centraliza o mapa nesta coordenada
		latLng = new LatLng(location.getLatitude(), location.getLongitude());
		// map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
		// Adicionar marcador

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int codigo, int resultado, Intent itRetorno) {
		super.onActivityResult(codigo, resultado, itRetorno);
		// Retornos pode ser:
		// 1 - Onibus de determinada linha;
		// 2 - Parada de Onibus mais proxima;
		// 0 - RETORNO_MENU operação cancelada.
		Log.d("LOG TEST", "MAPA 1");

		// BUNDLE COM ERRO
		// Bundle bundle = itRetorno != null ? itRetorno.getExtras() : null;
		// String msg = bundle.getString("msg");

		Log.d("LOG TEST", "ON PAUSE");

		// Retorno da opção do menu = 0
		if (codigo == RETORNO_MENU) {
			// 1 - Onibus de determinada linha;
			if (resultado == 1) {
				Toast.makeText(this, "SIM", Toast.LENGTH_SHORT).show();
				// adicionarMarcador(latLng);
				// Limpamdo o array lsita de dados

			}// 2 - Parada de Onibus mais proxima;
			else if (resultado == 2) {
				Toast.makeText(this, "NAO", Toast.LENGTH_SHORT).show();
				// Chama o WebService em um AsyncTask
				new TarefaWS().execute();

			}

		}

	}

	public void adicionarMarcador(LatLng latLng) {
		map.clear();

		// // Cria um marcador LOCAL ONDE ESTA
		Marker frameworkSystem = map.addMarker(new MarkerOptions()
				.position(latLng)
				.title("Ônibus 1222")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_locacao)));
		// Move a câmera para Framework System com zoom 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

	}

	// xxxxxxxxxxxxxxxxx INICIANDO CONSULTA ( PARADAS ) WEB SERVICE
	// XXXXXXXXXXXXXXXXXXXXX

	// Declaração de Variaveis Global da Class
	private ProgressDialog progressDialog;

	ListView listView;
	// Array que vai armazenar os dados da consulta e coloca no List
	ArrayList arrayDados = new ArrayList();
	// Classe
	Parada parada;

	// // Botão de carregar os dados
	// public void btnWSClick() {
	//
	//
	// // Chama o WebService em um AsyncTask
	// new TarefaWS().execute();
	//
	// }

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

			Log.d("PARADA TESTE", " 11  " + response);

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
			progressDialog = ProgressDialog.show(MapaActivity.this, "Aguarde",
					"processando...");
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
				Toast.makeText(MapaActivity.this,
						"Nenhum registro encontrado!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(MapaActivity.this,
						"Informações carregada com sucesso!",
						Toast.LENGTH_SHORT).show();
			}

			progressDialog.dismiss();

			// // Enviando os dados para o ListView (Atualizando a tela)
			// listView.setAdapter(new OnibusAdapter(Paradas.this,
			// arrayDados));
			LatLng l;
			Iterator<Parada> it = arrayDados.iterator();
			while (it.hasNext()) {
				Parada p = it.next();

				l = new LatLng(Double.parseDouble(p.getLatitude()),
						Double.parseDouble(p.getLongitude()));
				// // Cria um marcador
				Marker frameworkSystem = map.addMarker(new MarkerOptions()
						.position(l)
						.title(p.get_id() + " - " + p.getDescricao())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_locacao)));
				// Move a câmera para Framework System com zoom 15.
				// map.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 17));

			}

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
						// parada.setStatus(Boolean.parseBoolean("status"));

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
	// XXXXXXXXXXXXXXXXXXXXX

	// xxxxxxxxxxxxxxxxx INICIANDO CONSULTA ( ONIBUS ) WEB SERVICE
	// XXXXXXXXXXXXXXXXXXXXX

	Posicao posicao;

	// Tarefa assincrona para realizar requisição e tratar retorno
	class OnibusWS extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Limpamdo o array lsita de dados
			arrayDados.clear();

			// Animação enquando executa o web service
			progressDialog = ProgressDialog.show(MapaActivity.this, "Aguarde",
					"processando...");
		}

		@Override
		protected String doInBackground(Void... params) {
			// Passando link como parametro. getLink da class ConexãoServidor
			return executarWebService(ConexaoServidor.getConexaoServidor()
					.getLinkOnibus() + "122");
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (arrayDados.isEmpty()) {
				Toast.makeText(MapaActivity.this,
						"Nenhum registro encontrado!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(MapaActivity.this,
						"Informações carregada com sucesso!",
						Toast.LENGTH_SHORT).show();
			}

			progressDialog.dismiss();

			// // Enviando os dados para o ListView (Atualizando a tela)
			// listView.setAdapter(new OnibusAdapter(Paradas.this,
			// arrayDados));
			LatLng latLngTemp;
			Iterator<Posicao> it = arrayDados.iterator();
			while (it.hasNext()) {
				Posicao p = it.next();

				latLngTemp = new LatLng(Double.parseDouble(p.getLatitude()),
						Double.parseDouble(p.getLongitude()));
				// // Cria um marcador
				Marker frameworkSystem = map.addMarker(new MarkerOptions()
						.position(latLngTemp)
						.title(p.get_id() + " - " + p.get_id())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_locacao)));
				// Move a câmera para Framework System com zoom 15.
				// map.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 17));

			}

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
						// parada.setStatus(Boolean.parseBoolean("status"));

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

	// xxxxxxxxxxxxxxxxx FINALIZANDO CONSULTA ( ONIBUS ) WEB SERVICE
	// XXXXXXXXXXXXXXXXXXXXX

}
