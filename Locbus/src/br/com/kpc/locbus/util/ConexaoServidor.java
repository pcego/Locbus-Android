package br.com.kpc.locbus.util;

import br.com.kpc.locbus.R;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class ConexaoServidor {
	// Servidor casa http://187.44.38.252
	// Servidor restaurante http://187.44.47.206/
	
	private final String ipServidor = "http://177.44.8.231";
	

	// Link do web service
	private final String linkVeiculosPorLinha   = ipServidor + "/locBus/rest/veiculos/buscaPorLinha/";
	private final String linkParadasTodas       = ipServidor + "/locBus/rest/paradas/findAll";
	private final String linkParadasPorRua      = ipServidor + "/locBus/rest/paradas/getPorRua/";
	private final String linkParadasPorBairro   = ipServidor + "/locBus/rest/paradas/getPorBairro/";
	private final String linkTodaslinhas        = ipServidor + "/locBus/rest/linhas/findAll/";
	private final String linkUltimaPosicaoImei  = ipServidor + "/locBus/rest/posicoes/getUltimaPosicao/";
	private final String linkInformacaoParada   = ipServidor + "/locBus/rest/linhas/getByParada/";
	
	
	
	
	public String getLinkInformacaoParada() {
		return linkInformacaoParada;
	}

	public String getLinkVeiculosPorLinha() {
		return linkVeiculosPorLinha;
	}

	public String getLinkParadasTodas() {
		return linkParadasTodas;
	}

	public String getLinkTodaslinhas() {
		return linkTodaslinhas;
	}

	public String getLinkUltimaPosicaoImei() {
		return linkUltimaPosicaoImei;
	}

	public String getLinkParadasPorRua() {
		return linkParadasPorRua;
	}

	public String getLinkParadasPorBairro() {
		return linkParadasPorBairro;
	}

	private static ConexaoServidor conexaoServidor;

	public static ConexaoServidor getConexaoServidor() {
		if (conexaoServidor == null) {
			conexaoServidor = new ConexaoServidor();
		}
		return conexaoServidor;
	}

	// Função para verificar existência de conexão com a internet
	public static boolean verificaConexao(Context context) {
		boolean conectado;
		ConnectivityManager conectivtyManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conectivtyManager.getActiveNetworkInfo() != null
				&& conectivtyManager.getActiveNetworkInfo().isAvailable()
				&& conectivtyManager.getActiveNetworkInfo().isConnected()) {
			conectado = true;
		} else {
			conectado = false;
			Toast.makeText(context.getApplicationContext(),
					R.string.nao_existencia_internet, Toast.LENGTH_LONG).show();
		}
		return conectado;
	}

}
