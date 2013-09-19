package br.com.kpc.locbus.util;

public class ConexaoServidor {
	//Servidor casa       http://187.44.38.252
	//Servidor restaurante http://187.44.47.206/
	// Link do web service
	private final String linkOnibus = "http://187.44.47.206/locBus/rest/veiculos/buscaPorLinha/";
	private final String linkTodasParadas = "http://187.44.47.206/locBus/rest/paradas/findAll";
	private final String linkTodaslinhas = "http://187.44.47.206/locBus/rest/linhas/findAll/";
	
	
	public String getLinkOnibus() {
		return linkOnibus;
	}

	public String getLinkTodasParadas() {
		return linkTodasParadas;
	}

	public String getLinkTodaslinhas() {
		return linkTodaslinhas;
	}



	private static ConexaoServidor conexaoServidor;

	public static ConexaoServidor getConexaoServidor() {
		if (conexaoServidor == null) {
			conexaoServidor = new ConexaoServidor();
		}
		return conexaoServidor;
	}
}
