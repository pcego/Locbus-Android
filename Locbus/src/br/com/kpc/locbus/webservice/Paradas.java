package br.com.kpc.locbus.webservice;

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

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import br.com.kpc.locbus.MapaActivity;
import br.com.kpc.locbus.OnibusActivity;
import br.com.kpc.locbus.OnibusInforActivyty;
import br.com.kpc.locbus.R;
import br.com.kpc.locbus.adapter.OnibusAdapter;
import br.com.kpc.locbus.core.Veiculo;
import br.com.kpc.locbus.core.Parada;
import br.com.kpc.locbus.util.ConexaoServidor;

public class Paradas extends Activity {

	// xxxxxxxxxxxxxxxxx INICIANDO CONSULTA WEB SERVICE XXXXXXXXXXXXXXXXXXXXX

	// Declaração de Variaveis Global da Class
	private ProgressDialog progressDialog;

	ListView listView;
	// Array que vai armazenar os dados da consulta e coloca no List
	ArrayList arrayDados = new ArrayList();
	// Classe
	Parada parada;

	 // Botão de carregar os dados
	 public ArrayList<Parada> getParadas() {	
	
	 // Chama o WebService em um AsyncTask
	 new TarefaWS().execute();
	 
	 return arrayDados;
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
			progressDialog = ProgressDialog.show(getApplicationContext(), "Aguarde",
					"processando...");
		}

		@Override
		protected String doInBackground(Void... params) {
			// Passando link como parametro. getLink da class ConexãoServidor
			return executarWebService(ConexaoServidor.getConexaoServidor()
					.getLinkParadasTodas());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (arrayDados.isEmpty()) {
				Toast.makeText(getApplicationContext(),
						"Nenhum registro encontrado!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(),
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

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//				l = new LatLng(Double.parseDouble(p.getLatitude()),
//						Double.parseDouble(p.getLongitude()));
//				// // Cria um marcador
//				Marker frameworkSystem = map.addMarker(new MarkerOptions()
//						.position(l)
//						.title(p.get_id() + " - "+ p.getDescricao())
//						.icon(BitmapDescriptorFactory
//								.fromResource(R.drawable.icon_locacao)));

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

	// xxxxxxxxxxxxxxxxx FINALIZANDO CONSULTA WEB SERVICE XXXXXXXXXXXXXXXXXXXXX

}
