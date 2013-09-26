package br.com.kpc.locbus.util;

public class ConexaoServidor {
	//Servidor casa       http://187.44.38.252
	//Servidor restaurante http://187.44.47.206/
	// Link do web service
	private final String linkVeiculosPorLinha = "http://187.44.47.206/locBus/rest/veiculos/buscaPorLinha/";
	private final String linkTodasParadas = "http://187.44.47.206/locBus/rest/paradas/findAll";
	private final String linkTodaslinhas = "http://187.44.47.206/locBus/rest/linhas/findAll/";
	private final String linkUltimaPosicaoImei = "http://187.44.47.206/locBus/rest/posicoes/getUltimaPosicao/";
	
	
	
	public String getLinkVeiculosPorLinha() {
		return linkVeiculosPorLinha;
	}

	public String getLinkTodasParadas() {
		return linkTodasParadas;
	}

	public String getLinkTodaslinhas() {
		return linkTodaslinhas;
	}

	public String getLinkUltimaPosicaoImei() {
		return linkUltimaPosicaoImei;
	}



	private static ConexaoServidor conexaoServidor;

	public static ConexaoServidor getConexaoServidor() {
		if (conexaoServidor == null) {
			conexaoServidor = new ConexaoServidor();
		}
		return conexaoServidor;
	}
}
